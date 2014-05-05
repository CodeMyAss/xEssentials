package tv.mineinthebox.essentials.events.minigames;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;
import tv.mineinthebox.essentials.utils.MiniGame;

public class PlayerTeleportCheck implements Listener {
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if(e.isCancelled()) {
			return;
		}
		
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		
		if(MiniGame.isPlayerInArea(e.getPlayer())) {
			e.getPlayer().sendMessage(ChatColor.RED + "you cannot teleport when playing in a " + MiniGame.getArenaFromPlayer(e.getPlayer()).getClass().getSimpleName());
			e.setCancelled(true);
		} else if(xp.hasSavedInventory()) {
			e.getPlayer().sendMessage(ChatColor.GREEN + "loading your orginal inventory!");
			xp.loadInventory();
		}
	}

}
