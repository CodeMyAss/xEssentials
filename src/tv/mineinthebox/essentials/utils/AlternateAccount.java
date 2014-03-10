package tv.mineinthebox.essentials.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.BanType;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;


public class AlternateAccount {
	
	private String[] args;
	private xEssentialsOfflinePlayer[] off;
	
	public AlternateAccount(xEssentialsPlayer xp) {
		List<String> list = new ArrayList<String>();
		List<xEssentialsOfflinePlayer> list2 = new ArrayList<xEssentialsOfflinePlayer>();
		for(xEssentialsOfflinePlayer offs : xEssentials.getOfflinePlayers()) {
			if(!xp.getUser().equalsIgnoreCase(offs.getUser())) {
				if(xp.getIp().equalsIgnoreCase(offs.getIp())) {
					list.add(offs.getUser());
					list2.add(offs);
				}
			}
		}
		String[] alts = list.toArray(new String[list.size()]);
		xEssentialsOfflinePlayer[] alts2 = list2.toArray(new xEssentialsOfflinePlayer[list2.size()]);
		this.off = alts2;
		this.args = alts;
	}
	
	public AlternateAccount(xEssentialsOfflinePlayer xp) {
		List<String> list = new ArrayList<String>();
		List<xEssentialsOfflinePlayer> list2 = new ArrayList<xEssentialsOfflinePlayer>();
		for(xEssentialsOfflinePlayer offs : xEssentials.getOfflinePlayers()) {
			if(!xp.getUser().equalsIgnoreCase(offs.getUser())) {
				if(xp.getIp().equalsIgnoreCase(offs.getIp())) {
					list.add(offs.getUser());
					list2.add(offs);
				}
			}
		}
		String[] alts = list.toArray(new String[list.size()]);
		xEssentialsOfflinePlayer[] alts2 = list2.toArray(new xEssentialsOfflinePlayer[list2.size()]);
		this.off = alts2;
		this.args = alts;
	}
	
	/**
	 * @author xize
	 * @param gets the alternate names as Array
	 * @return String[]
	 */
	public String[] getAltNames() {
		return args;
	}
	
	/**
	 * @author xize
	 * @param gets the alternate names as a single unmodified string
	 * @return String
	 */
	public String getAltsToString() {
		String array = Arrays.toString(getAltNames()).replace("[", "").replace("]", "");
		return array;
	}
	
	/**
	 * @author xize
	 * @param gets the alt list in a stylish way!
	 * @return String
	 */
	public String getAltsDetailed() {
		List<String> list = new ArrayList<String>();
		for(xEssentialsOfflinePlayer offliner : off) {
			if(offliner.isPermBanned()) {
				list.add(BanType.BANNED.getPrefix()+ChatColor.GRAY+offliner.getUser());
			} else if(offliner.isTempBanned()) {
				list.add(BanType.TEMPBANNED.getPrefix()+ChatColor.GRAY+offliner.getUser());
			} else if(offliner.isBannedBefore()) {
				list.add(BanType.BANNED_BEFORE.getPrefix()+ChatColor.GRAY+offliner.getUser());
			} else {
				list.add(BanType.NEVER_BANNED.getPrefix()+ChatColor.GRAY+offliner.getUser());
			}
		}
		String[] args = list.toArray(new String[list.size()]);
		return ChatColor.GRAY + Arrays.toString(args).replace("[", "").replace("]", "");
	}
}
