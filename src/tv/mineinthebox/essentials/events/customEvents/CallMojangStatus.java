package tv.mineinthebox.essentials.events.customEvents;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.MojangStatusResponse;
import tv.mineinthebox.essentials.instances.MojangStatus;

public class CallMojangStatus {
	
	private static MojangStatus status;
	
	public void startMojangCheck() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(xEssentials.getPlugin(), new Runnable() {

			@Override
			public void run() {
				JSONParser parser = new JSONParser();
				try {
					URL url = new URL("http://status.mojang.com/check");
					HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
					httpcon.addRequestProperty("User-Agent", xEssentials.getPlugin().getName() + " " + xEssentials.getPlugin().getDescription().getVersion() + " status checker (By xize)");
					InputStreamReader rd = new InputStreamReader(httpcon.getInputStream());
					BufferedReader reader = new BufferedReader(rd);
					JSONArray json = (JSONArray) parser.parse(reader);
					HashMap<String, Boolean> bols = deSerializeJson(json);
					MojangStatus stat = new MojangStatus(bols.get("login"), bols.get("session"), bols.get("skins"));
					//disconnect
					reader.close();
					rd.close();
					httpcon.disconnect();
					if(status != null) {
						if(status.isLoginServerActive() != stat.isLoginServerActive()) {
							if(stat.isLoginServerActive()) {
								Bukkit.getPluginManager().callEvent(new MojangStatusEvent(stat, MojangStatusResponse.LOGIN_SERVER_ACTIVE));
							} else {
								Bukkit.getPluginManager().callEvent(new MojangStatusEvent(stat, MojangStatusResponse.LOGIN_SERVER_DOWN));
							}
						}
						if(status.isSessionServerActive() != stat.isSessionServerActive()) {
							if(stat.isSessionServerActive()) {
								Bukkit.getPluginManager().callEvent(new MojangStatusEvent(stat, MojangStatusResponse.SESSION_SERVER_ACTIVE));
							} else {
								Bukkit.getPluginManager().callEvent(new MojangStatusEvent(stat, MojangStatusResponse.SESSION_SERVER_DOWN));
							}
						}
						if(status.isSkinServerActive() != stat.isSkinServerActive()) {
							if(stat.isSkinServerActive()) {
								Bukkit.getPluginManager().callEvent(new MojangStatusEvent(stat, MojangStatusResponse.SKIN_SERVER_UP));
							} else {
								Bukkit.getPluginManager().callEvent(new MojangStatusEvent(stat, MojangStatusResponse.SKIN_SERVER_DOWN));
							}
						}
						status = stat;
					} else {
						Bukkit.getPluginManager().callEvent(new MojangStatusEvent(stat, MojangStatusResponse.UNKNOWN));
						status = stat;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
		}, 0L, 4000L);
	}
	
	/**
	 * @author xize
	 * @param deserialize a JsonArray and split it to a normal boolean array.
	 * @param for as example you can declare it as the following output:
	 * @param bol[0] - means login server
	 * @param bol[1] - means session server
	 * @param bol[2] - means skin server 
	 * @return Boolean[]
	 */
	private HashMap<String, Boolean> deSerializeJson(JSONArray json) {
		HashMap<String, Boolean> list = new HashMap<String, Boolean>();
		String login = ((JSONObject) json.get(1)).toJSONString();
		String session = ((JSONObject) json.get(2)).toJSONString();
		String skins = ((JSONObject) json.get(5)).toJSONString();
		if(login.contains("green")) {
			list.put("login", true);
		} else {
			list.put("login", false);
		}
		if(session.contains("green")) {
			list.put("session", true);
		} else {
			list.put("session", false);
		}
		if(skins.contains("green")) {
			list.put("skins", true);
		} else {
			list.put("skins", false);
		}
		return list;
	}

}
