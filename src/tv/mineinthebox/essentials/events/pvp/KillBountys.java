package tv.mineinthebox.essentials.events.pvp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.hook.Hooks;
import tv.mineinthebox.essentials.hook.VaultHook;

public class KillBountys implements Listener {

	private HashMap<UUID, UUID> entitys = new HashMap<UUID, UUID>();

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			if(e.getEntity() instanceof LivingEntity) {
				Player p = (Player) e.getDamager();
				entitys.put(p.getUniqueId(), e.getEntity().getUniqueId());
			}
		} else if(e.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getDamager();
			if(arrow.getShooter() instanceof Player) {
				if(e.getEntity() instanceof LivingEntity) {
					Player p = (Player) arrow.getShooter();
					entitys.put(p.getUniqueId(), e.getEntity().getUniqueId());
				}
			}
		} else if(e.getDamager() instanceof ThrownPotion) {
			ThrownPotion pot = (ThrownPotion) e.getDamager();
			if(pot.getShooter() instanceof Player) {
				if(e.getEntity() instanceof LivingEntity) {
					Player p = (Player) pot.getShooter();
					entitys.put(p.getUniqueId(), e.getEntity().getUniqueId());
				}
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if(entitys.containsValue(e.getEntity().getUniqueId())) {
			if(e.getEntity() instanceof Player) {
				Player killed = (Player) e.getEntity();
				UUID PlayerName = getPlayerNameFromUUID(e.getEntity().getUniqueId());
				if(PlayerName != null) {
					Player p = Bukkit.getPlayer(PlayerName);
					if(p instanceof Player) {
						if(Hooks.isVaultEnabled()) {
							VaultHook.desposit(p, Configuration.getPvpConfig().getKillBountyPrice());
							p.sendMessage(ChatColor.GOLD + "you earned " + Configuration.getPvpConfig().getKillBountyPrice() + "$! by killing " + ChatColor.GREEN + killed.getName());
							entitys.remove(p.getName());
						}
					}
				}
			} else {
				UUID PlayerName = getPlayerNameFromUUID(e.getEntity().getUniqueId());
				if(PlayerName != null) {
					Player p = Bukkit.getPlayer(PlayerName);
					if(p instanceof Player) {
						if(Hooks.isVaultEnabled()) {
							VaultHook.desposit(p, Configuration.getPvpConfig().getKillBountyPrice());
							p.sendMessage(ChatColor.GOLD + "you earned " + Configuration.getPvpConfig().getKillBountyPrice() + "$! by killing " + ChatColor.GREEN + e.getEntity().getType().name().toLowerCase().replace("_", " "));
							entitys.remove(p.getName());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(entitys.containsKey(e.getPlayer().getUniqueId())) {
			entitys.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onQuit(PlayerKickEvent e) {
		if(entitys.containsKey(e.getPlayer().getUniqueId())) {
			entitys.remove(e.getPlayer().getUniqueId());
		}
	}

	private UUID getPlayerNameFromUUID(UUID u) {
		Iterator<Entry<UUID, UUID>> it = entitys.entrySet().iterator();
		while(it.hasNext()) {
			Entry<UUID, UUID> map = it.next();
			if(map.getValue().equals(u)) {
				return map.getKey();
			}
		}
		return null;
	}

}
