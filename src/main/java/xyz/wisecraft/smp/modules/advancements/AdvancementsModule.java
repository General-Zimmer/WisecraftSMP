package xyz.wisecraft.smp.modules.advancements;

import com.craftaro.ultimatetimber.UltimateTimber;
import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.util.CoordAdapter;
import net.luckperms.api.LuckPerms;
import org.bukkit.Material;
import wtf.choco.veinminer.VeinMinerPlugin;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.adv.common_quests.*;
import xyz.wisecraft.smp.modules.advancements.adv.legacy.*;
import xyz.wisecraft.smp.modules.advancements.adv.tutorial_quests.*;
import xyz.wisecraft.smp.modules.advancements.listeners.LegacyRoles;
import xyz.wisecraft.smp.modules.advancements.listeners.TimberListeners;
import xyz.wisecraft.smp.modules.advancements.threads.GibRoles;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.io.File;
import java.util.Objects;

/**
 * This class is the module class for the Advancements module.
 */
public class AdvancementsModule implements xyz.wisecraft.smp.modulation.ModuleClass {

    private static AdvancementsModule module = null;
    private UltimateAdvancementAPI api;
    private AdvancementTab tutorial_quests;
    private AdvancementTab common_quests;
    private AdvancementTab legacy;
    private final boolean isTimberEnabled = setupDependency("UltimateTimber", UltimateTimber.class) != null;
    private final LuckPerms luck = setupDependency(LuckPerms.class);
    private final WisecraftCoreApi core = setupDependency(WisecraftCoreApi.class);
    private final boolean isVeinMinerEnabled = setupDependency("VeinMiner", VeinMinerPlugin.class) != null;


    public AdvancementsModule() {
        module = this;
    }

    @Override
    public boolean startModule() {


        if (!isModuleEnabled() || !hasAllHardDependencies() || plugin.getIsTesting()) return false;

        onEnable();
        registerEvents();
        registerCommands();
        return true;
    }

    @Override
    public void onEnable() {

        plugin.getAdv().enableSQLite(new File(plugin.getServer().getWorldContainer().getAbsolutePath() + "/world", "advancements.db"));
        api = UltimateAdvancementAPI.getInstance(plugin);
        initializeTabs();

        tutorial_quests.automaticallyShowToPlayers();
        tutorial_quests.automaticallyGrantRootAdvancement();
        common_quests.automaticallyShowToPlayers();
        common_quests.automaticallyGrantRootAdvancement();



    }

    @Override
    public void registerEvents() {
        if (isTimberEnabled && core != null)
            plugin.getServer().getPluginManager().registerEvents(new TimberListeners(core), plugin);


        if (luck == null)
            return;

        // Check for new citizens. This is async right after this step.
        String servName = OtherStorage.getServer_name();
        if (servName.equalsIgnoreCase("l-gp1")  || servName.equalsIgnoreCase("legacy")) {
            new GibRoles(core, luck).runTaskTimer(plugin, 20*60*10, 20*60*10);
            plugin.getServer().getPluginManager().registerEvents(new LegacyRoles(luck), plugin);
            legacy.automaticallyShowToPlayers();
            legacy.automaticallyGrantRootAdvancement();
        }

    }

    @Override
    public void onDisable() {
        plugin.getAdv().disable();
    }

    @Override
    public void registerCommands() {
        Objects.requireNonNull(plugin.getCommand("autoroles"), "command autoroles isn't registered").setExecutor(new Command(core, luck));

    }

    public void initializeTabs() {
        tutorial_quests = api.createAdvancementTab(AdvancementTabNamespaces.tutorial_quests_NAMESPACE);
        AdvancementKey tutorialquestsKey = new AdvancementKey(tutorial_quests.getNamespace(), "tutorialquests");
        CoordAdapter adaptertutorial_quests = CoordAdapter.builder().add(tutorialquestsKey, 0f, 0f).add(Craft_bundle.KEY, 2f, -1f).add(Craft_elytra.KEY, 1f, -1f).add(Regenworlds.KEY, 2f, 0f).add(First_timber.KEY, 3f, 1f).add(Timber_move.KEY, 4f, 1f).add(Veinmine.KEY, 3f, 0f).add(Old_timer.KEY, -1f, 0f).add(Sethome.KEY, 1f, 0f).build();
        common_quests = api.createAdvancementTab(AdvancementTabNamespaces.common_quests_NAMESPACE);
        AdvancementKey intro_commonKey = new AdvancementKey(common_quests.getNamespace(), "intro_common");
        CoordAdapter adaptercommon_quests = CoordAdapter.builder().add(tutorialquestsKey, 0f, 0f).add(Craft_bundle.KEY, 2f, -1f).add(Craft_elytra.KEY, 1f, -1f).add(Regenworlds.KEY, 2f, 0f).add(First_timber.KEY, 3f, 1f).add(Timber_move.KEY, 4f, 1f).add(Veinmine.KEY, 3f, 0f).add(Old_timer.KEY, -1f, 0f).add(Sethome.KEY, 1f, 0f).add(intro_commonKey, 0f, 0f).add(Lumberjack.KEY, 1f, 1f).add(Experiencedlumb.KEY, 2f, 1f).add(Expertlumb.KEY, 3f, 1f).add(Juggerjack.KEY, 4f, 1f).add(Flying_accident.KEY, 1f, -1f).add(Hedgehog.KEY, 1f, -2f).add(Ledgehog.KEY, 2f, -2f).add(Hugexp.KEY, 1f, -3f).add(Massivexp.KEY, 2f, -3f).add(Omega_xp.KEY, 3f, -3f).build();
        legacy = api.createAdvancementTab(AdvancementTabNamespaces.legacy_NAMESPACE);
        RootAdvancement tutorialquests = new RootAdvancement(tutorial_quests, tutorialquestsKey.getKey(), new AdvancementDisplay(Material.KNOWLEDGE_BOOK, "Welcome to tutorial quests!", AdvancementFrameType.TASK, false, false, adaptertutorial_quests.getX(tutorialquestsKey), adaptertutorial_quests.getY(tutorialquestsKey), "Here are advancements that can help you get started!"),"textures/block/lime_shulker_box.png",1);
        Craft_bundle craft_bundle = new Craft_bundle(tutorialquests,adaptertutorial_quests.getX(Craft_bundle.KEY), adaptertutorial_quests.getY(Craft_bundle.KEY));
        Craft_elytra craft_elytra = new Craft_elytra(tutorialquests,adaptertutorial_quests.getX(Craft_elytra.KEY), adaptertutorial_quests.getY(Craft_elytra.KEY));
        Sethome sethome = new Sethome(tutorialquests,adaptertutorial_quests.getX(Sethome.KEY), adaptertutorial_quests.getY(Sethome.KEY));
        Regenworlds regenworlds = new Regenworlds(sethome,adaptertutorial_quests.getX(Regenworlds.KEY), adaptertutorial_quests.getY(Regenworlds.KEY));
        First_timber first_timber = new First_timber(regenworlds,adaptertutorial_quests.getX(First_timber.KEY), adaptertutorial_quests.getY(First_timber.KEY));
        Timber_move timber_move = new Timber_move(first_timber,adaptertutorial_quests.getX(Timber_move.KEY), adaptertutorial_quests.getY(Timber_move.KEY));
        Veinmine veinmine = new Veinmine(regenworlds,adaptertutorial_quests.getX(Veinmine.KEY), adaptertutorial_quests.getY(Veinmine.KEY));
        Old_timer old_timer = new Old_timer(tutorialquests,adaptertutorial_quests.getX(Old_timer.KEY), adaptertutorial_quests.getY(Old_timer.KEY));
        tutorial_quests.registerAdvancements(tutorialquests ,craft_bundle ,craft_elytra ,regenworlds ,first_timber ,timber_move ,veinmine ,old_timer ,sethome );
        RootAdvancement intro_common = new RootAdvancement(common_quests, intro_commonKey.getKey(), new AdvancementDisplay(Material.BOOK, "Common advancements!", AdvancementFrameType.TASK, false, false, adaptercommon_quests.getX(intro_commonKey), adaptercommon_quests.getY(intro_commonKey), "Here's a bunch of advancements you can do! Some of them can be a bit tricky"),"textures/block/light_blue_shulker_box.png",1);
        Lumberjack lumberjack = new Lumberjack(intro_common,adaptercommon_quests.getX(Lumberjack.KEY), adaptercommon_quests.getY(Lumberjack.KEY));
        Experiencedlumb experiencedlumb = new Experiencedlumb(lumberjack,adaptercommon_quests.getX(Experiencedlumb.KEY), adaptercommon_quests.getY(Experiencedlumb.KEY));
        Expertlumb expertlumb = new Expertlumb(experiencedlumb,adaptercommon_quests.getX(Expertlumb.KEY), adaptercommon_quests.getY(Expertlumb.KEY));
        Juggerjack juggerjack = new Juggerjack(expertlumb,adaptercommon_quests.getX(Juggerjack.KEY), adaptercommon_quests.getY(Juggerjack.KEY));
        Flying_accident flying_accident = new Flying_accident(intro_common,adaptercommon_quests.getX(Flying_accident.KEY), adaptercommon_quests.getY(Flying_accident.KEY));
        Hedgehog hedgehog = new Hedgehog(intro_common,adaptercommon_quests.getX(Hedgehog.KEY), adaptercommon_quests.getY(Hedgehog.KEY));
        Ledgehog ledgehog = new Ledgehog(hedgehog,adaptercommon_quests.getX(Ledgehog.KEY), adaptercommon_quests.getY(Ledgehog.KEY));
        Hugexp hugexp = new Hugexp(intro_common,adaptercommon_quests.getX(Hugexp.KEY), adaptercommon_quests.getY(Hugexp.KEY));
        Massivexp massivexp = new Massivexp(hugexp,adaptercommon_quests.getX(Massivexp.KEY), adaptercommon_quests.getY(Massivexp.KEY));
        Omega_xp omega_xp = new Omega_xp(massivexp,adaptercommon_quests.getX(Omega_xp.KEY), adaptercommon_quests.getY(Omega_xp.KEY));
        common_quests.registerAdvancements(intro_common ,lumberjack ,experiencedlumb ,expertlumb ,juggerjack ,flying_accident ,hedgehog ,ledgehog ,hugexp ,massivexp ,omega_xp );
        RootAdvancement intro_legacy = new RootAdvancement(legacy, "intro_legacy", new AdvancementDisplay(Material.WRITTEN_BOOK, "Legacy advancements!", AdvancementFrameType.TASK, false, false, 0f, 0f , "This tab has legacy advancements"),"textures/block/shulker_box.png",1);
        String servName = OtherStorage.getServer_name();
        if (servName.equalsIgnoreCase("l-gp1")  || servName.equalsIgnoreCase("legacy")) {
            Citizen citizen = new Citizen(intro_legacy);
            Cit_time cit_time = new Cit_time(citizen);
            Cit_block_place cit_block_place = new Cit_block_place(citizen);
            Cit_block_break cit_block_break = new Cit_block_break(citizen);
            Cit_Dia_pick citDia_pick = new Cit_Dia_pick(citizen);
            citizen.registerTasks(cit_time,cit_block_place,cit_block_break, citDia_pick);
            Nobility nobility = new Nobility(citizen);
            Nob_time nob_time = new Nob_time(nobility);
            Nob_block_place nob_block_place = new Nob_block_place(nobility);
            Nob_block_break nob_block_break = new Nob_block_break(nobility);
            Nob_Elytra nobElytra = new Nob_Elytra(nobility);
            nobility.registerTasks(nob_time,nob_block_place,nob_block_break, nobElytra);
            legacy.registerAdvancements(intro_legacy ,citizen ,nobility );
        }

    }



    public static AdvancementsModule getModule() {
        return module;
    }

    public boolean isTimberEnabled() {
        return isTimberEnabled;
    }

    public LuckPerms getLuck() {
        return luck;
    }

    public WisecraftCoreApi getCore() {
        return core;
    }

    public boolean isVeinMinerEnabled() {
        return isVeinMinerEnabled;
    }
}
