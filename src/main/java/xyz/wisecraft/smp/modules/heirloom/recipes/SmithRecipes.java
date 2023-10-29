package xyz.wisecraft.smp.modules.heirloom.recipes;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BowHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;

import java.util.ArrayList;
import java.util.List;

public abstract class SmithRecipes {

    @Getter
    private static List<SmithingRecipe> recipe;
    public static final NamespacedKey smithKey = new NamespacedKey(WisecraftSMP.getInstance(), "smithing_heirloom");

    public static List<SmithingRecipe> setupSmithRecipes() {
        recipe = new ArrayList<>();
        bowHeirloomRecipe();
        return recipe;
    }

    private static void bowHeirloomRecipe() {
        ItemStack bow = new ItemStack(Material.BOW);
        BaseHeirloom.createHeirLoom(bow, HeirloomType.BOWHEIRLOOM, BowHeirloom.class);
        ItemMeta meta = bow.getItemMeta();
        meta.setDisplayName("Heirloom Bow");
        bow.setItemMeta(meta);

        RecipeChoice base = new RecipeChoice.MaterialChoice(Material.BOW);
        RecipeChoice template = new RecipeChoice.MaterialChoice(Material.AIR);
        RecipeChoice rune = new RecipeChoice.ExactChoice(HeirloomRunes.getGeneralRune());
        SmithingTransformRecipe recipe = new SmithingTransformRecipe(SmithRecipes.smithKey, bow, template, base, rune, false);
        SmithRecipes.recipe.add(recipe);
    }
}
