package tv.mineinthebox.essentials.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;

public class ShopSign {

	public static Chest getChestFromSign(Block sign) {
		//TO-DO adding lwc, and lockette support, so a player cannot put a shop sign on a chest he don't owns.
		BlockFace[] faces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
		for(BlockFace face : faces) {
			if(sign.getRelative(face).getType() == Material.CHEST) {
				return (Chest) sign.getRelative(face).getState();
			}
		}
		return null;
	}
	
	/**
	 * @author xize
	 * @param returns the buy price of the sign
	 * @return Double
	 */
	public static Double getBuyPrice(String s) {
		if(s.contains(" : ")) {
			String[] split = s.split(" : ");
			String buy = split[0];
			return Double.parseDouble(buy.replace("b ", ""));
		} else {
			return Double.parseDouble(s.replace("b ", ""));
		}
	}
	
	/**
	 * @author xize
	 * @param returns the sell price of the sign
	 * @return Double
	 */
	public static Double getSellPrice(String s) {
		if(s.contains(" : ")) {
			String[] split = s.split(" : ");
			String sell = split[1];
			return Double.parseDouble(sell.replace("s ", ""));
		} else {
			return Double.parseDouble(s.replace("s ", ""));
		}
	}
	
	/**
	 * @author xize
	 * @param returns the Material and sub data value for better serializing
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public static String getItemFromSign(String s) {
		if(s.contains(":")) {
			String[] split = s.split(":");
			String data = split[0];
			String subdata = split[1];
			if(isNumber(data)) {
				Material mat = Material.getMaterial(Integer.parseInt(data));
				return (mat.name()+":"+subdata);
			} else {
				Material mat = Material.getMaterial(data.toUpperCase());
				return (mat.name()+":"+subdata);
			}
		} else {
			if(isNumber(s)) {
				Material mat = Material.getMaterial(Integer.parseInt(s));
				return (mat.name()+":0");
			} else {
				Material mat = Material.getMaterial(s.toUpperCase());
				return (mat.name()+":0");
			}
		}
	}

	/**
	 * @author xize
	 * @param returns true whenever the sign is close enough to a chest
	 * @return Boolean
	 */
	public static boolean isAttachedOnChest(Block sign) {
		//TO-DO adding lwc, and lockette support, so a player cannot put a shop sign on a chest he don't owns.
		BlockFace[] faces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
		for(BlockFace face : faces) {
			if(sign.getRelative(face).getType() == Material.CHEST) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param returns true if the material line of the sign is a valid Material.
	 * @return Boolean
	 */
	@SuppressWarnings("deprecation")
	public static boolean isValidMaterial(String s) {
		if(s.contains(":")) {
			String[] split = s.split(":");
			String data = split[0];
			String subdata = split[1];
			if(isNumber(data)) {
				Material mat = Material.getMaterial(Integer.parseInt(data));
				return (mat != null && isNumber(subdata) && mat != Material.AIR);
			} else {
				Material mat = Material.getMaterial(data.toUpperCase());
				return (mat != null && mat != Material.AIR && isNumber(subdata));
			}
		} else {
			if(isNumber(s)) {
				Material mat = Material.getMaterial(Integer.parseInt(s));
				return (mat != null && mat != Material.AIR);
			} else {
				Material mat = Material.getMaterial(s.toUpperCase());
				return (mat != null && mat != Material.AIR);
			}
		}
	}

	/**
	 * @author xize
	 * @param validate the buy and sell on the shop sign, if invalid it returns false
	 * @return Boolean
	 */
	public static boolean validateBuyAndSell(String s) {
		if(s.contains(" : ")) {
			String[] split = s.split(" : ");
			String buy = split[0];
			String sell = split[1];
			if(buy.toLowerCase().startsWith("b ") && sell.toLowerCase().startsWith("s ")) {
				if(isNumber(buy.toLowerCase().replace("b ", "")) && isNumber(sell.toLowerCase().replace("s ", ""))) {
					return true;
				}
			}
		} else {
			if(s.startsWith("b ") || s.startsWith("s ")) {
				if(isNumber(s.toLowerCase().replace("b ", ""))) {
					return true;
				} else if(isNumber(s.toLowerCase().replace("s ", ""))) {
					return true;	
				}
			}
		}
		return false;
	}

	/**
	 * @author xize
	 * @param convert the string into a number
	 * @return Integer
	 */
	public static int getNumberFromString(String s) {
		try {
			Integer i = Integer.parseInt(s);
			if(i != null) {
				return i;
			}
		} catch(NumberFormatException e) {
			return 0;
		}
		return 0;
	}

	/**
	 * @author xize
	 * @param returns true whenever the line number seems to be a number
	 * @return Boolean
	 */
	public static boolean isNumber(String s) {
		try {
			Integer i = Integer.parseInt(s);
			if(i != null) {
				return true;
			}
		} catch(NumberFormatException e) {
			return false;
		}
		return false;
	}

}
