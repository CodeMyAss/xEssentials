package tv.mineinthebox.essentials.instances;

import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.plugin.Plugin;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.LogType;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class VaultEcoHandler implements Economy {

	 private final String name = "xEssentials-Eco";
	 private Plugin pl;
	 
	 
	 public VaultEcoHandler(Plugin pl) {
		 this.pl = pl;
		 xEssentials.getPlugin().log("Vault has been hooked to xEssentials to support our economy system", LogType.INFO);
	 }


	    @Override
	    public boolean isEnabled() {
	        if (pl == null) {
	            return false;
	        } else {
	            return pl.isEnabled();
	        }
	    }


	    @Override
	    public String getName() {
	        return name;
	    }


	    @Override
	    public String format(double amount) {
	       DecimalFormat format = new DecimalFormat(("#,##0.00"));
	       String formatted = format.format(amount);
	       if(formatted.endsWith(".")) {
	    	   formatted = formatted.substring(0, formatted.length() - 1);
	       }
	       return formatted;
	    }


	    @Override
	    public String currencyNameSingular() {
	        return Configuration.getEconomyConfig().getCurency();
	    }


	    @Override
	    public String currencyNamePlural() {
	    	return Configuration.getEconomyConfig().getCurency();
	    }


	    @Override
	    public double getBalance(String playerName) {
	        if (xEssentials.isEssentialsPlayer(playerName)) {
	            if(xEssentials.contains(playerName)) {
	            	return xEssentials.get(playerName).getTotalEssentialsMoney();
	            } else {
	            	return xEssentials.getOfflinePlayer(playerName).getTotalEssentialsMoney();
	            }
	        } else {
	            return 0;
	        }
	    }


	    @Override
	    public EconomyResponse withdrawPlayer(String playerName, double amount) {
	        if (amount < 0) {
	            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative funds");
	        }
	        if(xEssentials.isEssentialsPlayer(playerName)) {
	        	if(xEssentials.contains(playerName)) {
	        		xEssentialsPlayer xp = xEssentials.get(playerName);
	        		if(xp.hasPlayerEnoughMoneyFromPrice(amount)) {
	        			xp.payEssentialsMoney(amount);
	        			  return new EconomyResponse(amount, xp.getTotalEssentialsMoney(), ResponseType.SUCCESS, null);
	        		} else {
	        			 return new EconomyResponse(0, xp.getTotalEssentialsMoney(), ResponseType.FAILURE, "Insufficient funds");
	        		}
	        	} else {
	        		xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(playerName);
	        		if(off.hasPlayerEnoughMoneyFromPrice(amount)) {
	        			off.payEssentialsMoney(amount);
	        			 return new EconomyResponse(amount, off.getTotalEssentialsMoney(), ResponseType.SUCCESS, null);
	        		} else {
	        			 return new EconomyResponse(0, off.getTotalEssentialsMoney(), ResponseType.FAILURE, "Insufficient funds");
	        		}
	        	}
	        } else {
	        	return new EconomyResponse(0, 0, ResponseType.FAILURE, "account doesn't exist");
	        }
	    }


	    @Override
	    public EconomyResponse depositPlayer(String playerName, double amount) {
	        if (amount < 0) {
	            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot desposit negative funds");
	        }
	        
	        if(xEssentials.isEssentialsPlayer(playerName)) {
	        	if(xEssentials.contains(playerName)) {
	        		xEssentialsPlayer xp = xEssentials.get(playerName);
	        		xp.addEssentialsMoney(amount);
	        		return new EconomyResponse(amount, xp.getTotalEssentialsMoney(), ResponseType.SUCCESS, null);
	        	} else {
	        		xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(playerName);
	        		off.addEssentialsMoney(amount);
	        		return new EconomyResponse(amount, off.getTotalEssentialsMoney(), ResponseType.SUCCESS, null);
	        	}
	        } else {
	        	return new EconomyResponse(0,0, ResponseType.FAILURE, "Cannot give money to a player which doesn't exist!");
	        }
	    }


	    @Override
	    public boolean has(String playerName, double amount) {
	        return getBalance(playerName) >= amount;
	    }


	    @Override
	    public EconomyResponse createBank(String bank, String player) {
	    	if(xEssentials.isEssentialsPlayer(player)) {
	    		xEssentialsPlayer xp = xEssentials.get(player);
	    		if(xEssentials.isBank(bank)) {
	    			return new EconomyResponse(0, xp.getTotalEssentialsMoney(), ResponseType.FAILURE, "That account already exists.");
	    		} else {
	    			xEssentials.createBank(bank, player);
	    			return new EconomyResponse(0,0,ResponseType.SUCCESS, null);
	    		}
	    	} else {
	    		xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(player);
	    		if(xEssentials.isBank(bank)) {
	    			return new EconomyResponse(0, off.getTotalEssentialsMoney(), ResponseType.FAILURE, "That account already exists.");
	    		} else {
	    			xEssentials.createBank(bank, player);
	    			return new EconomyResponse(0,0,ResponseType.SUCCESS, null);
	    		}
	    	}
	    }


	    @Override
	    public EconomyResponse deleteBank(String bank) {
	        if (xEssentials.isBank(bank)) {
	            Bank banka = xEssentials.getBank(bank);
	            banka.remove();
	            return new EconomyResponse(0, 0, ResponseType.SUCCESS, "");
	        }
	        return new EconomyResponse(0, 0, ResponseType.FAILURE, "That bank account does not exist.");
	    }


	    @Override
	    public EconomyResponse bankHas(String bank, double amount) {
	    	if(xEssentials.isBank(bank)) {
	    		Bank banka = xEssentials.getBank(bank);
	    		if(banka.getAmount() >= amount) {
	    			 return new EconomyResponse(0, amount, ResponseType.SUCCESS, "");
	    		} else {
	    			 return new EconomyResponse(0, banka.getAmount(), ResponseType.FAILURE, "The accoount does not have enough!");
	    		}
	    	}
	    	return new EconomyResponse(0, 0, ResponseType.FAILURE, "The accoount does not exist!");
	    }


	    @Override
	    public EconomyResponse bankWithdraw(String name, double amount) {
	        if (amount < 0) {
	            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative funds");
	        }


	        return withdrawPlayer(name, amount);
	    }


	    @Override
	    public EconomyResponse bankDeposit(String name, double amount) {
	        if (amount < 0) {
	            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot desposit negative funds");
	        }
	        return depositPlayer(name, amount);
	    }


	    @Override
	    public EconomyResponse isBankOwner(String name, String playerName) {
	        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "xEssentials does not support Bank owners.");
	    }


	    @Override
	    public EconomyResponse isBankMember(String name, String playerName) {
	        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "xEssentials does not support Bank members.");
	    }


	    @Override
	    public EconomyResponse bankBalance(String bank) {
	        if (!xEssentials.isBank(bank)) {
	            return new EconomyResponse(0, 0, ResponseType.FAILURE, "There is no bank account with that name");
	        } else {
	            Bank banka = xEssentials.getBank(bank);
	        	return new EconomyResponse(0, banka.getAmount(), ResponseType.SUCCESS, null);
	        }
	    }


	    @Override
	    public List<String> getBanks() {
	        throw new UnsupportedOperationException("xEssentials does not support listing of bank accounts");
	    }


	    @Override
	    public boolean hasBankSupport() {
	        return true;
	    }


	    @Override
	    public boolean hasAccount(String playerName) {
	        return xEssentials.isEssentialsPlayer(playerName);
	    }


	    @Override
	    public boolean createPlayerAccount(String playerName) {
	        if (hasAccount(playerName)) {
	            return false;
	        }
	        //creates a fake player.
	        return (new xEssentialsPlayer(playerName) instanceof xEssentialsPlayer);
	    }


		@Override
		public int fractionalDigits() {
			return -1;
		}


	    @Override
	    public boolean hasAccount(String playerName, String worldName) {
	        return hasAccount(playerName);
	    }


	    @Override
	    public double getBalance(String playerName, String world) {
	        return getBalance(playerName);
	    }


	    @Override
	    public boolean has(String playerName, String worldName, double amount) {
	        return has(playerName, amount);
	    }


	    @Override
	    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
	        return withdrawPlayer(playerName, amount);
	    }


	    @Override
	    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
	        return depositPlayer(playerName, amount);
	    }


	    @Override
	    public boolean createPlayerAccount(String playerName, String worldName) {
	        return createPlayerAccount(playerName);
	    }
}
