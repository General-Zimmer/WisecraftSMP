package xyz.wisecraft.smp.modules.advancements.listeners;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.events.advancement.AdvancementProgressionUpdateEvent;
import net.ess3.provider.AbstractAchievementEvent;
import net.essentialsx.api.v2.services.discord.DiscordService;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.wisecraft.smp.modules.advancements.AdvancementsModule;

import java.util.Objects;

public class DiscordAdvListener implements Listener {


    final DiscordService apiDiscord;

    public DiscordAdvListener(@NotNull DiscordService apiDiscord) {
        this.apiDiscord = apiDiscord;
    }

    @EventHandler
    public void onUpdate(AdvancementProgressionUpdateEvent e) {
        Player p = Bukkit.getServer().getPlayer(Objects.requireNonNull(e.getTeamProgression().getAMember(), "PLAYER WASN'T ONLINE!"));
        if (p == null) throw new IllegalStateException("Player was null");

        int maxProgr = e.getAdvancement().getMaxProgression();
        if (!(e.getOldProgression() < maxProgr) || !(e.getNewProgression() >= maxProgr)) return;

        Advancement adv = e.getAdvancement();
        boolean gameRule = Boolean.TRUE.equals(p.getWorld().getGameRuleValue(GameRule.ANNOUNCE_ADVANCEMENTS));
        @Nullable DiscordService apiDiscord = AdvancementsModule.getModule().getApiDiscord();


        if (apiDiscord != null && gameRule && adv.getDisplay().doesAnnounceToChat()) {

            BaseComponent[] comText = adv.getAnnounceMessage(p);
            String AdvMessage = comText[2].toPlainText();
            AbstractAchievementEvent event = new AbstractAchievementEvent(p, AdvMessage);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

}
