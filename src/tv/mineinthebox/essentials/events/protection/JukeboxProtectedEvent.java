package tv.mineinthebox.essentials.events.protection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;

public class JukeboxProtectedEvent implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDestroy(BlockBreakEvent e) {
		if(xEssentials.getProtectionDatabase().isRegistered(e.getBlock())) {
			if(e.getBlock().getType() == Material.JUKEBOX) {
				if(xEssentials.getProtectionDatabase().removeProtectedBlock(e)) {
					e.getPlayer().sendMessage(ChatColor.RED + "you successfully unregistered that jukebox");
				} else {
					e.getPlayer().sendMessage(Configuration.getProtectionConfig().getDisallowMessage().replace("%BLOCK%", e.getBlock().getType().name()));
					e.setCancelled(true);
				}
			} else {
				e.getPlayer().sendMessage(Configuration.getProtectionConfig().getDisallowMessage().replace("%BLOCK%", e.getBlock().getType().name()));
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(e.getBlock().getType() == Material.JUKEBOX) {
			if(xEssentials.getProtectionDatabase().addProtectedBlock(e)) {
				e.getPlayer().sendMessage(ChatColor.GREEN + "registered private jukebox");
			} else {
				e.getPlayer().sendMessage(Configuration.getProtectionConfig().getDisallowMessage().replace("%BLOCK%", e.getBlock().getType().name()));
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType() == Material.JUKEBOX) {
				if(xEssentials.getProtectionDatabase().isRegistered(e.getClickedBlock())) {
					if(xEssentials.getProtectionDatabase().isOwner(e.getPlayer(), e.getClickedBlock()) || e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						e.getPlayer().sendMessage(ChatColor.GREEN + "opening private jukebox");
					} else {
						e.getPlayer().sendMessage(Configuration.getProtectionConfig().getDisallowMessage().replace("%BLOCK%", e.getClickedBlock().getType().name()));
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPiston(BlockPistonExtendEvent e) {
		for(Block block : e.getBlocks()) {
			if(block.getType() == Material.JUKEBOX) {
				if(xEssentials.getProtectionDatabase().isRegistered(block)) {
					e.setCancelled(true);
					break;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onExplosion(EntityExplodeEvent e) {
		for(Block block : e.blockList()) {
			if(block.getType() == Material.JUKEBOX) {
				if(xEssentials.getProtectionDatabase().isRegistered(block)) {
					e.setCancelled(true);
				}
			}
		}
	}

}
