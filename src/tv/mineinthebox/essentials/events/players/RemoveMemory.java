package tv.mineinthebox.essentials.events.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tv.mineinthebox.essentials.xEssentials;

public class RemoveMemory implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuitMemory(PlayerQuitEvent e) {
		if(xEssentials.contains(e.getPlayer().getName())) {
			xEssentials.remove(e.getPlayer().getName());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuitMemory(PlayerKickEvent e) {
		if(xEssentials.contains(e.getPlayer().getName())) {
			xEssentials.remove(e.getPlayer().getName());
		}
	}
}
