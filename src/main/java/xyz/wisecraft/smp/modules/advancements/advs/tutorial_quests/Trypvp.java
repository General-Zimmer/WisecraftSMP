package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Trypvp extends BaseAdvancement  {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "trypvp");


  public Trypvp(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.DIAMOND_SWORD, "TO WAAAAGHH!", AdvancementFrameType.TASK, true, true, x, y , "Enable pvp with /pvp"), parent, 1);
  }
}