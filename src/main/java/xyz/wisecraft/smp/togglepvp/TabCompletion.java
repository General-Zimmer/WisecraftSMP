package xyz.wisecraft.smp.togglepvp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter{
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			 String alias,  String[] args) {
		List<String> cmds = new ArrayList<String>();
		
		 switch (args.length) {
		  case 1:
		    if (sender.hasPermission("pvptoggle.allow")) {
		    	cmds.add("toggle");
		    	cmds.add("on");
		    	cmds.add("off");
		    }
		    if (sender.hasPermission("pvptoggle.reload")) {
		    	cmds.add("reload");
		    }
		    if (sender.hasPermission("pvptoggle.others")) {
		    	cmds.add("status");
		    }
		    return StringUtil.copyPartialMatches(args[0], cmds, new ArrayList<>());
		   
		    //https://www.spigotmc.org/threads/tabcompleter-not-working.406512/
		  }
		return null;
		
	}

}