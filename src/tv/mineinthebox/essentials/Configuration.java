package tv.mineinthebox.essentials;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import tv.mineinthebox.essentials.commands.CommandList;
import tv.mineinthebox.essentials.commands.command;
import tv.mineinthebox.essentials.configurations.BanConfig;
import tv.mineinthebox.essentials.configurations.BlockConfig;
import tv.mineinthebox.essentials.configurations.BroadcastConfig;
import tv.mineinthebox.essentials.configurations.ChatConfig;
import tv.mineinthebox.essentials.configurations.CommandConfig;
import tv.mineinthebox.essentials.configurations.EconomyConfig;
import tv.mineinthebox.essentials.configurations.EntityConfig;
import tv.mineinthebox.essentials.configurations.GreylistConfig;
import tv.mineinthebox.essentials.configurations.KitConfig;
import tv.mineinthebox.essentials.configurations.MotdConfig;
import tv.mineinthebox.essentials.configurations.PlayerConfig;
import tv.mineinthebox.essentials.configurations.PortalConfig;
import tv.mineinthebox.essentials.configurations.ProtectionConfig;
import tv.mineinthebox.essentials.configurations.PvpConfig;
import tv.mineinthebox.essentials.configurations.RulesConfig;
import tv.mineinthebox.essentials.configurations.ShopConfig;
import tv.mineinthebox.essentials.enums.ConfigType;
import tv.mineinthebox.essentials.enums.LogType;
import tv.mineinthebox.essentials.enums.MinigameType;
import tv.mineinthebox.essentials.events.CustomEventHandler;
import tv.mineinthebox.essentials.events.Handler;
import tv.mineinthebox.essentials.events.customEvents.CallEssentialsBroadcastEvent;
import tv.mineinthebox.essentials.instances.Kit;
import tv.mineinthebox.essentials.interfaces.Minigame;
import tv.mineinthebox.essentials.utils.ProtectionDB;

public class Configuration {

	//this will be the configs loaded in the memory
	//this will used by events and in events without instancing every time a new object this will be painfully awful in PlayerMoveEvent.
	private final static EnumMap<ConfigType, HashMap<String, Object>> configure = new EnumMap<ConfigType, HashMap<String, Object>>(ConfigType.class);
	private final static EnumMap<MinigameType, HashMap<String, Minigame>> minigames = new EnumMap<MinigameType, HashMap<String, Minigame>>(MinigameType.class);

	//use these configuration files as a singleton, this prevent smilliar problems for inside the jvm.
	//one of these problems is that CreatureSpawnEvent runs at every tick, its bad to use this for a factory method
	//which only returns a new instance of that config instead we only instance it as it isn't,
	private static BanConfig banconfig;
	private static BlockConfig blockconfig;
	private static BroadcastConfig broadcastconfig;
	private static ChatConfig chatconfig;
	private static CommandConfig commandconfig;
	private static EconomyConfig economyconfig;
	private static EntityConfig entityconfig;
	private static GreylistConfig greylistconfig;
	private static KitConfig kitconfig;
	private static MotdConfig motdconfig;
	private static PlayerConfig playerconfig;
	private static PortalConfig portalconfig;
	private static ProtectionConfig protectionconfig;
	private static PvpConfig pvpconfig;
	private static RulesConfig rulesconfig;
	private static ShopConfig shopconfig;



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
		createKitConfig();
		createCommandConfig();
		createEconomyConfig();
		createShopConfig();
		createProtectionConfig();
		createPortalConfig();
		loadSystemPresets(ConfigType.BAN);
		loadSystemPresets(ConfigType.BROADCAST);
		loadSystemPresets(ConfigType.CHAT);
		loadSystemPresets(ConfigType.ENTITY);
		loadSystemPresets(ConfigType.MOTD);
		loadSystemPresets(ConfigType.PLAYER);
		loadSystemPresets(ConfigType.PVP);
		loadSystemPresets(ConfigType.RULES);
		loadSystemPresets(ConfigType.GREYLIST);
		loadSystemPresets(ConfigType.BLOCKS);
		loadSystemPresets(ConfigType.KITS);
		loadSystemPresets(ConfigType.COMMAND);
		loadSystemPresets(ConfigType.ECONOMY);
		loadSystemPresets(ConfigType.SHOP);
		loadSystemPresets(ConfigType.PROTECTION);
		loadSystemPresets(ConfigType.PORTAL);
		for(Material mat : Material.values()) {
			materials.add(mat.name());
		}
	}

	private String serialize_name(String mob) {
		return mob.toString().toLowerCase();
	}

	private void createPortalConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "portal.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("portals.enable", false);
				con.set("portals.cooldown", 1000);
				World defaultWorld = Bukkit.getWorlds().get(0);
				for(String world : getWorlds()) {
					if(world.startsWith(defaultWorld.getName())) {
						con.set("portals.worlds."+world, true);
					} else {
						con.set("portals.worlds."+world, false);	
					}
				}
				con.save(f);
			} else {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				List<String> worlds = new ArrayList<String>(con.getConfigurationSection("portals.worlds").getKeys(false));
				if(worlds.size() < getWorlds().length) {
					xEssentials.getPlugin().log("New worlds found for portals!", LogType.INFO);
					for(String w : getWorlds()) {
						if(!worlds.contains(w)) {
							xEssentials.getPlugin().log("Registering world: " + w + " to false", LogType.INFO);
							con.set("portals.worlds."+w, false);
						}
					}	
					con.save(f);
				} else {
					xEssentials.getPlugin().log("No new worlds found for portals", LogType.INFO);
				}
				if(con.getBoolean("portals.enable")) {
					xEssentials.getPlugin().log("Loading worlds for portals!", LogType.INFO);
					for(String key : worlds) {
						if(con.getBoolean("portals.worlds."+key)) {
							World w = Bukkit.getWorld(key);
							if(!(w instanceof World)) {
								xEssentials.getPlugin().log("Loading world " + key, LogType.INFO);
								//Bukkit.createWorld(new WorldCreator(key));
								xEssentials.getPlugin().log("successfully loaded world " + key, LogType.INFO);
							} else {
								xEssentials.getPlugin().log(key + " whas already loaded, probably default world? " + key, LogType.INFO);
							}
						} else {
							xEssentials.getPlugin().log("World " + key + " is not loaded, disabled from portal config.", LogType.INFO);
						}
					}
				} else {
					xEssentials.getPlugin().log("portals has been disabled, we don't load other worlds now.\nonly update new worlds in the config.", LogType.INFO);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void createProtectionConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "protection.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("protection.enable", true);
				con.set("protection.protect.signs", true);
				con.set("protection.protect.chests", true);
				con.set("protection.protect.furnace", true);
				con.set("protection.protect.jukebox", true);
				con.set("protection.protect.dispenser", true);
				con.set("protection.message.disallow", "&cthis %BLOCK% has been protected by a spell");
				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void createShopConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "shops.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("shop.enable", true);
				con.set("shop.shop-admin-prefix", "Admin Shop");
				con.set("shop.disable-messages", false);
				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
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

	private void createEconomyConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "economy.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				con.set("economy.enable", true);
				con.set("economy.currency", "$");
				con.set("economy.startersAmount", 10.0);
				con.save(f);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void createCommandConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "commands.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				FileConfigurationOptions opt = con.options();
				opt.header("here you can specify whenever a command should be unregistered or not\nfor example you got a other plugin which has the /home command this would basicly conflict with xEssentials\nhereby you can change the behaviour by unregistering xEssentials commands here.");
				CommandList list = new CommandList();
				List<String> commands = new ArrayList<String>(Arrays.asList(list.getAllCommands));

				//blacklist, this will be handle by the configuration it self.
				commands.remove("money");
				commands.remove("cprivate");
				commands.remove("cmodify");
				commands.remove("cremove");
				commands.remove("portals");

				for(String command : commands) {
					con.set("command."+command+".enable", true);
				}
				con.save(f);
			} else {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				CommandList commandlist = new CommandList();
				List<String> commands = new ArrayList<String>(Arrays.asList(commandlist.getAllCommands));

				//blacklist, this will be handle by the configuration it self.
				commands.remove("money");
				commands.remove("cprivate");
				commands.remove("cmodify");
				commands.remove("cremove");
				commands.remove("portals");

				List<String> orginal = Arrays.asList(con.getConfigurationSection("command").getKeys(false).toArray(new String[0]));

				if(commands.size() != orginal.size()) {
					xEssentials.getPlugin().log("new commands detected!, adding them right now inside the command config!", LogType.INFO);
					for(String cmd : commands) {
						if(!orginal.contains(cmd)) {
							xEssentials.getPlugin().log("registering new command: " + cmd + " in commands.yml", LogType.INFO);
							con.set("command."+cmd+".enable", true);
						}
					}
					con.save(f);
				} else {
					xEssentials.getPlugin().log("there where no newer commands found to be added in commands.yml", LogType.INFO);
				}
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
				con.set("cleanup-on-chunkunload", false);
				con.set("Remove-Flying-Projectiles-On-ChunkLoad", false);
				for(EntityType entity : EntityType.values()) {
					if(entity.isAlive()) {
						if(entity != EntityType.PLAYER) {
							con.set("mobs.allowToSpawn." + serialize_name(entity.name()), true);
						}
					}
				}
				con.save(f);
			} else {
				//because if the file exist we go check if the entitys are up to date if its not we surely update it.
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				List<String> entitys = Arrays.asList(con.getConfigurationSection("mobs.allowToSpawn").getKeys(true).toArray(new String[0]));
				List<String> newentities = new ArrayList<String>();
				for(EntityType entity : EntityType.values()) {
					if(entity.isAlive()) {
						if(entity != EntityType.PLAYER) {
							newentities.add(serialize_name(entity.name()));
						}
					}
				}
				if(entitys.size() != newentities.size()) {
					xEssentials.getPlugin().log("new entities detected!, adding them right now inside the entity config!", LogType.INFO);
					for(String entity : newentities) {
						if(!entitys.contains(newentities)) {
							xEssentials.getPlugin().log("found new entity: " + entity + " adding now to entity.yml", LogType.INFO);
							con.set("mobs.allowToSpawn."+entity, true);
						}
					}
					con.save(f);
				} else {
					xEssentials.getPlugin().log("there where no newer entitys found to be added in entity.yml", LogType.INFO);
				}
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

	@SuppressWarnings("deprecation")
	private void createKitConfig() {
		try {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "kits.yml");
			if(!f.exists()) {
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				FileConfigurationOptions opt = con.options();
				opt.header("this kit has customizeable permissions!\nfor each kit name its like xEssentials.kit.<nameofkit>\nhowever this is a little scheme how to use the data value system:\nid:subdata:amount");
				String[] DiamondKit = {Material.DIAMOND_PICKAXE.getId()+":0:1", Material.DIAMOND_SPADE.name()+":0:1", Material.DIAMOND_AXE.name()+":0:1", Material.DIAMOND_SWORD+":0:1", Material.MELON+":0:30"};
				String[] IronKit = {Material.IRON_PICKAXE.getId()+":0:1", Material.IRON_SPADE.name()+":0:1", Material.IRON_AXE.name()+":0:1", Material.IRON_SWORD+":0:1", Material.MELON+":0:30"};
				String[] WoodKit = {Material.WOOD_PICKAXE.getId()+":0:1", Material.WOOD_SPADE.name()+":0:1", Material.WOOD_AXE.name()+":0:1", Material.WOOD_SWORD+":0:1", Material.MELON+":0:30"};
				con.set("cooldown.isEnabled", true);
				con.set("cooldown.time", 100);
				con.set("kit.diamondkit", DiamondKit);
				con.set("kit.ironkit", IronKit);
				con.set("kit.woodkit", WoodKit);
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
				con.set("ban.system.services.fishbans", false);
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
	private void loadSystemPresets(ConfigType cfg) {
		if(cfg == ConfigType.GREYLIST) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "greylist.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("enable", con.getBoolean("greylist.enable"));
			hash.put("port", con.getInt("greylist.serverport"));
			hash.put("group", con.getString("greylist.group"));
			configure.put(ConfigType.GREYLIST, hash);
		} else if(cfg == ConfigType.BAN) {
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
				hash.put("fishbans", con.getBoolean("ban.system.services.fishbans"));
				configure.put(ConfigType.BAN, hash);

			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if(cfg == ConfigType.BROADCAST) {
			try {
				File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "broadcast.yml");
				FileConfiguration con = YamlConfiguration.loadConfiguration(f);
				HashMap<String, Object> hash = new HashMap<String, Object>();
				hash.put("enable", con.getBoolean("broadcast.enable"));
				hash.put("prefix", con.getString("broadcast.prefix"));
				hash.put("suffix", con.getString("broadcast.suffix"));
				hash.put("messages", con.getStringList("broadcast.messages"));
				configure.put(ConfigType.BROADCAST, hash);

			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if(cfg == ConfigType.CHAT) {
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
				configure.put(ConfigType.CHAT, hash);

			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if(cfg == ConfigType.ENTITY) {
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
				hash.put("cleanup", con.getBoolean("cleanup-on-chunkunload"));
				hash.put("AntiFireball", con.getBoolean("Remove-Flying-Projectiles-On-ChunkLoad"));
				HashMap<String, Boolean> entitys = new HashMap<String, Boolean>();
				for(String key : con.getConfigurationSection("mobs.allowToSpawn").getKeys(true)) {
					entitys.put(key, con.getBoolean("mobs.allowToSpawn."+key));
				}
				hash.put("allowToSpawn", entitys);
				configure.put(ConfigType.ENTITY, hash);

			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if(cfg == ConfigType.MOTD) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "motd.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("NormalEnable", con.getBoolean("motd.normal.enable"));
			hash.put("RandomEnable", con.getBoolean("motd.random.enable"));
			hash.put("messages", con.getStringList("motd.messages"));
			hash.put("message", con.getString("motd.message"));
			configure.put(ConfigType.MOTD, hash);

		} else if(cfg == ConfigType.PLAYER) {
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
			configure.put(ConfigType.PLAYER, hash);

		} else if(cfg == ConfigType.PVP) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "pvp.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("disablePvp", con.getBoolean("disable-pvp"));
			hash.put("createClientSideGraveyard", con.getBoolean("createClientSideGraveyard"));
			hash.put("killBountyEnable", con.getBoolean("killBounty.enable"));
			hash.put("killBountyEarn", con.getDouble("killBounty.earn"));
			hash.put("npcReplaceLoggers", con.getBoolean("npcReplaceLoggers"));
			configure.put(ConfigType.PVP, hash);

		} else if(cfg == ConfigType.RULES) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "rules.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("prefix", con.getString("rules.prefix"));
			hash.put("suffix", con.getString("rules.suffix"));
			hash.put("rules", con.getStringList("rules.messages"));
			configure.put(ConfigType.RULES, hash);

		} else if(cfg == ConfigType.BLOCKS) {
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
			configure.put(ConfigType.BLOCKS, hash);
		} else if(cfg == ConfigType.KITS) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "kits.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			HashMap<String, Kit> kitss = new HashMap<String, Kit>();
			Kit[] kits = parseKits(con);
			for(Kit kit : kits) {
				kitss.put(kit.getKitName(), kit);
			}
			hash.put("isCooldown", con.getBoolean("cooldown.isEnabled"));
			hash.put("cooldownTime", con.getInt("cooldown.time"));
			hash.put("kits", kitss);
			configure.put(ConfigType.KITS, hash);
		} else if(cfg == ConfigType.COMMAND) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "commands.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			HashMap<String, Boolean> commands = new HashMap<String, Boolean>();
			String[] orginal = con.getConfigurationSection("command").getKeys(false).toArray(new String[0]);
			for(String cmd : orginal) {
				commands.put(cmd, con.getBoolean("command."+cmd+".enable"));
			}
			hash.put("commands", commands);
			configure.put(ConfigType.COMMAND, hash);
		} else if(cfg == ConfigType.ECONOMY) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "economy.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("enable", con.getBoolean("economy.enable"));
			hash.put("currency", con.getString("economy.currency"));
			hash.put("startersAmount", con.getDouble("economy.startersAmount"));
			configure.put(ConfigType.ECONOMY, hash);
		} else if(cfg == ConfigType.SHOP) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "shops.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("enable", con.getBoolean("shop.enable"));
			hash.put("AdminShopPrefix", con.getString("shop.shop-admin-prefix"));
			hash.put("disableMessages", con.getBoolean("shop.disable-messages"));
			configure.put(ConfigType.SHOP, hash);
		} else if(cfg == ConfigType.PROTECTION) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "protection.yml");
			FileConfiguration con= YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("enable", con.getBoolean("protection.enable"));
			hash.put("signEnable", con.getBoolean("protection.protect.signs"));
			hash.put("chestEnable", con.getBoolean("protection.protect.chests"));
			hash.put("furnaceEnable", con.getBoolean("protection.protect.furnace"));
			hash.put("jukeboxEnable", con.getBoolean("protection.protect.jukebox"));
			hash.put("dispenserEnable", con.getBoolean("protection.protect.dispenser"));
			hash.put("messageDisallow", con.getString("protection.message.disallow"));
			configure.put(ConfigType.PROTECTION, hash);
		} else if(cfg == ConfigType.PORTAL) {
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "portal.yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			HashMap<String, Object> hash = new HashMap<String, Object>();
			List<String> list = new ArrayList<String>(con.getConfigurationSection("portals.worlds").getKeys(false));
			hash.put("enable", con.getBoolean("portals.enable"));
			hash.put("cooldown", con.getInt("portals.cooldown"));
			hash.put("worlds", list);
			configure.put(ConfigType.PORTAL, hash);
		}
	}

	public static EnumMap<MinigameType, HashMap<String, Minigame>> getMinigameMap() {
		return minigames;
	}

	/**
	 * @author xize
	 * @param parse the kits from the kits.yml config
	 * @return Kit[]
	 */
	@SuppressWarnings("deprecation")
	private Kit[] parseKits(FileConfiguration con) {
		List<Kit> kits = new ArrayList<Kit>();
		for(String path : con.getConfigurationSection("kit").getKeys(true)) {
			String kitname = path;
			List<ItemStack> stacks = new ArrayList<ItemStack>();
			List<String> items = new ArrayList<String>(con.getStringList("kit."+kitname));
			for(String item : items) {
				String[] split = item.split(":");
				if(isNumberic(split[0])) {
					Material mat = Material.getMaterial(Integer.parseInt(split[0]));
					Short subdata = Short.parseShort(split[1]);
					int amount = Integer.parseInt(split[2]);
					ItemStack stack = new ItemStack(mat, amount);
					stack.setDurability(subdata);
					stacks.add(stack);
				} else {
					Material mat = Material.getMaterial(split[0].toUpperCase());
					Short subdata = Short.parseShort(split[1]);
					int amount = Integer.parseInt(split[2]);
					ItemStack stack = new ItemStack(mat, amount);
					stack.setDurability(subdata);
					stacks.add(stack);
				}
			}
			Kit kit = new Kit(kitname, stacks.toArray(new ItemStack[stacks.size()]));
			kits.add(kit);
		}
		return kits.toArray(new Kit[kits.size()]);
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
			if(isBlockNumberic(name)) {
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

	private static Boolean isNumberic(String arg) {
		try {
			Integer i = Integer.parseInt(arg);
			if(i != null) {
				return true;
			}
		} catch(NumberFormatException e) {
			return false;
		}
		return false;
	}

	private static Boolean isBlockNumberic(String s) {
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
	 * @param returns all the worlds, including worlds who aren't loaded.
	 * @return String[]
	 */
	public String[] getWorlds() {
		File dir = Bukkit.getWorldContainer();
		List<String> worlds = new ArrayList<String>();
		for(File f : dir.listFiles()) {
			if(f.isDirectory()) {
				File dat = new File(f.getPath() + File.separator + "level.dat");
				if(dat.exists()) {
					worlds.add(f.getName());
				}
			}
		}
		return worlds.toArray(new String[worlds.size()]);
	}

	public void loadMiniGames() {
		for(MinigameType type : MinigameType.values()) {
			File dir = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "minigames" + File.separator + type.name().toLowerCase());
			if(dir.isDirectory()) {
				for(File f : dir.listFiles()) {
					FileConfiguration con = YamlConfiguration.loadConfiguration(f);
					Minigame game = new Minigame(f, con);
					HashMap<String, Minigame> hash = new HashMap<String, Minigame>();
					hash.put(game.getArenaName().toLowerCase(), game);
					minigames.put(type, hash);
				}
			}
		}
	}

	private static List<String> materials = new ArrayList<String>();

	/**
	 * @author xize
	 * @param type
	 * @param hashName
	 * @param returns the value per category so we can easier maintain this in the feature.
	 * @return Object
	 */
	public static Object getConfigValue(ConfigType type, String hashName) {
		return configure.get(type).get(hashName);
	}

	/**
	 * @author xize
	 * @param set a config value in the memory, this is deprecated as we using our update system.
	 * @return void
	 * @deprecated
	 */
	public static void setConfigValue(ConfigType type, String hashName, Object value) {
		configure.get(type).put(hashName, value);
	}

	/**
	 * @author xize
	 * @param get the full memory configuration for protections
	 * @return ProtectionConfig
	 */
	public static ProtectionConfig getProtectionConfig() {
		if(protectionconfig instanceof ProtectionConfig) {
			return protectionconfig;
		} else {
			protectionconfig = new ProtectionConfig();
			return protectionconfig;
		}
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for bans
	 * @return BanConfig
	 */
	public static BanConfig getBanConfig() {
		if(banconfig instanceof BanConfig) {
			return banconfig;
		} else {
			banconfig = new BanConfig();
			return banconfig;
		}
	}

	/**
	 * @author xize
	 * @param get the GreyList config
	 * @return GreylistConfig
	 */
	public static GreylistConfig getGrayListConfig() {
		if(greylistconfig instanceof GreylistConfig) {
			return greylistconfig;
		} else {
			greylistconfig = new GreylistConfig();
			return greylistconfig;
		}
	}

	/**
	 * @author xize
	 * @param returns the EconomyConfig as memory loaded
	 * @return EconomyConfig
	 */
	public static EconomyConfig getEconomyConfig() {
		if(economyconfig instanceof EconomyConfig) {
			return economyconfig;
		} else {
			economyconfig = new EconomyConfig();
			return economyconfig;
		}
	}

	/**
	 * @author xize
	 * @param returns the BlockConfig
	 * @return BlockConfig
	 */
	public static BlockConfig getBlockConfig() {
		if(blockconfig instanceof BlockConfig) {
			return blockconfig;
		} else {
			blockconfig = new BlockConfig();
			return blockconfig;
		}
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for broadcasts
	 * @return BroadcastConfig
	 */
	public static BroadcastConfig getBroadcastConfig() {
		if(broadcastconfig instanceof BroadcastConfig) {
			return broadcastconfig;
		} else {
			broadcastconfig = new BroadcastConfig();
			return broadcastconfig;
		}
	}

	/**
	 * @author xize
	 * @param returns the memory version of CommandConfig
	 * @return CommandConfig
	 */
	public static CommandConfig getCommandConfig() {
		if(commandconfig instanceof CommandConfig) {
			return commandconfig;
		} else {
			commandconfig = new CommandConfig();
			return commandconfig;
		}
	}

	/**
	 * @author xize
	 * @param get the full memory configuration for Kits
	 * @return KitConfig
	 */
	public static KitConfig getKitConfig() {
		if(kitconfig instanceof KitConfig) {
			return kitconfig;
		} else {
			kitconfig = new KitConfig();
			return kitconfig;
		}
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for chat
	 * @return ChatConfig
	 */
	public static ChatConfig getChatConfig() {
		if(chatconfig instanceof ChatConfig) {
			return chatconfig;
		} else {
			chatconfig = new ChatConfig();
			return chatconfig;
		}
	}

	/**
	 * @author xize
	 * @param returns the full memory configuration for shops
	 * @return ShopConfig
	 */
	public static ShopConfig getShopConfig() {
		if(shopconfig instanceof ShopConfig) {
			return shopconfig;
		} else {
			shopconfig = new ShopConfig();
			return shopconfig;
		}
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for entitys
	 * @return enityConfig
	 */
	public static EntityConfig getEntityConfig() {
		if(entityconfig instanceof EntityConfig) {
			return entityconfig;
		} else {
			entityconfig = new EntityConfig();
			return entityconfig;
		}
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for motd
	 * @return motdConfig
	 */
	public static MotdConfig getMotdConfig() {
		if(motdconfig instanceof MotdConfig) {
			return motdconfig;
		} else {
			motdconfig = new MotdConfig();
			return motdconfig;
		}
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for all players
	 * @return playerConfig
	 */
	public static PlayerConfig getPlayerConfig() {
		if(playerconfig instanceof PlayerConfig) {
			return playerconfig;
		} else {
			playerconfig = new PlayerConfig();
			return playerconfig;
		}
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for pvp
	 * @return pvpConfig
	 */
	public static PvpConfig getPvpConfig() {
		if(pvpconfig instanceof PvpConfig) {
			return pvpconfig;
		} else {
			pvpconfig = new PvpConfig();
			return pvpconfig;
		}
	}

	/**
	 * @author xize
	 * @param returns the memory portal configuration
	 * @return PortalConfig
	 */
	public static PortalConfig getPortalConfig() {
		if(portalconfig instanceof PortalConfig) {
			return portalconfig;
		} else {
			portalconfig = new PortalConfig();
			return portalconfig;
		}
	}

	/**
	 * @author xize
	 * @param gets the full memory configuration for rules
	 * @return rulesConfig
	 */
	public static RulesConfig getRulesConfig() {
		if(rulesconfig instanceof RulesConfig) {
			return rulesconfig;
		} else {
			rulesconfig = new RulesConfig();
			return rulesconfig;
		}
	}

	public static boolean isSilenceToggled = false;

	public boolean reload() {
		Handler handler = new Handler();
		CustomEventHandler customhandler = new CustomEventHandler();
		handler.stop();
		if(CallEssentialsBroadcastEvent.isRunning()) {
			CallEssentialsBroadcastEvent.stop();
		}
		//clear responsible from the deepest tree in the HashMap in case things could get persistent in the jvm things need to be better safe than not.
		for(ConfigType aEnum : ConfigType.values()) {
			configure.get(aEnum).clear();
		}
		configure.clear();
		if(xEssentials.getGreyListServer() != null) {
			if(xEssentials.getGreyListServer().isRunning()) {
				xEssentials.getGreyListServer().disable();
			}
		}
		createConfigs();
		HandleCommandManager();
		if(Configuration.getGrayListConfig().isEnabled()) {
			xEssentials.getGreyListServer().createServer();
		}
		xEssentials.reloadPlayerBase(); 
		handler.start();
		customhandler.startCustomEvents();
		if(Configuration.getProtectionConfig().isProtectionEnabled()) {
			xEssentials.setProtectionDatabase(new ProtectionDB());	
		}

		return true;
	}

	/**
	 * @author xize
	 * @param handles the commands, for disable, enable
	 * @param this manager manages automaticly the commands defined in the commands.yml
	 */
	@SuppressWarnings("unchecked")
	public static void HandleCommandManager() {
		CommandList cmdlist = new CommandList();
		List<String> unregCommands = new ArrayList<String>(Configuration.getCommandConfig().getUnregisteredCommands());

		if(!Configuration.getEconomyConfig().isEconomyEnabled()) {
			unregCommands.add("money");
		}
		if(!Configuration.getProtectionConfig().isProtectionEnabled()) {
			unregCommands.add("cprivate");
			unregCommands.add("cmodify");
			unregCommands.add("cremove");
		}
		if(!Configuration.getPortalConfig().isPortalEnabled()) {
			unregCommands.add("portals");
		}

		for(String cmd : cmdlist.getAllCommands) {			
			if(!unregCommands.contains(cmd) && !Configuration.getCommandConfig().isRegistered(cmd)) {
				try{
					//forcibly make a new PluginCommand object
					Class<?> clazz = Class.forName("org.bukkit.command.PluginCommand");
					Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, Plugin.class);
					constructor.setAccessible(true);
					Field mf = Constructor.class.getDeclaredField("modifiers");
					mf.setAccessible(true);
					mf.setInt(constructor, constructor.getModifiers() &~Modifier.PROTECTED);

					PluginCommand command = (PluginCommand) constructor.newInstance(cmd, xEssentials.getPlugin());
					command.setExecutor(new command());
					List<String> aliases = (List<String>) xEssentials.getPlugin().getDescription().getCommands().get(command.getName()).get("aliases");
					command.setAliases(aliases);

					constructor.setAccessible(false);
					mf.setAccessible(false);

					Configuration.getCommandConfig().registerBukkitCommand(command);   
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else if(unregCommands.contains(cmd) && Configuration.getCommandConfig().isRegistered(cmd)) {
				PluginCommand command = xEssentials.getPlugin().getCommand(cmd);
				Configuration.getCommandConfig().unRegisterBukkitCommand(command);
			}
		}
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
