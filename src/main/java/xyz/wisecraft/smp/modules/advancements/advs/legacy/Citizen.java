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

public class Citizen extends MultiTasksAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.legacy_NAMESPACE, "citizen");


  public Citizen(Advancement parent) {
    super(KEY.getKey(), new AdvancementDisplay(Material.LANTERN, "New face in town", AdvancementFrameType.TASK, true, true, 1f, 1f , """
            To get Citizen:\s
            Break 4000 blocks
            place 2000 blocks
            play for 300 minutes
            Hold a diamond pickaxe"""), parent, 6301);

  }

  @Override
  public void giveReward(@NotNull Player player) {
    UtilAdv.addRole(player, "citizen");
  }
}