package tv.mineinthebox.essentials.events.customEvents;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.GreyListCause;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;

public class OfflinePlayerGreyListedEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private String group;
	private String p;
	private String OldGroup;
	private boolean cancel;
	private GreyListCause cause;

	public OfflinePlayerGreyListedEvent(String p, String group, String OldGroup, GreyListCause cause) {
		this.group = group;
		this.OldGroup = OldGroup;
		this.p = p;
		this.cause = cause;
	}

	/**
	 * @author xize
	 * @param get the new group the player is in
	 * @return String
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @author xize
	 * @param get the old group the player whas in
	 * @return String
	 */
	public String getOldGroup() {
		return OldGroup;
	}

	/**
	 * @author xize
	 * @param returns the type of cause why this event has been triggered
	 * @return
	 */
	public GreyListCause getCause() {
		return cause;
	}

	/**
	 * @author xize
	 * @param returns the offline player instance
	 * @return xEssentialsOfflinePlayer
	 */
	public xEssentialsOfflinePlayer getEssentialsOfflinePlayer() {
		return xEssentials.getOfflinePlayer(p);
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean bol) {
		if(bol) {
			cancel = true;
			xEssentials.getVault().setGroup(Bukkit.getWorlds().get(0),p, OldGroup);
		}
	}

}
