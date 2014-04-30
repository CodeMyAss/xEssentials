package tv.mineinthebox.essentials.instances;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public class SpleefArena {
	
	private HashSet<String> players = new HashSet<String>();
	
	private FileConfiguration con;
	private File f;
	private String arena;
	
	public SpleefArena(File f, FileConfiguration con) throws NullPointerException {
		if(f.exists()) {
			this.f = f;
			this.con = con;
		} else {
			throw new NullPointerException("arena file does not exist!");
		}
	}
	
	/**
	 * @author xize
	 * @param returns the arena name
	 * @return String
	 */
	public String getArenaName() {
		return arena;
	}
	
	/**
	 * @author xize
	 * @param returns the snow layer
	 * @return Block[]
	 */
	public Block[] getSnowLayer() {
		List<Block> blocks = new ArrayList<Block>();
		for(String s : con.getStringList("arena.snowlayers")) {
			String[] args = s.split(":");
			World w = Bukkit.getWorld(args[0]);
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			int z = Integer.parseInt(args[3]);
			Block block = new Location(w, x, y, z).getBlock();
			blocks.add(block);
		}
		return blocks.toArray(new Block[blocks.size()]);
	}
	
	/**
	 * @author xize
	 * @param returns all the spawnpoints where players possible can spawn.
	 * @return Location[]
	 */
	public Location[] getSpawnPoints() {
		List<Location> locs = new ArrayList<Location>();
		for(String s : con.getStringList("arena.spawnpoints")) {
			String[] args = s.split(":");
			World w = Bukkit.getWorld(args[0]);
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			int z = Integer.parseInt(args[3]);
			float yaw = Float.parseFloat(args[4]);
			float pitch = Float.parseFloat(args[5]);
			locs.add(new Location(w, x, y, z, yaw, pitch));
		}
		return locs.toArray(new Location[locs.size()]);
	}
	
	/**
	 * @author xize
	 * @param returns the max players allowed
	 * @return Integer
	 */
	public int getMaxPlayersAllowed() {
		return getSpawnPoints().length;
	}
	
	/**
	 * @author xize
	 * @param returns true if the game is already started
	 * @return boolean
	 */
	public boolean isRunning() {
		if(con.contains("arena.isrunning")) {
			return con.getBoolean("arena.isrunning");
		}
		return false;
	}
	
	public void setRunning(boolean bol) {
		con.set("arena.isrunning", bol);
		try {
			con.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		load();
	}
	
	/**
	 * @author xize
	 * @param alllows the player to join this spleef game!
	 * @param xp - the essentials player
	 * @return boolean
	 */
	public boolean PlayerSetArena(xEssentialsPlayer xp) {
		if(players.size() < getMaxPlayersAllowed() || isRunning()) {
			players.add(xp.getUser());
			return true;
		}
		return false;
	}
	
	/**
	 * @author xize
	 * @param name - the players name
	 * @return boolean
	 */
	public boolean isPlayerJoined(String name) {
		return players.contains(name);
	}
	
	private void load() {
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

}
