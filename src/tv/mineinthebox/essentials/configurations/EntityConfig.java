package tv.mineinthebox.essentials.configurations;

import java.util.HashMap;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.enums.ConfigEnum;


public class EntityConfig {
	
	/**
	 * @author xize
	 * @param returns whenever the weather is disabled or enabled
	 * @return boolean
	 */
	public boolean isWeatherDisabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "disableWeather");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns true when firespread is disabled
	 * @return boolean
	 */
	public boolean isFireSpreadDisabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "disableFirespread");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns true when explosions are disabled
	 * @return boolean
	 */
	public boolean isExplosionsDisabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "disableExplosion");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns true when fireworks are disabled
	 * @return boolean
	 */
	public boolean isFireworksDisabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "disableFirework");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns true when wither grief is disabled
	 * @return boolean
	 */
	public boolean isWitherGriefDisabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "disableWitherGrief");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns true when enderman grief is disabled
	 * @return boolean
	 */
	public boolean isEnderManGriefDisabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "disableEndermanGrief");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns true when enderdragon grief is disabled
	 * @return boolean
	 */
	public boolean isEnderDragonGriefDisabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "disableEnderdragonGrief");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns true when the custom aggro spawn is enabled
	 * @return boolean
	 */
	public boolean isCustomZombieAggroRangeEnabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "zombieCustomAggroEnable");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns the current custom aggro range of zombies
	 * @return Integer
	 */
	public Integer getCustomZombieAggroRange() {
		int number = (Integer) Configuration.getConfigValue(ConfigEnum.ENTITY, "zombieCustomAggroRange");
		return number;
	}
	
	/**
	 * @author xize
	 * @param returns true when spawn eggs are disabled
	 * @return boolean
	 */
	public boolean isSpawnEggsDisabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "disableSpawnEggs");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns true when logging for eggs is enabled
	 * @return boolean
	 */
	public boolean isLoggingSpawnEggsEnabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "logSpawnEggs");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns true when criticals are enabled
	 * @return boolean
	 */
	public boolean isCriticalsEnabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "useCriticals");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the configuration 'Remove-Flying-Projectiles-On-ChunkLoad' is enabled this will enable the functional base to remove witherskulls and fireballs on chunk load so the server cannot be abused nor get laggy due this. 
	 * @return boolean
	 */
	public boolean isChunkProtectionEnabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.ENTITY, "AntiFireball");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns a HashMap containing all configurated entitys
	 * @return HashMap<String, Boolean>()
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Boolean> getEntitys() {
		return (HashMap<String, Boolean>) Configuration.getConfigValue(ConfigEnum.ENTITY, "allowToSpawn");
	}

}
