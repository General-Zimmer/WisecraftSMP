package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class Sethome extends BaseAdvancement implements VanillaVisibility {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "sethome");


  public Sethome(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.LIGHT_BLUE_BED, "Find your home!", AdvancementFrameType.TASK, true, true, x, y , "You now have /home!"), parent, 1);

    registerEvent(PlayerCommandPreprocessEvent.class, e -> {
      if (e.isCancelled()) return;

      String cmd = e.getMessage().toLowerCase().split(" ")[0];

      if (cmd.equalsIgnoreCase("/sethome")) {
        incrementProgression(e.getPlayer());
      }

    });

  }
  @Override
  public void onGrant(@NotNull Player player, boolean giveRewards) {
    super.onGrant(player, giveRewards);
    UtilAdv.sendAdvancementGrantedAnnouncementDiscord(player, this);
  }
}