package tv.mineinthebox.essentials.events.auction;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.instances.MarketItem;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;
import tv.mineinthebox.essentials.utils.Crypt;

public class CommandAuctionEvent implements Listener {

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().startsWith("/auction")) {
			String[] args = getArgs(e.getMessage());
			if(e.getPlayer().hasPermission(PermissionKey.CMD_AUCTION.getPermission())) {
				if(args.length == 1) {
					e.getPlayer().sendMessage(ChatColor.GOLD + ".oO___[auction help]___Oo.");
					e.getPlayer().sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/auction help " + ChatColor.WHITE + ": shows help");
					e.getPlayer().sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/auction set <password> " + ChatColor.WHITE + ": set the password of the auction");
					e.getPlayer().sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/auction sell <cost> " + ChatColor.WHITE + ": sells the item in your hand in the auction");
					if(e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						e.getPlayer().sendMessage(ChatColor.RED  + "Admin: " + ChatColor.GRAY + "/auction delete <player> " + ChatColor.WHITE + ": removes all the items and auction related of this player");
						e.getPlayer().sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/auction reset <player> <newpassword> " + ChatColor.WHITE + ": reset the players password");
					}
				} else if(args.length == 2) {
					if(args[1].equalsIgnoreCase("help")) {
						e.getPlayer().sendMessage(ChatColor.GOLD + ".oO___[auction help]___Oo.");
						e.getPlayer().sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/auction help " + ChatColor.WHITE + ": shows help");
						e.getPlayer().sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/auction set <password> " + ChatColor.WHITE + ": set the password of the auction");
						e.getPlayer().sendMessage(ChatColor.DARK_GRAY + "Default: " + ChatColor.GRAY + "/auction sell <cost> " + ChatColor.WHITE + ": sells the item in your hand in the auction");
						if(e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
							e.getPlayer().sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/auction delete <player> " + ChatColor.WHITE + ": removes all the items and auction related of this player");
							e.getPlayer().sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/auction reset <player> <newpassword> " + ChatColor.WHITE + ": reset the players password");
						}
					}
				} else if(args.length == 3) {
					if(args[1].equalsIgnoreCase("set")) {
						xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
						try {
							xp.setAuctionPassword(Crypt.CryptToSaltedSha512(args[2]));
						} catch (NoSuchAlgorithmException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NoSuchProviderException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						e.getPlayer().sendMessage(ChatColor.GREEN + "you have successfully changed/registered your password");
					} else if(args[1].equalsIgnoreCase("delete")) {
						if(e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
							if(xEssentials.isEssentialsPlayer(args[2])) {
								if(xEssentials.contains(args[2])) {
									xEssentialsPlayer xp = xEssentials.get(args[2]);
									xEssentials.getAuctionDatabase().clearAuction(xp.getUser());
									e.getPlayer().sendMessage(ChatColor.GREEN + "successfully removed player " + xp.getUser() + " from the auctions");
								} else {
									xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(args[2]);
									xEssentials.getAuctionDatabase().clearAuction(off.getUser());
									e.getPlayer().sendMessage(ChatColor.GREEN + "successfully removed player " + off.getUser() + " from the auctions");
								}
							} else {
								Warnings.getWarnings(e.getPlayer()).playerHasNeverPlayedBefore();
							}
						} else {
							Warnings.getWarnings(e.getPlayer()).noPermission();
						}
					} else if(args[1].equalsIgnoreCase("sell")) {
						ItemStack item = e.getPlayer().getItemInHand();
						if(item.getType() != Material.AIR) {
							MarketItem market = new MarketItem(item);
							xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
							try {
								Double money = Double.parseDouble(args[2]);
								market.setOwner(xp.getUser());
								market.setCosts(money);
								market.setCreationDate(new Date(System.currentTimeMillis()));
								xp.sellAuctionItem(market);
								e.getPlayer().setItemInHand(null);
								Bukkit.broadcastMessage(ChatColor.GREEN + "[Auction] " + ChatColor.GRAY + xp.getUser() + " has put a new item in the auction!, item: " + item.getType().name() + " amount: " + item.getAmount() + " for " + money);
								Bukkit.broadcastMessage(ChatColor.GREEN + "[Auction] " + ChatColor.GRAY + "bid direct: " + Configuration.getAuctionConfig().getAuctionUrl() + "placebid.html?bid=" + xp.getUser() + "&productid=" + market.getProductId().toString() + "");
								Bukkit.broadcastMessage(ChatColor.GREEN + "[Auction] " + ChatColor.GRAY + "view auction profile: " + Configuration.getAuctionConfig().getAuctionUrl() + "user.html?player=" + xp.getUser() + "");
							} catch(NumberFormatException r) {
								e.getPlayer().sendMessage(ChatColor.RED + "invalid amount of money!");
							}
						} else {
							e.getPlayer().sendMessage(ChatColor.RED + "you cannot sell air...");
						}
					}
				} else if(args.length == 4) {
					if(args[1].equalsIgnoreCase("reset")) {
						if(e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
							if(xEssentials.isEssentialsPlayer(args[2])) {
								if(xEssentials.contains(args[2])) {
									xEssentialsPlayer xp = xEssentials.get(args[2]);
									try {
										xp.setAuctionPassword(Crypt.CryptToSaltedSha512(args[3]));
									} catch (NoSuchAlgorithmException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (NoSuchProviderException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									e.getPlayer().sendMessage(ChatColor.GREEN + "you have successfully changed " + xp.getUser() + " his password");
								} else {
									xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(args[2]);
									try {
										off.setAuctionPassword(Crypt.CryptToSaltedSha512(args[3]));
									} catch (NoSuchAlgorithmException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (NoSuchProviderException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									e.getPlayer().sendMessage(ChatColor.GREEN + "you have successfully changed " + off.getUser() + " his password");
								}
							} else {
								Warnings.getWarnings(e.getPlayer()).playerHasNeverPlayedBefore();
							}
						} else {
							Warnings.getWarnings(e.getPlayer()).noPermission();
						}
					}
				}
			} else {
				Warnings.getWarnings(e.getPlayer()).noPermission();
			}
			e.setCancelled(true);
		}
	}

	private String[] getArgs(String message) {
		String[] messages;
		if(message.contains(" ")) {
			messages = message.split(" ");
		} else {
			messages = new String[] {message};
		}
		return messages;
	}

}
