package xyz.wisecraft.smp.modules.heirloom.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;

import static xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom.heirloomTypeKey;

public class SmithingListener implements Listener {

    /**
     * Prevents heirlooms from being smithed and adds the enchantments from the input item to the output item
     * @param e The event
     */
    @EventHandler
    public void onSmithing(PrepareSmithingEvent e) {
        ItemStack in = e.getInventory().getInputEquipment();
        ItemStack out = e.getInventory().getResult();
        if (in == null) return;

        if (out != null) {
            ItemMeta meta = out.getItemMeta();
            if (meta == null) return;
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if (pdc.get(heirloomTypeKey, PersistentDataType.STRING) == null) return;

            in.getItemMeta().getEnchants().forEach((enchant, level) -> {
                if (enchant != Enchantment.MENDING)
                    meta.addEnchant(enchant, level, true);
            });
            out.setItemMeta(meta);
            e.setResult(out);
        }
    }
}
