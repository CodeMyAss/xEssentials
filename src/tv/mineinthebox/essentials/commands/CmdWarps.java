package tv.mineinthebox.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;

public class CmdWarps {

	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("warps")) {
			if(sender.hasPermission(PermissionKey.CMD_WARPS.getPermission())) {
				if(xEssentials.getWarps().length != 0) {
					StringBuilder build = new StringBuilder();
					for(int i = 0; i < xEssentials.getWarps().length; i++) {
						if(i == xEssentials.getWarps().length) {
							build.append(xEssentials.getWarps()[i].getWarpName());
						} else {
							build.append(xEssentials.getWarps()[i].getWarpName() + ", ");
						}
					}
					sender.sendMessage(ChatColor.GOLD + ".oO___[Warp list]___Oo.");
					sender.sendMessage(ChatColor.GRAY + build.toString());
				} else {
					sender.sendMessage(ChatColor.GOLD + ".oO___[Warp list]___Oo.");
					sender.sendMessage(ChatColor.RED + "no warps could be found!");
				}
			} else {
				Warnings.getWarnings(sender).noPermission();
			}
		}
		return false;
	}

}
