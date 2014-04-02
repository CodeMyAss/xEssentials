package tv.mineinthebox.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;

public class CmdTphere {
	
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("tphere")) {
			if(sender.hasPermission(PermissionKey.CMD_TP_HERE.getPermission())) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + ".oO___[tphere help]___Oo.");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/tphere help " + ChatColor.WHITE + ": shows help");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/tphere <player> " + ChatColor.WHITE + ": teleports a player to you!");
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("help")) {
						sender.sendMessage(ChatColor.GOLD + ".oO___[tphere help]___Oo.");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/tphere help " + ChatColor.WHITE + ": shows help");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/tphere <player> " + ChatColor.WHITE + ": teleports a player to you!");
					} else {
						if(sender instanceof Player) {
							Player p = (Player) sender;
							Player victem = xEssentials.getOfflinePlayer(args[0]).getPlayer();
							if(victem instanceof Player) {
								sender.sendMessage(ChatColor.GREEN + "teleported " + victem.getName() + " to you!");
								victem.sendMessage(ChatColor.GREEN + sender.getName() + " has teleported you to his location!");
								victem.teleport(p);
							} else {
								sender.sendMessage(ChatColor.RED + "could not find a online player with that name!");
							}
						} else {
							Warnings.getWarnings(sender).consoleMessage();
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
