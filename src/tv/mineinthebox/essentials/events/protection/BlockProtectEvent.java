package tv.mineinthebox.essentials.events.protection;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;

public class BlockProtectEvent implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void onDestroy(BlockBreakEvent e) {
		if(xEssentials.getProtectionDatabase().isRegistered(e.getBlock())) {
			if(xEssentials.getProtectionDatabase().removeProtectedBlock(e)) {
				e.getPlayer().sendMessage(ChatColor.RED + "you successfully unregistered that block");
			} else {
				e.getPlayer().sendMessage(Configuration.getProtectionConfig().getDisallowMessage().replace("%BLOCK%", e.getBlock().getType().name()));
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(xEssentials.getProtectionDatabase().isRegistered(e.getClickedBlock())) {
				if(xEssentials.getProtectionDatabase().isOwner(e.getPlayer(), e.getClickedBlock()) || e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
					e.getPlayer().sendMessage(ChatColor.GREEN + "that private block belongs to you");
				} else {
					e.getPlayer().sendMessage(Configuration.getProtectionConfig().getDisallowMessage().replace("%BLOCK%", e.getClickedBlock().getType().name()));
				}
			}
		}
	}

	@EventHandler
	public void onPiston(BlockPistonExtendEvent e) {
		for(Block block : e.getBlocks()) {
			if(xEssentials.getProtectionDatabase().isRegistered(block)) {
				e.setCancelled(true);
				break;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onExplosion(EntityExplodeEvent e) {
		for(Block block : e.blockList()) {
			if(xEssentials.getProtectionDatabase().isRegistered(block)) {
				e.setCancelled(true);
			}
		}
	}

}
