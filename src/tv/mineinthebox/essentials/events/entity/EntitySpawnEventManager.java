package tv.mineinthebox.essentials.events.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import tv.mineinthebox.essentials.Configuration;

public class EntitySpawnEventManager implements Listener {

	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent e) {
		if(Configuration.getEntityConfig().getEntitys().containsKey(e.getEntityType().name().toLowerCase())) {
			if(!Configuration.getEntityConfig().getEntitys().get(e.getEntityType().name().toLowerCase())) {
				e.setCancelled(true);
			}
		}
	}
	
}
