package tv.mineinthebox.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.GreyListCause;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.events.customEvents.OfflinePlayerGreyListedEvent;
import tv.mineinthebox.essentials.events.customEvents.PlayerGreyListedEvent;
import tv.mineinthebox.essentials.hook.Hooks;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;
public class CmdGreylist {

	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("greylist")) {
			if(sender.hasPermission(PermissionKey.CMD_GREYLIST.getPermission())) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + ".oO___[greylist help]___Oo.");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/greylist help " + ChatColor.WHITE + ": shows help");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/greylist on " + ChatColor.WHITE + ": activates greylist");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/greylist off " + ChatColor.WHITE + ": disable greylist");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/greylist add <player> " + ChatColor.WHITE + ": excempt the player to the greylist");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/greylist remove <player> " + ChatColor.WHITE + ": remove the player from the greylist");
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("help")) {
						sender.sendMessage(ChatColor.GOLD + ".oO___[greylist help]___Oo.");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/greylist help " + ChatColor.WHITE + ": shows help");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/greylist on " + ChatColor.WHITE + ": activates greylist");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/greylist off " + ChatColor.WHITE + ": disable greylist");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/greylist add <player> " + ChatColor.WHITE + ": excempt the player to the greylist");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/greylist remove <player> " + ChatColor.WHITE + ": remove the player from the greylist");
					} else if(args[0].equalsIgnoreCase("on")) {
						if(!Configuration.getGrayListConfig().isEnabled()) {
							Configuration.getGrayListConfig().setEnabled(true);
							sender.sendMessage(ChatColor.GREEN + "you have successfully enabled the greylist server!");
						} else {
							sender.sendMessage(ChatColor.RED + "the greylist server whas already active!");
						}
					} else if(args[0].equalsIgnoreCase("off")) {
						if(Configuration.getGrayListConfig().isEnabled()) {
							Configuration.getGrayListConfig().setEnabled(false);
							sender.sendMessage(ChatColor.GREEN + "you have successfully disabled the greylist server!");
						} else {
							sender.sendMessage(ChatColor.RED + "the greylist server whas already shuted down!");
						}
					}
				} else if(args.length == 2) {
					if(args[0].equalsIgnoreCase("add")) {
						if(xEssentials.isEssentialsPlayer(args[1])) {
							if(xEssentials.contains(args[1])) {
								xEssentialsPlayer xp = xEssentials.get(args[1]);
								xp.setGreyListed(true);
								if(Hooks.isVaultEnabled()) {
									String oldGroup = xEssentials.getVault().getGroup(xp.getPlayer());
									String newGroup = Configuration.getGrayListConfig().getGroup();
									xEssentials.getVault().setGroup(Bukkit.getWorlds().get(0), xp.getUser(), newGroup);
									Bukkit.getPluginManager().callEvent(new PlayerGreyListedEvent(xp.getPlayer(), newGroup, oldGroup, GreyListCause.COMMAND));
								} else {
									sender.sendMessage(ChatColor.RED + "no vault installed!");
									return false;
								}
								sender.sendMessage(ChatColor.GREEN + "you successfully greylisted " + xp.getUser());
							} else {
								xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(args[1]);
								off.setGreyListed(true);
								if(Hooks.isVaultEnabled()) {
									String oldGroup = xEssentials.getVault().getGroup(Bukkit.getWorlds().get(0), off.getUser());
									String newGroup = Configuration.getGrayListConfig().getGroup();
									xEssentials.getVault().setGroup(Bukkit.getWorlds().get(0), off.getUser(), newGroup);
									Bukkit.getPluginManager().callEvent(new OfflinePlayerGreyListedEvent(off.getUser(), newGroup, oldGroup, GreyListCause.COMMAND));
								} else {
									sender.sendMessage(ChatColor.RED + "no vault installed!");
									return false;
								}
								sender.sendMessage(ChatColor.GREEN + "you successfully greylisted offline player " + off.getUser());
							}
						} else {
							sender.sendMessage(ChatColor.RED + "this player has never played before!");
						}
					} else if(args[0].equalsIgnoreCase("remove")) {
						if(xEssentials.isEssentialsPlayer(args[1])) {
							if(xEssentials.contains(args[1])) {
								xEssentialsPlayer xp = xEssentials.get(args[1]);
								xp.setGreyListed(false);
								if(Hooks.isVaultEnabled()) {
									String DefaultGroup = xEssentials.getVault().getDefaultGroup();
									xEssentials.getVault().setGroup(Bukkit.getWorlds().get(0), xp.getUser(), DefaultGroup);
								} else {
									sender.sendMessage(ChatColor.RED + "no vault intalled!");
									return false;
								}
								sender.sendMessage(ChatColor.GREEN + "you have successfully removed " + xp.getUser() + " from the greylist!");
							} else {
								xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(args[1]);
								off.setGreyListed(false);
								if(Hooks.isVaultEnabled()) {
									String DefaultGroup = xEssentials.getVault().getDefaultGroup();
									xEssentials.getVault().setGroup(Bukkit.getWorlds().get(0), off.getUser(), DefaultGroup);
								} else {
									sender.sendMessage(ChatColor.RED + "no vault intalled!");
									return false;
								}
								sender.sendMessage(ChatColor.GREEN + "you have successfully removed " + off.getUser() + " from the greylist!");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "this player has never played before!");
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
