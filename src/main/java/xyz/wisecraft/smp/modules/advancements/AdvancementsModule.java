package xyz.wisecraft.smp.modules.advancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementMain;
import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.util.CoordAdapter;
import lombok.Getter;
import net.essentialsx.api.v2.services.discord.DiscordService;
import net.essentialsx.api.v2.services.discord.MessageType;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.advs.common_quests.*;
import xyz.wisecraft.smp.modules.advancements.advs.common_quests.maxjob2.*;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.Citizen;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.Nobility;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.citizen.Cit_Dia_pick;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.citizen.Cit_block_break;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.citizen.Cit_block_place;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.citizen.Cit_time;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.nobility.Nob_Elytra;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.nobility.Nob_block_break;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.nobility.Nob_block_place;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.nobility.Nob_time;
import xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests.*;
import xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests.allspecialty.*;
import xyz.wisecraft.smp.modules.advancements.cmd.AdvCMD;
import xyz.wisecraft.smp.modules.advancements.listeners.DiscordAdvListener;
import xyz.wisecraft.smp.modules.advancements.listeners.LegacyRoles;
import xyz.wisecraft.smp.modules.advancements.listeners.TimberListeners;
import xyz.wisecraft.smp.modules.advancements.threads.GibRoles;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is the module class for the Advancements module.
 */
@SuppressWarnings("unused")
public class AdvancementsModule extends ModuleClass {

    @Getter
    private static AdvancementsModule module;
    private static UltimateAdvancementAPI api = null;
    private static AdvancementTab tutorial_quests = null;
    private static AdvancementTab common_quests = null;
    private static AdvancementTab legacy = null;
    private final boolean isTimberEnabled = setupDependency("UltimateTimber");
    @Getter
    private final LuckPerms luck = setupDependency(LuckPerms.class);
    @Getter
    private final WisecraftCoreApi core = setupDependency(WisecraftCoreApi.class);
    private final boolean isVeinMinerEnabled = setupDependency("VeinMiner");
    private final boolean isJobsEnabled = setupDependency("Jobs");
    private final boolean isTownyEnabled = setupDependency("Towny");
    @Getter
    private final @Nullable DiscordService apiDiscord = Bukkit.getServicesManager().load(DiscordService.class);
    @Getter
    private static AdvancementMain advapi = null;
    public AdvancementsModule(long id) {
        super(id);
        module = this;
    }

    @Override
    public void onEnable() {

        if (advapi == null) {
            advapi = new AdvancementMain(WisecraftSMP.getInstance());
            initModule();
            return;
        }
        Bukkit.getScheduler().runTask(WisecraftSMP.getInstance(), this::initModule);
    }

    @Override
    public @NotNull Set<Listener> registerListeners() {
        HashSet<Listener> listeners = new HashSet<>();
        if (isTimberEnabled && core != null)
            listeners.add(new TimberListeners(core));

        if (luck == null) return listeners;

        if (apiDiscord != null) {
            listeners.add(new DiscordAdvListener(apiDiscord));
        }

        String servName = OtherStorage.getServer_name();
        if (servName.equalsIgnoreCase("l-gp1")  || servName.equalsIgnoreCase("legacy")) {
            new GibRoles(core, luck).runTaskTimer(plugin, 20*60*10, 20*60*10);
            listeners.add(new LegacyRoles());
            legacy.automaticallyShowToPlayers();
            legacy.automaticallyGrantRootAdvancement();
        }

        return listeners;
    }

    @Override
    public void onDisable() {
        AdvancementsModule.advapi.disable();
    }

    @Override
    public @NotNull Set<BukkitCommand> registerCommands() {
        HashSet<BukkitCommand> commands = new HashSet<>(1);
        commands.add(new AdvCMD(core, luck));

        return commands;
    }

    public void initializeTabs() {
        tutorial_quests = api.createAdvancementTab(AdvancementTabNamespaces.tutorial_quests_NAMESPACE);
        AdvancementKey tutorialquestsKey = new AdvancementKey(tutorial_quests.getNamespace(), "tutorialquests");
        CoordAdapter.@NotNull CoordAdapterBuilder adapterBuildertutorial_quests = CoordAdapter.builder()
                .add(tutorialquestsKey, 0f, 0f).add(Craft_bundle.KEY, 2f, -1f)
                .add(Craft_elytra.KEY, 1f, -1f).add(Regenworlds.KEY, 2f, 0f)
                .add(First_timber.KEY, 3f, 1f).add(Timber_move.KEY, 4f, 1f).add(Veinmine.KEY, 3f, 0f)
                .add(Old_timer.KEY, -1f, 0f).add(Sethome.KEY, 1f, 0f).add(Jointown.KEY, 1f, 3f)
                .add(Gettownplot.KEY, 2f, 3f).add(Gettingjob.KEY, 1f, 2f).add(Trypvp.KEY, 1f, 1f)
                .add(Firstspecialty.KEY, 2f, 2f).add(Allspecialty.KEY, 3f, 2f);


        @NotNull CoordAdapter adaptertutorial_quests = adapterBuildertutorial_quests.build();
        HashSet<BaseAdvancement> tutorial_questsSet = new HashSet<>();
        RootAdvancement tutorialquests = new RootAdvancement(tutorial_quests, tutorialquestsKey.getKey(), new AdvancementDisplay(Material.KNOWLEDGE_BOOK, "Welcome to tutorial quests!", AdvancementFrameType.TASK, false, false, adaptertutorial_quests.getX(tutorialquestsKey), adaptertutorial_quests.getY(tutorialquestsKey), "Here are advancements that can help you get started!"),"textures/block/lime_shulker_box.png",1);


        Craft_bundle craft_bundle = new Craft_bundle(tutorialquests,adaptertutorial_quests.getX(Craft_bundle.KEY), adaptertutorial_quests.getY(Craft_bundle.KEY));
        tutorial_questsSet.add(craft_bundle);

        Craft_elytra craft_elytra = new Craft_elytra(tutorialquests,adaptertutorial_quests.getX(Craft_elytra.KEY), adaptertutorial_quests.getY(Craft_elytra.KEY));
        tutorial_questsSet.add(craft_elytra);

        Sethome sethome = new Sethome(tutorialquests,adaptertutorial_quests.getX(Sethome.KEY), adaptertutorial_quests.getY(Sethome.KEY));
        tutorial_questsSet.add(sethome);

        Regenworlds regenworlds = new Regenworlds(sethome,adaptertutorial_quests.getX(Regenworlds.KEY), adaptertutorial_quests.getY(Regenworlds.KEY));
        tutorial_questsSet.add(regenworlds);

        if (isTimberEnabled()) {
            First_timber first_timber = new First_timber(regenworlds,adaptertutorial_quests.getX(First_timber.KEY), adaptertutorial_quests.getY(First_timber.KEY));
            tutorial_questsSet.add(first_timber);
            Timber_move timber_move = new Timber_move(first_timber,adaptertutorial_quests.getX(Timber_move.KEY), adaptertutorial_quests.getY(Timber_move.KEY));
            tutorial_questsSet.add(timber_move);
        }


        if (isVeinMinerEnabled) {
            Veinmine veinmine = new Veinmine(regenworlds,adaptertutorial_quests.getX(Veinmine.KEY), adaptertutorial_quests.getY(Veinmine.KEY));
            tutorial_questsSet.add(veinmine);
        }


        Old_timer old_timer = new Old_timer(tutorialquests,adaptertutorial_quests.getX(Old_timer.KEY), adaptertutorial_quests.getY(Old_timer.KEY));
        tutorial_questsSet.add(old_timer);

        if (isTownyEnabled) {
            Jointown jointown = new Jointown(tutorialquests,adaptertutorial_quests.getX(Jointown.KEY), adaptertutorial_quests.getY(Jointown.KEY));
            tutorial_questsSet.add(jointown);

            Gettownplot gettownplot = new Gettownplot(jointown,adaptertutorial_quests.getX(Gettownplot.KEY), adaptertutorial_quests.getY(Gettownplot.KEY));
            tutorial_questsSet.add(gettownplot);
        }



        Trypvp trypvp = new Trypvp(tutorialquests,adaptertutorial_quests.getX(Trypvp.KEY), adaptertutorial_quests.getY(Trypvp.KEY));
        tutorial_questsSet.add(trypvp);


        if (isJobsEnabled) {
            Gettingjob gettingjob = new Gettingjob(tutorialquests, adaptertutorial_quests.getX(Gettingjob.KEY), adaptertutorial_quests.getY(Gettingjob.KEY));
            tutorial_questsSet.add(gettingjob);
            Firstspecialty firstspecialty = new Firstspecialty(gettingjob, adaptertutorial_quests.getX(Firstspecialty.KEY), adaptertutorial_quests.getY(Firstspecialty.KEY));
            tutorial_questsSet.add(firstspecialty);

            Allspecialty allspecialty = new Allspecialty(firstspecialty, adaptertutorial_quests.getX(Allspecialty.KEY), adaptertutorial_quests.getY(Allspecialty.KEY));
            tutorial_questsSet.add(allspecialty);

            Farmerspec farmerspec = new Farmerspec(allspecialty);
            Minerspec minerspec = new Minerspec(allspecialty);
            Enchantspec enchantspec = new Enchantspec(allspecialty);
            Explorerspec explorerspec = new Explorerspec(allspecialty);
            Blacksmithspec blacksmithspec = new Blacksmithspec(allspecialty);
            allspecialty.registerTasks(farmerspec, minerspec, enchantspec, explorerspec, blacksmithspec);
        }

        tutorial_quests.registerAdvancements(tutorialquests, tutorial_questsSet);

        // Common quests
        common_quests = api.createAdvancementTab(AdvancementTabNamespaces.common_quests_NAMESPACE);
        AdvancementKey intro_commonKey = new AdvancementKey(common_quests.getNamespace(), "intro_common");
        CoordAdapter adaptercommon_quests = CoordAdapter.builder().add(tutorialquestsKey, 0f, 0f).add(Craft_bundle.KEY, 2f, -1f).add(Craft_elytra.KEY, 1f, -1f).add(Regenworlds.KEY, 2f, 0f).add(First_timber.KEY, 3f, 1f).add(Timber_move.KEY, 4f, 1f).add(Veinmine.KEY, 3f, 0f).add(Old_timer.KEY, -1f, 0f).add(Sethome.KEY, 1f, 0f).add(Jointown.KEY, 1f, 3f).add(Gettownplot.KEY, 2f, 3f).add(Gettingjob.KEY, 1f, 2f).add(Trypvp.KEY, 1f, 1f).add(Firstspecialty.KEY, 2f, 2f).add(Allspecialty.KEY, 3f, 2f).add(intro_commonKey, 0f, 0f).add(Lumberjack.KEY, 1f, 1f).add(Experiencedlumb.KEY, 2f, 1f).add(Expertlumb.KEY, 3f, 1f).add(Juggerjack.KEY, 4f, 1f).add(Flying_accident.KEY, 1f, -1f).add(Hedgehog.KEY, 1f, -2f).add(Ledgehog.KEY, 2f, -2f).add(Hugexp.KEY, 1f, -3f).add(Massivexp.KEY, 2f, -3f).add(Omega_xp.KEY, 3f, -3f).add(Maxjob.KEY, 1f, 2f).add(Maxjob2atonce.KEY, 2f, 3f).add(Maxjob2.KEY, 2f, 2f).add(Maxjob3.KEY, 3f, 2f).build();

        HashSet<BaseAdvancement> common_questsSet = new HashSet<>();
        RootAdvancement intro_common = new RootAdvancement(common_quests, intro_commonKey.getKey(), new AdvancementDisplay(Material.BOOK, "Common advancements!", AdvancementFrameType.TASK, false, false, adaptercommon_quests.getX(intro_commonKey), adaptercommon_quests.getY(intro_commonKey), "Here's a bunch of advancements you can do! Some of them can be a bit tricky"),"textures/block/light_blue_shulker_box.png",1);

        if (isTimberEnabled()) {
            Lumberjack lumberjack = new Lumberjack(intro_common, adaptercommon_quests.getX(Lumberjack.KEY), adaptercommon_quests.getY(Lumberjack.KEY));
            common_questsSet.add(lumberjack);
            Experiencedlumb experiencedlumb = new Experiencedlumb(lumberjack, adaptercommon_quests.getX(Experiencedlumb.KEY), adaptercommon_quests.getY(Experiencedlumb.KEY));
            common_questsSet.add(experiencedlumb);
            Expertlumb expertlumb = new Expertlumb(experiencedlumb, adaptercommon_quests.getX(Expertlumb.KEY), adaptercommon_quests.getY(Expertlumb.KEY));
            common_questsSet.add(expertlumb);
            Juggerjack juggerjack = new Juggerjack(expertlumb, adaptercommon_quests.getX(Juggerjack.KEY), adaptercommon_quests.getY(Juggerjack.KEY));
            common_questsSet.add(juggerjack);
        }
        Flying_accident flying_accident = new Flying_accident(intro_common,adaptercommon_quests.getX(Flying_accident.KEY), adaptercommon_quests.getY(Flying_accident.KEY));
        common_questsSet.add(flying_accident);
        Hedgehog hedgehog = new Hedgehog(intro_common,adaptercommon_quests.getX(Hedgehog.KEY), adaptercommon_quests.getY(Hedgehog.KEY));
        common_questsSet.add(hedgehog);
        Ledgehog ledgehog = new Ledgehog(hedgehog,adaptercommon_quests.getX(Ledgehog.KEY), adaptercommon_quests.getY(Ledgehog.KEY));
        common_questsSet.add(ledgehog);
        Hugexp hugexp = new Hugexp(intro_common,adaptercommon_quests.getX(Hugexp.KEY), adaptercommon_quests.getY(Hugexp.KEY));
        common_questsSet.add(hugexp);
        Massivexp massivexp = new Massivexp(hugexp,adaptercommon_quests.getX(Massivexp.KEY), adaptercommon_quests.getY(Massivexp.KEY));
        common_questsSet.add(massivexp);
        Omega_xp omega_xp = new Omega_xp(massivexp,adaptercommon_quests.getX(Omega_xp.KEY), adaptercommon_quests.getY(Omega_xp.KEY));
        common_questsSet.add(omega_xp);

        if (isJobsEnabled) {
            Maxjob maxjob = new Maxjob(intro_common, adaptercommon_quests.getX(Maxjob.KEY), adaptercommon_quests.getY(Maxjob.KEY));
            common_questsSet.add(maxjob);
            Maxjob2atonce maxjob2atonce = new Maxjob2atonce(maxjob, adaptercommon_quests.getX(Maxjob2atonce.KEY), adaptercommon_quests.getY(Maxjob2atonce.KEY));
            common_questsSet.add(maxjob2atonce);
            Maxjob2 maxjob2 = new Maxjob2(maxjob, adaptercommon_quests.getX(Maxjob2.KEY), adaptercommon_quests.getY(Maxjob2.KEY));
            common_questsSet.add(maxjob2);

            Maxfarmer maxfarmer = new Maxfarmer(maxjob2);
            Maxminer maxminer = new Maxminer(maxjob2);
            Maxblacksmith maxblacksmith = new Maxblacksmith(maxjob2);
            Maxexplorer maxexplorer = new Maxexplorer(maxjob2);
            Maxenchanter maxenchanter = new Maxenchanter(maxjob2);
            maxjob2.registerTasks(maxfarmer, maxminer, maxblacksmith, maxexplorer, maxenchanter);

            Maxjob3 maxjob3 = new Maxjob3(maxjob2, adaptercommon_quests.getX(Maxjob3.KEY), adaptercommon_quests.getY(Maxjob3.KEY));
            common_questsSet.add(maxjob3);
        }

        common_quests.registerAdvancements(intro_common, common_questsSet);

        String servName = OtherStorage.getServer_name();
        if (servName.equalsIgnoreCase("l-gp1")  || servName.equalsIgnoreCase("legacy")) {
            legacy = api.createAdvancementTab(AdvancementTabNamespaces.legacy_NAMESPACE);
            RootAdvancement intro_legacy = new RootAdvancement(legacy, "intro_legacy", new AdvancementDisplay(Material.WRITTEN_BOOK, "Legacy advancements!", AdvancementFrameType.TASK, false, false, 0f, 0f , "This tab has legacy advancements"),"textures/block/shulker_box.png",1);

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

    private void initModule() {
        Bukkit.getServer().getOnlinePlayers().forEach( player ->
                player.kick(
                        Component.text("Advancements was loaded/reloaded and you were kicked to prevent Data issues. You can rejoin now."),
                        PlayerKickEvent.Cause.PLUGIN));
        // Kicking players before advapi.load() is called to probably prevent events from being triggered because they leave. (I don't know if this is needed)
        advapi.load();
        advapi.enableSQLite(new File(plugin.getServer().getWorldContainer().getAbsolutePath() + "/world", "advancements.db"));
        api = UltimateAdvancementAPI.getInstance(plugin);

        initializeTabs();
        tutorial_quests.automaticallyShowToPlayers();
        tutorial_quests.automaticallyGrantRootAdvancement();
        common_quests.automaticallyShowToPlayers();
        common_quests.automaticallyGrantRootAdvancement();
    }

    @Override
    public boolean hasAllHardDependencies() {
        return !plugin.getIsTesting();
    }

    public boolean isTimberEnabled() {
        return isTimberEnabled;
    }

    public boolean isVeinMinerEnabled() {
        return isVeinMinerEnabled;
    }
}
