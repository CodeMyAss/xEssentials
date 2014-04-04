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
	
	/**
	 * @author xize
	 * @param p - player name
	 * @param amount - the amount which get removed from the bank account of this player
	 * @param withdraw money of the player
	 * @return void
	 */
	public static void withdraw(String p, Double amount) {
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		Economy econ = economyProvider.getProvider();
		econ.withdrawPlayer(p, amount);
	}
	
	/**
	 * @author xize
	 * @param p - player name
	 * @param amount - the amount which get checked if the player has enough
	 * @return Boolean
	 */
	public static boolean hasEnough(String p, Double amount) {
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		Economy econ = economyProvider.getProvider();
		return ((econ.getBalance(p) - amount) > 0.0);
	}
	
	/**
	 * @author xize
	 * @param player - the player who pays
	 * @param to - the player who receives the payment
	 * @param amount - the amount of money
	 */
	public static void pay(String player, String to, Double amount) {
		withdraw(player, amount);
		desposit(player, amount);
	}

}
