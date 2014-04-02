package tv.mineinthebox.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.utils.TpaManager;

public class CmdTpAccept {
	
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("tpaccept")) {
			if(sender.hasPermission(PermissionKey.CMD_TP_ACCEPT.getPermission())) {
				if(TpaManager.containsKey(sender.getName())) {
					Player p = (Player) sender;
					Player victem = xEssentials.getOfflinePlayer(TpaManager.get(sender.getName())).getPlayer();
					if(victem instanceof Player) {
						victem.teleport(p);
						victem.sendMessage(ChatColor.GREEN + sender.getName() + " has successfully accepted your tpa request!");
						sender.sendMessage(ChatColor.GREEN + "you have successfully accepted " + victem.getName() + " his tpa request!");
						TpaManager.remove(sender.getName());
					} else {
						sender.sendMessage(ChatColor.RED + "the player went offline!");
						TpaManager.remove(sender.getName());
					}
				} else {
					sender.sendMessage(ChatColor.RED + "you don't have tpa requests open!");
				}
			} else {
				Warnings.getWarnings(sender).noPermission();
			}
		}
		return false;
	}

}
