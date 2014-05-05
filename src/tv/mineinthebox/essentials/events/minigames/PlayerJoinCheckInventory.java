package tv.mineinthebox.essentials.events.minigames;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class PlayerJoinCheckInventory implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		
		if(xp.hasSavedInventory() && !xEssentials.getPlugin().isPlayerInArea(e.getPlayer())) {
			e.getPlayer().sendMessage(ChatColor.GREEN + "since it seems you have quited before a minigame ended we have backuped your inventory!");
			xp.loadInventory();
		}
	}

}
