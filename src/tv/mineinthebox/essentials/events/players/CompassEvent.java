package tv.mineinthebox.essentials.events.players;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class CompassEvent implements Listener {

	@EventHandler
	public void onCompassMove(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getPlayer().getItemInHand().getType() == Material.WATCH) {
				xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
				if(xp.hasCompass()) {
					Player p = xp.getCompass().getPlayer();
					if(p instanceof Player) {
						Vector direction = p.getLocation().toVector().subtract(e.getPlayer().getLocation().toVector()).normalize().multiply(2);
						e.getPlayer().setVelocity(direction);
					} else {
						Location loc = xp.getCompass().getLocation();
						Vector direction = loc.toVector().subtract(e.getPlayer().getLocation().toVector()).normalize().multiply(2);
						e.getPlayer().setVelocity(direction);
					}
				}
			}
		}
	}
}
