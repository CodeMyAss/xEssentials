package tv.mineinthebox.essentials.events.customEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.GreyListCause;
import tv.mineinthebox.essentials.enums.HookEnum;
import tv.mineinthebox.essentials.hook.BPermissionsHook;
import tv.mineinthebox.essentials.hook.GroupManagerHook;
import tv.mineinthebox.essentials.hook.PermissionsExHook;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;
public class PlayerGreyListedEvent extends PlayerEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private String group;
	private String OldGroup;
	private HookEnum hook;
	private boolean cancel;
	private GreyListCause cause;
	
	public PlayerGreyListedEvent(Player who, String group, String OldGroup, HookEnum bpermissions, GreyListCause cause) {
		super(who);
		this.group = group;
		this.OldGroup = OldGroup;
		this.hook = bpermissions;
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
	 * @param returns the type of cause why this event has been triggered
	 * @return
	 */
	public GreyListCause getCause() {
		return cause;
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
	 * @param returns the xEssentials player!
	 * @return xEssentialsPlayer
	 */
	public xEssentialsPlayer getEssentialsPlayer() {
		return xEssentials.get(player.getName());
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
			if(hook == HookEnum.BPERMISSIONS) {
				BPermissionsHook.setGroup(player.getName(), OldGroup);
			} else if(hook == HookEnum.GROUPMANAGER) {
				GroupManagerHook.setGroup(player.getName(), OldGroup);
			} else if(hook == HookEnum.PERMISSIONSEX) {
				PermissionsExHook.setGroup(player.getName(), OldGroup);
			}
		}
	}

}
