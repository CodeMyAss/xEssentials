package tv.mineinthebox.essentials.hook;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;

public class BPermissionsHook {
	
	/**
	 * @author xize
	 * @param p
	 * @param returns the current suffix from this player
	 * @return String
	 */
	public static String getSuffix(Player p) {
		String bPermissions = ApiLayer.getValue(p.getPlayer().getWorld().getName(), CalculableType.USER, p.getPlayer().getName(), "suffix");
		return bPermissions;
	}
	
	/**
	 * @author xize
	 * @param adds the player to the default group
	 * @return void
	 */
	public static void setGroup(String p, String group) {
		ApiLayer.addGroup(Bukkit.getWorlds().toArray(new World[Bukkit.getWorlds().size()])[0].getName(), CalculableType.USER, p, group);
	}
	
	/**
	 * @author xize
	 * @param experimental may does not work
	 * @return String
	 */
	public static String getDefaultGroup() {
		return ApiLayer.getGroups(Bukkit.getWorlds().toArray(new World[Bukkit.getWorlds().size()])[0].getName(), CalculableType.GROUP, "default")[0];
	}
	
	/**
	 * @author xize
	 * @param gets the group of a player
	 * @return String
	 */
	public static String getGroup(String p) {
		return ApiLayer.getValue(Bukkit.getWorlds().toArray(new World[Bukkit.getWorlds().size()])[0].getName(), CalculableType.USER, p, "group");
	}

}
