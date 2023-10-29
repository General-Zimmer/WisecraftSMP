package xyz.wisecraft.smp.modules.jobsextra.util;

import com.gamingmesh.jobs.container.Job;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static xyz.wisecraft.smp.modules.jobsextra.storage.JobsStorage.getJobLevel;

public class UtilCommon {

    public static void sendNoMessage(Player p, Job pJob, String extra_to) {


        p.sendMessage(ChatColor.AQUA + "You need to have the job \""+ pJob.getName() +"\" and be job lvl " + getJobLevel(pJob) + " to " + extra_to);
    }
}
