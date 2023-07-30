package xyz.wisecraft.smp.modules.advancements.adv.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;

public class Sethome extends BaseAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "sethome");


  public Sethome(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.LIGHT_BLUE_BED, "Find your home!", AdvancementFrameType.TASK, true, true, x, y , "You now have /home!"), parent, 1);

    registerEvent(PlayerCommandPreprocessEvent.class, e -> {
      if (e.isCancelled()) return;

      if (e.getMessage().equalsIgnoreCase("/sethome")) {
        incrementProgression(e.getPlayer());
      }

    });

  }
}