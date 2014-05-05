package tv.mineinthebox.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.MinigameType;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.instances.SpleefArena;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;
import tv.mineinthebox.essentials.utils.MiniGame;

public class CmdSpleef {

	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("spleef")) {
			if(sender.hasPermission(PermissionKey.CMD_SPLEEF.getPermission())) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + ".oO___[Spleef help]___Oo.");
					sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/spleef help " + ChatColor.WHITE + ": shows help");
					sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/spleef list " + ChatColor.WHITE + ": shows all arenas");
					sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/spleef join <arena> " + ChatColor.WHITE + ": allows you to join a arena");
					sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/spleef quit " + ChatColor.WHITE + ": can only be used if there is no other player");
					if(sender.hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/spleef create <arena> " + ChatColor.WHITE + ": create a arena envorinment based on pos1 and pos2");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/spleef remove <arena> " + ChatColor.WHITE + ": removes the arena!");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/spleef sp add <arena> " + ChatColor.WHITE + ": adds a spawnpoint");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/spleef sp remove <id> <arena> " + ChatColor.WHITE + ": remove a spawnpoint from the arena");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/spleef sp list <arena> " + ChatColor.WHITE + ": shows all the spawnpoint id's");
					}	
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("help")) {
						sender.sendMessage(ChatColor.GOLD + ".oO___[Spleef help]___Oo.");
						sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/spleef help " + ChatColor.WHITE + ": shows help");
						sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/spleef list " + ChatColor.WHITE + ": shows all arenas");
						sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/spleef join <arena> " + ChatColor.WHITE + ": allows you to join a arena");
						sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/spleef quit " + ChatColor.WHITE + ": can only be used if there is no other player");
						if(sender.hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/spleef create <arena> " + ChatColor.WHITE + ": create a arena envorinment based on pos1 and pos2");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/spleef remove <arena> " + ChatColor.WHITE + ": removes the arena!");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/spleef sp add <arena> " + ChatColor.WHITE + ": adds a spawnpoint");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/spleef sp remove <id> <arena> " + ChatColor.WHITE + ": remove a spawnpoint from the arena");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/spleef sp list <arena> " + ChatColor.WHITE + ": shows all the spawnpoint id's");
						}	
					} else if(args[0].equalsIgnoreCase("list")) {
						StringBuilder build = new StringBuilder();
						SpleefArena[] arenas = MiniGame.getAllSpleefArenas();
						for(int i = 0; i < arenas.length; i++) {
							if(i == arenas.length) {
								SpleefArena arena = arenas[i];
								build.append(MinigameType.SPLEEF.getPrefix() + arena.getArenaName());
							} else {
								SpleefArena arena = arenas[i];
								build.append(MinigameType.SPLEEF.getPrefix() + arena.getArenaName() + ChatColor.WHITE + ", ");
							}
						}
					} else if(args[0].equalsIgnoreCase("quit")) {
						if(sender instanceof Player) {
							xEssentialsPlayer xp  = xEssentials.get(sender.getName()); 
							if(MiniGame.isPlayerInArea(xp.getPlayer())) {
								 Object obj = MiniGame.getArenaFromPlayer(xp.getPlayer());
								 if(obj instanceof SpleefArena) {
									 SpleefArena arena = (SpleefArena) obj;
									 if(arena.getJoinedCount() == 1) {
										 arena.removePlayer(sender.getName());
										 sender.sendMessage(ChatColor.GREEN + "you successfully left the arena!");
										 arena.setRunning(false);
										 xp.getPlayer().performCommand("spawn");
									 }
								 } else {
									 //TO-DO
								 }
							 } else {
								 sender.sendMessage(ChatColor.RED + "you aren't joined inside a arena!");
							 }
						} else {
							Warnings.getWarnings(sender).consoleMessage();
						}
					}
				} else if(args.length == 2) {
					if(args[0].equalsIgnoreCase("join")) {
						
					} else if(args[0].equalsIgnoreCase("create")) {
						
					} else if(args[0].equalsIgnoreCase("remove")) {
						
					}
				} else if(args.length == 3) {

				} else if(args.length == 4) {

				}
			} else {
				Warnings.getWarnings(sender).noPermission();
			}
		}
		return false;
	}

}
