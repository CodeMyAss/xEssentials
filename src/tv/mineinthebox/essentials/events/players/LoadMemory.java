package tv.mineinthebox.essentials.events.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.instances.MojangUUID;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class LoadMemory implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinSetMemory(PlayerJoinEvent e) {
		try {
			String uuid = new MojangUUID(e.getPlayer()).getUniqueId();
			xEssentialsPlayer xp = new xEssentialsPlayer(e.getPlayer(), uuid);
			xEssentials.set(e.getPlayer().getName(), xp);
		} catch(Exception r) {
			e.setJoinMessage("");
			e.getPlayer().kickPlayer("failed to join, cannot retrieve UUID of this player");
		}
	}

}
