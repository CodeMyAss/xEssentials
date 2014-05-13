package tv.mineinthebox.essentials.events.backpackEvent;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import tv.mineinthebox.essentials.instances.BackPack;
import tv.mineinthebox.essentials.utils.BackPackData;

public class BackPackCloseEvent implements Listener {
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "Backpack:")) {
			if(BackPackData.users.containsKey(e.getPlayer().getName())) {
				try {
					BackPack backpack = new BackPack(BackPackData.users.get(e.getPlayer().getName()));
					e.getPlayer().getInventory().remove(BackPackData.users.get(e.getPlayer().getName()));
					e.getPlayer().getInventory().addItem(backpack);
					backpack.saveBackPack(e.getInventory());
					BackPackData.users.remove(e.getPlayer().getName());
					Player p = (Player) e.getPlayer();
					p.sendMessage(ChatColor.GREEN + "closing backpack!...");
					p.playSound(e.getPlayer().getLocation(), Sound.CHEST_CLOSE, 1F, 1F);
				} catch (Exception e1) {}
			}
		}
	}
}
