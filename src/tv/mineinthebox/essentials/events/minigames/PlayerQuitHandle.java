package tv.mineinthebox.essentials.events.minigames;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tv.mineinthebox.essentials.instances.SpleefArena;
import tv.mineinthebox.essentials.utils.MiniGame;

public class PlayerQuitHandle implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(MiniGame.isPlayerInArea(e.getPlayer())) {
			Object obj = MiniGame.getArenaFromPlayer(e.getPlayer());
			if(obj instanceof SpleefArena) {
				SpleefArena arena = (SpleefArena) obj;
				arena.removePlayer(e.getPlayer().getName());
			} else {
				//TO-DO hungergames arena.
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerKickEvent e) {
		if(MiniGame.isPlayerInArea(e.getPlayer())) {
			Object obj = MiniGame.getArenaFromPlayer(e.getPlayer());
			if(obj instanceof SpleefArena) {
				SpleefArena arena = (SpleefArena) obj;
				arena.removePlayer(e.getPlayer().getName());
			} else {
				//TO-DO hungergames arena.
			}
		}
	}

}
