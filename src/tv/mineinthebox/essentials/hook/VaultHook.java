package tv.mineinthebox.essentials.hook;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
	
	/**
	 * @author xize
	 * @param p - player
	 * @param amount - currency
	 * @param gives the player some money!
	 * @return void
	 */
	public static void desposit(Player p, Double amount) {
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		Economy econ = economyProvider.getProvider();
		econ.depositPlayer(p.getName(), amount);
	}
	
	/**
	 * @author xize
	 * @param p - player name
	 * @param amount - currency
	 * @param gives the player some money!
	 * @return void
	 */
	public static void desposit(String p, Double amount) {
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		Economy econ = economyProvider.getProvider();
		econ.depositPlayer(p, amount);
	}

}
