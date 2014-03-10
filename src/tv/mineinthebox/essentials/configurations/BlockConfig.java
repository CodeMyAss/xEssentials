package tv.mineinthebox.essentials.configurations;

import java.util.List;

import org.bukkit.ChatColor;
import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.enums.ConfigEnum;

public class BlockConfig {
	
	/**
	 * @author xize
	 * @param returns true whenever the bedrock place is disabled
	 * @return Boolean
	 */
	public boolean isBedrockPlaceDisabled() {
		return (Boolean) Configuration.getConfigValue(ConfigEnum.BLOCKS, "DisableBedrockPlace");
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the bedrock break is disabled
	 * @return Boolean
	 */
	public boolean isBedrockBreakDisabled() {
		return (Boolean) Configuration.getConfigValue(ConfigEnum.BLOCKS, "DisableBedrockBreak");
	}
	
	/**
	 * @author xize
	 * @param returns true whenever this notify option is enabled
	 * @return Boolean
	 */
	public boolean isNotifyOnBreakEnabled() {
		return (Boolean) Configuration.getConfigValue(ConfigEnum.BLOCKS, "NotifyOnBreak");
	}
	
	/**
	 * @author xize
	 * @param returns the message whenever a player tries to break a specific block
	 * @return String
	 */
	public String getNotifyOnBreakMessage() {
		return ChatColor.translateAlternateColorCodes('&', (String)Configuration.getConfigValue(ConfigEnum.BLOCKS, "NotifyOnBreakMessage"));
	}
	
	/**
	 * @author xize
	 * @param returns the blocks as a true serialized String Material:Durabillity
	 * @return List<String>()
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBlocksFromNotify() {
		return (List<String>) Configuration.getConfigValue(ConfigEnum.BLOCKS, "NotifyOnBreakBlocks");
	}
	
	/**
	 * @author xize
	 * @param returns true whenever this option is enabled
	 * @return Boolean
	 */
	public boolean isNotifyOnConsumeEnabled() {
		return (Boolean) Configuration.getConfigValue(ConfigEnum.BLOCKS, "NotifyOnItemUse");
	}
	
	/**
	 * @author xize
	 * @param returns the message whenever a player is using a consumed item
	 * @return String
	 */
	public String getNotifyOnConsumeMessage() {
		return ChatColor.translateAlternateColorCodes('&', (String) Configuration.getConfigValue(ConfigEnum.BLOCKS, "NotifyOnItemUseMessage"));
	}
	
	/**
	 * @author xize
	 * @param returns the list of consumed items
	 * @return List<String>()
	 */
	@SuppressWarnings("unchecked")
	public List<String> getConsumedItemsFromNotify() {
		return (List<String>) Configuration.getConfigValue(ConfigEnum.BLOCKS, "NotifyOnItemUseItems");
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the block blacklist is enabled
	 * @return Boolean
	 */
	public boolean isBlockBlacklistEnabled() {
		return (Boolean) Configuration.getConfigValue(ConfigEnum.BLOCKS, "isBlockBlackListEnabled");
	}
	
	/**
	 * @author xize
	 * @param returns the block black list
	 * @return List<String>()
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBlockBlackList() {
		return (List<String>)Configuration.getConfigValue(ConfigEnum.BLOCKS, "getBlockBlacklist");
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the item black list is enabled 
	 * @return Boolean
	 */
	public boolean isItemBlacklistEnabled() {
		return (Boolean) Configuration.getConfigValue(ConfigEnum.BLOCKS, "getItemBlackListEnabled");
	}
	
	/**
	 * @author xize
	 * @param returns the Item blacklist
	 * @return List<String>()
	 */
	@SuppressWarnings("unchecked")
	public List<String> getItemBlackList() {
		return (List<String>)Configuration.getConfigValue(ConfigEnum.BLOCKS, "getItemBlacklist");
	}

}
