package tv.mineinthebox.essentials.hook;

import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsExHook {
	
	/**
	 * @author xize
	 * @param p
	 * @param returns the current suffix of PermissionsEx
	 * @return
	 */
	public static String getSuffx(Player p) {
		String pex = PermissionsEx.getUser(p.getPlayer().getName()).getSuffix();
		return pex;
	}
	
	/**
	 * @author xize
	 * @param sets the user in the default group
	 * @return void
	 */
	public static void setGroup(String p, String group) {
		PermissionsEx.getUser(p).addGroup(group);
	}
	
	/**
	 * @author xize
	 * @param get the group
	 * @return String
	 */
	public static String getGroup(String p) {
		return PermissionsEx.getUser(p).getGroups()[0].getName();
	}
	
	/**
	 * @author xize
	 * @param get the default group
	 * @return String
	 */
	public static String getDefaultGroup() {
		return PermissionsEx.getPermissionManager().getDefaultGroup().getName();
	}

}
