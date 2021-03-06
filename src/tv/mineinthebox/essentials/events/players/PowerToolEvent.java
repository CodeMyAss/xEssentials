package tv.mineinthebox.essentials.events.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class PowerToolEvent implements Listener {

	@EventHandler
	public void onPowerTool(PlayerInteractEvent e) {
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(xp.hasPowerTool() && xp.getPlayer().hasPermission(PermissionKey.CMD_POWERTOOL.getPermission())) {
				xp.getPlayer().performCommand(xp.getPowerTool());
			}
		}
	}
}
