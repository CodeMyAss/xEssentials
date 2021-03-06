package tv.mineinthebox.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class CmdFirefly {
	
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("firefly")) {
			if(sender instanceof Player) {
				if(sender.hasPermission(PermissionKey.CMD_FIREFLY.getPermission())) {
					if(xEssentials.contains(sender.getName())) {
						xEssentialsPlayer xp = xEssentials.get(sender.getName());
						if(xp.isFirefly()) {
							xp.setFirefly(false);
							sender.sendMessage(ChatColor.GREEN + "you successfully disabled firefly!");
						} else {
							xp.setFirefly(true);
							sender.sendMessage(ChatColor.GREEN + "you successfully enabled firefly!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "you don't seems to be listed at our global list, please reload xEssentials");
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
