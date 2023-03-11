package xyz.wisecraft.smp.togglepvp.listeners;

import com.songoda.ultimatetimber.events.TreeDamageEvent;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.extra.UtilCommon;
import xyz.wisecraft.smp.togglepvp.Chat;
import xyz.wisecraft.smp.togglepvp.utils.PVPUtil;
import xyz.wisecraft.smp.togglepvp.utils.Util;

import java.util.Collection;
import java.util.Iterator;

//todo reorganize this mess
public class PvPListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	//fired when an entity is hit
	public void onHit(EntityDamageByEntityEvent e) {
		if (WisecraftSMP.blockedWorlds.contains(e.getEntity().getWorld().getName())) return;

		//check if attack was a player
		if (e.getDamager() instanceof Player attacker && e.getEntity() instanceof Player victim) {
			Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
			Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (isAttackerPVPOff) {
				e.setCancelled(true);
				Chat.send(attacker, "PVP_DISABLED");
			} else if (isVictimPVPOff != null && isVictimPVPOff) {
				e.setCancelled(true);
				Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
			} else {
				Util.setCooldownTime(attacker);
				Util.setCooldownTime(victim);
			}
			//checks if damage was done by a projectile
		} else if (e.getDamager() instanceof Projectile arrow) {
			if(arrow.getShooter() instanceof Player) {
				if(e.getEntity() instanceof Player victim) {
					Player attacker = (Player) arrow.getShooter();
					Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
					Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
					if(attacker == victim) {
						return;
					}
					if(isAttackerPVPOff) {
						e.setCancelled(true);
						Chat.send(attacker, "PVP_DISABLED");
					} else if(isVictimPVPOff != null && isVictimPVPOff) {
						e.setCancelled(true);
						Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
					} else {
						Util.setCooldownTime(attacker);
						Util.setCooldownTime(victim);
					}
				}
			}
		} else if (e.getDamager() instanceof LightningStrike && e.getDamager().getMetadata("TRIDENT").size() >= 1 && e.getEntity() instanceof Player victim) {
			Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (isVictimPVPOff != null && isVictimPVPOff) e.setCancelled(true);
		} else if (e.getDamager() instanceof Firework && e.getEntity() instanceof Player victim) {
			Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (isVictimPVPOff != null && isVictimPVPOff)
				e.setCancelled(true);

		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onTimber(TreeDamageEvent e) {
		Player victim = e.getVictim();
		Player attacker = UtilCommon.getWhoTimber(victim, 2.5);
		if (attacker == null) return;

		Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
		Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
		if (isVictimPVPOff) {
			e.setCancelled(true);
			Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
		} else if (isAttackerPVPOff) {
			e.setCancelled(true);
			Chat.send(attacker, "PVP_DISABLED");
		} else {
			Util.setCooldownTime(attacker);
			Util.setCooldownTime(victim);
		}

	}

	@EventHandler(ignoreCancelled = true)
	//fired when a player is shot with a flaming arrow
	public void onFlameArrow(EntityCombustByEntityEvent e) {
		if (WisecraftSMP.blockedWorlds.contains(e.getEntity().getWorld().getName())) return;

		if(e.getCombuster() instanceof Arrow arrow) {
			if(arrow.getShooter() instanceof Player attacker && e.getEntity() instanceof Player victim) {
				Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
				Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());

				if (isAttackerPVPOff) {
					e.setCancelled(true);
				} else if (isVictimPVPOff != null && isVictimPVPOff) {
					e.setCancelled(true);
				} else {
					Util.setCooldownTime(attacker);
					Util.setCooldownTime(victim);
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	//fired when a splash potion is thrown
	public void onPotionSplash(PotionSplashEvent e) {
		if (WisecraftSMP.blockedWorlds.contains(e.getEntity().getWorld().getName())) return;


		if(e.getPotion().getShooter() instanceof Player) {
			Collection<LivingEntity> affected = e.getAffectedEntities();
			for(LivingEntity entity : affected) {
				if(entity instanceof Player victim) {
					if (PVPUtil.isEffectsPositive(e.getPotion().getEffects())) return;

					Player attacker = (Player) e.getPotion().getShooter();
					Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
					Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
					if(attacker != victim) {
						if(isAttackerPVPOff) {
							for(LivingEntity livingEntity : affected){
								if(livingEntity instanceof Player && livingEntity != attacker){
									e.setIntensity(livingEntity, 0);
								}
							}
							Chat.send(attacker, "PVP_DISABLED");

						} else if(isVictimPVPOff != null && isVictimPVPOff) {
							for(LivingEntity ent : affected){
								if(ent instanceof Player && ent != attacker){
									e.setIntensity(ent, 0);
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
	public void onCloudEffects(AreaEffectCloudApplyEvent e) {
		if (WisecraftSMP.blockedWorlds.contains(e.getEntity().getWorld().getName())) return;

		if(e.getEntity().getSource() instanceof Player) {
			Iterator<LivingEntity> it = e.getAffectedEntities().iterator();
			while(it.hasNext()) {
				LivingEntity entity = it.next();
				if(entity instanceof Player victim) {
					if (PVPUtil.isEffectsPositive(e.getEntity().getBasePotionData().getType().getEffectType())) return;

					Player attacker = (Player) e.getEntity().getSource();
					Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
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
	public void onPlayerFishing (PlayerFishEvent e) {
		if (WisecraftSMP.blockedWorlds.contains(e.getPlayer().getWorld().getName())) return;


		if (e.getCaught() instanceof final Player victim) {
			final Player attacker = e.getPlayer();
			Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
			Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (attacker.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD || attacker.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD) {
				if (isAttackerPVPOff) {
					e.setCancelled(true);
					Chat.send(attacker, "PVP_DISABLED");
				} else if (isVictimPVPOff != null && isVictimPVPOff) {
					e.setCancelled(true);
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
	public void onLightningStrike(LightningStrikeEvent e){
		if(e.getCause() == LightningStrikeEvent.Cause.TRIDENT){
			e.getLightning().setMetadata("TRIDENT", new FixedMetadataValue(
					WisecraftSMP.instance, e.getLightning().getLocation()
			));
		}
	}
}