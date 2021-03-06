package tv.mineinthebox.essentials.events.players;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.hook.Hooks;
import tv.mineinthebox.essentials.hook.WorldGuardHook;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class PlayerJoinMessage implements Listener {

	@EventHandler
	public void onjoin(PlayerJoinEvent e) {
		if(xEssentials.contains(e.getPlayer().getName())) {
			xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
			if(Hooks.isWorldGuardEnabled()) {
				if(xp.isVanished()) {
					e.setJoinMessage("");
				} else {
					e.setJoinMessage(WorldGuardHook.sendJoinMessage(e.getPlayer()));	
				}
			} else {
				if(xp.isStaff()) {
					if(xp.isVanished()) {
						e.setJoinMessage("");
					} else {
						if(e.getPlayer().getName().equalsIgnoreCase("Xeph0re")) {
							e.setJoinMessage(ChatColor.GRAY + "Developer of xEssentials " + ChatColor.GREEN + e.getPlayer().getName() + ChatColor.GRAY + " has joined the game!");
						} else {
							e.setJoinMessage(ChatColor.GRAY + "Staff member " + ChatColor.GREEN + e.getPlayer().getName() + ChatColor.GRAY + " has joined the game!");	
						}	
					}
				} else {
					e.setJoinMessage(ChatColor.GREEN + "player " + ChatColor.GRAY + e.getPlayer().getName() + ChatColor.GREEN + " has joined the game!");
				}

			}
		}
	}

}
