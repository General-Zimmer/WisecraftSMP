package xyz.wisecraft.smp.togglepvp.listeners;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
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
import xyz.wisecraft.smp.togglepvp.Chat;
import xyz.wisecraft.smp.togglepvp.utils.Util;


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
			Boolean attackerState = WisecraftSMP.instance.players.get(attacker.getUniqueId());
			//player who was hit
			Boolean victimState = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (attackerState) {
				event.setCancelled(true);
				Chat.send(attacker, "PVP_DISABLED");
			} else if (victimState != null && victimState) {
				event.setCancelled(true);
				Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getDisplayName());
			} else {
				Util.setCooldownTime(attacker);
				Util.setCooldownTime(victim);
			}
			//checks if damage was done by a projectile
		} else if (event.getDamager() instanceof Projectile arrow) {
			if(arrow.getShooter() instanceof Player) {
				if(event.getEntity() instanceof Player victim) {
					Player attacker = (Player) arrow.getShooter();
					Boolean attackerState = WisecraftSMP.instance.players.get(attacker.getUniqueId());
					Boolean victimState = WisecraftSMP.instance.players.get(victim.getUniqueId());
					if(attacker == victim) {
						return;
					}
					if(attackerState) {
						event.setCancelled(true);
						Chat.send(attacker, "PVP_DISABLED");
					} else if(victimState != null && victimState) {
						event.setCancelled(true);
						Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getDisplayName());
					} else {
						Util.setCooldownTime(attacker);
						Util.setCooldownTime(victim);
					}
				}
			}
			//checks if damage was done by a potion
		} else if(event.getDamager() instanceof ThrownPotion potion) {
			if (potion.getShooter() instanceof Player attacker && event.getEntity() instanceof Player victim) {
				Boolean attackerState = WisecraftSMP.instance.players.get(attacker.getUniqueId());
				Boolean victimState = WisecraftSMP.instance.players.get(victim.getUniqueId());
				if(attacker == victim) {
					return;
				}
				if (attackerState) {
					event.setCancelled(true);
					Chat.send(attacker, "PVP_DISABLED");
				} else if (victimState != null && victimState) {
					event.setCancelled(true);
					Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getDisplayName());
				} else {
					Util.setCooldownTime(attacker);
					Util.setCooldownTime(victim);
				}
			}
		} else if (event.getDamager() instanceof LightningStrike && event.getDamager().getMetadata("TRIDENT").size() >= 1 && event.getEntity() instanceof Player victim) {
			Boolean victimState = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (victimState != null && victimState) {
				event.setCancelled(true);
			}
		} else if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player victim) {
			Boolean victimState = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (victimState != null && victimState) {
				event.setCancelled(true);
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
				Boolean attackerState = WisecraftSMP.instance.players.get(attacker.getUniqueId());
				Boolean victimState = WisecraftSMP.instance.players.get(victim.getUniqueId());
				if (attackerState) {
					event.setCancelled(true);
				} else if (victimState != null && victimState) {
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
		if (WisecraftSMP.blockedWorlds.contains(event.getEntity().getWorld().getName())) {
			return;
		}

		if(event.getPotion().getShooter() instanceof Player) {
			for(LivingEntity entity : event.getAffectedEntities()) {
				if(entity instanceof Player victim) {
					Player attacker = (Player) event.getPotion().getShooter();
					Boolean attackerState = WisecraftSMP.instance.players.get(attacker.getUniqueId());
					Boolean victimState = WisecraftSMP.instance.players.get(victim.getUniqueId());
					if(attacker != victim) {
						if(attackerState) {
							Collection<LivingEntity> affected = event.getAffectedEntities();
							for(LivingEntity ent : affected){
								if(ent instanceof Player && ent != attacker){
									event.setIntensity(ent, 0);
								}
							}
							Chat.send(attacker, "PVP_DISABLED");
						} else if(victimState != null && victimState) {
							Collection<LivingEntity> affected = event.getAffectedEntities();
							for(LivingEntity ent : affected){
								if(ent instanceof Player && ent != attacker){
									event.setIntensity(ent, 0);
								}
							}
							Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getDisplayName());
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
		if (WisecraftSMP.blockedWorlds.contains(event.getEntity().getWorld().getName())) {
			return;
		}

		if(event.getEntity().getSource() instanceof Player) {
			Iterator<LivingEntity> it = event.getAffectedEntities().iterator();
			while(it.hasNext()) {
				LivingEntity entity = it.next();
				if(entity instanceof Player && entity != null) {
					Player attacker = (Player) event.getEntity().getSource();
					Boolean attackerState = WisecraftSMP.instance.players.get(attacker.getUniqueId());
					Player victim = (Player) entity;
					Boolean victimState = WisecraftSMP.instance.players.get(victim.getUniqueId());
					if(victimState != null && victimState) {
						it.remove();
					} else if(attackerState) {
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
		if (WisecraftSMP.blockedWorlds.contains(event.getPlayer().getWorld().getName())) {
			return;
		}

		if (event.getCaught() instanceof final Player victim) {
			final Player attacker = event.getPlayer();
			Boolean attackerState = WisecraftSMP.instance.players.get(attacker.getUniqueId());
			Boolean victimState = WisecraftSMP.instance.players.get(victim.getUniqueId());
			if (attacker.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD || attacker.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD) {
				if (attackerState) {
					event.setCancelled(true);
					Chat.send(attacker, "PVP_DISABLED");
				} else if (victimState != null && victimState) {
					event.setCancelled(true);
					Chat.send(attacker, "PVP_DISABLED_OTHERS", victim.getDisplayName());
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
			event.getLightning().setMetadata("TRIDENT", new FixedMetadataValue(WisecraftSMP.instance, event.getLightning().getLocation()));
		}
	}
}