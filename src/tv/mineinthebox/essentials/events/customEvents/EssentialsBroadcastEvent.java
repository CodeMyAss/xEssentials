package tv.mineinthebox.essentials.events.customEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EssentialsBroadcastEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private String message;
	
	public EssentialsBroadcastEvent(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
