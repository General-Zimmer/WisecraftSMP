package xyz.wisecraft.smp.togglepvp.listeners;

import com.songoda.ultimatetimber.events.TreeDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.wisecraft.core.data.templates.Timers;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.advancements.util.UtilAdv;
import xyz.wisecraft.smp.togglepvp.Chat;
import xyz.wisecraft.smp.togglepvp.utils.Util;

import java.util.*;

//todo reorganize this mess
//todo fix Timber being able to damage other players
public class PvP implements Listener {

	@EventHandler(ignoreCancelled = true)
	//fired when an entity is hit
	public void onHit(EntityDamageByEntityEvent event) {
		if (WisecraftSMP.blockedWorlds.contains(event.getEntity().getWorld().getName())) {
			return;
		}

		//check if attack was a player
		if (event.getDamager() instanceof Player attacker && event.getEntity() instanceof Player victim) {
			//player who hit
			Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
			//player who was hit
			Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (isAttackerPVPOff) {
				event.setCancelled(true);
				Chat.send(attacker, "PVP_DISABLED");
			} else if (isVictimPVPOff != null && isVictimPVPOff) {
				event.setCancelled(true);
				Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
			} else {
				Util.setCooldownTime(attacker);
				Util.setCooldownTime(victim);
			}
			//checks if damage was done by a projectile
		} else if (event.getDamager() instanceof Projectile arrow) {
			if(arrow.getShooter() instanceof Player) {
				if(event.getEntity() instanceof Player victim) {
					Player attacker = (Player) arrow.getShooter();
					Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
					Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
					if(attacker == victim) {
						return;
					}
					if(isAttackerPVPOff) {
						event.setCancelled(true);
						Chat.send(attacker, "PVP_DISABLED");
					} else if(isVictimPVPOff != null && isVictimPVPOff) {
						event.setCancelled(true);
						Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
					} else {
						Util.setCooldownTime(attacker);
						Util.setCooldownTime(victim);
					}
				}
			}
			//checks if damage was done by a potion
		} else if(event.getDamager() instanceof ThrownPotion potion) {
			if (potion.getShooter() instanceof Player attacker && event.getEntity() instanceof Player victim) {
				Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
				Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
				if(attacker == victim) {
					return;
				}
				if (isAttackerPVPOff) {
					event.setCancelled(true);
					Chat.send(attacker, "PVP_DISABLED");
				} else if (isVictimPVPOff != null && isVictimPVPOff) {
					event.setCancelled(true);
					Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
				} else {
					Util.setCooldownTime(attacker);
					Util.setCooldownTime(victim);
				}
			}
		} else if (event.getDamager() instanceof LightningStrike && event.getDamager().getMetadata("TRIDENT").size() >= 1 && event.getEntity() instanceof Player victim) {
			Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (isVictimPVPOff != null && isVictimPVPOff) {
				event.setCancelled(true);
			}
		} else if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player victim) {
			Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (isVictimPVPOff != null && isVictimPVPOff) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onTimber(TreeDamageEvent e) {

		Player victim = e.getVictim();
		UUID victimUUID = victim.getUniqueId();
		List<Player> players = new ArrayList<>();
		// Check who broke a tree recently
		for (Player attacker : Bukkit.getOnlinePlayers()) {
			Timers times = WisecraftSMP.core.getTimers().get(attacker.getUniqueId());
			double seconds = UtilAdv.calcCurrentSeconds(times.getTree());
			UUID attackerUUID = attacker.getUniqueId();

			if (!victimUUID.toString().equals(attackerUUID.toString()) && seconds < 2.5) {
				players.add(attacker);
			}
		}
		if (players.isEmpty())
			return;


		// Are they near the player who takes damage?
		for (Player attacker : players) {
			Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
			Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (!isVictimPVPOff && !isAttackerPVPOff) return;
			if (victim.getLocation().distanceSquared(attacker.getLocation()) <= 1024) {
					e.setCancelled(true);
					Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
				}
		}
	}

	@EventHandler(ignoreCancelled = true)
	//fired when a player is shot with a flaming arrow
	public void onFlameArrow(EntityCombustByEntityEvent event) {
		if (WisecraftSMP.blockedWorlds.contains(event.getEntity().getWorld().getName())) {
			return;
		}

		if(event.getCombuster() instanceof Arrow arrow) {
			if(arrow.getShooter() instanceof Player attacker && event.getEntity() instanceof Player victim) {
				Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
				Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
				if (isAttackerPVPOff) {
					event.setCancelled(true);
				} else if (isVictimPVPOff != null && isVictimPVPOff) {
					event.setCancelled(true);
				} else {
					Util.setCooldownTime(attacker);
					Util.setCooldownTime(victim);
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	//fired when a splash potion is thrown
	public void onPotionSplash(PotionSplashEvent event) {
		if (WisecraftSMP.blockedWorlds.contains(event.getEntity().getWorld().getName())) return;

		if(event.getPotion().getShooter() instanceof Player) {
			for(LivingEntity entity : event.getAffectedEntities()) {
				if(entity instanceof Player victim) {
					Player attacker = (Player) event.getPotion().getShooter();
					Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
					Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
					if(attacker != victim) {
						if(isAttackerPVPOff) {
							Collection<LivingEntity> affected = event.getAffectedEntities();
							for(LivingEntity ent : affected){
								if(ent instanceof Player && ent != attacker){
									event.setIntensity(ent, 0);
								}
							}
							Chat.send(attacker, "PVP_DISABLED");
						} else if(isVictimPVPOff != null && isVictimPVPOff) {
							Collection<LivingEntity> affected = event.getAffectedEntities();
							for(LivingEntity ent : affected){
								if(ent instanceof Player && ent != attacker){
									event.setIntensity(ent, 0);
								}
							}
							Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
						} else {
							Util.setCooldownTime(attacker);
							Util.setCooldownTime(victim);
						}
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	//fired when lingering potion cloud is active
	public void onCloudEffects(AreaEffectCloudApplyEvent event) {
		if (WisecraftSMP.blockedWorlds.contains(event.getEntity().getWorld().getName())) return;

		if(event.getEntity().getSource() instanceof Player) {
			Iterator<LivingEntity> it = event.getAffectedEntities().iterator();
			while(it.hasNext()) {
				LivingEntity entity = it.next();
				if(entity instanceof Player) {
					Player attacker = (Player) event.getEntity().getSource();
					Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
					Player victim = (Player) entity;
					Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
					if(isVictimPVPOff != null && isVictimPVPOff) {
						it.remove();
					} else if(isAttackerPVPOff) {
						it.remove();
					} else {
						Util.setCooldownTime(attacker);
						Util.setCooldownTime(victim);
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	//fired when a player uses a fishing rod
	public void onPlayerFishing (PlayerFishEvent event) {
		if (WisecraftSMP.blockedWorlds.contains(event.getPlayer().getWorld().getName())) return;


		if (event.getCaught() instanceof final Player victim) {
			final Player attacker = event.getPlayer();
			Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
			Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (attacker.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD || attacker.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD) {
				if (isAttackerPVPOff) {
					event.setCancelled(true);
					Chat.send(attacker, "PVP_DISABLED");
				} else if (isVictimPVPOff != null && isVictimPVPOff) {
					event.setCancelled(true);
					Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
				} else {
					Util.setCooldownTime(attacker);
					Util.setCooldownTime(victim);
				}
			}
		}
	}

	//Tag lightning strike as from a trident
	@EventHandler(ignoreCancelled = true)
	public void onLightningStrike(LightningStrikeEvent event){
		if(event.getCause() == LightningStrikeEvent.Cause.TRIDENT){
			event.getLightning().setMetadata("TRIDENT", new FixedMetadataValue(
					WisecraftSMP.instance, event.getLightning().getLocation()
			));
		}
	}
}