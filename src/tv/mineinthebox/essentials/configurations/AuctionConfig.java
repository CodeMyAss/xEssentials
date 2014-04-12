package tv.mineinthebox.essentials.configurations;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.enums.ConfigType;

public class AuctionConfig {
	
	/**
	 * @author xize
	 * @param returns true whenever the auction is enabled!
	 * @return Boolean
	 */
	public boolean isAuctionEnabled() {
		return (Boolean) Configuration.getConfigValue(ConfigType.AUCTION, "enable");
	}
	
	/**
	 * @author xize
	 * @param returns the port for the webserver
	 * @return
	 */
	public int getPort() {
		return (Integer) Configuration.getConfigValue(ConfigType.AUCTION, "port");
	}
	
	/**
	 * @author xize
	 * @param returns the increasing auto bid
	 * @return Double
	 */
	public double getIncreasedBid() {
		return (Double) Configuration.getConfigValue(ConfigType.AUCTION, "bid");
	}
	
	/**
	 * @author xize
	 * @param returns the auction url
	 * @return String
	 */
	public String getAuctionUrl() {
		return ((String) Configuration.getConfigValue(ConfigType.AUCTION, "url")+":"+getPort()+"/");
	}
}
