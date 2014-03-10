package tv.mineinthebox.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.utils.TpaManager;

public class CmdTpDeny {
	
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("tpdeny")) {
			if(sender.hasPermission(PermissionKey.CMD_TP_DENY.getPermission())) {
				if(TpaManager.containsKey(sender.getName())) {
					Player requester = Bukkit.getPlayer(TpaManager.get(sender.getName()));
					if(requester instanceof Player) {
						requester.sendMessage(ChatColor.RED + sender.getName() + " has denied your tpa request!");
					}
					sender.sendMessage(ChatColor.GREEN + "successfully denied " + TpaManager.get(sender.getName()) + " his request!");
					TpaManager.remove(sender.getName());
				} else {
					sender.sendMessage(ChatColor.RED + "you don't have any tpa requests open!");
				}
			} else {
				Warnings.getWarnings(sender).noPermission();
			}
		}
		return false;
	}

}
