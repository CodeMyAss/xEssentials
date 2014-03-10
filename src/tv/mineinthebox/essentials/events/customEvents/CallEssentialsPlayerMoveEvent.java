package tv.mineinthebox.essentials.events.customEvents;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class CallEssentialsPlayerMoveEvent implements Listener {
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Bukkit.getPluginManager().callEvent(new EssentialsPlayerMoveEvent(e.getPlayer(), e));
	}

}
