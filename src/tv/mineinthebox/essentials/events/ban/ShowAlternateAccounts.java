package tv.mineinthebox.essentials.events.ban;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class ShowAlternateAccounts implements Listener {

	@EventHandler
	public void showAltsOnJoin(PlayerJoinEvent e) {
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		if(!xp.isStaff()) {
			if(xp.hasAlternateAccounts()) {
				for(xEssentialsPlayer player : xEssentials.getPlayers()) {
					if(player.isStaff()) {
						player.getPlayer().sendMessage(ChatColor.GOLD + "-----------------------------------------------------");
						player.getPlayer().sendMessage(ChatColor.GOLD + ".oO___[Alternate Accounts for player " + xp.getUser()+"]___Oo.");
						player.getPlayer().sendMessage(ChatColor.GREEN + xp.getUser() + ChatColor.GRAY + " has may alternate accounts!");
						player.getPlayer().sendMessage(xp.getAlternateAccounts().getAltsDetailed());
						player.getPlayer().sendMessage(ChatColor.GOLD + "-----------------------------------------------------");
					}
				}
			}
		}
	}

}
