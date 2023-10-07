package xyz.wisecraft.smp.modules.advancements.advs.common_quests;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class Hedgehog extends BaseAdvancement  {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "hedgehog");


  public Hedgehog(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.ARROW, "Hedgehog", AdvancementFrameType.CHALLENGE, true, true, x, y , "Have 10 arrows in you at once"), parent, 10);

    registerEvent(ArrowBodyCountChangeEvent.class, e -> {
      Entity n = e.getEntity();

      if (!(n instanceof Player)) return;
      Player p = (Player) e.getEntity();
      if (e.getNewAmount() > this.getProgression(p)) {
        this.setProgression(p, e.getNewAmount());
      }

    });
  }
  @Override
  public void onGrant(@NotNull Player player, boolean giveRewards) {
    super.onGrant(player, giveRewards);
    UtilAdv.sendAdvancementGrantedAnnouncementDiscord(player, this);
  }
}