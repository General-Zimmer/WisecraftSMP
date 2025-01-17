package xyz.wisecraft.smp.modules.cropharvester.events;

import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PrepareCropHarvestEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private final Ageable block;

    public PrepareCropHarvestEvent(@NotNull Player who, @NotNull Ageable block, boolean async) {
        super(who, async);
        this.block = block;
    }

    public PrepareCropHarvestEvent(@NotNull Player who, @NotNull Ageable block) {
        this(who, block, false);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {return handlers;}
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelState) {
        this.cancelled = cancelState;
    }

    @SuppressWarnings("unused")
    public Ageable getBlock() {
        return block;
    }
}
