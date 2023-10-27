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
        recipe.shape("ANA", "NLN", "ANA");
        recipe.setIngredient('A', new RecipeChoice.MaterialChoice(Material.LAPIS_BLOCK));
        recipe.setIngredient('N', new RecipeChoice.MaterialChoice(Material.NETHERITE_INGOT, Material.DIAMOND_BLOCK));
        recipe.setIngredient('L', new RecipeChoice.MaterialChoice(Material.MAGMA_CREAM, Material.LAVA_BUCKET));
        GeneralRune.recipe.add(recipe);
    }

}
