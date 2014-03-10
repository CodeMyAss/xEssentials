package tv.mineinthebox.essentials;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import tv.mineinthebox.essentials.configurations.BanConfig;
import tv.mineinthebox.essentials.configurations.BlockConfig;
import tv.mineinthebox.essentials.configurations.BroadcastConfig;
import tv.mineinthebox.essentials.configurations.ChatConfig;
import tv.mineinthebox.essentials.configurations.EntityConfig;
import tv.mineinthebox.essentials.configurations.GreylistConfig;
import tv.mineinthebox.essentials.configurations.MotdConfig;
import tv.mineinthebox.essentials.configurations.PlayerConfig;
import tv.mineinthebox.essentials.configurations.PvpConfig;
import tv.mineinthebox.essentials.configurations.RulesConfig;
import tv.mineinthebox.essentials.enums.ConfigEnum;
import tv.mineinthebox.essentials.enums.LogType;
import tv.mineinthebox.essentials.events.CustomEventHandler;
import tv.mineinthebox.essentials.events.Handler;
import tv.mineinthebox.essentials.events.customEvents.CallEssentialsBroadcastEvent;
import tv.mineinthebox.essentials.greylist.GreyListServer;

public class Configuration {

	//this will be the configs loaded in the memory
	//this will used by events and in events without instancing every time a new object this will be painfully awful in PlayerMoveEvent.
	private static final EnumMap<ConfigEnum, HashMap<String, Object>> configure = new EnumMap<ConfigEnum, HashMap<String, Object>>(ConfigEnum.class);
	private static List<String> materials = new ArrayList<String>();
	
	/**
	 * 
	 * @author xize
	 * @param checks if the config exists else create new configuration files
	 * @return void
	 * 
	 */
	public void createConfigs() {
		createEntityConfig();
		createPlayerConfig();
		createMotdConfig();
		createBanConfig();
		createBroadcastConfig();
		createChatConfig();
		createPvpConfig();
		createRulesConfig();
		createGreyListConfig();
		createBlockConfig();
		loadSystemPresets(ConfigEnum.BAN);
		loadSystemPresets(ConfigEnum.BROADCAST);
		loadSystemPresets(ConfigEnum.CHAT);
		loadSystemPresets(ConfigEnum.ENTITY);
		loadSystemPresets(ConfigEnum.MOTD);
		loadSystemPresets(ConfigEnum.PLAYER);
		loadSystemPresets(ConfigEnum.PVP);
		loadSystemPresets(ConfigEnum.RULES);
		loadSystemPresets(ConfigEnum.GREYLIST);
		loadSystemPresets(ConfigEnum.BLOCKS);
		for(Material mat : Material.values()) {
			materials.add(mat.name());
		}
	}

	private String serialize_name(String mob) {
		return mob.toString().toLowerCase();
	}

	private void createBlockConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "blocks.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("disable-bedrock-place", false);
				con.set("disable-bedrock-break", false);
				con.set("notify-admin-on-block-break.enable", false);
				con.set("notify-admin-on-block-break.message", "&2%PLAYER% &7has tried to break &2%BLOCK%&7 at &2%LOCATION%");
				String[] blocks = {Material.GLASS.name(), Material.DIAMOND_ORE.name(), "2:0", Material.COBBLESTONE.name()+":2"};
				con.set("notify-admin-on-block-break.blocks", Arrays.asList(blocks).toArray());
				con.set("notify-admin-on-item-use.enable", false);
				con.set("notify-admin-on-item-use.message", "&2%PLAYER% &7has tried to use &2%ITEM% &7at &2%LOCATION%");
				String[] items = {Material.FLINT_AND_STEEL.name(), Material.FIREBALL.name(), Material.FIRE.name()};
				con.set("notify-admin-on-item-use.items", Arrays.asList(items).toArray());
				con.set("block.blacklist.enable", false);
				con.set("block.blacklist.blocks", blocks);
				con.set("item.blacklist.enable", false);
				con.set("item.blacklist.items", items);
				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createEntityConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "entity.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("disable-weather", false);
				con.set("disable-firespread", false);
				con.set("disable-explosion", false);
				con.set("disable-firework", false);
				con.set("disable-wither-grief", false);
				con.set("disable-enderman-grief", false);
				con.set("disable-enderdragon-grief", false);
				con.set("zombie-custom-aggro.enable", false);
				con.set("zombie-custom-aggro.range", 10);
				con.set("disable-spawneggs", false);
				con.set("log.spawnEggs", false);
				con.set("use-criticals", false);
				con.set("Remove-Flying-Projectiles-On-ChunkLoad", false);
				for(EntityType entity : EntityType.values()) {
					if(entity.isAlive()) {
						if(entity != EntityType.PLAYER) {
							con.set("mobs.allowToSpawn." + serialize_name(entity.name()), true);
						}
					}
				}
				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void createGreyListConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "greylist.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("greylist.enable", false);
				con.set("greylist.serverport", 8001);
				con.set("greylist.group", "citizen");
				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void createPlayerConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "player.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("useSeperatedInventorys", false);
				con.set("save-playerInventory", false);
				con.set("godmode-inAfk", false);
				con.set("steve-hurt-sound.enable", false);
				con.set("entitysCanUseHeadOnPlayerDeath", false);
				con.set("enable-realistic-glass", false);
				con.set("canDefaultUseMoreHomes", false);
				con.set("maxHomes", 3);
				con.set("broadcast-achievements", false);
				con.set("auto-refresh-anvil", false);
				con.set("KeepInventoryOnDeath", false);
				con.set("CancelHunger", false);
				con.set("PortalCreation.DisableCustomSizes", false);
				con.set("PortalCreation.DisablePortalCreation", false);
				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void createMotdConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "motd.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				ArrayList<String> list = new ArrayList<String>();
				list.add("message 1");
				list.add("message 2");
				con.set("motd.normal.enable", false);
				con.set("motd.random.enable", false);
				con.set("motd.messages", list);
				con.set("motd.message", "default motd for xEssentials");
				con.save(f);
				list.clear();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void createBanConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "ban.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("ban.system.enablePwnAgeProtection", false);
				con.set("ban.system.enableAntiFloodSpam", false);
				con.set("ban.system.enableHumanSpamProtection", false);
				con.set("ban.system.PwnAgeProtection.banMessage", "[PwnAge] spam hacks");
				con.set("ban.system.AntiFloodSpam.banMessage", "[FloodSpam] spam hacks");
				con.set("ban.system.HumanSpamProtection.banMessage", "[normal spam] dont spam!");
				con.set("ban.system.showAlternateAccounts", false);
				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void createBroadcastConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "broadcast.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				ArrayList<String> list = new ArrayList<String>();
				list.add("&ebroadcast 1");
				list.add("&ebroadcast 2");
				list.add("&ebroadcast 3");
				con.set("broadcast.enable", false);
				con.set("broadcast.prefix", "&e[broadcast]");
				con.set("broadcast.suffix", "&2");
				con.set("broadcast.messages", list);
				con.save(f);
				list.clear();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void createChatConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "chat.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("chat.enable.playerHighlights", false);
				con.set("chat.enable.smilleys", false);
				con.set("chat.enable.hashtag", "&e@");
				con.set("chat.enable.antiAddvertise", false);
				con.set("rss.useRssBroadcast", false);
				con.set("rss.useRssUrl", "https://mojang.com/feed/");
				con.set("checkMojangStatus", false);
				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void createPvpConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "pvp.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("disable-pvp", false);
				con.set("createClientSideGraveyard", false);
				con.set("killBounty.enable", false);
				con.set("killBounty.earn", 5.0);
				con.set("npcReplaceLoggers", false);
				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void createRulesConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "rules.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				ArrayList<String> list = new ArrayList<String>();
				list.add("1. No griefing, also random non generated blocks or farms are seen as considered griefing.");
				list.add("2. No asking for operator or any other rank, it's on ours to decide whenever we want.");
				list.add("3. Do not cheat, hacked clients, dupes, spambots, xray mods or xray texture packs are forbidden.");
				list.add("4. addvertising is forbidden, it's a silly thing, also it means you are not allowed to addvertise our server on others");
				list.add("5. swearing is allowed, but limited you are not allowed to offend players or talk about racism/religions or the server it self");
				list.add("6. whining is not allowed, asking items, or complaining about warps, all are falling under the justification of whining use the forums instead ;)");
				con.set("rules.prefix", "&2[Rules]");
				con.set("rules.suffix", "&7");
				con.set("rules.messages", list);
				con.save(f);
				list.clear();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//no worries me is gonna create a wrap so we can use its power without knowing every key:D
	private void loadSystemPresets(ConfigEnum cfg) {
		if(cfg == ConfigEnum.GREYLIST) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "greylist.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("enable", con.getBoolean("greylist.enable"));
			hash.put("port", con.getInt("greylist.serverport"));
			hash.put("group", con.getString("greylist.group"));
			configure.put(ConfigEnum.GREYLIST, hash);
		} else if(cfg == ConfigEnum.BAN) {
			try {
				File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "ban.yml");
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				HashMap<String, Object> hash = new HashMap<String, Object>();
				hash.put("enablePwnAgeProtection", con.getBoolean("ban.system.enablePwnAgeProtection"));
				hash.put("enableAntiFloodSpam", con.getBoolean("ban.system.enableAntiFloodSpawm"));
				hash.put("enableHumanSpamProtection", con.getBoolean("ban.system.enableHumanSpamProtection"));
				hash.put("PwnAgeProtectionBanMessage", con.getString("ban.system.PwnAgeProtection.banMessage"));
				hash.put("AntiFloodSpamBanMessage", con.getString("ban.system.AntiFloodSpam.banMessage"));
				hash.put("HumanSpamProtectionBanMessage", con.getString("ban.system.HumanSpamProtection.banMessage"));
				hash.put("showAlternateAccounts", con.getBoolean("ban.system.showAlternateAccounts"));
				configure.put(ConfigEnum.BAN, hash);

			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if(cfg == ConfigEnum.BROADCAST) {
			try {
				File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "broadcast.yml");
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				HashMap<String, Object> hash = new HashMap<String, Object>();
				hash.put("enable", con.getBoolean("broadcast.enable"));
				hash.put("prefix", con.getString("broadcast.prefix"));
				hash.put("suffix", con.getString("broadcast.suffix"));
				hash.put("messages", con.getStringList("broadcast.messages"));
				configure.put(ConfigEnum.BROADCAST, hash);

			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if(cfg == ConfigEnum.CHAT) {
			try {
				File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "chat.yml");
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				HashMap<String, Object> hash = new HashMap<String, Object>();
				hash.put("enable", con.getBoolean("chat.enable.playerHighlights"));
				hash.put("smilleysEnable", con.getBoolean("chat.enable.smilleys"));
				hash.put("hashTag", con.getString("chat.enable.hashtag"));
				hash.put("antiAddvertiseEnabled", con.getBoolean("chat.enable.antiAddvertise"));
				hash.put("RssEnabled", con.getBoolean("rss.useRssBroadcast"));
				hash.put("RssUrl", con.getString("rss.useRssUrl"));
				hash.put("MojangStatus", con.getBoolean("checkMojangStatus"));
				configure.put(ConfigEnum.CHAT, hash);

			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if(cfg == ConfigEnum.ENTITY) {
			try {
				File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "entity.yml");
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				HashMap<String, Object> hash = new HashMap<String, Object>();
				hash.put("disableWeather", con.getBoolean("disable-weather"));
				hash.put("disableFirespread", con.getBoolean("disable-firespread"));
				hash.put("disableExplosion", con.getBoolean("disable-explosion"));
				hash.put("disableFirework", con.getBoolean("disable-firework"));
				hash.put("disableWitherGrief", con.getBoolean("disable-wither-grief"));
				hash.put("disableEndermanGrief", con.getBoolean("disable-enderman-grief"));
				hash.put("disableEnderdragonGrief", con.getBoolean("disable-enderdragon-grief"));
				hash.put("zombieCustomAggroEnable", con.getBoolean("zombie-custom-aggro.enable"));
				hash.put("zombieCustomAggroRange", con.getInt("zombie-custom-aggro.range"));
				hash.put("disableSpawnEggs", con.getBoolean("disable-spawneggs"));
				hash.put("logSpawnEggs", con.getBoolean("log.spawnEggs"));
				hash.put("useCriticals", con.getBoolean("use-criticals"));
				hash.put("AntiFireball", con.getBoolean("Remove-Flying-Projectiles-On-ChunkLoad"));
				HashMap<String, Boolean> entitys = new HashMap<String, Boolean>();
				for(String key : con.getConfigurationSection("mobs.allowToSpawn").getKeys(true)) {
					entitys.put(key, con.getBoolean("mobs.allowToSpawn."+key));
				}
				hash.put("allowToSpawn", entitys);
				configure.put(ConfigEnum.ENTITY, hash);

			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if(cfg == ConfigEnum.MOTD) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "motd.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("NormalEnable", con.getBoolean("motd.normal.enable"));
			hash.put("RandomEnable", con.getBoolean("motd.random.enable"));
			hash.put("messages", con.getStringList("motd.messages"));
			hash.put("message", con.getString("motd.message"));
			configure.put(ConfigEnum.MOTD, hash);

		} else if(cfg == ConfigEnum.PLAYER) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "player.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("useSeperatedInventorys", con.getBoolean("useSeperatedInventorys"));
			hash.put("savePlayerInventory", con.getBoolean("save-playerInventory"));
			hash.put("godmodeInAfk", con.getBoolean("godmode-inAfk"));
			hash.put("steve-hurt-sound", con.getBoolean("steve-hurt-sound.enable"));
			hash.put("entitysCanUseHeadOnPlayerDeath", con.getBoolean("entitysCanUseHeadOnPlayerDeath"));
			hash.put("enableRealisticGlass", con.getBoolean("enable-realistic-glass"));
			hash.put("canDefaultUseMoreHomes", con.getBoolean("canDefaultUseMoreHomes"));
			hash.put("maxHomes", con.getInt("maxHomes"));
			hash.put("PlayerAchievements", con.getBoolean("broadcast-achievements"));
			hash.put("anvil", con.getBoolean("auto-refresh-anvil"));
			hash.put("keepinv", con.getBoolean("KeepInventoryOnDeath"));
			hash.put("hunger", con.getBoolean("CancelHunger"));
			hash.put("DisableCustomSize", con.getBoolean("PortalCreation.DisableCustomSizes"));
			hash.put("DisablePortals", con.getBoolean("PortalCreation.DisablePortalCreation"));
			configure.put(ConfigEnum.PLAYER, hash);

		} else if(cfg == ConfigEnum.PVP) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "pvp.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("disablePvp", con.getBoolean("disable-pvp"));
			hash.put("createClientSideGraveyard", con.getBoolean("createClientSideGraveyard"));
			hash.put("killBountyEnable", con.getBoolean("killBounty.enable"));
			hash.put("killBountyEarn", con.getDouble("killBounty.earn"));
			hash.put("npcReplaceLoggers", con.getBoolean("npcReplaceLoggers"));
			configure.put(ConfigEnum.PVP, hash);

		} else if(cfg == ConfigEnum.RULES) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "rules.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("prefix", con.getString("rules.prefix"));
			hash.put("suffix", con.getString("rules.suffix"));
			hash.put("rules", con.getStringList("rules.messages"));
			configure.put(ConfigEnum.RULES, hash);

		} else if(cfg == ConfigEnum.BLOCKS) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "blocks.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("DisableBedrockPlace", con.getBoolean("disable-bedrock-place"));
			hash.put("DisableBedrockBreak", con.getBoolean("disable-bedrock-break"));
			hash.put("NotifyOnBreak", con.getBoolean("notify-admin-on-block-break.enable"));
			hash.put("NotifyOnBreakMessage", con.getString("notify-admin-on-block-break.message"));
			hash.put("NotifyOnBreakBlocks", serializeItemList(con.getStringList("notify-admin-on-block-break.blocks")));
			hash.put("NotifyOnItemUse", con.getBoolean("notify-admin-on-item-use.enable"));
			hash.put("NotifyOnItemUseMessage", con.getString("notify-admin-on-item-use.message"));
			hash.put("NotifyOnItemUseItems", serializeItemList(con.getStringList("notify-admin-on-item-use.items")));
			hash.put("isBlockBlackListEnabled", con.getBoolean("block.blacklist.enable"));
			hash.put("getBlockBlacklist", serializeItemList(con.getStringList("block.blacklist.blocks")));
			hash.put("getItemBlackListEnabled", con.getBoolean("item.blacklist.enable"));
			hash.put("getItemBlacklist", serializeItemList(con.getStringList("item.blacklist.items")));
			configure.put(ConfigEnum.BLOCKS, hash);
		}
	}
	
	/**
	 * @author xize
	 * @param returns a new list with updated materials against data values
	 * @return List<String>()
	 */
	@SuppressWarnings("deprecation")
	private static List<String> serializeItemList(List<String> list) {
		List<String> updatedMaterialList = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++) {
			String name = list.get(i);
			if(isNumberic(name)) {
				String[] data = name.split(":");
				int dataValue = Integer.parseInt(data[0]);
				Short subData = Short.parseShort(data[1]);
				Material mat = Material.getMaterial(dataValue);
				updatedMaterialList.add(mat.name()+":"+subData);
			} else {
				if(name.contains(":")) {
					String[] names = name.split(":");
					if(isValidMaterial(names[0])) {
						try {
							Short dura = Short.parseShort(names[1]);
							Material mat = Material.getMaterial(names[0].toUpperCase());
							updatedMaterialList.add(mat.name()+":"+dura);
						} catch(NumberFormatException e) {
							xEssentials.getPlugin().log("invalid durabillity found in blocks.yml! name: " + names[1], LogType.SEVERE);
						}
					}
				} else {
					if(isValidMaterial(name)) {
						Material mat = Material.getMaterial(name.toUpperCase());
						updatedMaterialList.add(mat.name()+":"+0);
					}	
				}
			}
		}
		return updatedMaterialList;
	}
	
	private static boolean isValidMaterial(String s) {
		try {
			Material mat = Material.getMaterial(s.toUpperCase());
			if(mat != null) {
				return true;
			}
		} catch(IllegalArgumentException e) {
			xEssentials.getPlugin().log("invalid Material name in blocks.yml! called: " + s, LogType.SEVERE);
		}
		return false;
	}
	
	private static Boolean isNumberic(String s) {
		if(s.contains(":")) {
			String[] split = s.split(":");
			try {
				Integer data = Integer.parseInt(split[0]);
				Short sub = Short.parseShort(split[1]);
				if(data != null && sub != null) {
					return true;
				} else {
					return false;
				}
			} catch(NumberFormatException e) {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * @author xize
	 * @param returns a list with Material names, this can be used for auto complete functions in commands
	 * @return List<String>()
	 */
	public static List<String> getMaterials() {
		return materials;
	}

	/**
	 * @author xize
	 * @param type
	 * @param hashName
	 * @param returns the value per category so we can easier maintain this in the feature.
	 * @return Object
	 */
	public static Object getConfigValue(ConfigEnum type, String hashName) {
		return configure.get(type).get(hashName);
	}

	/**
	 * @author xize
	 * @param set a config value in the memory, this is deprecated as we using our update system.
	 * @return void
	 * @deprecated
	 */
	public static void setConfigValue(ConfigEnum type, String hashName, Object value) {
		configure.get(type).put(hashName, value);
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for bans
	 * @return banConfig
	 */
	public static BanConfig getBanConfig() {
		BanConfig config = new BanConfig();
		return config;
	}

	/**
	 * @author xize
	 * @param get the GreyList config
	 * @return GreylistConfig
	 */
	public static GreylistConfig getGrayListConfig() {
		GreylistConfig grey = new GreylistConfig();
		return grey;
	}
	
	/**
	 * @author xize
	 * @param returns the BlockConfig
	 * @return BlockConfig
	 */
	public static BlockConfig getBlockConfig() {
		BlockConfig config = new BlockConfig();
		return config;
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for broadcasts
	 * @return broadcastConfig
	 */
	public static BroadcastConfig getBroadcastConfig() {
		BroadcastConfig config = new BroadcastConfig();
		return config;
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for chat
	 * @return chatConfig
	 */
	public static ChatConfig getChatConfig() {
		ChatConfig config = new ChatConfig();
		return config;
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for entitys
	 * @return enityConfig
	 */
	public static EntityConfig getEntityConfig() {
		EntityConfig config = new EntityConfig();
		return config;
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for motd
	 * @return motdConfig
	 */
	public static MotdConfig getMotdConfig() {
		MotdConfig config = new MotdConfig();
		return config;
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for all players
	 * @return playerConfig
	 */
	public static PlayerConfig getPlayerConfig() {
		PlayerConfig config = new PlayerConfig();
		return config;
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for pvp
	 * @return pvpConfig
	 */
	public static PvpConfig getPvpConfig() {
		PvpConfig config = new PvpConfig();
		return config;
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for rules
	 * @return rulesConfig
	 */
	public static RulesConfig getRulesConfig() {
		RulesConfig config = new RulesConfig();
		return config;
	}
	
	public static boolean isSilenceToggled = false;

	public boolean reload() {
		Handler handler = new Handler();
		CustomEventHandler customhandler = new CustomEventHandler();
		handler.stop();
		//clear responsible from the deepest tree in the HashMap in case things could get persistent in the jvm things need to be better safe than not.
		CallEssentialsBroadcastEvent.stop();
		for(ConfigEnum aEnum : ConfigEnum.values()) {
			configure.get(aEnum).clear();
		}
		configure.clear();
		for(ConfigEnum aEnum : ConfigEnum.values()) {
			loadSystemPresets(aEnum);
		}
		if(xEssentials.server != null) {
			if(xEssentials.server.isRunning()) {
				xEssentials.server.disable();
			}
		}
		createConfigs();
		if(Configuration.getGrayListConfig().isEnabled()) {
			xEssentials.server = new GreyListServer(Configuration.getGrayListConfig().getPort());
			xEssentials.server.createServer();
		}
		xEssentials.reloadPlayerBase(); 
		handler.start();
		customhandler.startCustomEvents();

		return true;
	}

	/**
	 * @author xize
	 * @param reloads the configuration included with event checks
	 * @return boolean
	 */
	public static boolean reloadConfiguration() {
		Configuration conf = new Configuration();
		return conf.reload();
	}
}
