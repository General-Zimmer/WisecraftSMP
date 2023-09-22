package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import wtf.choco.veinminer.api.event.player.PlayerVeinMineEvent;
import xyz.wisecraft.smp.modules.advancements.AdvancementsModule;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;

public class Veinmine extends BaseAdvancement implements VanillaVisibility {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "veinmine");


  public Veinmine(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.IRON_PICKAXE, "Veinminer!", AdvancementFrameType.TASK, true, true, x, y , "Use veinminer by crouching and breaking an ore!"), parent, 1);

    if (AdvancementsModule.getModule().isVeinMinerEnabled())
      registerEvent(PlayerVeinMineEvent.class, EventPriority.MONITOR, e -> {
        if (e.isCancelled()) return;

        Player p = e.getPlayer();
        if (!e.isCancelled()) {
          incrementProgression(p);
        }
      });
  }
}