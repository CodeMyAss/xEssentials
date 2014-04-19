package tv.mineinthebox.essentials.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.LogType;

public class xEssentialsGlobalDB {

	public static String[] getOfflineNames() {
		List<String> list = new ArrayList<String>();
		try {
			Connection con = getConnection();
			Statement state = con.createStatement();
			ResultSet set = state.executeQuery("SELECT user FROM players");
			while(set.next()) {
				list.add(set.getString(1));
			}
			state.close();
			set.close();
			con.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * @author xize
	 * @param returns fresh sqlite Connection 
	 * @return Connection
	 */
	private static Connection getConnection() {
		File dir = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "databases");
		if(!dir.isDirectory()) {
			dir.mkdir();
		}
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlite:plugins/xEssentials/databases/players.db");
			return con;
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite in craftbukkit, this is probably because you are running a outdated build!", LogType.SEVERE);
		}
		return null;
	}

}
