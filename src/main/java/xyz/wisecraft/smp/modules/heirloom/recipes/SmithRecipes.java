package xyz.wisecraft.smp.modules.heirloom.recipes;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BowHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;

import java.util.ArrayList;
import java.util.List;

public abstract class SmithRecipes {

    @Getter
    private static List<Recipe> recipe;
    public static final NamespacedKey smithKey = new NamespacedKey(WisecraftSMP.getInstance(), "smithing_heirloom");

    public static List<Recipe> setupSmithRecipes() {
        recipe = new ArrayList<>();
        smithRecipe();
        return recipe;
    }

    private static void smithRecipe() {
        ItemStack bow = new ItemStack(Material.BOW);
        BaseHeirloom.createHeirLoom(bow, HeirloomType.BOWHEIRLOOM, BowHeirloom.class);
        ItemMeta meta = bow.getItemMeta();
        meta.setDisplayName("Heirloom Bow");
        bow.setItemMeta(meta);

        RecipeChoice base = new RecipeChoice.MaterialChoice(Material.BOW);
        RecipeChoice template = new RecipeChoice.MaterialChoice(Material.AIR);
        RecipeChoice rune = new RecipeChoice.ExactChoice(GeneralRune.getRune());
        SmithingTransformRecipe recipe = new SmithingTransformRecipe(SmithRecipes.smithKey, bow, template, base, rune, false);
        SmithRecipes.recipe.add(recipe);
    }
}
