package xyz.wisecraft.smp.modules.heirloom.recipes;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.wisecraft.smp.WisecraftSMP;

import java.util.ArrayList;
import java.util.List;

public abstract class GeneralRune {

    public final static NamespacedKey runeKey = new NamespacedKey(WisecraftSMP.getInstance(), "general_rune");

    @Getter
    private static List<Recipe> recipe;
    @Getter
    private static ItemStack rune;

    // todo future me or Daniel, please get a better name for this. I'm begging me.
    // todo Also, maybe use adventure text component for name. I'm not sure if that's possible (It is copilot), but it would be cool. (Yes it would, copilot)
    public static List<Recipe> setupRune() {
        recipe = new ArrayList<>();
        craftRecipe();
        return recipe;
    }

    private static void craftRecipe() {
        GeneralRune.rune = new ItemStack(Material.STONE);

        ItemMeta meta = GeneralRune.rune.getItemMeta();
        meta.setDisplayName("Rune");
        meta.getPersistentDataContainer().set(runeKey, PersistentDataType.STRING, "general_rune");


        GeneralRune.rune.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(runeKey, GeneralRune.rune);
        recipe.shape("NDN", "DND", "NDN");
        recipe.setIngredient('D', new RecipeChoice.MaterialChoice.MaterialChoice.MaterialChoice(Material.DIAMOND_BLOCK, Material.DIAMOND));
        recipe.setIngredient('N', new RecipeChoice.MaterialChoice.MaterialChoice.MaterialChoice(Material.NETHERITE_BLOCK, Material.AIR));
        GeneralRune.recipe.add(recipe);
    }

}
