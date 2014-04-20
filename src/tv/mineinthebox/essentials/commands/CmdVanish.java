package tv.mineinthebox.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.events.customEvents.VanishChangeEvent;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class CmdVanish {

	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("vanish")) {
			if(sender instanceof Player) {
				if(sender.hasPermission(PermissionKey.CMD_VANISH.getPermission())) {
					Player p = (Player) sender;
					xEssentialsPlayer xp = xEssentials.get(p.getName());
					if(args.length == 0) {
						if(xp.isVanished()) {
							xp.unvanish();
							xp.setNoPickUp(false);
							sender.sendMessage(ChatColor.GREEN + "you are successfully unvanished ;-)");
							Bukkit.getPluginManager().callEvent(new VanishChangeEvent(p, xp));
						} else {
							xp.vanish();
							xp.setNoPickUp(true);
							sender.sendMessage(ChatColor.GREEN + "you are successfully vanished ;-)");
							Bukkit.getPluginManager().callEvent(new VanishChangeEvent(p, xp));
						}
					} else if(args.length == 1) {
						if(args[0].equalsIgnoreCase("help")) {
							sender.sendMessage(ChatColor.GOLD + ".oO___[vanish help]___Oo.");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/vanish " + ChatColor.WHITE + ": vanish yourself for other players");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/vanish nopickup " + ChatColor.WHITE + ": toggles nopickup or de-toggles nopickup");
							sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/vanish effect " + ChatColor.WHITE + ": toggles the effects of vanish");
						} else if(args[0].equalsIgnoreCase("nopickup")) {
							if(xp.isVanished()) {
								if(xp.isNoPickUpEnabled()) {
									xp.setNoPickUp(false);
									sender.sendMessage(ChatColor.GREEN + "successfully disabled nopickup, you can now pickup items and interact with chests");
								} else {
									xp.setNoPickUp(true);
									sender.sendMessage(ChatColor.GREEN + "successfully enabled nopickup, you can now not pickup items and interact with chests");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "you can only use this option while being vanished!");
							}
						} else if(args[0].equalsIgnoreCase("effect")) {
							if(xp.hasVanishEffects()) {
								xp.setVanishEffects(false);
								sender.sendMessage(ChatColor.GREEN + "successfully disabled vanish effects for yourself");
							} else {
								xp.setVanishEffects(true);
								sender.sendMessage(ChatColor.GREEN + "successfully enabled vanish effects for yourself");
							}
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
