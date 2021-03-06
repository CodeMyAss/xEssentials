package tv.mineinthebox.essentials.events.players;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class FreezePlayerEvent implements Listener {
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		if(xp.isFreezed()) {
			Location from = e.getFrom();
			from.setPitch(e.getTo().getPitch());
			from.setYaw(e.getTo().getYaw());
			e.setTo(from);
		}
	}

}
