package xyz.wisecraft.smp.modules.togglepvp.listeners;

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
import xyz.wisecraft.smp.modules.togglepvp.utils.UtilChat;
import xyz.wisecraft.smp.modules.togglepvp.utils.PVPUtil;
import xyz.wisecraft.smp.modules.togglepvp.utils.UtilPlayers;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * PvPListener
 */
public class PvPListener implements Listener {

	private final WisecraftSMP plugin = WisecraftSMP.getInstance();
	private final HashMap<UUID, Boolean> pvpPlayers = PVPStorage.getPVPPlayers();
	private final List<String> blockedWorlds = PVPStorage.getBlockedWorlds();

	// todo prevent placing fire and lava near players

	/**
	 * Cancel damage if pvp isn't enabled on either players
	 * @param e EntityDamageByEntityEvent
	 */
	@EventHandler(ignoreCancelled = true)
	public void onHit(EntityDamageByEntityEvent e) {
		if (blockedWorlds.contains(e.getEntity().getWorld().getName())) return;

		//check if attack was a player
		if (e.getDamager() instanceof Player attacker && e.getEntity() instanceof Player victim) {

			if (PVPUtil.checkPVPStates(attacker, victim))
				e.setCancelled(true);

			//checks if damage was done by a projectile
		} else if (e.getDamager() instanceof Projectile arrow) {
			if(!(arrow.getShooter() instanceof Player)) return;

			if(e.getEntity() instanceof Player victim) {
				Player attacker = (Player) arrow.getShooter();

				if (PVPUtil.checkPVPStates(attacker, victim)) e.setCancelled(true);
			}
		} else if (e.getDamager() instanceof LightningStrike &&
				e.getDamager().getMetadata("TRIDENT").size() >= 1 &&
				e.getEntity() instanceof Player victim) {
			Boolean isVictimPVPOff = pvpPlayers.get(victim.getUniqueId());

			if (isVictimPVPOff != null && isVictimPVPOff) e.setCancelled(true);
		} else if (e.getDamager() instanceof Firework && e.getEntity() instanceof Player victim) {
			Boolean isVictimPVPOff = pvpPlayers.get(victim.getUniqueId());

			if (isVictimPVPOff != null && isVictimPVPOff) e.setCancelled(true);
		}
	}

	/**
	 * Fired when a player is hit by a arrow
	 * @param e PlayerFishEvent
	 */
	@EventHandler(ignoreCancelled = true)
	//fired when a player is shot with a flaming arrow
	public void onFlameArrow(EntityCombustByEntityEvent e) {
		if (blockedWorlds.contains(e.getEntity().getWorld().getName())) return;

		if(e.getCombuster() instanceof Arrow arrow) {
			if(arrow.getShooter() instanceof Player attacker && e.getEntity() instanceof Player victim) {
				Boolean isAttackerPVPOff = pvpPlayers.get(attacker.getUniqueId());
				Boolean isVictimPVPOff = pvpPlayers.get(victim.getUniqueId());

				if (isAttackerPVPOff) {
					e.setCancelled(true);
				} else if (isVictimPVPOff != null && isVictimPVPOff) {
					e.setCancelled(true);
				} else {
					UtilPlayers.setCooldownTime(attacker);
					UtilPlayers.setCooldownTime(victim);
				}
			}
		}
	}

	/**
	 * Fired when a player is hit by a splash potion
	 * @param e PlayerFishEvent
	 */
	@EventHandler(ignoreCancelled = true)
	public void onPotionSplash(PotionSplashEvent e) {
		if (blockedWorlds.contains(e.getEntity().getWorld().getName())) return;


		if(e.getPotion().getShooter() instanceof Player) {
			Collection<LivingEntity> affected = e.getAffectedEntities();
			for(LivingEntity entity : affected) {
				if(entity instanceof Player victim) {
					if (PVPUtil.isEffectsPositive(e.getPotion().getEffects())) return;

					Player attacker = (Player) e.getPotion().getShooter();
					Boolean isAttackerPVPOff = pvpPlayers.get(attacker.getUniqueId());
					Boolean isVictimPVPOff = pvpPlayers.get(victim.getUniqueId());
					if(attacker != victim) {
						if(isAttackerPVPOff) {
							for(LivingEntity livingEntity : affected){
								if(livingEntity instanceof Player && livingEntity != attacker){
									e.setIntensity(livingEntity, 0);
								}
							}
							UtilChat.send(attacker, "PVP_DISABLED");

						} else if(isVictimPVPOff != null && isVictimPVPOff) {
							for(LivingEntity ent : affected){
								if(ent instanceof Player && ent != attacker){
									e.setIntensity(ent, 0);
								}
							}
							UtilChat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
						} else {
							UtilPlayers.setCooldownTime(attacker);
							UtilPlayers.setCooldownTime(victim);
						}
					}
				}
			}
		}
	}

	/**
	 * Fired when a player is hit by a lingering potion cloud
	 * @param e AreaEffectCloudApplyEvent
	 */
	@EventHandler(ignoreCancelled = true)
	//fired when lingering potion cloud is active
	public void onCloudEffects(AreaEffectCloudApplyEvent e) {
		if (blockedWorlds.contains(e.getEntity().getWorld().getName())) return;

		if(e.getEntity().getSource() instanceof Player) {
			for (LivingEntity entity : e.getAffectedEntities()) {
				if (entity instanceof Player victim) {
					if (PVPUtil.isEffectsPositive(e.getEntity().getBasePotionData().getType().getEffectType())) return;

					Player attacker = (Player) e.getEntity().getSource();
					if (PVPUtil.checkPVPStates(attacker, victim))
						e.setCancelled(true);
				}
			}
		}
	}

	/**
	 * Fired when a player is hit by a trident
	 * @param e EntityDamageByEntityEvent
	 */
	@EventHandler(ignoreCancelled = true)
	public void onPlayerFishing (PlayerFishEvent e) {
		if (blockedWorlds.contains(e.getPlayer().getWorld().getName())) return;


		if (e.getCaught() instanceof final Player victim) {
			final Player attacker = e.getPlayer();

			if (attacker.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD ||
					attacker.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD) {
				if (PVPUtil.checkPVPStates(attacker, victim))
					e.setCancelled(true);
			}
		}
	}

	/**
	 * Fired when a player is hit by a trident
	 * @param e EntityDamageByEntityEvent
	 */
	@EventHandler(ignoreCancelled = true)
	public void onLightningStrike(LightningStrikeEvent e){
		if(e.getCause() == LightningStrikeEvent.Cause.TRIDENT){
			e.getLightning().setMetadata("TRIDENT", new FixedMetadataValue(
					plugin, e.getLightning().getLocation()
			));
		}
	}
}