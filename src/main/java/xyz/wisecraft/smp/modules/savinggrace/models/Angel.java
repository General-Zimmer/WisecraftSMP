package xyz.wisecraft.smp.modules.savinggrace.models;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.User;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.SimpleTextInput;
import lombok.Getter;
import net.ess3.api.IEssentials;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.savinggrace.enums.PlayerState;
import xyz.wisecraft.smp.modules.savinggrace.storage.AngelStorage;
import xyz.wisecraft.smp.modules.tutorialstuff.util.UtilRandom;

import java.util.*;
import java.util.logging.Level;

import static com.earth2me.essentials.I18n.tl;
import static xyz.wisecraft.smp.modules.tutorialstuff.util.UtilRandom.getToolTypes;

/**
 * Class for the Angel object.
 */
public class Angel {

    private boolean isGraceInactive = true;

    /**
     * -- GETTER --
     *  Gets graces
     *
     */
    @Getter
    private int graces;
    @Getter
    private PlayerState hasGraceRecently = PlayerState.STALE;
    private static final WisecraftSMP plugin = WisecraftSMP.getInstance();
    /**
     * Get the amount of graces the player has left
     */
    public Angel(String starterKit, IEssentials ess, Player p) {
        this.resetGrace(p.hasPermission("wisecraft.donator"));
    }

    /**
     * Give player their saved gear
     */
    public void giveGraceMessage(Player p) {

        if (hasGraceRecently == PlayerState.STARTER_KIT) {
            p.sendMessage("You have been granted some new items.");
            p.sendMessage(ChatColor.BLUE + "You didn't /sethome!");
        } else if (hasGraceRecently == PlayerState.GRACE_RECENTLY && this.getGraces() > 0) {
            p.sendMessage(ChatColor.AQUA + "Your gear have been saved. You have " + this.getGraces() + " graces left!");
        } else {
            p.sendMessage("You have no graces left, no gear was saved.");
        }

        hasGraceRecently = PlayerState.STALE;
    }


    /**
     * Give starter gear and teleport to tutorial
     */
    public void giveStarter(PlayerDeathEvent e, IEssentials ess) {

        ArrayList<ItemStack> kitItems;
        try {
            Kit kit = new Kit("starter", ess);
            kitItems = new ArrayList<>(getKitItems(ess, ess.getUser(e.getPlayer()), kit.getItems()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        @NotNull List<ItemStack> itemKeep = e.getItemsToKeep();
        kitItems.forEach(item -> itemKeep.add(item.clone()));

        @NotNull List<ItemStack> drops = e.getDrops();
        Iterator<ItemStack> kitIterator = kitItems.iterator();
        while (kitIterator.hasNext() && !drops.isEmpty()) {
            ItemStack item = kitIterator.next();
            Iterator<ItemStack> dropIterator = drops.iterator();
            loopThroughDrops(dropIterator, item, kitIterator);
        }

        hasGraceRecently = PlayerState.STARTER_KIT;
    }

    /**
     * Loop through drops and remove items that are the same as the kit item
     * @param dropIterator The iterator for the drops
     * @param item The item to compare to
     * @param kitIterator The iterator for the kit items
     */
    private void loopThroughDrops(Iterator<ItemStack> dropIterator, ItemStack item, Iterator<ItemStack> kitIterator) {
        // drop loop
        while (dropIterator.hasNext() && item.getAmount() > 0) {
            ItemStack drop = dropIterator.next();
            if (!drop.getType().equals(item.getType())) continue;

            // Logic
            if (drop.getAmount() < item.getAmount()) {
                item.setAmount(item.getAmount() - drop.getAmount());
                dropIterator.remove();
                if (dropIterator.hasNext()) loopThroughDrops(dropIterator, item, kitIterator);
            } else if (drop.getAmount() == item.getAmount()) {
                kitIterator.remove();
                dropIterator.remove();
            } else {
                drop.setAmount(drop.getAmount() - item.getAmount());
                kitIterator.remove();
            }

        }
    }

    public void saveGear(PlayerDeathEvent e) {
        if (this.getGraces() <= 0) {return;}
        PlayerInventory inv = e.getPlayer().getInventory();

        HashMap<Integer, ItemStack> gear = getArmorToSave(inv);
        gear.putAll(getToolsToSave(inv));

        gear.forEach((slot, item) -> {
            if (item != null) {
                e.getDrops().remove(item);
                e.getItemsToKeep().add(item);
            }
        });

        graces--;
        hasGraceRecently = PlayerState.GRACE_RECENTLY;
        this.safeDelete(e.getPlayer().getUniqueId());
    }

    private HashMap<Integer, ItemStack> getToolsToSave(PlayerInventory inv) {
        HashMap<Integer, ItemStack> tools = new HashMap<>(4);
        ArrayList<Material> toolTypes = getToolTypes();


        int currentAmount = 0;
        //Tool save
        for(int i = 0; i < 9; i++) {
            ItemStack item = inv.getItem(i);

            if (item != null && currentAmount < 4 && toolTypes.contains(item.getType())) {
                //add tool for recovery
                tools.put(i, item);
                //grace limit
                currentAmount++;
            }
        }
        return tools;
    }

    /**
     * Save armor the player was wearing
     * <p>
     * Note: Indexes 36 through 39 refer to the armor slots. 36 is boots, 37 is leggings, 38 is chestplate, and 39 is helmet
     * @param inv The inventory of the player
     */
    private HashMap<Integer, ItemStack> getArmorToSave(PlayerInventory inv) {
        HashMap<Integer, ItemStack> armor = new HashMap<>(4);
        ArrayList<Material> containers = UtilRandom.getContainerTypes();

        for (int i = 36; i <= 39; i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && !containers.contains(item.getType())) {
                armor.put(i, item);
            }
        }
        return armor;
    }

    /**
     * Reset graces for a player
     * @param hasPerm Whether or not the player has the donator permission
     */
    public void resetGrace(boolean hasPerm) {
        if (hasPerm)
            this.graces = 2;
        else
            this.graces = 1;
    }

    /**
     * Deletes a player's angel if they've left the server
     * @param UUID The UUID of the player to delete
     */
    public void safeDelete(UUID UUID) {

        if (this.isGraceInactive()) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    Player player = Bukkit.getPlayer(UUID);
                    if (player == null) {
                        AngelStorage.getAngels().remove(UUID);
                        return;
                    }

                    Angel angel = AngelStorage.getAngels().get(UUID);

                    angel.resetGrace(player.hasPermission("wisecraft.donator"));
                    angel.setGraceActive(false);
                    plugin.getLogger().log(Level.INFO, "Grace timer stopped for: " + Bukkit.getPlayer(UUID));
                    player.sendMessage(ChatColor.AQUA + "Your graces have been reset");
                }


            }.runTaskLater(plugin, 20*30);
            //}.runTaskLater(plugin, 20*60*60*2); // 2 hour
            plugin.getLogger().log(Level.INFO, "Grace timer started for: " + Bukkit.getPlayer(UUID));
            this.setGraceActive(true);
        }
    }

    /**
     * Gets the items in a kit. Taken from Essentials
     * @param ess The instance of Essentials
     * @param user The user to get the kit for
     * @param items The items in the kit
     * @return The items in the kit
     * @throws Exception If the kit doesn't exist
     */
    private List<ItemStack> getKitItems(IEssentials ess, User user, List<String> items) throws Exception {
            final IText input = new SimpleTextInput(items);
            final IText output = new KeywordReplacer(input, user.getSource(), ess, true, true);

            final boolean allowUnsafe = ess.getSettings().allowUnsafeEnchantments();
            final List<ItemStack> itemList = new ArrayList<>();
            for (final String kitItem : output.getLines()) {

                final ItemStack stack;

                if (kitItem.startsWith("@")) {
                    if (ess.getSerializationProvider() == null) {
                        ess.getLogger().log(Level.WARNING, tl("kitError3", "starter", user.getName()));
                        continue;
                    }
                    stack = ess.getSerializationProvider().deserializeItem(Base64Coder.decodeLines(kitItem.substring(1)));
                } else {
                    final String[] parts = kitItem.split(" +");
                    final ItemStack parseStack = ess.getItemDb().get(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : 1);

                    if (parseStack.getType() == Material.AIR) {
                        continue;
                    }

                    final MetaItemStack metaStack = new MetaItemStack(parseStack);

                    if (parts.length > 2) {
                        // We pass a null sender here because kits should not do perm checks
                        metaStack.parseStringMeta(null, allowUnsafe, parts, 2, ess);
                    }

                    stack = metaStack.getItemStack();
                }

                itemList.add(stack);
            }
            return itemList;
    }

    public void tpTutorial(Player p) {
        World tut = Bukkit.getWorld("tutorial");
        if (tut != null)
            //todo Need a tick delay otherwise they will teleport to essentials spawn. Need to figure out why
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getServer().getWorlds().forEach(world -> plugin.getLogger().log(Level.INFO, world.getName()));
                    Location loc = new Location(tut, 5.54, 204.00, 8.51, -85.80f, -1.58f);
                    if (!p.teleport(loc)) {
                        plugin.getLogger().log(Level.INFO, "teleport failed for " + p.getName() + " to tutorial. Using backup method");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + p.getName() + " tutorial");
                    } else
                        plugin.getLogger().log(Level.INFO, "Teleported " + p.getName() + " to tutorial");
                }
            }.runTaskLater(plugin, 2);
    }

    public boolean isGraceInactive() {return this.isGraceInactive;}

    /**
     * Sets whether or not a player has a grace timer active
     * @param bool Whether or not a player has a grace timer active
     */
    public void setGraceActive(boolean bool) {this.isGraceInactive = bool;}

}
