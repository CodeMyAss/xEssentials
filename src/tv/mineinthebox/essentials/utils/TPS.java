package tv.mineinthebox.essentials.utils;

import java.lang.management.ManagementFactory;

import org.bukkit.Bukkit;

import tv.mineinthebox.essentials.xEssentials;

public class TPS {

	private static int tps = 0;
	private static long second = 0;
	
	public static float getServerTicks() {
		return tps;
	}
	
	public static Long getServerUpTime() {
		return ManagementFactory.getRuntimeMXBean().getStartTime();
	}
	
	public static long garbageCollectorMax() {
		Long time = ((Runtime.getRuntime().maxMemory() / 1024) / 1024);
		return time;
	}
	
	public static long getMemoryMax() {
		Long time = ((Runtime.getRuntime().totalMemory() / 1024) / 1024);
		return time;
	}
	
	public static long getFreeMemory() {
		Long time = ((Runtime.getRuntime().freeMemory() / 1024) / 1024);
		return time;
	}
	
	public static void startTps() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(xEssentials.getPlugin(), new Runnable() {
			long sec;
			int ticks;
			@Override
			public void run() {
				sec = System.currentTimeMillis() / 1000;
				if (second == sec) {
					ticks++; 
				} else {
					second = sec;
					tps = tps == 0 ? ticks : (tps + ticks) / 2;
					ticks = 0;
				}
			}
		}, 21, 1);
	}

}
