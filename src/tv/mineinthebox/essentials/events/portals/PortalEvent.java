package tv.mineinthebox.essentials.events.portals;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.instances.Portal;

public class PortalEvent implements Listener {
	
	@EventHandler
	public void onEnter(PlayerPortalEvent e) {
		if(e.isCancelled()) {
			return;
		}
		
		e.setCancelled(true);
		
		Block block = getPortalNearby(e.getPlayer().getLocation().getBlock());
		if(!(block instanceof Block)) {
			return;
		}
		for(Portal portal : Configuration.getPortalConfig().getPortals().values()) {
			List<Block> blocks = Arrays.asList(portal.getInnerBlocks());
			if(blocks.contains(block)) {
				e.useTravelAgent(false);
				if(portal.isLinked()) {
					Portal linked = portal.getLinkedPortal();
					if(!(linked instanceof Portal)) {
						e.setCancelled(true);
						return;
					}
					e.setTo(linked.getInnerBlocks()[(linked.getInnerBlocks().length-1)].getLocation());
					e.setCancelled(false);
				} else {
					e.setCancelled(true);
				}
				break;
			}
		}
		e.setCancelled(false);
	}
	
	public Block getPortalNearby(Block block) {
		BlockFace[] faces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.SELF}; 
		for(BlockFace face : faces) {
			if(block.getRelative(face).getType() == Material.PORTAL) {
				return block.getRelative(face);
			}
		}
		return null;
	}

}
