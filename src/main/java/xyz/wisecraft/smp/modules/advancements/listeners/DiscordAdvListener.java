package xyz.wisecraft.smp.modules.advancements.listeners;

import com.fren_gor.ultimateAdvancementAPI.events.advancement.AdvancementProgressionUpdateEvent;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import net.essentialsx.api.v2.services.discord.DiscordService;
import net.essentialsx.api.v2.services.discord.MessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class DiscordAdvListener implements Listener {


    final DiscordService apiDiscord;

    public DiscordAdvListener(@NotNull DiscordService apiDiscord) {
        this.apiDiscord = apiDiscord;
    }

    @EventHandler
    private void onUpdate(AdvancementProgressionUpdateEvent e) {

    }
}
