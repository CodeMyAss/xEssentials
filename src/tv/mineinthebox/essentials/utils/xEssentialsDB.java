package tv.mineinthebox.essentials.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.LogType;

public class xEssentialsDB implements Serializable{
	private static final long serialVersionUID = 4169103058604767745L;
	
	private String player;
	private String uuid;
	private String ipadres;

	public xEssentialsDB(String player) throws Exception {
		this.player = player;
		this.uuid = getLastKnownUUID(player);
		if(this.uuid == null || this.uuid.equalsIgnoreCase("null")) {
			throw new NullPointerException("player does not exist!");
		}
		if(isNewDatabase()) {
			xEssentials.getPlugin().log("player sqlite backend not active, activating right now.", LogType.INFO);
			createTables();
		}
	}

	public xEssentialsDB(Player player, String uuid) {
		this.player = player.getName();
		this.uuid = uuid;
		this.ipadres = player.getAddress().getAddress().getHostAddress();
		if(isNewDatabase()) {
			xEssentials.getPlugin().log("player sqlite backend not active, activating right now.", LogType.INFO);
			createTables();
			if(!doesPlayerDataExist()) {
				player.sendMessage("boolean works, new player");
				registerPlayer();
			}
		} else {
			if(!doesPlayerDataExist()) {
				player.sendMessage("boolean works, new player");
				registerPlayer();
			}
		}
	}

	private final String getLastKnownUUID(String player) {
		try {
			Connection con = getConnection();
			Statement state = con.createStatement();
			ResultSet set = state.executeQuery("SELECT uuid FROM players WHERE user='" + player + "'");
			String id = set.getString("uuid");
			state.close();
			set.close();
			con.close();
			return id;
		} catch(Exception e) {}
		return null;
	}

	public void updatePlayer(String name, String uid) {
		try {
			Connection con = getConnection();
			Statement state = con.createStatement();
			state.executeUpdate("UPDATE players SET user='" + name + "' WHERE uuid='" + uid + "'");
			state.close();
			con.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void registerPlayer() {
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("isDefault", true);
			hash.put("ip", ipadres);
			hash.put("user", player);
			hash.put("firefly", false);
			hash.put("fly", false);
			hash.put("torch", false);
			hash.put("uuid", uuid);
			if(Configuration.getEconomyConfig().isEconomyEnabled()) {
				hash.put("money", Configuration.getEconomyConfig().getStartersMoney());
			}
			saveMap(hash);
	}

	/**
	 * @author xize
	 * @param returns true whenever the database does not exists, else false
	 * @return Boolean
	 */
	public boolean isNewDatabase() {
		return !new File(xEssentials.getPlugin().getDataFolder() + File.separator + "databases" + File.separator + "players.db").exists();
	}

	/**
	 * @author xize
	 * @param creates the new tables into the database
	 */
	public void createTables() {
		String table = "CREATE TABLE IF NOT EXISTS `players` ("+ 
				"`id` INT," +
				"`user` TEXT NOT NULL, " +
				"`uuid` TEXT UNIQUE NOT NULL, " +
				"`map` LONGBLOB NOT NULL, " +
				"PRIMARY KEY (`id`) " +
				")";
		try {
			Connection con = getConnection();
			Statement state = con.createStatement();
			state.executeUpdate(table);
			state.close();
			con.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author xize
	 * @param returns true whenever the map data has been found, else false
	 * @return Boolean
	 */
	public boolean doesPlayerDataExist() {
		try {
			Connection con = getConnection();
			Statement state = con.createStatement();
			ResultSet set = state.executeQuery("SELECT map FROM players WHERE uuid='" + uuid + "'");
			if(set.isBeforeFirst()) {
				state.close();
				set.close();
				con.close();
				return true;
			} else {
				state.close();
				set.close();
				con.close();
				return false;
			} 
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @author xize
	 * @param saves the player data to the database, if the table does not exist creating a new one.
	 * @param hash - the HashMap with default data or modified data which could be overwritten.
	 */
	public void saveMap(HashMap<String, Object> hash) {
		try {
			Connection con = getConnection();
			SerializeObjectToBlob data = new SerializeObjectToBlob(hash);
			if(!doesPlayerDataExist()) {
				String query = "INSERT INTO players(user, uuid, map) VALUES(?, ?, ?)";
				PreparedStatement state = con.prepareStatement(query);
				state.setString(1, player);
				state.setString(2, uuid);
				state.setBytes(3, data.getBlob());
				state.executeUpdate();
				state.close();
				con.close();
				return;
			} else {
				PreparedStatement state = con.prepareStatement("UPDATE players SET map=? WHERE uuid='" + uuid + "'");
				state.setBytes(1, data.getBlob());
				state.executeUpdate();
				state.close();
				con.close();
				return;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author xize
	 * @param returns the HashMap, from the player, else a new generated HashMap will be generated and also be committed to the database.
	 * @return HashMap<String, Object>
	 * @throws NullPointerException, when the query is wrong and the player does not exist.
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getMap() {
		if(doesPlayerDataExist()) {
			try {
				Connection con = getConnection();
				Statement state = con.createStatement();
				ResultSet set = state.executeQuery("SELECT * FROM players WHERE user='" + player + "'");
				DeSerializeObjectFromBlob data = new DeSerializeObjectFromBlob(set.getBytes("map"));
				HashMap<String, Object> hash = (HashMap<String, Object>) data.getObject();
				//HashMap<String, Object> hash2 = new HashMap<String, Object>(hash);
				state.close();
				set.close();
				con.close();
				return hash;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		throw new NullPointerException("unknown data");
	}

	/**
	 * @author xize
	 * @param returns fresh sqlite Connection 
	 * @return Connection
	 */
	private Connection getConnection() {
		File dir = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "databases");
		if(!dir.isDirectory()) {
			dir.mkdir();
		}
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlite:plugins/xEssentials/databases/players.db");
			con.setAutoCommit(true);
			return con;
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite in craftbukkit, this is probably because you are running a outdated build!", LogType.SEVERE);
		}
		return null;
	}
}

class SerializeObjectToBlob implements Callable<byte[]> {
	
	private final Object obj;

	public SerializeObjectToBlob(Object obj) {
		this.obj = obj;
	}

	/**
	 * @author xize
	 * @param returns the byte[] array as a blob for mysql, this will be done through a separated thread.
	 * @return byte[]
	 * @throws Exception - when the duration takes to long
	 */
	public byte[] getBlob() throws Exception {
		return call();
	}

	@Override
	public byte[] call() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream obout = new ObjectOutputStream(out);
		obout.writeObject(obj);
		obout.flush();
		obout.close();
		out.close();
		return out.toByteArray();
	}
}

class DeSerializeObjectFromBlob implements Callable<Object> {
	
	private final byte[] bytes;

	public DeSerializeObjectFromBlob(byte[] bytes) {
		this.bytes = bytes;
	}

	/**
	 * @author xize
	 * @param returns the object back to its orginal state byte for byte.
	 * @return Object
	 * @throws Exception - when the duration takes to long
	 */
	public Object getObject() throws Exception {
		return call();
	}

	@Override
	public Object call() throws Exception {
		ByteArrayInputStream input;
		ObjectInputStream objinput;
		input = new ByteArrayInputStream(bytes);
		objinput = new ObjectInputStream(input);
		Object newobj = objinput.readObject();
		input.close();
		objinput.close();
		return newobj;
	}
}

