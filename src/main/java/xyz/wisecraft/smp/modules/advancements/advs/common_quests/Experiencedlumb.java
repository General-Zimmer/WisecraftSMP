package xyz.wisecraft.smp.modules.advancements.advs.common_quests;

import com.craftaro.ultimatetimber.events.TreeFellEvent;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import xyz.wisecraft.smp.modules.advancements.AdvancementsModule;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class Experiencedlumb extends BaseAdvancement implements VanillaVisibility {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "experiencedlumb");


  public Experiencedlumb(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.STONE_AXE, "Experienced lumber", AdvancementFrameType.CHALLENGE, true, true, x, y , "One heck of a cutter. You've cut 5000 trees!"), parent, 5000);

    if (AdvancementsModule.getModule().isTimberEnabled())
      registerEvent(TreeFellEvent.class, e -> UtilAdv.checkTimber(e, this));
  }

}