package tv.mineinthebox.essentials.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.instances.Kit;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class CmdKit {
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("kit")) {
			if(args.length == 1) {
				if(sender.hasPermission(PermissionKey.CMD_KIT.getPermission())) {
					List<Kit> kits = new ArrayList<Kit>(Configuration.getKitConfig().getConfigKits().values());
					List<String> names = new ArrayList<String>();
					for(Kit kit : kits) {
						if(kit.getKitName().toUpperCase().startsWith(args[0].toUpperCase())) {
							names.add(kit.getKitName());
						}
					}
					return names;
				}
			}
		}
		return null;
	}

	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("kit")) {
			if(sender instanceof Player) {
				if(sender.hasPermission(PermissionKey.CMD_KIT.getPermission())) {
					if(args.length == 0) {
						sender.sendMessage(ChatColor.GOLD + ".oO___[kit help]___Oo.");
						sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/kit <kit name> " + ChatColor.WHITE + ": get the kit by name in your inventory!");
						sender.sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/kits" + ChatColor.WHITE + ": shows you all the kits!");
					} else if(args.length == 1) {
						HashMap<String, Kit> kits = new HashMap<String, Kit>(Configuration.getKitConfig().getConfigKits());
						if(kits.containsKey(args[0])) {
							if(sender.hasPermission(PermissionKey.CMD_KIT.getPermission()+"."+args[0])) {
								xEssentialsPlayer xp = xEssentials.get(sender.getName());
								if(Configuration.getKitConfig().isCooldownEnabled()) {
									if(xp.hasKitCooldown()) {
										long cooldown = xp.getKitCooldown() / 1000L + (long)Configuration.getKitConfig().getCoolDown() - System.currentTimeMillis() / 1000L;
										if(cooldown > 0L) {
											DecimalFormat df = new DecimalFormat("#.##");
											sender.sendMessage(ChatColor.RED + "you cannot use kits at this time please wait " + df.format((double)cooldown/60.0D) + " seconds!");
											return false;
										} else {
											xp.removeKitCoolDown();
											Kit kit = kits.get(args[0]);
											xp.getPlayer().getInventory().addItem(kit.getKitItems());
											sender.sendMessage(ChatColor.GREEN + "enjoy your " + kit.getKitName() + "!");
											xp.setKitCooldown(System.currentTimeMillis());
										}
									} else {
										Kit kit = kits.get(args[0]);
										xp.getPlayer().getInventory().addItem(kit.getKitItems());
										sender.sendMessage(ChatColor.GREEN + "enjoy your " + kit.getKitName() + "!");
										xp.setKitCooldown(System.currentTimeMillis());
									}
								} else {
									Kit kit = kits.get(args[0]);
									xp.getPlayer().getInventory().addItem(kit.getKitItems());
									sender.sendMessage(ChatColor.GREEN + "enjoy your " + kit.getKitName() + "!");
								}
							} else {
								Warnings.getWarnings(sender).noPermission();
							}
						} else {
							sender.sendMessage(ChatColor.RED + "this kit does not exist!");
						}
					}
				} else {
					Warnings.getWarnings(sender).noPermission();
				}
			} else {
				Warnings.getWarnings(sender).consoleMessage();
			}
		}
		return false;
	}

}
