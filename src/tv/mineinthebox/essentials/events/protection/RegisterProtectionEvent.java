package tv.mineinthebox.essentials.events.protection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tv.mineinthebox.essentials.xEssentials;

public class RegisterProtectionEvent implements Listener {

	private final List<Material> materials() {
		Material[] materials = {
				Material.CHEST, 
				Material.TRAPPED_CHEST,
				Material.IRON_DOOR_BLOCK,
				Material.WOODEN_DOOR,
				Material.SIGN_POST,
				Material.WALL_SIGN,
				Material.FURNACE,
				Material.JUKEBOX,
				Material.TRAP_DOOR
		};
		return Arrays.asList(materials);
	}

	public static HashSet<String> players = new HashSet<String>();

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(players.contains(e.getPlayer().getName())) {
				if(!xEssentials.getProtectionDatabase().isRegistered(e.getClickedBlock())) {
					if(materials().contains(e.getClickedBlock().getType())) {
						if(xEssentials.getProtectionDatabase().addProtectedBlock(e.getPlayer(), e.getClickedBlock())) {
							e.getPlayer().sendMessage(ChatColor.GREEN + "successfully registered private " + e.getClickedBlock().getType().name());
							players.remove(e.getPlayer().getName());
							e.setCancelled(true);
						} else {
							e.getPlayer().sendMessage(ChatColor.RED + "something went wrong");
							players.remove(e.getPlayer().getName());
							e.setCancelled(true);
						}
					} else {
						e.getPlayer().sendMessage(ChatColor.RED + "this is not a valid block to be registered");
						players.remove(e.getPlayer().getName());
						e.setCancelled(true);
					}
				} else {
					e.getPlayer().sendMessage(ChatColor.RED + "this block is already registered!");
					players.remove(e.getPlayer().getName());
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(players.contains(e.getPlayer().getName())) {
			players.remove(e.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerKickEvent e) {
		if(players.contains(e.getPlayer().getName())) {
			players.remove(e.getPlayer().getName());
		}
	}

}
