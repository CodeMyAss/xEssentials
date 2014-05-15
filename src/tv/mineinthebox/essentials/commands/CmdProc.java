package tv.mineinthebox.essentials.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class CmdProc {
	
	private List<String> getPlayerByName(String p) {
		List<String> s = new ArrayList<String>();
		for(xEssentialsOfflinePlayer name : xEssentials.getOfflinePlayers()) {
			if(name.getUser().toUpperCase().startsWith(p.toUpperCase())) {
				s.add(name.getUser());
			}
		}
		return s;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("proc")) {
			if(args.length == 1) {
				if(sender.hasPermission(PermissionKey.CMD_PROC.getPermission())) {
					List<String> list = getPlayerByName(args[0]);
					return list;
				}
			}
		}
		return null;
	}
	
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("proc")) {
			if(sender.hasPermission(PermissionKey.CMD_PROC.getPermission())) {
				if(args.length == 0) {
					if(sender instanceof Player) {
						xEssentialsPlayer xp = xEssentials.get(sender.getName());
						if(xp.hasProc()) {
							xp.setProc(false);
							sender.sendMessage(ChatColor.GREEN + "proc has been disabled");
						} else {
							xp.setProc(true);
							sender.sendMessage(ChatColor.GREEN + "proc has been enabled!");
						}
					} else {
						Warnings.getWarnings(sender).consoleMessage();
					}
				} else if(args.length == 1) {
					if(xEssentials.isEssentialsPlayer(args[0])) {
						if(xEssentials.contains(args[0])) {
							xEssentialsPlayer xp = xEssentials.get(args[0]);
							if(xp.hasProc()) {
								xp.setProc(false);
								sender.sendMessage(ChatColor.GREEN + "proc has been disabled for player " + xp.getUser() + "!");
							} else {
								xp.setProc(true);
								sender.sendMessage(ChatColor.GREEN + "proc has been enabled for player " + xp.getUser() + "!");
							}
						} else {
							xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(args[0]);
							if(off.hasProc()) {
								off.setProc(false);
								sender.sendMessage(ChatColor.GREEN + "proc has been disabled for player " + off.getUser() + "!");
							} else {
								off.setProc(true);
								sender.sendMessage(ChatColor.GREEN + "proc has been enabled for player " + off.getUser() + "!");
							}
						}
					} else {
						Warnings.getWarnings(sender).playerHasNeverPlayedBefore();
					}
				}
			} else {
				Warnings.getWarnings(sender).noPermission();
			}
		}
		return false;
	}

}
