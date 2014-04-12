package tv.mineinthebox.essentials.auction.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.bukkit.inventory.ItemStack;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.LogType;

public class ContentPage {
	
	private final String fileName = "plugins/xEssentials/databases/auction";
	
	/**
	 * @author xize
	 * @param loggedin - a boolean whenever the player logged in, he will see the buy button
	 * @param isAdmin - a boolean whenever the player is logged in as admin he will see a delete button.
	 * @return String
	 */
	public String parseRecentContent(boolean loggedin, boolean isAdmin) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			ResultSet set = state.executeQuery("SELECT * FROM auction_items ORDER BY date DESC LIMIT 0,8");
			state.close();
			con.commit();
			con.close();
			StringBuilder build = new StringBuilder();
			build.append("<div id=\"recentproducts\"/>\n");
			if(set.isBeforeFirst()) {
				if(loggedin) {
					if(isAdmin) {
						while(set.next()) {
							build.append("<div id=\"product\"/>\n" +
									"<div id=\"productphoto\"/><img src=\"images/" + set.getString("imagepath") + "\" height=\"84\" width=\"84\"/></div>\n" +
									"<div id=\"title\"/>by <a href=\"user.html?player=" + set.getString("owner") + "/\"/>" + set.getString("owner") + "</a> at " + new Date(set.getLong("date")) + "</div>\n" +
									"<div id=\"description\"/>serial number: " + set.getString("productkey") + "\namount: " +  ((ItemStack)set.getObject("item")).getAmount() + "\nfor: $" + set.getDouble("cost") + "-\n</div>\n" +
									"<div id=\"button\"/><a href=\"placebid.html?bid=" + set.getString("owner") + "&productid=" + set.getString("UUID") + "\"/></a></div>\n" +
									"<div id=\"delete\"/><a href=\"remove.html?delproduct=" + set.getString("owner") + "&productid=" + set.getString("UUID") + "\"/></a></div>\n" +
									"</div>\n");
						}
					} else {
						while(set.next()) {
							build.append("<div id=\"product\"/>\n" +
									"<div id=\"productphoto\"/><img src=\"images/" + set.getString("imagepath") + "\" height=\"84\" width=\"84\"/></div>\n" +
									"<div id=\"title\"/>by <a href=\"user.html?player=" + set.getString("owner") + "/\"/>" + set.getString("owner") + "</a> at " + new Date(set.getLong("date")) + "</div>\n" +
									"<div id=\"description\"/>serial number: " + set.getString("productkey") + "\namount: " +  ((ItemStack)set.getObject("item")).getAmount() + "\nfor: $" + set.getDouble("cost") + "-\n</div>\n" +
									"<div id=\"button\"/><a href=\"placebid.html?bid=" + set.getString("owner") + "&productid=" + set.getString("UUID") + "\"/></a></div>\n" +
									"</div>\n");
						}	
					}
				} else {
					while(set.next()) {
						build.append("<div id=\"product\"/>\n" +
								"<div id=\"productphoto\"/><img src=\"images/" + set.getString("imagepath") + "\" height=\"84\" width=\"84\"/></div>\n" +
								"<div id=\"title\"/>by <a href=\"user.html?player=" + set.getString("owner") + "/\"/>" + set.getString("owner") + "</a> at " + new Date(set.getLong("date")) + "</div>\n" +
								"<div id=\"description\"/>serial number: " + set.getString("productkey") + "\namount: " +  ((ItemStack)set.getObject("item")).getAmount() + "\nfor: $" + set.getDouble("cost") + "-\n</div>\n" +
								"</div>\n");
					}	
				}
			} else {
				build.append("<div id=\"empty\"/><h1>No recent items where found!</h1><p>no recent items where found inside the shop.<br/>this could mean or the database is wiped or either its a new Minecraft auction!</p></div>");
			}
			build.append("</div>");
			return build.toString();
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
		return null;
	}
	
	public boolean hasTag(File f) {
		try {
			BufferedReader buff = new BufferedReader(new FileReader(f));
			String line;
			try {
				while((line = buff.readLine()) != null) {
					if(line.contains("{content}")) {
						buff.close();
						return true;
					}
				}
				buff.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
