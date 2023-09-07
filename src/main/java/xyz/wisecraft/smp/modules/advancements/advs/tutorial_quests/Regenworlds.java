package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;

import java.util.ArrayList;
import java.util.List;

public class Regenworlds extends BaseAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "regenworlds");


  public Regenworlds(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.DIAMOND_ORE, "The forever regenerating lands!", AdvancementFrameType.TASK, true, true, x, y , "Use /resourceworld tp <world> to gather items!"), parent, 1);

    registerEvent(PlayerChangedWorldEvent.class, e -> {
      Player p = e.getPlayer();
      String playerWorld = p.getWorld().getName();

      List<String> regenWorlds = new ArrayList<>();
      regenWorlds.add("resource_world");
      regenWorlds.add("resource_nether");
      regenWorlds.add("resource_end");
      if (regenWorlds.contains(playerWorld)) {
        incrementProgression(p);
      }
    });
  }
}