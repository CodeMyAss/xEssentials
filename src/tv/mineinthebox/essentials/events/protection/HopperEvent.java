package tv.mineinthebox.essentials.events.protection;

import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import tv.mineinthebox.essentials.xEssentials;

public class HopperEvent implements Listener {
	
	@EventHandler
	public void cancelhopper(InventoryMoveItemEvent e) {
		if(e.getSource().getHolder() instanceof Chest || e.getSource().getHolder() instanceof DoubleChest || e.getSource().getHolder() instanceof Furnace) {
			BlockState state = (BlockState)e.getSource().getHolder();
			if(xEssentials.getProtectionDatabase().isRegistered(state.getBlock())) {
				e.setCancelled(true);
			}
		} else if(e.getDestination().getHolder() instanceof Chest || e.getDestination().getHolder() instanceof DoubleChest || e.getDestination() instanceof Furnace) {
			BlockState state = (BlockState)e.getDestination().getHolder();
			if(xEssentials.getProtectionDatabase().isRegistered(state.getBlock())) {
				e.setCancelled(true);
			}
		}
	}

}
