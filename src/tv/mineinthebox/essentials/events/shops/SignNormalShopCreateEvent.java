package tv.mineinthebox.essentials.events.shops;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.utils.ShopSign;

public class SignNormalShopCreateEvent implements Listener {
	
	@EventHandler
	public void onSignCreate(SignChangeEvent e) {
		if(e.getLine(0).equalsIgnoreCase("[shop]")) {
			if(e.getPlayer().hasPermission(PermissionKey.SIGN_SHOP.getPermission())) {
				int amount = ShopSign.getNumberFromString(e.getLine(1));
				if(!(amount > 64 || amount == 0)) {
					if(ShopSign.validateBuyAndSell(e.getLine(2))) {
						if(ShopSign.isValidMaterial(e.getLine(3))) {
							if(ShopSign.isAttachedOnChest(e.getBlock())) {
								e.setLine(0, e.getPlayer().getName());
								e.setLine(3, ShopSign.getItemFromSign(e.getLine(3).toUpperCase()));
								e.getPlayer().sendMessage(ChatColor.GREEN + "you successfully created a shop sign!");
							} else {
								e.getBlock().breakNaturally();
								e.getPlayer().sendMessage(ChatColor.RED + "there whas no chest found at the location of the sign!");
							}
						} else {
							e.getBlock().breakNaturally();
							e.getPlayer().sendMessage(ChatColor.RED + "the material you put on the shop sign is invalid!");
						}
					} else {
						e.getBlock().breakNaturally();
						e.getPlayer().sendMessage(ChatColor.RED + "buy and sell is invalid!");
					}
				} else {
					e.getBlock().breakNaturally();
					e.getPlayer().sendMessage(ChatColor.RED + "you have inserted a invalid amount of items on the sign!");
				}
			} else {
				e.getBlock().breakNaturally();
				e.getPlayer().sendMessage(ChatColor.RED + "you are not allowed to make this kind of signs!");
			}
		}
	}

}
