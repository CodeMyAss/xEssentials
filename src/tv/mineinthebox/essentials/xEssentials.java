package tv.mineinthebox.essentials;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;
import tv.mineinthebox.essentials.utils.TPS;


public class xEssentials extends JavaPlugin {

	private static xEssentials pl;
	private final Configuration conf = new Configuration();
	private final Handler Handler = new Handler();
	private final CustomEventHandler customhandler = new CustomEventHandler();
	private final CommandList cmdlist = new CommandList();
	private final RealisticGlass glass = new RealisticGlass();
	public static GreyListServer server = null;

	public void onEnable() {
		pl = this;
		log("has been enabled", LogType.INFO);
		conf.createConfigs();
		Handler.start();
		customhandler.startCustomEvents();
		if(Bukkit.getOnlinePlayers().length > 0) {
			Configuration.reloadConfiguration();
		}
		for(String cmd : cmdlist.getAllCommands) {
			getCommand(cmd).setExecutor(new command());	
		}
		if(Configuration.getPlayerConfig().isRealisticGlassEnabled()) {
			glass.loadGlassBlocks();	
		}
		TPS.startTps();
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
		if(Configuration.getBroadcastConfig().isBroadcastEnabled()) {
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

	/**
	 * @author xize
	 * @param get all the offline players in a array
	 * @return xEssentialsOfflinePlayer[]
	 */
	public static xEssentialsOfflinePlayer[] getOfflinePlayers() {
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
			return xplayers;
		} catch(Exception e) {
			e.printStackTrace();
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

	private static HashMap<String, xEssentialsPlayer> players = new HashMap<String, xEssentialsPlayer>();

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
			xEssentialsPlayer xp = new xEssentialsPlayer(p);
			players.put(p.getName().toLowerCase(), xp);
		}
	}

	/**
	 * @author xize
	 * @param get all the user names in the current hashmap!
	 * @return Iterator<String>
	 */
	public static Iterator<String> getKeys() {
		Iterator<String> it = players.keySet().iterator();
		return it;
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
}
