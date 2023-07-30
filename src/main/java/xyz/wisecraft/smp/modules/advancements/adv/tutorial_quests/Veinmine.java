package xyz.wisecraft.smp.modules.advancements.adv.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import wtf.choco.veinminer.api.event.player.PlayerVeinMineEvent;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;

import java.util.Objects;

public class Veinmine extends BaseAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "veinmine");


  public Veinmine(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.IRON_PICKAXE, "Veinminer!", AdvancementFrameType.TASK, true, true, x, y , "Use veinminer by crouching and breaking an ore!"), parent, 1);

    registerEvent(PlayerVeinMineEvent.class, e -> {

      Player p = e.getPlayer();
      if (!e.isCancelled()) {
        incrementProgression(p);
      }
    });
  }
}