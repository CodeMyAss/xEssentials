package tv.mineinthebox.essentials.instances;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PlayerTaskEnum;

public class xEssentialsOfflinePlayer {

	private final File f;
	private final FileConfiguration con;
	private String player;
	private AlternateAccount accounts;

	/**
	 * @author xize
	 * @param a constructor which pass our interface, if there is no user file this constructor will create a new one this constructor only allows type Player.
	 * @return xEssentialsOfflinePlayer
	 * @throws NullPointerException when there is no configuration file of this player
	 */
	public xEssentialsOfflinePlayer(String player) {
		this.f = xEssentials.getOfflinePlayerFile(player);
		this.player = player;
		if(!this.f.getName().equals(null)) {
			if(this.f.exists()){
				this.con = YamlConfiguration.loadConfiguration(this.f);	
			} else {
				throw new NullPointerException();
			}
		} else {
			throw new NullPointerException();
		}
	}
	
	public xEssentialsOfflinePlayer(OfflinePlayer offliner) {
		this.f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "players" + File.separator + offliner.getName() + ".yml");
		if(!this.f.exists()) {
			this.con = YamlConfiguration.loadConfiguration(this.f);
			this.con.set("isDefault", true);
			this.con.set("ip", "fake");
			this.con.set("user", offliner.getName());
			this.con.set("fly", false);
			this.con.set("torch", false);
			this.con.set("firefly", false);
			this.con.set("uuid", offliner.getUniqueId().toString());
			if(Configuration.getEconomyConfig().isEconomyEnabled()){
				this.con.set("money", Configuration.getEconomyConfig().getStartersMoney());
			}
			try {
				this.con.save(this.f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update();
		} else {
			this.con = YamlConfiguration.loadConfiguration(this.f);
		}
	}

	/**
	 * @author xize
	 * @param returns true whenever the speed is enabled
	 * @return boolean
	 */
	public boolean isSpeedEnabled() {
		update();
		if(con.contains("isSpeed")) {
			return con.getBoolean("isSpeed");
		}
		return false;
	}

	/**
	 * @author xize
	 * @param sets the speed of a player
	 * @return void
	 */
	public void setSpeed(int i) {
		con.set("isSpeed", true);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getPlayer().setWalkSpeed(i);
		update();
	}

	/**
	 * @author xize
	 * @param prepares a command task on players next join
	 * @return void
	 */
	public void PrepareLoginTask(String command, PlayerTaskEnum task) {
		con.set("task.command", command);
		con.set("task.type", task.name());
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param removes the walk speed
	 * @return void
	 */
	public void removeSpeed() {
		con.set("isSpeed", false);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
		getPlayer().setWalkSpeed(0.2f);
	}

	public boolean isGreyListed() {
		update();
		if(con.contains("isDefault")) {
			if(!con.getBoolean("isDefault")) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public void setGreyListed(Boolean bol) {
		con.set("isDefault", bol);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}


	/**
	 * @author xize
	 * @param checks whenever the player is boomed
	 * @return Boolean
	 */
	public boolean isBoom() {
		update();
		if(con.contains("isBoom")) {
			return con.getBoolean("isBoom");
		}
		return false;
	}

	/**
	 * @author xize
	 * @param set the boom status of this player
	 * @return void
	 */
	public void setBoom() {
		con.set("isBoom", true);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param remove the boom status of this player
	 * @return void
	 */
	public void removeBoom() {
		con.set("isBoom", null);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param returns true whenever the player is cursed to be a potato!
	 * @return boolean
	 */
	public boolean isPotato() {
		update();
		if(con.contains("isPotato")) {
			return con.getBoolean("isPotato");
		}
		return false;
	}

	/**
	 * @author xize
	 * @param remove potato curse of this player!
	 * @return void
	 */
	public void removePotato() {
		con.set("isPotato", false);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param set a potato curse of this player!
	 * @return void
	 */
	public void setPotato() {
		con.set("isPotato", true);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param gets the player if online
	 * @return
	 */
	public Player getPlayer() {
		Player p = Bukkit.getPlayer(UUID.fromString(getUniqueId()));
		if(p instanceof Player) {
			return p;
		}
		return null;
	}

	/**
	 * 
	 * @author xize
	 * @param gets the ip adress of this player
	 * @return String
	 * 
	 */
	public String getIp() {
		update();
		return con.getString("ip");
	}

	/**
	 * @author xize
	 * @param allows to unvanish the player when offline
	 * @return void
	 */
	public void unvanish() {
		update();
		con.set("isVanished", false);
		con.set("noPickUp", false);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the player has vanish effects
	 * @return Boolean
	 */
	public boolean hasVanishEffects() {
		update();
		if(con.getBoolean("vanishEffects")) {
			return con.getBoolean("vanishEffects");
		}
		return false;
	}
	
	/**
	 * @author xize
	 * @param sets the vanish effects of this player
	 * @param bol - the boolean
	 */
	public void setVanishEffects(Boolean bol) {
		con.set("vanishEffects", bol);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * 
	 * @author xize
	 * @param set the ip of this player
	 * @return boolean
	 * 
	 */
	public boolean setIp(String ip) {
		con.set("ip", ip);
		try {
			con.save(f);
			update();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 
	 * @author xize
	 * @param gets the username from the configuration of this player, this will also get updated whenever the name does not match with the uniqueID
	 * @return String 
	 *
	 */
	public String getUser() {
		update();
		return con.getString("user");
	}

	/**
	 * @author xize
	 * @param returns true when this player is muted
	 * @return boolean
	 */
	public boolean isMuted() {
		update();
		if(con.contains("muted.isMuted")) {
			return con.getBoolean("muted.isMuted");
		} else {
			return false;
		}
	}

	/**
	 * @author xize
	 * @param removes the mute
	 * @return void
	 */
	public void unmute() {
		con.set("muted", null);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param mutes the player for chatting
	 * @param time - where the milliseconds are the modified date.
	 * @return void
	 */
	public void mute(Long time) {
		con.set("muted.isMuted", true);
		con.set("muted.mutedTime", time);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param get the modified time in milliseconds
	 * @return Long
	 */
	public Long getMutedTime() {
		return con.getLong("muted.mutedTime");
	}

	/**
	 * @author xize
	 * @param check whenever this player is perm banned
	 * @return boolean
	 */
	public boolean isPermBanned() {
		return Bukkit.getServer().getBanList(Type.NAME).isBanned(player);
	}

	/**
	 * @author xize
	 * @param sets the player permbanned
	 * @return void
	 */
	public void setPermBanned(String reason, String who) {
		Bukkit.getServer().getBanList(Type.NAME).addBan(player, reason, null, who).save();
		con.set("banned.isBanned", true);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param gets the ban message of this player
	 * @return String
	 */
	public String getBanMessage() {
		return Bukkit.getServer().getBanList(Type.NAME).getBanEntry(player).getReason();
	}

	/**
	 * 
	 * @author xize
	 * @param returns true if the player is temp banned
	 * @return boolean
	 * 
	 */
	public boolean isTempBanned() {
		if(Bukkit.getBanList(Type.NAME).isBanned(player)) {
			Date date = Bukkit.getServer().getBanList(Type.NAME).getBanEntry(player).getExpiration();
			if(date != null) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param bans a player tempory (Long time, String reason, String who)
	 * @return boolean
	 */
	public void setTempbanned(Long time, String reason, String who) {
		Date date = new Date(time);
		Bukkit.getServer().getBanList(Type.NAME).addBan(player, reason, date, who).save();
		con.set("tempbanned.isBanned", true);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param gets the tempory ban message
	 * @return String
	 */
	public String getTempBanMessage() {
		return Bukkit.getServer().getBanList(Type.NAME).getBanEntry(player).getReason();
	}

	/**
	 * @author xize
	 * @param unbans the player for both Tempbanned or PermBanned
	 * @return void
	 */
	public void unban() {
		Bukkit.getServer().getBanList(Type.NAME).pardon(player);
	}

	/**
	 * @author xize
	 * @param returns true whenever the player is banned before
	 * @return boolean
	 */
	public boolean isBannedBefore() {
		update();
		if(con.contains("banned.isBanned")) {
			if(!con.getBoolean("banned.isBanned")) {
				return true;
			}
		} else if(con.contains("tempbanned.isBanned")) {
			if(!con.getBoolean("tempbanned.isBanned")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param gets the time remaining of the ban
	 * @return Long
	 */
	public Long getTempbanRemaining() {
		return Bukkit.getServer().getBanList(Type.NAME).getBanEntry(player).getExpiration().getTime();
	}

	/**
	 * @author xize
	 * @param get the Unique ID of this player
	 * @return Long
	 */
	public String getUniqueId() {
		update();
		if(con.contains("uuid")) {
			return con.getString("uuid");
		}  else {
			String uid = f.getName().replace(".yml", "");
			return (uid.substring(0, 8) + "-" + uid.substring(8, 12) + "-" + uid.substring(12, 16) + "-" + uid.substring(16, 20) + "-" +uid.substring(20, 32));
		}
	}

	/**
	 * 
	 * @author xize
	 * @param checks whenever fly is enabled for this player
	 * @return boolean
	 * 
	 */
	public boolean isFlying() {
		update();
		return con.getBoolean("fly");
	}

	/**
	 * 
	 * @author xize
	 * @param checks whenever torch is enabled for this player
	 * @return boolean
	 * 
	 */
	public boolean isTorch() {
		update();
		return con.getBoolean("torch");
	}

	/**
	 * 
	 * @author xize
	 * @param checks whenever the player has set his home
	 * @return boolean
	 * 
	 */
	public boolean hasHome() {
		update();
		if(con.contains("homes")) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param returns true if the home name is valid within the String
	 * @return boolean
	 */
	public boolean isValidHome(String home) {
		update();
		if(con.contains("homes."+home)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @author xize
	 * @param gets all homes of this player!
	 * @return List<Home>
	 * 
	 */
	public List<Home> getAllHomes() {
		update();
		List<Home> homes = new ArrayList<Home>();
		if(hasHome()) {
			for(String home : con.getConfigurationSection("homes").getKeys(false)) {
				Home playerhome = new Home(con, home);
				homes.add(playerhome);
			}
		}
		return homes;
	}

	/**
	 * @author xize
	 * @param homeName
	 * @return Home
	 * @throws NullPointerException
	 * 
	 */
	public Home getHome(String homeName) throws NullPointerException {
		update();
		Home home = new Home(con, homeName);
		return home;
	}

	/**
	 * @author xize
	 * @param gets the fixed size of all the homes from this player
	 * @return int
	 */
	public int getAmountOfHomes() {
		//returns a fixed version for permissions;)
		return (this.getAllHomes().size());
	}

	/**
	 * @author xize
	 * @param remove the home
	 * @param home
	 * @return void
	 */
	public void removeHome(String home) {
		update();
		con.set("homes."+home.toLowerCase(), null);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param is true when vanished otherwise false
	 * @return boolean
	 */
	public boolean isVanished() {
		update();
		if(con.contains("isVanished")) {
			return con.getBoolean("isVanished");
		}
		return false;
	}

	/**
	 * @author xize
	 * @param gets the last location of this player
	 * @return Location
	 */
	public Location getLocation() {
		update();
		if(con.contains("lastLocation")) {
			return new Location(Bukkit.getWorld(con.getString("lastLocation.world")), con.getDouble("lastLocation.x"), con.getDouble("lastLocation.y"), con.getDouble("lastLocation.z"), con.getInt("lastLocation.yaw"), con.getInt("lastLocation.pitch"));
		}
		return null;
	}

	/**
	 * @author xize
	 * @param checks whenever the player has a offline inventory
	 * @return boolean
	 */
	public boolean hasOfflineInventory() {
		update();
		if(con.contains("offlineInventory.contents")) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param returns the offline inventory
	 * @return Inventory
	 */
	@SuppressWarnings("unchecked")
	public Inventory getOfflineInventory(Player viewer) {
		update();
		Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER);
		if(hasOfflineInventory()) {
			ItemStack[] items = ((List<ItemStack>)con.get("offlineInventory.contents")).toArray(new ItemStack[0]);	
			inv.setContents(items);
		}
		return inv;
	}

	/**
	 * @author xize
	 * @param checks whenever this player has open modreqs
	 * @return boolean
	 */
	public boolean hasModreqsOpen() {
		update();
		if(con.contains("modreqs")) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param checks whenever a id is a valid ID
	 * @return boolean
	 */
	public boolean isValidModreqId(int id) {
		update();
		if(con.contains("modreqs."+"modreq"+id)) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param gets the Modreq object containing all data
	 * @return Modreq
	 */
	public Modreq getModreq(int id) {
		update();
		Modreq mod = new Modreq(con, id);
		return mod;
	}

	/**
	 * @author xize
	 * @param removes a modreq
	 * @return void
	 * @throws NullPointerException when the node doesn't exist
	 */
	public void removeModreq(int id) {
		update();
		if(isValidModreqId(id)) {
			con.set("modreqs."+"modreq"+id, null);
			if(con.getConfigurationSection("modreqs").getKeys(true).isEmpty()) {
				con.set("modreqs", null);
			}
			try {
				con.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update();
		} else {
			throw new NullPointerException("you cannot remove a configuration node for a modreq wich doesn't exists!");
		}
	}

	/**
	 * @author xize
	 * @param returns a array of modreqs for this player
	 * @return Modreq[]
	 */
	public Modreq[] getModreqs() {
		update();
		List<Modreq> items = new ArrayList<Modreq>();
		for(int i = 0; con.contains("modreqs."+"modreq"+i); i++) {
			if(isValidModreqId(i)) {
				Modreq mod = new Modreq(con, i);
				items.add(mod);	
			} else {
				break;
			}
		}
		Modreq[] modreqs = items.toArray(new Modreq[items.size()]);
		return modreqs;
	}

	/**
	 * @author xize
	 * @param returns true whenever the player has a modreq done message
	 * @return boolean
	 */
	public boolean hasModreqDoneMessage() {
		update();
		if(con.contains("modreq.done.message")) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param returns the modreq done message
	 * @return String
	 */
	public String getModreqDoneMessage() {
		return con.getString("modreq.done.message");
	}

	/**
	 * @author xize
	 * @param remove the modreq done message
	 * @return void
	 */
	public void removeGetModregDoneMessage() {
		con.set("modreq", null);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param set the last modreq done message for this player when used /done
	 * @return void
	 */
	public void setModreqDoneMessage(String message) {
		con.set("modreq.done.message", message);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param clear the inventory on players relogs!
	 * @return void
	 */
	public void ClearInventoryOnRelog() {
		update();
		con.set("ClearInventory", true);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param get the alternate accounts of this player!
	 * @return AlternateAccount
	 */
	public AlternateAccount getAlternateAccounts() {
		if(accounts instanceof AlternateAccount) {
			return accounts;
		} else {
			this.accounts = new AlternateAccount(this);
			return accounts;
		}
	}

	/**
	 * @author xize
	 * @param check whenever the player has alternate accounts!
	 * @return boolean
	 */
	public boolean hasAlternateAccounts() {
		AlternateAccount alts = getAlternateAccounts();
		if(alts.getAltNames().length > 0) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param set the silence state of the player
	 * @param Boolean
	 * @return void
	 */
	public void setSilenced(Boolean bol) {
		con.set("silenced", bol);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param returns true whenever the player has silenced the chat
	 * @return Boolean
	 */
	public boolean isSilenced() {
		update();
		if(con.contains("silenced")) {
			return con.getBoolean("silenced");
		}
		return false;
	}

	/**
	 * @author xize
	 * @param add a player to this players ignore list, this means that this player ignores the chat by the called name.
	 * @return void
	 */
	public void addIgnoredPlayer(String s) {
		if(hasIgnoredPlayers()) {
			List<String> list = new ArrayList<String>(getIgnoredPlayers());
			list.add(s);
			con.set("IgnoredPlayers", list.toArray());
			try {
				con.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update();
		} else {
			String[] a = {s};
			con.set("IgnoredPlayers", Arrays.asList(a).toArray());
			try {
				con.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update();	
		}
	}

	/**
	 * @author xize
	 * @param get a list of all player names which get ignored by this player
	 * @return List<String>()
	 */
	public List<String> getIgnoredPlayers() {
		return con.getStringList("IgnoredPlayers");
	}

	/**
	 * @author xize
	 * @param returns true whenever the player ignores a player
	 * @return Boolean
	 */
	public boolean hasIgnoredPlayers() {
		update();
		if(con.contains("IgnoredPlayers")) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param remove a player from the ignore list
	 * @return void
	 */
	public void removeIgnoredPlayer(String s) {
		List<String> list = new ArrayList<String>(getIgnoredPlayers());
		if(list.size() == 1) {
			con.set("IgnoredPlayers", null);
			try {
				con.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update();
		} else {
			list.remove(s);
			con.set("IgnoredPlayers", list.toArray());
			try {
				con.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update();
		}
	}

	/**
	 * @author xize
	 * @param returns true whenever the player is freezed
	 * @return Boolean
	 */
	public boolean isFreezed() {
		if(con.contains("isFreezed")) {
			return con.getBoolean("isFreezed");
		}
		return false;
	}

	/**
	 * @author xize
	 * @param freezes the player or unfreeze the player
	 * @param void
	 */
	public void setFreezed(Boolean bol) {
		con.set("isFreezed", bol);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	public boolean isTrollMode() {
		if(con.contains("isTrollmode")) {
			return con.getBoolean("isTrollmode");
		}
		return false;
	}

	public void setTrollMode(Boolean bol) {
		con.set("isTrollmode", bol);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param returns true if the player has command restrictions
	 * @return Boolean
	 */
	public boolean hasCommandRestrictions() {
		if(con.contains("command-restrictions")) {
			if(!con.getStringList("command-restrictions").isEmpty()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param command - the command+args which needs to be disabled
	 * @param reason - the reason why the command is blocked for the player
	 * @param taskCommand - an aditional task: command+args, if null we ignore this.
	 * @return void
	 */
	public void setCommandRestriction(String command, String reason, String taskCommand) {
		if(taskCommand == null) {
			List<String> list = new ArrayList<String>(con.getStringList("command-restrictions"));
			list.add(command+","+reason);
			con.set("command-restrictions", list);
			try {
				con.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update();	
		} else {
			List<String> list = new ArrayList<String>(con.getStringList("command-restrictions"));
			list.add(command+","+reason+","+taskCommand);
			con.set("command-restrictions", list);
			try {
				con.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update();
		}
	}

	/**
	 * @author xize
	 * @param returns a list of all command restrictions for this player
	 * @return CommandRestriction[]
	 */
	public RestrictedCommand[] getCommandRestrictions() {
		List<String> commands = new ArrayList<String>(con.getStringList("command-restrictions"));
		List<RestrictedCommand> restricts = new ArrayList<RestrictedCommand>();
		for(String args : commands) {
			RestrictedCommand cmd = new RestrictedCommand(args);
			restricts.add(cmd);
		}
		return restricts.toArray(new RestrictedCommand[restricts.size()]);
	}

	/**
	 * @author xize
	 * @param check if the player has a restriction inside the list, make sure to use hasCommandRestrictions() first.
	 * @return Boolean
	 */
	public boolean hasContainedRestriction(String command) {
		for(RestrictedCommand restriction : getCommandRestrictions()) {
			if(restriction.getCommand().equalsIgnoreCase(command)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param remove the command restriction
	 * @return void
	 */
	public void removeCommandRestriction(RestrictedCommand cmd) {
		List<String> list = new ArrayList<String>(con.getStringList("command-restrictions"));
		list.remove(cmd.getSerializedKey());
		con.set("command-restrictions", list);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param set the kit cooldown
	 * @return void
	 */
	public void setKitCooldown(Long cooldown) {
		con.set("kitCooldown", cooldown);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param remove the kit cooldown
	 * @return void
	 */
	public void removeKitCoolDown() {
		con.set("kitCooldown", null);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param return the saved cooldown of this player
	 * @return Long
	 */
	public Long getKitCooldown() {
		return con.getLong("kitCooldown");
	}

	/**
	 * @author xize
	 * @param returns true whenever the player has the cooldown else false
	 * @return Boolean
	 */
	public Boolean hasKitCooldown() {
		if(con.contains("kitCooldown")) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param returns true if the player owns warps
	 * @return Boolean
	 */
	public boolean hasOwnedWarps() {
		update();
		if(con.contains("warp")) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param returns all the warps of this player!
	 * @return Warp[]
	 */
	public Warp[] getWarps() {
		update();
		List<Warp> warps = new ArrayList<Warp>();
		for(String key : con.getConfigurationSection("warp").getKeys(false)) {
			Warp warp = new Warp(con, getUser(), key);
			warps.add(warp);
		}
		return warps.toArray(new Warp[warps.size()]);
	}

	/**
	 * @author xize
	 * @param warpname - the warpname
	 * @param loc - the Location of the player
	 * @param sets the warp.
	 */
	public void setWarp(String warpname, Location loc) {
		con.set("warp."+warpname.toLowerCase()+".x", loc.getX());
		con.set("warp."+warpname.toLowerCase()+".y", loc.getY());
		con.set("warp."+warpname.toLowerCase()+".z", loc.getZ());
		con.set("warp."+warpname.toLowerCase()+".world", loc.getWorld().getName());
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param warpname - the disired warp name to be removed
	 */
	public void removeWarp(String warpname) {
		con.set("warp."+warpname.toLowerCase(), null);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param key - possible warp name
	 * @param returns true whenever the possible name of the warp exist
	 * @return Boolean
	 */
	public Boolean isValidWarp(String key) {
		update();
		if(con.contains("warp."+key.toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param get a warp through a key
	 * @return Warp
	 * @throws NullPointerException - when there is no warp and you didn't used isValidWarp()
	 */
	public Warp getWarp(String key) {
		if(isValidWarp(key)) {
			return new Warp(con, getUser(), key);
		}
		throw new NullPointerException("this player has no warp called " + key);
	}

	/**
	 * @author xize
	 * @param returns the total amount of Essentials money of this player
	 * @return Double
	 */
	public Double getTotalEssentialsMoney() {
		update();   
		return con.getDouble("money");
	}

	/**
	 * @author xize
	 * @param returns true if the player has money if its 0.0 or the config entry doesn't exist it is false
	 * @return Boolean
	 */
	public Boolean hasEssentialsMoney() {
		update();
		if(con.contains("money")) {
			if(con.getDouble("money") > 0.0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param this withdraws the players money
	 * @return Boolean
	 */
	public Boolean payEssentialsMoney(Double price) {
		update();
		Double money = (getTotalEssentialsMoney()-price);
		if(money > 0.0 || money == 0.0) {
			con.set("money", money);
			try {
				con.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update();
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param price - the price the player is gonna be to pay
	 * @param toPayTo - the retriever
	 * @return Boolean - if the player has no money it will be false.
	 */
	public Boolean payEssentialsMoney(Double price, xEssentialsOfflinePlayer toPayTo) {
		update();
		Double money = (getTotalEssentialsMoney()-price);
		if(money > 0.0 || money == 0.0) {
			con.set("money", money);
			try {
				con.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update();
			toPayTo.addEssentialsMoney(price);
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param this will add money to the players bank
	 */
	public void addEssentialsMoney(Double price) {
		update();
		con.set("money", getTotalEssentialsMoney()+price);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param price - the price the player is gonna be to pay
	 * @param toPayTo - the retriever
	 * @return Boolean - if the player has no money it will be false.
	 */
	public Boolean payEssentialsMoney(Double price, xEssentialsPlayer toPayTo) {
		update();
		Double money = (getTotalEssentialsMoney()-price);
		if(money > 0.0) {
			con.set("money", money);
			try {
				con.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update();
			toPayTo.addEssentialsMoney(price);
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param price - the price
	 * @return Boolean
	 */
	public boolean hasPlayerEnoughMoneyFromPrice(Double price) {
		update();
		if(getTotalEssentialsMoney() > price || getTotalEssentialsMoney() == price) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param clear the money of the player
	 * @return void
	 */
	public void clearMoney() {
		update();
		con.set("money", null);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param get all the shop signs from the player
	 * @return List<String>()
	 */
	public List<String> getSignShops() {
		return con.getStringList("signshops");
	}

	/**
	 * @author xize
	 * @param adds a shop sign to this player
	 * @param loc - the Location
	 */
	public void addShopSign(Location loc) {
		List<String> signs = new ArrayList<String>(getSignShops());
		String serialize = loc.getWorld().getName()+":"+loc.getX()+":"+loc.getY()+":"+loc.getZ();
		signs.add(serialize);
		con.set("signshops", null);
		con.set("signshops", signs.toArray());
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}

	/**
	 * @author xize
	 * @param loc - the Location
	 * @param checks if the sign is a contained sign in the configuration of this player
	 * @return Boolean
	 */
	public boolean containsShopSign(Location loc) {
		update();
		String serialize = loc.getWorld().getName()+":"+loc.getX()+":"+loc.getY()+":"+loc.getZ();
		List<String> list = new ArrayList<String>(getSignShops());
		return list.contains(serialize);
	}

	/**
	 * @author xize
	 * @param removes the sign
	 * @param loc - the Location
	 */
	public void removeShopSign(Location loc) {
		update();
		String serialize = loc.getWorld().getName()+":"+loc.getX()+":"+loc.getY()+":"+loc.getZ();
		List<String> locations = new ArrayList<String>(getSignShops());
		if(locations.contains(serialize)) {
			locations.remove(serialize);
			con.set("signshops", null);
			con.set("signshops", locations.toArray());
			try {
				con.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update(); 
		} else {
			throw new NullPointerException("sign does not exist on this player!");
		}
	}
	
	/**
	 * @author xize
	 * @param returns atleast 8 results of this players name history
	 * @return List<String>()
	 */
	public List<String> getNameHistory() {
		return con.getStringList("name-history");
	}
	
	/**
	 * @author xize
	 * @param returns true if the player has name history
	 * @return Boolean
	 */
	public boolean hasNameHistory() {
		return !con.getStringList("name-history").isEmpty();
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the player has a saved inventory
	 * @return Boolean
	 */
	public boolean hasSavedInventory() {
		if(con.contains("orginalinv")) {
			return true;
		}
		return false;
	}
	
	/**
	 * @author xize
	 * @param bol - when the boolean is true enable procs, else disable procs
	 */
	public void setProc(boolean bol) {
		con.set("proc.enable", bol);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update();
	}
	
	/**
	 * @author xize
	 * @param return true when the player has procs
	 * @return Boolean
	 */
	public boolean hasProc() {
		if(con.contains("proc.enable")) {
			return con.getBoolean("proc.enable");
		}
		return false;
	}


	/**
	 * @author xize
	 * @param gets updated at every call so we know that the configuration stored in the memory is still recent with the flat saved file!
	 * @return void
	 */
	private void update() {
		try {
			con.load(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((con == null) ? 0 : con.hashCode());
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		xEssentialsOfflinePlayer other = (xEssentialsOfflinePlayer) obj;
		if (con == null) {
			if (other.con != null)
				return false;
		} else if (!con.equals(other.con))
			return false;
		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}

}
