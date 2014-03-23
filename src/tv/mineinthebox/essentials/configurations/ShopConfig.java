package tv.mineinthebox.essentials.configurations;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.enums.ConfigType;

public class ShopConfig {
	
	public boolean isShopsEnabled() {
		return (Boolean) Configuration.getConfigValue(ConfigType.SHOP, "enable");
	}
	
	public String getAdminPrefix() {
		return (String) Configuration.getConfigValue(ConfigType.SHOP, "AdminShopPrefix");
	}
	
	public boolean isTaxEnabled() {
		return (Boolean) Configuration.getConfigValue(ConfigType.SHOP, "isTaxEnabled");
	}
	
	public Double getTax() {
		return (Double) Configuration.getConfigValue(ConfigType.SHOP, "shop.tax.price");
	}
	
	public boolean isCooldownEnabled() {
		return (Boolean) Configuration.getConfigValue(ConfigType.SHOP, "isCooldown");
	}
	
	public int getCooldown() {
		return (Integer) Configuration.getConfigValue(ConfigType.SHOP, "getCooldownTime");
	}
	
	public boolean isShopMessagesDisabled() {
		return (Boolean) Configuration.getConfigValue(ConfigType.SHOP, "disableMessages");
	}

}
