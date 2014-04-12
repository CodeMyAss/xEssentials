package tv.mineinthebox.essentials.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.LogType;
import tv.mineinthebox.essentials.instances.AuctionBidUser;
import tv.mineinthebox.essentials.instances.DeSerializeObjectFromBlob;
import tv.mineinthebox.essentials.instances.MarketItem;
import tv.mineinthebox.essentials.instances.SerializeObjectToBlob;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class AuctionSqlite {

	private final String fileName = "plugins/xEssentials/databases/auction";

	public AuctionSqlite() {
		File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "databases");
		if(!f.isDirectory()) {
			f.mkdir();
		}
		if(isNewDatabase()) {
			xEssentials.getPlugin().log("no sqlite database found for auctions, setting one up right now!", LogType.INFO);
			createNewDatabase();
		} else {
			xEssentials.getPlugin().log("sqlite database found for auctions at: " + fileName + ".db", LogType.INFO);
		}
	}

	/**
	 * @author xize
	 * @param query - which handles the query
	 * @param table - this is the specific table where the value is in which we search
	 * @return Object
	 */
	public Object getObjectFromQuery(String query, String table) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			ResultSet result = state.executeQuery(query);
			Object obj = result.getObject(table);
			state.close();
			con.close();
			return obj;
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
		return null;
	}

	/**
	 * @author xize
	 * @param query - which handles the query
	 * @param table - returns all the table values
	 * @return List<Object>()
	 * @throws NullPointerException - when the query has none result
	 */
	public List<Object> getObjectsFromQuery(String query, List<String> tables) {
		try {
			List<Object> objs = new ArrayList<Object>();
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			ResultSet result = state.executeQuery(query);
			for(String table : tables) {
				objs.add(result.getObject(table));
			}
			state.close();
			con.close();
			return objs;
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
		throw new NullPointerException("could not create a list, when there is no table found in sqlite!");
	}

	/**
	 * @author xize
	 * @param query - insert or update a query 
	 */
	public void doQuery(String query) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			state.executeUpdate(query);
			state.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			//xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
	}

	private void createNewDatabase() {
		
		//TO-DO looking for auto increment for id's since sqlite doesn't support this we cannot lay our heads on user names as primary keys.
		
		String users = "CREATE TABLE IF NOT EXISTS `auction_users` ("+ 
				"`date` text NOT NULL," +
				"`username` text UNIQUE NOT NULL, " +
				"`products` int NOT NULL, " +
				"`UUID` text NOT NULL, " +
				"`password` text NOT NULL, " +
				"`SESSION_ID` text NOT NULL, " +
				"PRIMARY KEY (`username`) " +
				")";

		String items = "CREATE TABLE IF NOT EXISTS `auction_items` (" +
				"`owner` text NOT NULL," +
				"`cost` double NOT NULL," +
				"`productkey` text NOT NULL," +
				"`date` text NOT NULL," +
				"`imagepath` text NOT NULL," +
				"`item` longblob NOT NULL," +
				"PRIMARY KEY (`owner`) " +
				")";

		String bids = "CREATE TABLE IF NOT EXISTS `auction_bids` (" +
				"`productid` TEXT NOT NULL," +
				"`bid` DOUBLE NOT NULL," +
				"`username` TEXT NOT NULL," +
				"`playerUUID` text NOT NULL," +
				"PRIMARY KEY (`username`) " +
				")";

		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			state.executeUpdate(users);
			state.executeUpdate(items);
			state.executeUpdate(bids);
			state.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
	}

	private boolean isNewDatabase() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			ResultSet set = state.executeQuery("SELECT * FROM auction_users");
			if(set.isBeforeFirst()) {
				return false;
			}
			state.close();
			con.close();
		} catch (SQLException e) {
			return true;
		} catch(ClassNotFoundException e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
		return false;
	}

	public boolean doesPlayerExist(String player) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			ResultSet set = state.executeQuery("SELECT * FROM auction_users WHERE username='" + player + "'");
			if(set.getString("username").equalsIgnoreCase(player)) {
				state.close();
				con.close();
				return true;
			} else {
				state.close();
				con.close();	
			}
		} catch(SQLException r) {
			return false;
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
		return false;
	}

	/**
	 * @author xize
	 * @param item - the market item which is a extend of ItemStack
	 * @param returns true whenever the placing is correct
	 * @return Boolean
	 */
	public boolean placeItemOnSale(MarketItem item) {
		String owner = item.getOwner();
		String imagePath = item.getItemIconImage();
		String productkey = item.getProductId().toString().replace("-", "");
		double cost = item.getMoney();
		long date = item.getCreationDate();
		ItemStack item1 = (ItemStack) item;		
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			String insert = setValueString(new String[] {owner, Double.toString(cost), productkey, Long.toString(date), imagePath, Arrays.toString(serializeObject(item1))});
			state.executeUpdate("INSERT INTO auction_items(owner, cost, productkey, date, imagepath, item) VALUES(" + insert + ")");
			state.close();
			con.close();
			return true;
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
		return false;
	}

	/**
	 * @author xize
	 * @param productkey - the unique id which we use for lookups into the database
	 * @return ItemStack
	 */
	public ItemStack getItemFromSale(String productkey) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			ResultSet set = state.executeQuery("SELECT item FROM auction_items WHERE productkey='" + productkey + "'");
			ItemStack item = (ItemStack) deserializeObject(set.getBytes("item"));
			state.close();
			con.close();
			return item;
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
		return null;
	}

	public MarketItem getMarketItemFromSale(String productkey) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			ResultSet set = state.executeQuery("SELECT * FROM auction_items WHERE productkey='" + productkey + "'");
			ItemStack stack = (ItemStack) deserializeObject(set.getBytes("item"));
			MarketItem item = new MarketItem(stack);
			item.setOwner(set.getString("owner"));
			item.setCosts(set.getDouble("cost"));
			item.setCreationDate(new Date(set.getLong("date")));
			state.close();
			con.close();
			return item;
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
		return null;
	}

	public void removeItemFromSale(String productkey) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			state.executeUpdate("DELETE * FROM auction_items WHERE productkey='" + productkey + "'");
			state.executeUpdate("DELETE * FROM auction_bids WHERE productkey='"+ productkey + "'");
			state.close();
			con.close();
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
	}

	private byte[] serializeObject(Object obj) {
			SerializeObjectToBlob blob = new SerializeObjectToBlob(obj);
			try {
				return blob.getBlob();
			} catch (Exception e) {
				xEssentials.getPlugin().log("could not serialize object for the auction!", LogType.SEVERE);
			}
			return null;
	}

	private Object deserializeObject(byte[] bytes) {
		DeSerializeObjectFromBlob blob = new DeSerializeObjectFromBlob(bytes);
		try {
			return blob.getBlob();
		} catch (Exception e) {
			xEssentials.getPlugin().log("could not deserialize object for the auction!", LogType.SEVERE);
		}
		return null;
	}

	public void updatePlayer(String oldname, String newname) {
		try {
			if(doesPlayerExist(oldname)) {
				Class.forName("org.sqlite.JDBC");
				Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
				Statement state = con.createStatement();
				state.executeUpdate("UPDATE auction_users SET username='" + newname + "' WHERE username='"+ oldname +"'");
				state.executeUpdate("UPDATE auction_items SET owner='" + newname + "' WHERE owner='"+ oldname +"'");
				state.executeUpdate("UPDATE auction_bids SET username='" + newname +"' WHERE username='"+ oldname +"'");
				state.close();
				con.close();	
			}
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
	}

	public void clearAuction(String player) {
		if(doesPlayerExist(player)) {
			try {
				Class.forName("org.sqlite.JDBC");
				Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
				Statement state = con.createStatement();
				state.executeUpdate("DELETE * FROM auction_users WHERE username='" + player +"'");
				state.executeUpdate("DELETE * FROM auction_items WHERE owner='" + player +"'");
				state.executeUpdate("DELETE * FROM auction_bids WHERE username='" + player +"'");
				state.close();
				con.close();
			} catch(Exception e) {

			}
		}
	}

	public void addBid(String productkey, Double bid, xEssentialsPlayer xp) {
		try {			
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			String insert = setValueString(new String[] {productkey, Double.toString(bid), xp.getUser(), xp.getUniqueId()});
			state.executeUpdate("INSERT INTO auction_bids(productid, bid, username, playerUUID) VALUES(" + insert + ")");
			state.close();
			con.close();
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
	}
	
	public void addBid(String productkey, Double bid, xEssentialsOfflinePlayer off) {
		try {			
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			String insert = setValueString(new String[] {productkey, Double.toString(bid), off.getUser(), off.getUniqueId()});
			state.executeUpdate("INSERT INTO auction_bids(productid, bid, username, playerUUID) VALUES("+ insert + ")");
			state.close();
			con.close();
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
	}

	public List<AuctionBidUser> getBidList(String productkey) {
		List<AuctionBidUser> users = new ArrayList<AuctionBidUser>();
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
			Statement state = con.createStatement();
			ResultSet result = state.executeQuery("SELECT * FROM auction_bids WHERE productkey=" + productkey + "");
			while(result.next()) {
				Double bid = result.getDouble("bid");
				String username = result.getString("username");
				AuctionBidUser user = new AuctionBidUser(username, productkey, bid);
				users.add(user);
			}
			state.close();
			con.close();
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite files for the " + fileName + " system, is craftbukkit up to date?", LogType.SEVERE);
		}
		Collections.sort(users);
		return users;
	}
	
	/**
	 * @author xize
	 * @param returns the correct syntax for sql for VALUES() in mysql
	 * @return String
	 */
	public String setValueString(String[] args) {
		StringBuilder build = new StringBuilder();
		for(int i = 0; i < args.length; i++) {
			if(i == (args.length-1)) {
				build.append("'" + args[i] + "'");
			} else {
				build.append("'" + args[i] + "',");
			}
		}
		return build.toString();
	}
}
