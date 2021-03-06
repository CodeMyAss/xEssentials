package tv.mineinthebox.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class CmdBoom {
	
	public boolean execute(CommandSender sender, String[] args, Command cmd) {
		if(cmd.getName().equalsIgnoreCase("boom")) {
			if(sender.hasPermission(PermissionKey.CMD_BOOM.getPermission())) {
				if(args.length == 1) {
					Player boom = xEssentials.getOfflinePlayer(args[0]).getPlayer();
					if(boom instanceof Player) {
						if(boom.getGameMode() == GameMode.CREATIVE) {
							boom.setGameMode(GameMode.SURVIVAL);
						} else if(boom.isFlying()) {
							boom.setFlying(false);
						}
						xEssentialsPlayer xp = xEssentials.get(boom.getName());
						xp.setBoom();
						sender.sendMessage(ChatColor.GREEN + "You are boomed!");
						Bukkit.broadcastMessage(ChatColor.GRAY + "The player " + ChatColor.GREEN +  args[0] + ChatColor.GRAY + " has been boomed by " + ChatColor.GREEN + sender.getName());
						Location loc = boom.getPlayer().getLocation();
						loc.setY(loc.getY() + 100);
						int speed = 10;
						Vector vector = loc.toVector().subtract(boom.getLocation().toVector()).normalize();
						boom.setVelocity(vector.multiply(speed));
					} else {
						sender.sendMessage(ChatColor.RED + "This player is not online");
					}
				} else {
					sender.sendMessage(ChatColor.GREEN + "Syntax: /boom <player> - explodes a player high in the sky.");
				}
			}
		} else {
			Warnings.getWarnings(sender).noPermission();
		}
			return false;
	}

}
