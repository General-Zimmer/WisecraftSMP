package xyz.wisecraft.smp.modules.advancements.advs.common_quests;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

import java.util.Objects;

public class Ledgehog extends BaseAdvancement implements VanillaVisibility {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "ledgehog");


  public Ledgehog(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.SPECTRAL_ARROW, "The hog of ledges", AdvancementFrameType.CHALLENGE, true, true, x, y , "Have 25 arrows in ya body at once and die from falling"), parent, 1);

    registerEvent(PlayerDeathEvent.class, e -> {

      Player p = e.getEntity();
      if (p.getArrowsInBody() >= 25 && Objects.requireNonNull(
              e.getDeathMessage(), "Death message is null").equalsIgnoreCase(p.getName() + " fell from a high place") ) {
        incrementProgression(p);
      }
    });
  }

}