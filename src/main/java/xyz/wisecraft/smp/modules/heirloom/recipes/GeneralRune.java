package xyz.wisecraft.smp.modules.heirloom.recipes;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.wisecraft.smp.WisecraftSMP;

public abstract class GeneralRune {

    public final static NamespacedKey runeKey = new NamespacedKey(WisecraftSMP.getInstance(), "general_rune");

    @Getter
    private static ShapedRecipe recipe;

    public static void urMom() {
        ItemStack itemStack = new ItemStack(Material.STONE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("General Rune");
        meta.getPersistentDataContainer().set(runeKey, PersistentDataType.STRING, "general_rune");


        itemStack.setItemMeta(meta);
        recipe = new ShapedRecipe(runeKey, itemStack);
        recipe.shape("DDD", "DDD", "DDN");
        recipe.setIngredient('D', Material.DIAMOND);
    }
}
