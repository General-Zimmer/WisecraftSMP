package xyz.wisecraft.smp.modules.advancements.adv.common_quests;

import com.craftaro.ultimatetimber.events.TreeFellEvent;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class Lumberjack extends BaseAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "lumberjack");


  public Lumberjack(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.WOODEN_AXE, "Lumber Jack", AdvancementFrameType.GOAL, true, true, x, y , "I am a Jack, and I'm cutting cutting logs (1000 of them)"), parent, 1000);

    if (WisecraftSMP.getInstance().isTimberEnabled())
      registerEvent(TreeFellEvent.class, e -> UtilAdv.checkTimber(e, this));
  }
}