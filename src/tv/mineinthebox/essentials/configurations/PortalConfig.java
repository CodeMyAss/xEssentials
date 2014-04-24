package tv.mineinthebox.essentials.configurations;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.ConfigType;
import tv.mineinthebox.essentials.instances.Portal;

@SuppressWarnings("unchecked")
public class PortalConfig {
	
	/**
	 * @author xize
	 * @param returns true whenever the portal is enabled
	 * @return Boolean
	 */
	public boolean isPortalEnabled() {
		return (Boolean) Configuration.getConfigValue(ConfigType.PORTAL, "enable");
	}
	
	/**
	 * @author xize
	 * @param returns the cooldown time
	 * @return Integer
	 */
	public int getCooldown() {
		return (Integer) Configuration.getConfigValue(ConfigType.PORTAL, "cooldown");
	}
	
	/**
	 * @author xize
	 * @param returns the list of all worlds inside the configuration
	 * @return List<String>()
	 */
	public List<String> getWorlds() {
		return (List<String>) Configuration.getConfigValue(ConfigType.PORTAL, "worlds");
	}
	
	/**
	 * @author xize
	 * @param returns a HashMap of Portals
	 * @return HashMap<String, Portal>()
	 */
	public HashMap<String, Portal> getPortals() {
		HashMap<String, Portal> portals = new HashMap<String, Portal>();
		File dir = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "portals");
		if(dir.isDirectory()) {
			File[] list = dir.listFiles();
			for(File f : list) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				Portal portal = new Portal(con, f);
				portals.put(portal.getPortalName(), portal);
			}
		}
		return portals;
	}
	
	/**
	 * @author xize
	 * @param name - the name of the portal
	 * @return Portal
	 * @throws NullPointerException - when the name does not exist
	 */
	public Portal getPortal(String name) throws Exception {
		if(getPortals().containsKey(name)) {
			return getPortals().get(name);
		}
		throw new NullPointerException("portal name does not exist!");
	}

}
