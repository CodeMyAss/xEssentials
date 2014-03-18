package tv.mineinthebox.essentials.commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.enums.PermissionKey;

public class CmdWild {
	
	private int xRadius = 100000;
	private int zRadius = 100000;
	
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("wild")) {
			if(sender.hasPermission(PermissionKey.CMD_WILD.getPermission())) {
				if(args.length == 0) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						Random randx = new Random((xRadius)+p.getLocation().getBlockX());
						Random randz = new Random((zRadius)+p.getLocation().getBlockZ());
						int x = randx.nextInt();
						int z = randz.nextInt();
						Location loc = new Location(p.getWorld(), x, p.getWorld().getHighestBlockYAt(x, z), z);
						loc.getWorld().refreshChunk(loc.getChunk().getX(), loc.getChunk().getZ());
						p.teleport(loc);
						sender.sendMessage(ChatColor.GREEN + "you successfully has teleported to the wild!");
					} else {
						Warnings.getWarnings(sender).consoleMessage();
					}
				} else if(args.length == 1) {
					Player p = Bukkit.getPlayer(args[0]);
					if(p instanceof Player) {
						Random randx = new Random((xRadius)+p.getLocation().getBlockX());
						Random randz = new Random((zRadius)+p.getLocation().getBlockZ());
						int x = randx.nextInt();
						int z = randz.nextInt();
						Location loc = new Location(p.getWorld(), x, p.getWorld().getHighestBlockYAt(x, z), z);
						loc.getWorld().refreshChunk(loc.getChunk().getX(), loc.getChunk().getZ());
						p.teleport(loc);
						p.sendMessage(ChatColor.GREEN + "you successfully has teleported to the wild!");	
					} else {
						sender.sendMessage(ChatColor.RED + "this player is not online!");
					}
				}
			} else {
				Warnings.getWarnings(sender).noPermission();
			}
		}
		return false;
	}

}
