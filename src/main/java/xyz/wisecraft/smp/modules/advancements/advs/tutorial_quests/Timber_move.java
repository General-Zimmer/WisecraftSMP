package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.wisecraft.core.data.templates.Timers;
import xyz.wisecraft.smp.modules.advancements.AdvancementsModule;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.util.UtilCommon;

import java.util.Objects;

public class Timber_move extends BaseAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "timber_move");

  AdvancementsModule module = AdvancementsModule.getModule();

  public Timber_move(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.OAK_SAPLING, "You didn't mooove!", AdvancementFrameType.TASK, true, false, x, y , "Trees can be very lethal"), parent, 1);

    if (module.isTimberEnabled())
      registerEvent(PlayerDeathEvent.class, e -> {

        String deathMessage = Objects.requireNonNull(e.getDeathMessage(), "Death message is null");
        Player victim = e.getPlayer();

        if (deathMessage.equalsIgnoreCase(victim.getName() + " died")) {
          Timers times = module.getCore().getTimers().get(victim.getUniqueId());
          double secSinceVictimTimber = UtilCommon.calcCurrentSeconds(times.getTree());

          // todo check if this can be replaced with whoTimber
          if (secSinceVictimTimber < 1.7) {
            e.setDeathMessage(victim.getName() + " was crushed under their timber");
            incrementProgression(victim);
          }
        }
      });
  }
}