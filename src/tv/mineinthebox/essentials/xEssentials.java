package tv.mineinthebox.essentials;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import tv.mineinthebox.essentials.commands.CommandList;
import tv.mineinthebox.essentials.commands.command;
import tv.mineinthebox.essentials.enums.LogType;
import tv.mineinthebox.essentials.events.CustomEventHandler;
import tv.mineinthebox.essentials.events.Handler;
import tv.mineinthebox.essentials.events.customEvents.CallEssentialsBroadcastEvent;
import tv.mineinthebox.essentials.events.customEvents.CallRssFeedEvent;
import tv.mineinthebox.essentials.events.players.RealisticGlass;
import tv.mineinthebox.essentials.greylist.GreyListServer;
import tv.mineinthebox.essentials.instances.Warp;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;
import tv.mineinthebox.essentials.utils.ProtectionDB;
import tv.mineinthebox.essentials.utils.TPS;


public class xEssentials extends JavaPlugin {

	private static xEssentials pl;
	private final Configuration conf = new Configuration();
	private final Handler Handler = new Handler();
	private final CustomEventHandler customhandler = new CustomEventHandler();
	private final CommandList cmdlist = new CommandList();
	private final RealisticGlass glass = new RealisticGlass();
	public static GreyListServer server = null;
	public static ProtectionDB protectiondb = null;

	public void onEnable() {
		pl = this;
		log("has been enabled", LogType.INFO);
		conf.createConfigs();
		Handler.start();
		customhandler.startCustomEvents();
		if(Bukkit.getOnlinePlayers().length > 0) {
			reloadPlayerBase();
		}
		for(String cmd : cmdlist.getAllCommands) {
				getCommand(cmd).setExecutor(new command());	
		}
		if(Configuration.getPlayerConfig().isRealisticGlassEnabled()) {
			glass.loadGlassBlocks();	
		}
		TPS.startTps();
		Configuration.HandleCommandManager();
		if(Configuration.getProtectionConfig().isProtectionEnabled()) {
			protectiondb = new ProtectionDB();	
		}
		server = new GreyListServer(Configuration.getGrayListConfig().getPort());
		if(Configuration.getGrayListConfig().isEnabled()) {
			server.createServer();
		}
	}

	public void onDisable() {
		xEssentials.players.clear();
		if(Configuration.getPlayerConfig().isRealisticGlassEnabled()) {
			glass.saveGlassBlocks();	
		}
		log("has been disabled!", LogType.INFO);
		if(Configuration.getChatConfig().isRssBroadcastEnabled()) {CallRssFeedEvent.saveLastFeed();}
		if(server instanceof GreyListServer) {
			if(server.isRunning()) {
				server.disable();
			}
		}
		if(CallEssentialsBroadcastEvent.isRunning()) {
			CallEssentialsBroadcastEvent.stop();
		}
	}

	/**
	 * @author xize
	 * @param name - the name of the possible player
	 * @return boolean
	 */
	public static boolean isEssentialsPlayer(String name) {
		for(xEssentialsOfflinePlayer off : getOfflinePlayers()) {
			if(off.getUser().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @author xize
	 * @param String, logType
	 * @param uses to log things through the console
	 * 
	 */
	public void log(String message, LogType type) {
		String prefix = "&2[&8xEssentials&2]&f";
		if(type == LogType.INFO) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+":"+message));
		} else if(type == LogType.SEVERE) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[Error]"+prefix+":"+message));
		} else if(type == LogType.DEBUG) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[Debug]"+prefix+":"+message));
		}
	}

	/**
	 * 
	 * @author xize
	 * @param returns a static instance back for the plugin
	 * @return xEssentials (JavaPlugin)
	 * 
	 */
	public static xEssentials getPlugin() {
		return pl;
	}

	private static HashSet<xEssentialsOfflinePlayer> offliners = new HashSet<xEssentialsOfflinePlayer>();

	/**
	 * @author xize
	 * @param get all the offline players in a array
	 * @return xEssentialsOfflinePlayer[]
	 */
	public static xEssentialsOfflinePlayer[] getOfflinePlayers() {
		if(offliners.isEmpty()) {
			List<xEssentialsOfflinePlayer> players = new ArrayList<xEssentialsOfflinePlayer>();
			try {
				File dir = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "players");
				File[] list = dir.listFiles();
				for(File f : list) {
					FileConfiguration con = YamlConfiguration.loadConfiguration(f);
					if(con.isSet("user")) {
						xEssentialsOfflinePlayer off = new xEssentialsOfflinePlayer(con.getString("user"));
						players.add(off);
					}
				}
				xEssentialsOfflinePlayer[] xplayers = players.toArray(new xEssentialsOfflinePlayer[players.size()]);
				offliners.addAll(players);
				return xplayers;
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			File dir = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "players");
			File[] list = dir.listFiles();
			if(list.length == offliners.size()) {
				//since its equals we don't need to for loop again!
				return offliners.toArray(new xEssentialsOfflinePlayer[offliners.size()]);
			} else {
				//since there are new offlineplayers we need to reset our system.
				offliners.clear();
				List<xEssentialsOfflinePlayer> players = new ArrayList<xEssentialsOfflinePlayer>();
				try {
					for(File f : list) {
						FileConfiguration con = YamlConfiguration.loadConfiguration(f);
						if(con.isSet("user")) {
							xEssentialsOfflinePlayer off = new xEssentialsOfflinePlayer(con.getString("user"));
							players.add(off);
						}
					}
					xEssentialsOfflinePlayer[] xplayers = players.toArray(new xEssentialsOfflinePlayer[players.size()]);
					offliners.addAll(players);
					return xplayers;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * @author xize
	 * @param player - the name of the player
	 * @return xEssentialsOfflinePlayer
	 */
	public static xEssentialsOfflinePlayer getOfflinePlayer(String player) {
		try {
			File dir = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "players");
			if(dir.isDirectory()) {
				File[] list = dir.listFiles();
				for(File f : list) {
					FileConfiguration con = YamlConfiguration.loadConfiguration(f);
					if(con.isSet("user")) {
						if(con.getString("user").equalsIgnoreCase(player)) {
							xEssentialsOfflinePlayer off = new xEssentialsOfflinePlayer(player);
							return off;
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author xize
	 * @param player - returns the file of the desired name
	 * @return File
	 * @throws NullPointerException when the file is null
	 */
	public static File getOfflinePlayerFile(String player) {
		try {
			File dir = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "players");
			if(dir.isDirectory()) {
				File[] list = dir.listFiles();
				for(File f : list) {
					FileConfiguration con = YamlConfiguration.loadConfiguration(f);
					if(con.isSet("user")) {
						if(con.getString("user").equalsIgnoreCase(player)) {
							return f;
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Map<String, xEssentialsPlayer> players = new ConcurrentHashMap<String, xEssentialsPlayer>();

	/**
	 * @author xize
	 * @param player name
	 * @return boolean
	 */
	public static boolean contains(String player) {
		if(players.containsKey(player.toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param clears the xEssentialsPlayers HashMap
	 * @return void
	 */
	public static void clear() {
		players.clear();
	}

	/**
	 * @author xize
	 * @param player name
	 * @return xEssentialsPlayer
	 * @throws NullPointerException
	 */
	public static xEssentialsPlayer get(String playerName) throws NullPointerException {
		return players.get(playerName.toLowerCase());
	}

	/**
	 * @author xize
	 * @param returns true whenever the xEssentials HashMap is empty!
	 * @return boolean
	 */
	public static boolean isEmpty() {
		return players.isEmpty();
	}

	/**
	 * 
	 * @author xize
	 * @param String, xEssentialsPlayer
	 * @return boolean
	 * 
	 */
	public static boolean set(String playerName, xEssentialsPlayer player) {
		if(!players.containsKey(playerName.toLowerCase())) {
			players.put(playerName.toLowerCase(), player);
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @author xize
	 * @param String
	 * @return boolean
	 * 
	 */
	public static boolean remove(String playerName) {
		if(players.containsKey(playerName.toLowerCase())) {
			players.remove(playerName.toLowerCase());
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param reloads all user input for onEnable
	 * @return void
	 */
	public static void reloadPlayerBase() {
		players.clear();
		for(Player p : Bukkit.getOnlinePlayers()) {
			xEssentialsPlayer xp = new xEssentialsPlayer(p, xEssentials.getOfflinePlayer(p.getName()).getUniqueId());
			players.put(p.getName().toLowerCase(), xp);
		}
	}

	/**
	 * @author xize
	 * @param gets all the xEssentialsPlayers in a array
	 * @return xEssentialsPlayer[]
	 */
	public static xEssentialsPlayer[] getPlayers() {
		xEssentialsPlayer[] users = players.values().toArray(new xEssentialsPlayer[players.size()]);
		return users;
	}
	
	/**
	 * @author xize
	 * @param get all the warps
	 * @return Warp[]
	 */
	public static Warp[] getWarps() {
		List<Warp> warps = new ArrayList<Warp>();
		for(xEssentialsOfflinePlayer off : getOfflinePlayers()) {
			if(off.hasOwnedWarps()) {
				warps.addAll(Arrays.asList(off.getWarps()));
			}
		}
		return warps.toArray(new Warp[warps.size()]);
	}

	/**
	 * @author xize
	 * @param check if the warp exist
	 * @return Boolean
	 */
	public static boolean doesWarpExist(String key) {
		for(Warp warp : getWarps()) {
			if(warp.getWarpName().equalsIgnoreCase(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param get the warp by using the name
	 * @return Warp
	 * @throws NullPointerException - when the warp does not exist!
	 */
	public static Warp getWarpByName(String name) {
		if(doesWarpExist(name)) {
			for(Warp warp : getWarps()) {
				if(warp.getWarpName().equalsIgnoreCase(name)) {
					return warp;
				}
			}
		}
		throw new NullPointerException("warp does not exist");
	}
	
	/**
	 * @author xize
	 * @param player - the players name, can also be a offline players name
	 * @return Double
	 */
	public static Double getMoney(String player) {
		if(xEssentials.isEssentialsPlayer(player)) {
			if(xEssentials.contains(player)) {
				xEssentialsPlayer xp = xEssentials.get(player);
				return xp.getTotalEssentialsMoney();
			} else {
				xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(player);
				return off.getTotalEssentialsMoney();
			}
		}
		throw new NullPointerException("this player has never played before!");
	}
	
	/**
	 * @author xize
	 * @param player - the player name, can be a offline player to
	 * @param money - money, which get removed from the bank!
	 */
	public static void withdrawMoney(String player, Double money) {
		if(xEssentials.isEssentialsPlayer(player)) {
			if(xEssentials.contains(player)) {
				xEssentialsPlayer xp = xEssentials.get(player);
				Double moneyz = xp.getTotalEssentialsMoney();
				if(money > moneyz) {
					throw new IndexOutOfBoundsException("cannot withdraw money below zero");
				}
				xp.clearMoney();
				xp.addEssentialsMoney((moneyz-money));
			} else {
				xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(player);
				Double moneyz = off.getTotalEssentialsMoney();
				if(money > moneyz) {
					throw new IndexOutOfBoundsException("cannot withdraw money below zero");
				}
				off.clearMoney();
				off.addEssentialsMoney((moneyz-money));
			}
		} else {
			throw new NullPointerException("this player has never played before!");
		}
	}
	
	/**
	 * @author xize
	 * @param player - the player name, can be a offline player to
	 * @param money - money, which get added to the bank
	 */
	public static void depositMoney(String player, Double money) {
		if(xEssentials.isEssentialsPlayer(player)) {
			if(xEssentials.contains(player)) {
				xEssentialsPlayer xp = xEssentials.get(player);
				Double moneyz = xp.getTotalEssentialsMoney();
				xp.clearMoney();
				xp.addEssentialsMoney(moneyz+money);
			} else {
				xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(player);
				Double moneyz = off.getTotalEssentialsMoney();
				off.clearMoney();
				off.addEssentialsMoney(moneyz+money);
			}
		} else {
			throw new NullPointerException("this player has never played before!");
		}
	}
	
	/**
	 * @author xize
	 * @param returns the database
	 * @return ProtectionDB
	 */
	public static ProtectionDB getProtectionDatabase() {
		return protectiondb;
	}
}
