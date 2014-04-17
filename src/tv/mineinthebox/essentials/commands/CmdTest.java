package tv.mineinthebox.essentials.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdTest {
	
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("test")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				sender.sendMessage("your uuid(bukkit): " + p.getUniqueId().toString());
				sender.sendMessage("your uuid(legacy-name): " + UUID.nameUUIDFromBytes(p.getName().getBytes()).toString().replace("-", ""));
				sender.sendMessage("your uuid(email): " + UUID.nameUUIDFromBytes("glucassen@live.nl".getBytes()).toString().replace("-", ""));
			} else {
				sender.sendMessage("an console cannot run this");
			}
		}
		return false;
	}

}
