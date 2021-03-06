package tv.mineinthebox.essentials;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import tv.mineinthebox.essentials.enums.MinigameType;
import tv.mineinthebox.essentials.events.CustomEventHandler;
import tv.mineinthebox.essentials.events.Handler;
import tv.mineinthebox.essentials.events.customEvents.CallEssentialsBroadcastEvent;
import tv.mineinthebox.essentials.events.customEvents.CallRssFeedEvent;
import tv.mineinthebox.essentials.events.players.RealisticGlass;
import tv.mineinthebox.essentials.greylist.GreyListServer;
import tv.mineinthebox.essentials.hook.Hooks;
import tv.mineinthebox.essentials.hook.VaultHook;
import tv.mineinthebox.essentials.instances.Bank;
import tv.mineinthebox.essentials.instances.SpleefArena;
import tv.mineinthebox.essentials.instances.Warp;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;
import tv.mineinthebox.essentials.interfaces.Minigame;
import tv.mineinthebox.essentials.utils.ProtectionDB;
import tv.mineinthebox.essentials.utils.TPS;


public class xEssentials extends JavaPlugin {

	private static xEssentials pl;
	private final Configuration conf = new Configuration();
	private final Handler Handler = new Handler();
	private final CustomEventHandler customhandler = new CustomEventHandler();
	private final CommandList cmdlist = new CommandList();
	private final RealisticGlass glass = new RealisticGlass();
	private static GreyListServer server = null;
	private static ProtectionDB protectiondb = null;
	private static VaultHook vault = null;

	public void onEnable() {
		pl = this;
		log("has been enabled", LogType.INFO);
		conf.createConfigs();
		conf.loadMiniGames();
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

		if(Hooks.isVaultEnabled()) {
			vault = new VaultHook();
			if(Configuration.getEconomyConfig().isEconomyEnabled()) {
				vault.hookEconomyInVault();
			}
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
			xEssentialsPlayer xp = new xEssentialsPlayer(p, xEssentials.getOfflinePlayer(p.getName()).getUniqueId().replaceAll("-", ""));
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

	/**
	 * @author xize
	 * @param sets the database
	 */
	public static void setProtectionDatabase(ProtectionDB db) {
		protectiondb = db;
	}


	/**
	 * @author xize
	 * @param adds a minigame into our global system.
	 * @param type - the MinigameType enum
	 * @param a - the arena.
	 * @throws NullPointerException when the object is something else rather than the Arena Object
	 */
	public boolean addGame(Minigame a, MinigameType typ) throws Exception {
		EnumMap<MinigameType, HashMap<String, Minigame>> hash = getMinigameMap();
		for(MinigameType type : MinigameType.values()) {
			if(a.getType() == type) {
				HashMap<String, Minigame> arena = new HashMap<String, Minigame>();
				hash.put(type, arena);

				return true;
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param removes a minigame!
	 * @param type - the MinigameType enum
	 * @param arena - the arena name
	 * @throws NullPointerException - when the arena does not exist!
	 */
	public void removeGame(String arena) throws Exception {
		if(isArena(arena)) {
			Minigame game = getArena(arena);
			game.remove();
		} else {
			throw new NullPointerException("cannot remove a game which doesn't exist!");
		}
	}

	/**
	 * @author xize
	 * @param gets the Arena specified by the player
	 * @param p - the player
	 * @return Minigame - this could be HungerGamesArena or SpleefArena
	 */
	public Minigame getArenaFromPlayer(Player p) {
		EnumMap<MinigameType, HashMap<String, Minigame>> hash = getMinigameMap();
		for(MinigameType type : MinigameType.values()) {
			if(hash.containsKey(type)) {
				for(Minigame game : hash.get(type).values()) {
					if(game.isPlayerJoined(p.getName())) {
						return game;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @author xize
	 * @param returns true if the player does exist in one of the arenas!
	 * @param p - the Player
	 * @return Boolean
	 */
	public boolean isPlayerInArea(Player p) {
		return (getArenaFromPlayer(p) instanceof Minigame ? true : false);
	}

	/**
	 * @author xize
	 * @param s - the possible arena name
	 * @return Minigame - the super class of arena types.
	 * @throws NullPointerException
	 */
	public Minigame getArena(String s) {
		EnumMap<MinigameType, HashMap<String, Minigame>> hash = getMinigameMap();
		for(MinigameType type : MinigameType.values()) {
			if(hash.containsKey(type)) {
				if(hash.get(type).containsKey(s.toLowerCase())) {
					return (Minigame) hash.get(type).get(s.toLowerCase());
				}
			}
		}
		throw new NullPointerException("arena name does not exist!, please use isArena() first!");
	}

	/**
	 * @author xize
	 * @param s - the arena name
	 * @param type - the minigame type
	 * @return Boolean
	 */
	public boolean isArena(String s) {
		EnumMap<MinigameType, HashMap<String, Minigame>> hash = getMinigameMap();
		for(MinigameType type : MinigameType.values()) {
			if(hash.containsKey(type)) {
				if(hash.get(type).containsKey(s.toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param returns all the spleef arenas
	 * @return SpleefArena[]
	 */
	public SpleefArena[] getAllSpleefArenas() {
		EnumMap<MinigameType, HashMap<String, Minigame>> hash = getMinigameMap();
		List<SpleefArena> arenas = new ArrayList<SpleefArena>();
		if(hash.containsKey(MinigameType.SPLEEF)) {
			Iterator<Entry<String, Minigame>> it = hash.get(MinigameType.SPLEEF).entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, Minigame> entry = it.next();
				if(entry.getValue() instanceof SpleefArena) {
					arenas.add((SpleefArena)entry.getValue());
				}
			}
		}
		return arenas.toArray(new SpleefArena[arenas.size()]);
	}

	/**
	 * @author xize
	 * @param gets the Enum map holding all loaded minigames, in a abstract way.
	 * @return EnumMap<MinigameType, HashMap<String, Object>>()
	 */
	private EnumMap<MinigameType, HashMap<String, Minigame>> getMinigameMap() {
		return Configuration.getMinigameMap();
	}

	/**
	 * @author xize
	 * @param returns the GreyListServer
	 * @return GreyListServer
	 */
	public static GreyListServer getGreyListServer() {
		return server;
	}

	/**
	 * @author xize
	 * @param returns the vault hook
	 * @return VaultHook
	 * @throws NullPointerException - if Vault is not installed.
	 */
	public static VaultHook getVault() {
		if(Hooks.isVaultEnabled()) {
			return vault;
		}
		throw new NullPointerException("Vault is not installed!, please update vault if possible!");
	}
	
	/**
	 * @author xize
	 * @param bank - the bank account
	 * @param p - the player
	 */
	public static void createBank(String bank, String playername) {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "banks" + File.separator + bank.toLowerCase() + ".yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("bank.owner", playername);
				con.set("bank.name", bank);
				con.set("bank.amount", 0.0);
				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the bank account is found!
	 * @param name - the banks name
	 * @return Boolean
	 */
	public static boolean isBank(String name) {
		File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "banks" + File.separator + name.toLowerCase() + ".yml");
		return f.exists();
	}
	
	/**
	 * @author xize
	 * @param returns the Bank object.
	 * @param name - the banks account
	 * @return Bank
	 * @throws NullPointerException - when the name doesn't exist
	 */
	public static Bank getBank(String name) {
		if(isBank(name)) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "banks" + File.separator + name.toLowerCase()+".yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			Bank bank = new Bank(f, con);
			return bank;
		}
		throw new NullPointerException("bank does not exist with that name!");
	}
	
	/**
	 * @author xize
	 * @param bank - the bank name
	 */
	public static void deleteBank(String bank) {
		File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "banks" + File.separator + bank.toLowerCase()+".yml");
		if(f.exists()) {
			f.delete();
		}
	}
}
