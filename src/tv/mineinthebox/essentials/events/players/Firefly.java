package tv.mineinthebox.essentials.events.players;

import org.bukkit.Effect;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class Firefly implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFirefly(PlayerMoveEvent e) {
		if(xEssentials.contains(e.getPlayer().getName())) {
			xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
			if(xp.isFirefly()) {
				if(e.getFrom().distance(e.getTo()) > 0) {
					if(xp.isVanished()) {
						//only draw client side particles while being vanished
						e.getPlayer().playEffect(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
						e.getPlayer().playEffect(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(), Effect.ENDER_SIGNAL, 1);
					} else {
						//draw particles towards everyone
						e.getPlayer().getWorld().playEffect(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
						e.getPlayer().getWorld().playEffect(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(), Effect.ENDER_SIGNAL, 1);	
					}
				}
			}
		}
	}

}
