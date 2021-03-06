package tv.mineinthebox.essentials.events.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class PlayerIgnorePlayerChatEvent implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onIgnoreChat(AsyncPlayerChatEvent e) {
		for(xEssentialsPlayer xp : xEssentials.getPlayers()) {
			if(xp.hasIgnoredPlayers()) {
				if(xp.getIgnoredPlayers().contains(e.getPlayer().getName())) {
					e.getRecipients().remove(xp.getPlayer());
				}
			}
		}
	}

}
