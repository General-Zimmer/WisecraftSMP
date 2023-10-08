package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.palmergames.bukkit.towny.event.plot.changeowner.PlotClaimEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class Gettownplot extends BaseAdvancement  {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "gettownplot");


  public Gettownplot(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.GRASS_BLOCK, "Rent a plot!", AdvancementFrameType.TASK, true, false, x, y , "By using /plot claim and manage it with /plotmenu"), parent, 1);

    registerEvent(PlotClaimEvent.class, e -> {
      Player p = (e.getNewResident() != null) ? e.getNewResident().getPlayer() : null;
      if (p!= null && !isGranted(p))
        grant(p);
    });
  }

}