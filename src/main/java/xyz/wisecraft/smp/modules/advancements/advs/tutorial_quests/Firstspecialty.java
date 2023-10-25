package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import org.bukkit.Material;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;

public class Firstspecialty extends BaseAdvancement {

    public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "firstspecialty");


    public Firstspecialty(Advancement parent, float x, float y) {
        super(KEY.getKey(), new AdvancementDisplay(Material.DEBUG_STICK, "Use a job's specialty", AdvancementFrameType.TASK, true, true, x, y, "Use a job's specialty"), parent, 1);
    }

}