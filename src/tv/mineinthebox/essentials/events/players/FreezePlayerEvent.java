package tv.mineinthebox.essentials.events.players;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import tv.mineinthebox.essentials.events.customEvents.EssentialsPlayerMoveEvent;

public class FreezePlayerEvent implements Listener {
	
	@EventHandler
	public void onMove(EssentialsPlayerMoveEvent e) {
		if(e.getPlayer().isFreezed()) {
			Location from = e.getFrom();
			from.setPitch(e.getTo().getPitch());
			from.setYaw(e.getTo().getYaw());
			e.setTo(from);
		}
	}

}
