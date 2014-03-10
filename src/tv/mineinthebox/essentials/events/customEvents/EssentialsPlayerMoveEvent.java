package tv.mineinthebox.essentials.events.customEvents;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class EssentialsPlayerMoveEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private PlayerMoveEvent e;
	private Player p;
	
	public EssentialsPlayerMoveEvent(Player who, PlayerMoveEvent e) {
		this.p = who;
		this.e = e;
	}
	
	public Location getTo() {
		return e.getTo();
	}
	
	public Location getFrom() {
		return e.getFrom();
	}
	
	public void setFrom(Location loc) {
		e.setFrom(loc);
	}
	
	public void setTo(Location loc) {
		e.setTo(loc);
	}
	
	public xEssentialsPlayer getPlayer() {
		return xEssentials.get(p.getName());
	}
	
	public boolean isCancelled() {
		return e.isCancelled();
	}

	public void setCancelled(boolean arg0) {
		e.setCancelled(arg0);
	}

	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
