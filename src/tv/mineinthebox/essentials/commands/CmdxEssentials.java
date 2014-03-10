package tv.mineinthebox.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.utils.TPS;

public class CmdxEssentials {
	
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("xEssentials")) {
			if(sender.hasPermission(PermissionKey.CMD_XESSENTIALS.getPermission())) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + ".oO___[xEssentials version " + xEssentials.getPlugin().getDescription().getVersion() + "___Oo.");
					sender.sendMessage(ChatColor.GREEN + "this plugin is written by Xeph0re AKA xize ");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials reload " + ChatColor.WHITE + ": reloads the plugin");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials help " + ChatColor.WHITE + ": shows help");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials tps " + ChatColor.WHITE + ": shows tps of the server");
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("help")) {
						sender.sendMessage(ChatColor.GOLD + ".oO___[xEssentials version " + xEssentials.getPlugin().getDescription().getVersion() + "___Oo.");
						sender.sendMessage(ChatColor.GREEN + "this plugin is written by Xeph0re AKA xize ");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials reload " + ChatColor.WHITE + ": reloads the plugin");
					} else if(args[0].equalsIgnoreCase("reload")) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&3xEssentials&2]&f " + ChatColor.GRAY + "reloading xEssentials version " + xEssentials.getPlugin().getDescription().getVersion()));
						Configuration.reloadConfiguration();
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&3xEssentials&2]&f " + ChatColor.GRAY + "reload completed!"));
					} else if(args[0].equalsIgnoreCase("tps")) {
						if(sender.hasPermission(PermissionKey.CMD_TPS.getPermission())) {
							ChatColor tpsColor = null;
							sender.sendMessage(ChatColor.GOLD + ".oO___[tps]___Oo.");
							sender.sendMessage(ChatColor.GRAY + "Gc max: " + TPS.garbageCollectorMax() + "mb");
							sender.sendMessage(ChatColor.GRAY + "Free memory: " + TPS.getFreeMemory() + "mb");
							sender.sendMessage(ChatColor.GRAY + "Max memory " + TPS.getMemoryMax() + "mb");
							if(TPS.getServerTicks() < 15) {
								tpsColor = ChatColor.GREEN;
							} else if(TPS.getServerTicks() > 15 && TPS.getServerTicks() < 25) {
								tpsColor = ChatColor.YELLOW;
							} else if(TPS.getServerTicks() > 25) {
								tpsColor = ChatColor.RED;
							}
							sender.sendMessage(ChatColor.GRAY + "ticks: " + tpsColor + TPS.getServerTicks());
						} else {
							Warnings.getWarnings(sender).noPermission();
						}
					}
				}
			} else {
				Warnings.getWarnings(sender).noPermission();
			}
		}
		return false;
	}

}
