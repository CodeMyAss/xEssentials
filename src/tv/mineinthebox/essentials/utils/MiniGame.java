package tv.mineinthebox.essentials.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.EnumMap;
import java.util.HashMap;

import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.enums.MinigameType;
import tv.mineinthebox.essentials.instances.SpleefArena;

public class MiniGame {

	/**
	 * @author xize
	 * @param adds a minigame into our global system.
	 * @param type - the MinigameType enum
	 * @param a - the arena.
	 * @throws NullPointerException when the object is something else rather than the Arena Object
	 */
	public static void addGame(MinigameType type, Object a) throws Exception {
		EnumMap<MinigameType, HashMap<String, Object>> hash = getReflectedMap();
		if(type == MinigameType.HUNGERGAMES) {
			//TO-DO
		} else if(type == MinigameType.SPLEEF) {
			if(a instanceof SpleefArena) {
				SpleefArena arena = (SpleefArena) a;
				HashMap<String, Object> hasha = new HashMap<String, Object>();
				hasha.put(arena.getArenaName(), arena);
				hash.put(type, hasha);
			} else {
				throw new NullPointerException("this object is not a SpleefArena Object!");
			}
		}
	}

	/**
	 * @author xize
	 * @param removes a minigame!
	 * @param type - the MinigameType enum
	 * @param arena - the arena name
	 * @throws NullPointerException - when the arena does not exist!
	 */
	public static void removeGame(MinigameType type, String arena) throws Exception {
		EnumMap<MinigameType, HashMap<String, Object>> hash = getReflectedMap();
		if(type == MinigameType.HUNGERGAMES) {
			//TO-DO
		} else if(type == MinigameType.SPLEEF) {
			if(hash.get(type).containsKey(arena)) {
				hash.get(type).remove(arena);
			} else {
				throw new NullPointerException("arena does not exist!, and cannot be removed.");
			}
		}
	}
	
	/**
	 * @author xize
	 * @param gets the Arena specified by the player
	 * @param p - the player
	 * @return Object - this could be HungerGamesArena or SpleefArena
	 */
	public static Object getArenaFromPlayer(Player p) {
		try {
			EnumMap<MinigameType, HashMap<String, Object>> hash = getReflectedMap();
			if(hash.containsKey(MinigameType.HUNGERGAMES)) {
				//TO-DO
			}
			if(hash.containsKey(MinigameType.SPLEEF)) {
				for(Object obj : hash.get(MinigameType.SPLEEF).values()) {
					SpleefArena arena = (SpleefArena) obj;
					if(arena.isPlayerJoined(p.getName())) {
						return arena;
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
	 * @param returns true if the player does exist in one of the arenas!
	 * @param p - the Player
	 * @return Boolean
	 */
	public static boolean isPlayerInArea(Player p) {
		//TO-DO return (getArena(p) instanceof SpleefArena || getArena(p) instanceof HungerGamesArena ? true : false)
		return (getArenaFromPlayer(p) instanceof SpleefArena ? true : false);
	}

	/**
	 * @author xize
	 * @param tries to resolve a reflected map in a not cloned way!
	 * @return EnumMap<MinigameType, HashMap<String, Object>>()
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected static EnumMap<MinigameType, HashMap<String, Object>> getReflectedMap() throws Exception {
		Class<?> clazz = Class.forName("tv.mineinthebox.essentials.Configuration");
		Field f1 = clazz.getDeclaredField("minigames");
		f1.setAccessible(true);
		Field f2 = clazz.getDeclaredField("modifiers");
		f2.setAccessible(true);
		f2.setInt(f1, f1.getModifiers() &~Modifier.FINAL &~Modifier.STATIC);
		EnumMap<MinigameType, HashMap<String, Object>> hash = (EnumMap<MinigameType, HashMap<String, Object>>) f1.get("minigames");
		f1.setAccessible(false);
		f2.setAccessible(false);
		return hash;
	}

}
