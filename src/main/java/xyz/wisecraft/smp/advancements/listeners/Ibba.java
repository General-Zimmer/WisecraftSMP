package xyz.wisecraft.smp.advancements.listeners;


import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.advancements.util.UtilAdv;

public class Ibba implements Listener {
    private final WisecraftSMP plugin;

    public Ibba() {
        this.plugin = WisecraftSMP.instance;
    }

    @EventHandler
    public void arrowHit(ArrowBodyCountChangeEvent e) {
        Entity n = e.getEntity();

        if (!(n instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (e.getNewAmount() >= 10) {
            NamespacedKey key = new NamespacedKey(plugin, "hedgehog");

            UtilAdv.gibCri("hedge", key, p);
        }
    }

    @EventHandler
    public void ledgehog(PlayerDeathEvent e) {

        Player p = e.getEntity();
        if (e.getDeathMessage().equalsIgnoreCase(p.getName() + " fell from a high place") & p.getArrowsInBody() >= 25) {
            NamespacedKey key = new NamespacedKey(plugin, "ledgehog");

            UtilAdv.gibCri("ledgehog", key, p);
        }
    }
}