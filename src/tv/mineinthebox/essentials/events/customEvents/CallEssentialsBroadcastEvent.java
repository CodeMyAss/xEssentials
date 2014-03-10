package tv.mineinthebox.essentials.events.customEvents;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;

public class CallEssentialsBroadcastEvent {

	private static BukkitTask task;

	/**
	 * @author xize
	 * @param start the broadcast system
	 * @return void
	 */
	public static void startBroadcast() {
		final List<String> list = Configuration.getBroadcastConfig().getMessages();
		final ListIterator<String> it = list.listIterator();
		task = Bukkit.getScheduler().runTaskTimer(xEssentials.getPlugin(), new Runnable() {

			@Override
			public void run() {
				if(it.hasNext()) {
					String message = Configuration.getBroadcastConfig().getPrefix() + Configuration.getBroadcastConfig().getSuffix() + ChatColor.translateAlternateColorCodes('&', it.next());
					Bukkit.broadcastMessage(message);
					Bukkit.getPluginManager().callEvent(new EssentialsBroadcastEvent(message));
				} else {
					while(it.hasPrevious()) {
						it.previous();
					}
					if(it.hasNext()) {
						String message = Configuration.getBroadcastConfig().getPrefix() + Configuration.getBroadcastConfig().getSuffix() + ChatColor.translateAlternateColorCodes('&', it.next());
						Bukkit.broadcastMessage(message);
						Bukkit.getPluginManager().callEvent(new EssentialsBroadcastEvent(message));
					}
				}
			}

		}, 100, 1400);
	}
	
	/**
	 * @author xize
	 * @param stop the broadcast system
	 * @return void
	 */
	public static void stop() {
		if(task != null) {
			task.cancel();	
		}
	}

}
