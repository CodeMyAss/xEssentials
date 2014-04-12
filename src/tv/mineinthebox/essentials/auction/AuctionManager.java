package tv.mineinthebox.essentials.auction;

import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.LogType;
import tv.mineinthebox.essentials.hook.Hooks;
import tv.mineinthebox.essentials.hook.VaultHook;
import tv.mineinthebox.essentials.instances.AuctionBidUser;
import tv.mineinthebox.essentials.instances.MarketItem;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class AuctionManager {

	private static HashMap<String, Integer> productTask = new HashMap<String, Integer>();

	public static void startBid(String productkey, String firstBidPlayer) {
		if(!productTask.containsKey(productkey)) {
			Bukkit.broadcastMessage(ChatColor.GREEN + "[Auction] " + ChatColor.GRAY + " ");
			startScheduler(productkey);
		}
	}

	private static void startScheduler(final String productkey) {
		int task = Bukkit.getScheduler().scheduleSyncDelayedTask(xEssentials.getPlugin(), new Runnable() {

			@Override
			public void run() {
				MarketItem item = xEssentials.getAuctionDatabase().getMarketItemFromSale(productkey);
				if(xEssentials.getAuctionDatabase().getBidList(productkey).isEmpty()) {
					Iterator<AuctionBidUser> it = xEssentials.getAuctionDatabase().getBidList(productkey).iterator();
					Bukkit.broadcastMessage(ChatColor.GREEN + "[Auction] " + ChatColor.GRAY + "auction of player " + item.getOwner() + " has been ended!");
					while(it.hasNext()) {
						AuctionBidUser user = it.next();
						Double cost = user.getBid();
						if(Configuration.getEconomyConfig().isEconomyEnabled()) {
							if(user.getOfflinePlayer().hasPlayerEnoughMoneyFromPrice(cost)) {
								Bukkit.broadcastMessage(ChatColor.GREEN + "[Auction] " + ChatColor.GRAY + "the highest bid is: " + cost + Configuration.getEconomyConfig().getCurency() + " by " + user.getUser());
								if(xEssentials.contains(user.getUser())) {
									xEssentialsPlayer xp = xEssentials.get(user.getUser());
									if(item.isPlayerOnline()) {
										xEssentialsPlayer auctionHolder = item.getPlayer();
										xp.payEssentialsMoney(cost, auctionHolder);
										ItemStack item1 = (ItemStack) item;
										xp.getPlayer().getInventory().addItem(item1);
									} else {
										xEssentialsOfflinePlayer auctionHolder = item.getOfflinePlayer();
										xp.payEssentialsMoney(cost, auctionHolder);
										ItemStack item1 = (ItemStack) item;
										xp.getPlayer().getInventory().addItem(item1);
									}
								} else {
									//TO-DO adding items in custom inventory
									xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(user.getUser());
									if(item.isPlayerOnline()) {
										xEssentialsPlayer auctionHolder = item.getPlayer();
										off.payEssentialsMoney(cost, auctionHolder);
										ItemStack item1 = (ItemStack) item;
										off.setBoughtAuctionItem(item1);
									} else {
										xEssentialsOfflinePlayer auctionHolder = item.getOfflinePlayer();
										off.payEssentialsMoney(cost, auctionHolder);
										ItemStack item1 = (ItemStack) item;
										off.setBoughtAuctionItem(item1);
									}
								}
								break;
							} else {
								if(user.getOfflinePlayer().getPlayer() instanceof Player) {
									user.getOfflinePlayer().getPlayer().sendMessage(ChatColor.GREEN + "[Auction] " + ChatColor.GRAY + "since you have added the highest bid, but already spent your money we skipped to the next highest bid");
								}
							}
						} else {
							if(Hooks.isVaultEnabled()) {
								if(VaultHook.hasEnough(user.getUser(), cost)) {
									Bukkit.broadcastMessage(ChatColor.GREEN + "[Auction] " + ChatColor.GRAY + "the highest bid is: " + cost + Configuration.getEconomyConfig().getCurency() + " by " + user.getUser());
									if(xEssentials.contains(user.getUser())) {
										xEssentialsPlayer xp = xEssentials.get(user.getUser());
										if(item.isPlayerOnline()) {
											xEssentialsPlayer auctionHolder = item.getPlayer();
											VaultHook.pay(user.getUser(), auctionHolder.getUser(), cost);
											ItemStack item1 = (ItemStack) item;
											xp.getPlayer().getInventory().addItem(item1);
										} else {
											xEssentialsOfflinePlayer auctionHolder = item.getOfflinePlayer();
											VaultHook.pay(user.getUser(), auctionHolder.getUser(), cost);
											ItemStack item1 = (ItemStack) item;
											xp.getPlayer().getInventory().addItem(item1);
										}
									} else {
										//TO-DO adding items in custom inventory
										xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(user.getUser());
										if(item.isPlayerOnline()) {
											xEssentialsPlayer auctionHolder = item.getPlayer();
											VaultHook.pay(user.getUser(), auctionHolder.getUser(), cost);
											ItemStack item1 = (ItemStack) item;
											off.setBoughtAuctionItem(item1);
										} else {
											xEssentialsOfflinePlayer auctionHolder = item.getOfflinePlayer();
											VaultHook.pay(user.getUser(), auctionHolder.getUser(), cost);
											ItemStack item1 = (ItemStack) item;
											off.setBoughtAuctionItem(item1);
										}
									}
									break;
								} else {
									if(user.getOfflinePlayer().getPlayer() instanceof Player) {
										user.getOfflinePlayer().getPlayer().sendMessage(ChatColor.GREEN + "[Auction] " + ChatColor.GRAY + "since you have added the highest bid, but already spent your money we skipped to the next highest bid");
									}
								}
							} else {
								xEssentials.getPlugin().log("the Auction system wasn't able to find a economy system installed!", LogType.SEVERE);
							}
						}
					}
				}
				productTask.remove(productkey);
				xEssentials.getAuctionDatabase().removeItemFromSale(productkey);
			}

		}, 20*60*60);
		productTask.put(productkey, task);
	}

}
