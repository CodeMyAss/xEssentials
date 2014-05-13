package tv.mineinthebox.essentials.events.backpackEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tv.mineinthebox.essentials.instances.BackPack;
import tv.mineinthebox.essentials.utils.BackPackData;

public class BackPackOnQuitEvent implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(BackPackData.users.containsKey(e.getPlayer().getName())) {
			BackPack backpack;
			try {
				backpack = new BackPack(BackPackData.users.get(e.getPlayer().getName()));
				backpack.saveBackPack(e.getPlayer().getOpenInventory().getTopInventory());
				BackPackData.users.remove(e.getPlayer().getName());
			} catch (Exception e1) {}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerKickEvent e) {
		if(BackPackData.users.containsKey(e.getPlayer().getName())) {
			BackPack backpack;
			try {
				backpack = new BackPack(BackPackData.users.get(e.getPlayer().getName()));
				backpack.saveBackPack(e.getPlayer().getOpenInventory().getTopInventory());
				BackPackData.users.remove(e.getPlayer().getName());
			} catch (Exception e1) {}
		}
	}

}
