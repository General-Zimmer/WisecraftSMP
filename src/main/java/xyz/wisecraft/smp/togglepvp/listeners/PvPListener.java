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
//todo Only prevent bad positon effects
public class PvPListener implements Listener {

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
		Player attacker = UtilCommon.getWhoTimber(victim, 2.5);
		Boolean isVictimPVPOff = WisecraftSMP.instance.players.get(victim.getUniqueId());

		if (attacker != null) {
			Boolean isAttackerPVPOff = WisecraftSMP.instance.players.get(attacker.getUniqueId());
			if (isVictimPVPOff) {
				e.setCancelled(true);
				Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
			}
			else if (isAttackerPVPOff) {
				e.setCancelled(true);
				Chat.send(attacker, "PVP_DISABLED");
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