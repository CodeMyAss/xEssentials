package tv.mineinthebox.essentials.events.customEvents;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.LogType;
import tv.mineinthebox.essentials.instances.RssFeed;

public class CallRssFeedEvent  {
	
	protected static boolean isItemFound = false;
	private static RssFeed feed;

	public void onChatSentRssBroadcast() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(xEssentials.getPlugin(), new Runnable() {
			@Override
			public void run() {
				try {
					loadLastFeed();
					isItemFound = false;
					String author = null;
					String link = null;
					String title = null;
					URL url = new URL(Configuration.getChatConfig().getRssUrl());
					HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
					httpcon.addRequestProperty("User-Agent", xEssentials.getPlugin().getName() + " " + xEssentials.getPlugin().getDescription().getVersion() + " robot (By xize)");
					InputStreamReader input = new InputStreamReader(httpcon.getInputStream());
					BufferedReader reader = new BufferedReader(input);
					String line;
					String text = "";
					while((line = reader.readLine()) != null) {
						if(line.contains("<item>")) {
							isItemFound = true;
						}
						if(isItemFound) {
	                         if(line.contains("<title>")) {
                                 text = "";
                                 int firstPos = line.indexOf("<title>");
                                 String temp = line.substring(firstPos);
                                 temp = temp.replace("<title>", "");
                                 int lastPos = temp.indexOf("</title>");
                                 temp = temp.substring(0,lastPos);
                                 title = text+= temp;
                         }
                         if(line.contains("<link>")) {
                                 text = "";
                                 int firstPos = line.indexOf("<link>");
                                 String temp = line.substring(firstPos);
                                 temp = temp.replace("<link>", "");
                                 int lastPos = temp.indexOf("</link>");
                                 temp = temp.substring(0,lastPos);
                                 link = text+= temp;
                         }
                         if(line.contains("<author>")) {
                                 text = "";
                                 int firstPos = line.indexOf("<author>");
                                 String temp = line.substring(firstPos);
                                 temp = temp.replace("<author>", "");
                                 int lastPos = temp.indexOf("</author>");
                                 temp = temp.substring(0,lastPos);
                                 author = text+= temp;
                                 break;
                         }
						}
					}
					reader.close();
					input.close();
					httpcon.disconnect();
					RssFeed afeed = new RssFeed(title, author, link);
					if(feed != null) {
						if(!feed.getTitle().equalsIgnoreCase(afeed.getTitle())) {
							feed = afeed;
							for(Player p : Bukkit.getOnlinePlayers()) {
								Bukkit.getPluginManager().callEvent(new RssFeedEvent(p, feed));        
							}        
						}
					} else {
						feed = afeed;
						for(Player p : Bukkit.getOnlinePlayers()) {
							Bukkit.getPluginManager().callEvent(new RssFeedEvent(p, feed));        
						}
					}


				} catch (MalformedURLException e1) {
					xEssentials.getPlugin().log("the url is wrong for the RSS!", LogType.SEVERE);
				} catch (IOException e1) {
					xEssentials.getPlugin().log("Connection timeout for the RSS event! " + e1.getCause(), LogType.SEVERE);
				}

			}
		}, 100, 2500);
	}

	public static void saveLastFeed() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "lastRssFeed.yml");
			YamlConfiguration con = YamlConfiguration.loadConfiguration(f);
			con.set("author", feed.getAuthor());
			con.set("title", feed.getTitle());
			con.set("link", feed.getLink());
			con.save(f);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadLastFeed() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "lastRssFeed.yml");
			if(f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				RssFeed rss = new RssFeed(con.getString("title"), con.getString("author"), con.getString("link"));
				feed = rss;
				f.delete();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


}

