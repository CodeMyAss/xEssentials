package tv.mineinthebox.essentials.events.backpackEvent;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import tv.mineinthebox.essentials.instances.BackPack;
import tv.mineinthebox.essentials.utils.BackPackData;

public class BackPackOpenEvent implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if(e.getItem() instanceof ItemStack) {
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
				if(e.getItem().hasItemMeta()) {
					if(e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().hasDisplayName()) {
						try {
							BackPack backpack = new BackPack(e.getItem());
							e.getPlayer().sendMessage(ChatColor.GREEN + "opening backpack!...");
							e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CHEST_OPEN, 1F, 1F);
							e.getPlayer().openInventory(backpack.getBackPack());
							BackPackData.users.put(e.getPlayer().getName(), e.getItem());
							e.setCancelled(true);
						} catch (Exception e1) {e1.printStackTrace();}
					}
				}
			}
		}
	}

}
