package xyz.wisecraft.smp.advancements.events;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.advancements.util.Methods;

import java.util.ArrayList;
import java.util.List;

public class QuestEvents implements Listener {

	private final WisecraftSMP plugin;
	private final LuckPerms luck;

	public QuestEvents() {
		this.plugin = WisecraftSMP.instance;
		this.luck = WisecraftSMP.luck;
	}

	@EventHandler
	public void roles(PlayerAdvancementDoneEvent e) {

		Advancement adv = e.getAdvancement();
		Player p = e.getPlayer();

		//todo replace these checks with a method
		NamespacedKey keyCit = new NamespacedKey(plugin, "citizen");
		Advancement advCitizen = Bukkit.getAdvancement(keyCit);
		//Citzen check
		if (adv.equals(advCitizen))
			Methods.addRole(plugin, luck, p, "citizen");

		NamespacedKey keyNob = new NamespacedKey(plugin, "nobility");
		Advancement advNoble = Bukkit.getAdvancement(keyNob);
		//Noble check
		if (adv.equals(advNoble))
			Methods.addRole(plugin, luck, p, "noble");
	}

	@EventHandler
	public void resourceworld(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		String wld = p.getWorld().getName();

		List<String> wlds = new ArrayList<>();
		wlds.add("resource_world");
		wlds.add("resource_nether");
		wlds.add("resource_end");
		if (wlds.contains(wld)) {
			NamespacedKey key = new NamespacedKey(plugin, "regenworld");
			Methods.gibCri("resourceworld", key, p);
		}
	}


	@SuppressWarnings("ConstantConditions")
	@EventHandler
	public void Dying(PlayerDeathEvent e) {
		Player p = e.getEntity().getPlayer();

		if (e.getDeathMessage().equalsIgnoreCase(p.getName() + " experienced kinetic energy") ) {
			NamespacedKey key = new NamespacedKey(plugin, "accident_flying");

			Methods.gibCri("wall", key, p);
		}

	}

	@EventHandler
	public void onCraft(CraftItemEvent e) {


		ItemStack res = e.getRecipe().getResult();
		ItemStack bundle = new ItemStack(Material.BUNDLE);
		ItemStack elytra = new ItemStack(Material.ELYTRA);

		if (bundle.equals(res)) {
			Player p = Bukkit.getPlayer(e.getWhoClicked().getUniqueId());
			NamespacedKey key = new NamespacedKey(plugin, "bundle");
			Methods.gibCri("bundle", key, p);
		}
		if (elytra.equals(res)) {
			Player p = Bukkit.getPlayer(e.getWhoClicked().getUniqueId());
			NamespacedKey key = new NamespacedKey(plugin, "craft_elytra");
			Methods.gibCri("elytra", key, p);
		}
	}

	@EventHandler
	public void level(PlayerLevelChangeEvent e) {
		Player p = e.getPlayer();
		int lvl = e.getNewLevel();

		switch (lvl) {
			case 100 -> {
				NamespacedKey key = new NamespacedKey(plugin, "hugexp");
				Methods.gibCri("lvl100", key, p);
			}
			case 250 -> {
				NamespacedKey key = new NamespacedKey(plugin, "massivexp");
				Methods.gibCri("lvl250", key, p);
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	@EventHandler
	public void RoleAdvMissingCheck(PlayerJoinEvent e) {

		new BukkitRunnable() {
			@Override
			public void run() {
				Player p = e.getPlayer();

				NamespacedKey keyCit = new NamespacedKey(plugin, "citizen");
				Advancement advCitizen = Bukkit.getAdvancement(keyCit);
				NamespacedKey keyNob = new NamespacedKey(plugin, "nobility");
				Advancement advNoble = Bukkit.getAdvancement(keyNob);

				Node citizenN = Methods.buildNode(plugin, "citizen");
				Node nobleN = Methods.buildNode(plugin, "noble");

				boolean advC = p.getAdvancementProgress(advCitizen).isDone();
				boolean roleC = luck.getPlayerAdapter(Player.class).getUser(p).getNodes().contains(citizenN);

				//Citzen check
				if (!advC && roleC)
					Methods.removeRole(plugin, luck, p, "citizen");
				else if (advC && !roleC)
					//sync with main
					new BukkitRunnable() {
						@Override
						public void run() {
							p.getAdvancementProgress(advCitizen).revokeCriteria("citizen");
						}
					}.runTask(plugin);


				//Noble check
				if (!p.getAdvancementProgress(advNoble).isDone() &&
						luck.getPlayerAdapter(Player.class).getUser(p).getNodes().contains(nobleN))
					Methods.removeRole(plugin, luck, p, "noble");
				else if (p.getAdvancementProgress(advNoble).isDone() &&
						!luck.getPlayerAdapter(Player.class).getUser(p).getNodes().contains(nobleN))
					//sync with main
					new BukkitRunnable() {
						@Override
						public void run() {
							p.getAdvancementProgress(advNoble).revokeCriteria("noble");
						}
					}.runTask(plugin);
			}
		}.runTask(plugin);


	}


}
