package tv.mineinthebox.essentials.utils;

import java.util.HashMap;

import tv.mineinthebox.essentials.instances.Home;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;

public class HomeInviteManager {

	private static HashMap<String, xEssentialsOfflinePlayer> homePlayers = new HashMap<String, xEssentialsOfflinePlayer>();
	private static HashMap<String, Home> homes = new HashMap<String, Home>();

	/**
	 * @author xize
	 * @param get the requested home
	 * @return Home
	 */
	public static Home getRequestedHome(String playerName) {
		return homes.get(playerName);
	}

	/**
	 * @author xize
	 * @param requester - the person who later accepts or deny the request
	 * @param home - the home of the persons home the player gets sended to
	 * @return void
	 */
	public static boolean setRequestedHome(String victem, String home) {
			if(get(victem).isValidHome(home)) {
				homes.put(victem, get(victem).getHome(home));
				return true;
			}
		return false;
	}

	/**
	 * @author xize
	 * @param victem - the person who accepts the home.
	 * @return void
	 */
	public static void removeRequestedHome(String victem) {
		homes.remove(victem);
	}

	/**
	 * @author xize
	 * @param gets the value represent by the key
	 * @return xEssentialsPlayer
	 */
	public static xEssentialsOfflinePlayer get(String key) {
		return homePlayers.get(key.toLowerCase());
	}

	/**
	 * @author xize
	 * @param key - is the key of the Map
	 * @param checks whenever the key contains in the home invite list
	 * @return boolean
	 */
	public static boolean containsKey(String key) {
		if(homePlayers.containsKey(key.toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param user - put the player who requests the home invite
	 * @param user2 - put the player who sents the home invite
	 * @return void
	 */
	public static void put(String user, xEssentialsOfflinePlayer user2) {
		homePlayers.put(user.toLowerCase(), user2);
	}

	/**
	 * @author xize
	 * @param removes the key from the home list
	 * @param key - represents the key of the HashMap
	 * @return void
	 */
	public static void remove(String key) {
		homePlayers.remove(key.toLowerCase());
	}

	/**
	 * @author xize
	 * @param check whenever the value exists in the HashMap
	 * @return boolean
	 */
	public static boolean containsValue(String value) {
		return homePlayers.containsValue(value.toLowerCase());
	}
}
