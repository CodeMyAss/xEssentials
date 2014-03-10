package tv.mineinthebox.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.GreyListCause;
import tv.mineinthebox.essentials.enums.HookEnum;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.events.customEvents.OfflinePlayerGreyListedEvent;
import tv.mineinthebox.essentials.events.customEvents.PlayerGreyListedEvent;
import tv.mineinthebox.essentials.hook.BPermissionsHook;
import tv.mineinthebox.essentials.hook.GroupManagerHook;
import tv.mineinthebox.essentials.hook.PermissionsExHook;
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
								if(Bukkit.getPluginManager().isPluginEnabled("bPermissions")) {
									String oldGroup = BPermissionsHook.getGroup(xp.getUser());
									String newgroup = Configuration.getGrayListConfig().getGroup();
									BPermissionsHook.setGroup(xp.getUser(), Configuration.getGrayListConfig().getGroup());
									Bukkit.getPluginManager().callEvent(new PlayerGreyListedEvent(xp.getPlayer(), newgroup, oldGroup, HookEnum.BPERMISSIONS, GreyListCause.COMMAND));
								} else if(Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
									String oldGroup = PermissionsExHook.getGroup(xp.getUser());
									String newgroup = Configuration.getGrayListConfig().getGroup();
									PermissionsExHook.setGroup(xp.getUser(), Configuration.getGrayListConfig().getGroup());
									Bukkit.getPluginManager().callEvent(new PlayerGreyListedEvent(xp.getPlayer(), newgroup, oldGroup, HookEnum.PERMISSIONSEX, GreyListCause.COMMAND));
								} else if(Bukkit.getPluginManager().isPluginEnabled("GroupManager")) {
									String oldGroup = GroupManagerHook.getGroup(xp.getUser());
									String newgroup = Configuration.getGrayListConfig().getGroup();
									GroupManagerHook.setGroup(xp.getUser(), Configuration.getGrayListConfig().getGroup());
									Bukkit.getPluginManager().callEvent(new PlayerGreyListedEvent(xp.getPlayer(), newgroup, oldGroup, HookEnum.GROUPMANAGER, GreyListCause.COMMAND));
								}
								sender.sendMessage(ChatColor.GREEN + "you successfully greylisted " + xp.getUser());
							} else {
								xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(args[1]);
								off.setGreyListed(true);
								if(Bukkit.getPluginManager().isPluginEnabled("bPermissions")) {
									String oldGroup = BPermissionsHook.getGroup(off.getUser());
									String newgroup = Configuration.getGrayListConfig().getGroup();
									BPermissionsHook.setGroup(off.getUser(), Configuration.getGrayListConfig().getGroup());
									Bukkit.getPluginManager().callEvent(new OfflinePlayerGreyListedEvent(off.getUser(), newgroup, oldGroup, HookEnum.BPERMISSIONS, GreyListCause.COMMAND));
								} else if(Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
									String oldGroup = PermissionsExHook.getGroup(off.getUser());
									String newgroup = Configuration.getGrayListConfig().getGroup();
									PermissionsExHook.setGroup(off.getUser(), Configuration.getGrayListConfig().getGroup());
									Bukkit.getPluginManager().callEvent(new OfflinePlayerGreyListedEvent(off.getUser(), newgroup, oldGroup, HookEnum.PERMISSIONSEX, GreyListCause.COMMAND));
								} else if(Bukkit.getPluginManager().isPluginEnabled("GroupManager")) {
									String oldGroup = GroupManagerHook.getGroup(off.getUser());
									String newgroup = Configuration.getGrayListConfig().getGroup();
									GroupManagerHook.setGroup(off.getUser(), Configuration.getGrayListConfig().getGroup());
									Bukkit.getPluginManager().callEvent(new OfflinePlayerGreyListedEvent(off.getUser(), newgroup, oldGroup, HookEnum.GROUPMANAGER, GreyListCause.COMMAND));
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
								if(Bukkit.getPluginManager().isPluginEnabled("bPermissions")) {
									String DefaultGroup = BPermissionsHook.getDefaultGroup();
									BPermissionsHook.setGroup(xp.getUser(), DefaultGroup);
								} else if(Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
									String DefaultGroup = PermissionsExHook.getDefaultGroup();
									PermissionsExHook.setGroup(xp.getUser(), DefaultGroup);
								} else if(Bukkit.getPluginManager().isPluginEnabled("GroupManager")) {
									String DefaultGroup = GroupManagerHook.getDefaultGroup();
									GroupManagerHook.setGroup(xp.getUser(), DefaultGroup);
								}
								sender.sendMessage(ChatColor.GREEN + "you have successfully removed " + xp.getUser() + " from the greylist!");
							} else {
								xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(args[1]);
								off.setGreyListed(false);
								if(Bukkit.getPluginManager().isPluginEnabled("bPermissions")) {
									String DefaultGroup = BPermissionsHook.getDefaultGroup();
									BPermissionsHook.setGroup(off.getUser(), DefaultGroup);
								} else if(Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
									String DefaultGroup = PermissionsExHook.getDefaultGroup();
									PermissionsExHook.setGroup(off.getUser(), DefaultGroup);
								} else if(Bukkit.getPluginManager().isPluginEnabled("GroupManager")) {
									String DefaultGroup = GroupManagerHook.getDefaultGroup();
									GroupManagerHook.setGroup(off.getUser(), DefaultGroup);
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
