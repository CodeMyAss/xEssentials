package tv.mineinthebox.essentials.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class TpaManager {
	
	private static HashMap<String, String> tpaPlayers = new HashMap<String, String>();
	
	/**
	 * @author xize
	 * @param gets the value represent by the key
	 * @return String
	 */
	public static String get(String key) {
		return tpaPlayers.get(key.toLowerCase());
	}
	
	/**
	 * @author xize
	 * @param key - is the key of the Map
	 * @param checks whenever the key contains in the tpa list
	 * @return boolean
	 */
	public static boolean containsKey(String key) {
		if(tpaPlayers.containsKey(key.toLowerCase())) {
			return true;
		}
		return false;
	}
	
	/**
	 * @author xize
	 * @param user - put the player who requests the tpa
	 * @param user2 - put the player who sents the tpa
	 * @return void
	 */
	public static void put(String user, String user2) {
		tpaPlayers.put(user.toLowerCase(), user2.toLowerCase());
	}
	
	/**
	 * @author xize
	 * @param removes the key from the tpa list
	 * @param key - represents the key of the HashMap
	 * @return void
	 */
	public static void remove(String key) {
		tpaPlayers.remove(key.toLowerCase());
	}
	
	/**
	 * @author xize
	 * @param check whenever the value exists in the HashMap
	 * @return boolean
	 */
	public static boolean containsValue(String value) {
		return tpaPlayers.containsValue(value.toLowerCase());
	}
	
	/**
	 * @author xize
	 * @param extract the key from the HashMap value!
	 * @return String
	 */
	public String ExtractKeyFromValue(String value) {
		Iterator<Entry<String, String>> it = tpaPlayers.entrySet().iterator();
		while(it.hasNext()) {
			if(it.next().getValue().equals(value.toLowerCase())) {
				return it.next().getKey();
			}
		}
		return null;
	}

}
