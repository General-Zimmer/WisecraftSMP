package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.palmergames.bukkit.towny.event.resident.NewResidentEvent;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Jointown extends BaseAdvancement  {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "jointown");


  public Jointown(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.LANTERN, "New face in town", AdvancementFrameType.TASK, true, true, x, y , "Join a town ", "You can also create a town with /town new <Town name> and manage it with /townmenu"), parent, 1);

    registerEvent(NewResidentEvent.class, e -> {

      Player p = e.getResident().getPlayer();

      if (p != null && isGranted(p))
        grant(p);
    });
  }
}