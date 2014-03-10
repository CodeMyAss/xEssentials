package tv.mineinthebox.essentials.events.players;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.events.customEvents.EssentialsPlayerMoveEvent;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class PotatoMoveEvent implements Listener {

	@EventHandler
	public void onPotatoMove(EssentialsPlayerMoveEvent e) {
		if(e.getPlayer().isPotato()) {
			if(e.getFrom().distanceSquared(e.getTo()) > 0) {
				Item item = e.getPlayer().getPotato();
				Vector direction = e.getTo().toVector().subtract(item.getLocation().toVector()).normalize();
				item.setVelocity(direction.multiply(0.5));
			}
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if(e.getItem().getItemStack().getType() == Material.POTATO_ITEM) {
			for(Entity entity : e.getItem().getNearbyEntities(5, 5, 5)) {
				if(entity instanceof Player) {
					xEssentialsPlayer xp = xEssentials.get(((Player) entity).getName());
					if(xp.isPotato()) {
						if(e.getItem().equals(xp.getPotato())) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		if(xp.isPotato()) {
			e.getPlayer().sendMessage(ChatColor.GREEN + "potatos cannot pickup items!");
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		if(xp.isPotato()) {
			e.getPlayer().sendMessage(ChatColor.GREEN + "potatos cannot place blocks!");
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockBreakEvent e) {
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		if(xp.isPotato()) {
			e.getPlayer().sendMessage(ChatColor.GREEN + "potatos cannot break blocks!");
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		if(xp instanceof xEssentialsPlayer) {	
			if(xp.isPotato()) {
				xp.unvanish();
				xp.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
				xp.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
				xp.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				Item potato = xp.getPotato();
				potato.remove();
				xp.removePotato();
			}	
		}
	}

	@EventHandler
	public void onQuit(PlayerKickEvent e) {
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		if(xp instanceof xEssentialsPlayer) {	
			if(xp.isPotato()) {
				xp.unvanish();
				xp.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
				xp.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
				xp.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				Item potato = xp.getPotato();
				potato.remove();
				xp.removePotato();
			}
		}	
	}

}
