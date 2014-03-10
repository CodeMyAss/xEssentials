 package tv.mineinthebox.essentials.hook;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GroupManagerHook {
	
	/**
	 * @author xize
	 * @param p
	 * @param returns the current suffix for GroupManager
	 * @return String
	 */
	public static String getSuffix(Player p) {
		GroupManager mg = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
		String suffix = mg.getWorldsHolder().getWorldPermissions(p.getPlayer()).getUserSuffix(p.getPlayer().getName());
		return suffix;
	}
	
	/**
	 * @author xize
	 * @param set the player to default group
	 * @return void
	 */
	public static void setGroup(String p, String group) {
		GroupManager mg = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
		mg.getWorldsHolder().getWorldDataByPlayerName(p).addGroup(new Group(group));
	}
	
	/**
	 * @author xize
	 * @param get the group of a player
	 * @return String
	 */
	public static String getGroup(String p) {
		GroupManager mg = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
		return ((Group) mg.getWorldsHolder().getWorldDataByPlayerName(p).getDefaultGroup()).getName();
	}
	
	/**
	 * @author xize
	 * @param get the default group
	 * @return String
	 */
	public static String getDefaultGroup() {
		GroupManager mg = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
		return mg.getWorldsHolder().getDefaultWorld().getDefaultGroup().getName();
	}

}
