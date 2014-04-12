package tv.mineinthebox.essentials.auction.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.http.Cookie;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.auction.AuctionManager;
import tv.mineinthebox.essentials.hook.Hooks;
import tv.mineinthebox.essentials.hook.VaultHook;
import tv.mineinthebox.essentials.instances.MarketItem;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class PlaceBidPage {
	
	public String getBidPage(Cookie cookie, String productkey) {
		StringBuilder build = new StringBuilder();
		build.append("<div id=\"result\"/>\n");
		if(cookie instanceof Cookie) {
			if(xEssentials.isEssentialsPlayer(cookie.getName())) {
				xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(cookie.getName());
				if(off.getAuctionSession().equalsIgnoreCase(cookie.getValue())) {
					MarketItem item = xEssentials.getAuctionDatabase().getMarketItemFromSale(productkey);
					if(Configuration.getEconomyConfig().isEconomyEnabled()) {
						if(off.hasPlayerEnoughMoneyFromPrice((item.getMoney()+Configuration.getAuctionConfig().getIncreasedBid()))) {
							if(xEssentials.contains(off.getUser())) {
								xEssentialsPlayer xp = xEssentials.get(off.getUser());
								xEssentials.getAuctionDatabase().addBid(productkey, (item.getMoney()+Configuration.getAuctionConfig().getIncreasedBid()), xp);
								AuctionManager.startBid(productkey, xp.getUser());
								build.append("<p>success you have did a bid on a item from " + item.getOwner() + "!</p>");
							} else {
								xEssentials.getAuctionDatabase().addBid(productkey, (item.getMoney()+Configuration.getAuctionConfig().getIncreasedBid()), off);
								AuctionManager.startBid(productkey, off.getUser());
								build.append("<p>success you have did a bid on a item from " + item.getOwner() + "!</p>");
							}
						} else {
							build.append("<p>you don't have enough money to afford this!</p>");
						}
					} else { 
						if(Hooks.isVaultEnabled()) {
							if(VaultHook.hasEnough(off.getUser(), (item.getMoney()+Configuration.getAuctionConfig().getIncreasedBid()))) {
								xEssentials.getAuctionDatabase().addBid(productkey, (item.getMoney()+Configuration.getAuctionConfig().getIncreasedBid()), off);
								AuctionManager.startBid(productkey, off.getUser());
								build.append("<p>success you have did a bid on a item from " + item.getOwner() + "!</p>");
							} else {
								build.append("<p>you don't have enough money to afford this!</p>");
							}
						} else {
							build.append("<p>Oh oh, we couldn't find any economy system on the minecraft server please contact the owner.</p>");
						}
					}
				} else {
					build.append("<p>invalid cookie!, or your password has changed please clear your cookies.</p>");
				}
			} else {
				build.append("<p>invalid cookie!</p>");
			}
		} else {
			build.append("<p>you are not logged in!</p>");
		}
		build.append("</div>");
		return build.toString();
	}
	
	public boolean hasTag(File f) {
		try {
			BufferedReader buff = new BufferedReader(new FileReader(f));
			String line;
			try {
				while((line = buff.readLine()) != null) {
					if(line.contains("{bidstatus}")) {
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
