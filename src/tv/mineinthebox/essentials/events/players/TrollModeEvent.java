package tv.mineinthebox.essentials.events.players;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class TrollModeEvent implements Listener {

	@EventHandler
	public void onTroll(PlayerInteractEntityEvent e) {
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		if(xp.isTrollMode()) {
			if(e.getRightClicked() instanceof Player) {
				Player p = (Player) e.getRightClicked();
				e.getPlayer().setPassenger(p);
				e.getPlayer().sendMessage(ChatColor.GREEN + "now left click on a block to place the player!");
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
			xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
			if(xp.isTrollMode()) {
				if(xp.getPlayer().getPassenger() instanceof Player) {
					Player p = (Player) xp.getPlayer().getPassenger();
					p.getVehicle().eject();
					p.teleport(e.getClickedBlock().getRelative(BlockFace.UP).getLocation());
					e.getPlayer().sendMessage(ChatColor.GREEN + "placed player now he should be confused!");
					e.setCancelled(true);
				}
			}
		}
	}

}
