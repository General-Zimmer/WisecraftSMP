package xyz.wisecraft.smp.modules.advancements.advs.legacy;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.MultiTasksAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class Nobility extends MultiTasksAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.legacy_NAMESPACE, "nobility");


  public Nobility(Advancement parent) {
    super(KEY.getKey(), new AdvancementDisplay(Material.SOUL_LANTERN, "Nobility", AdvancementFrameType.TASK, true, true, 2f, 1f , """
            To get Citizen:\s
            Break 15000 blocks
            place 10000 blocks
            play for 1200 minutes
            Hold a elytra"""), parent, 26201);
  }

  @Override
  public void giveReward(@NotNull Player player) {
    UtilAdv.addRole(player, "noble");
  }
}