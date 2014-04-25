package tv.mineinthebox.essentials.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.events.portals.PortalSelectedCreateEvent;
import tv.mineinthebox.essentials.instances.Portal;

public class CmdPortals {
	
	private List<String> getUnloadedWorlds(String p) {
		List<String> list = new ArrayList<String>();
		for(String s : Configuration.getPortalConfig().getWorlds()) {
			if(s.startsWith(p)) {
				World world = Bukkit.getWorld(s);
				if(!(world instanceof World)) {
					list.add(s);
				}
			}
		}
		return list;
	}
	
	private List<String> getLoadedWorlds(String p) {
		List<String> list = new ArrayList<String>();
		for(String s : Configuration.getPortalConfig().getWorlds()) {
			if(s.startsWith(p)) {
				World world = Bukkit.getWorld(s);
				if(world instanceof World) {
					list.add(s);
				}
			}
		}
		return list;
	}
	
	private List<String> getPortals(String p) {
		List<String> list = new ArrayList<String>();
		for(Portal portal : Configuration.getPortalConfig().getPortals().values()) {
			if(portal.getPortalName().startsWith(p)) {
				list.add(portal.getPortalName());
			}
		}
		return list;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("portals")) {
			if(sender.hasPermission(PermissionKey.CMD_PORTALS.getPermission())) {
				if(args.length == 2) {
					if(args[0].equalsIgnoreCase("load")) {
						return getUnloadedWorlds(args[1]);
					} else if(args[0].equalsIgnoreCase("unload")) {
						return getLoadedWorlds(args[1]);
					} else if(args[0].equalsIgnoreCase("delete")) {
						return getPortals(args[1]);
					} else if(args[0].equalsIgnoreCase("link")) {
						return getPortals(args[1]);
					}
				} else if(args.length == 3) {
					if(args[0].equalsIgnoreCase("link")) {
						return getPortals(args[2]);
					}
				}
			}
		}
		return null;
	}
	
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("portals")) {
			if(sender.hasPermission(PermissionKey.CMD_PORTALS.getPermission())) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + ".oO___[portals help]___Oo.");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals help " + ChatColor.WHITE + ": shows help");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals world list " + ChatColor.WHITE + ": shows a list of all loaded/unloaded worlds");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals list " + ChatColor.WHITE + ": shows a list of all portal names");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals load <world> " + ChatColor.WHITE + ": load a world again");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals unload <world> " + ChatColor.WHITE + ": unload a world");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals create <portal-name> " + ChatColor.WHITE + ": creates a selection and adress a name/id to it");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals delete <portal-name> " + ChatColor.WHITE + ": delete a portal");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals link <portal1> <portal2> " + ChatColor.WHITE + ": links portals to each other.");
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("help")) {
						sender.sendMessage(ChatColor.GOLD + ".oO___[portals help]___Oo.");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals help " + ChatColor.WHITE + ": shows help");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals world list " + ChatColor.WHITE + ": shows a list of all loaded/unloaded worlds");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals list " + ChatColor.WHITE + ": shows a list of all portal names");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals load <world> " + ChatColor.WHITE + ": load a world again");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals unload <world> " + ChatColor.WHITE + ": unload a world");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals create <portal-name> " + ChatColor.WHITE + ": creates a selection and adress a name/id to it");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals delete <portal-name> " + ChatColor.WHITE + ": delete a portal");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/portals link <portal1> <portal2> " + ChatColor.WHITE + ": links portals to each other.");
					} else if(args[0].equalsIgnoreCase("list")) {
						File dir = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "portals");
						if(dir.isDirectory()) {
							StringBuilder build = new StringBuilder();
							HashMap<String, Portal> portals = new HashMap<String, Portal>(Configuration.getPortalConfig().getPortals());
							for(int i = 0; i < portals.values().size(); i++) {
								Portal portal = portals.values().toArray(new Portal[portals.size()])[i];
								if(i == portals.values().size()) {
									build.append(ChatColor.GREEN + "(" + ChatColor.GRAY + portal.getPortalName() + ", isLinked="+ (portal.isLinked() ? ChatColor.GREEN + "true(" + ChatColor.GRAY + portal.getLinkedPortal().getPortalName() + ChatColor.GREEN + ")" : ChatColor.RED + "false") + ChatColor.GREEN + ")");
								} else {
									build.append(ChatColor.GREEN + "(" + ChatColor.GRAY + portal.getPortalName() + ", isLinked="+ (portal.isLinked() ? ChatColor.GREEN + "true(" + ChatColor.GRAY + portal.getLinkedPortal().getPortalName() + ChatColor.GREEN + ")" : ChatColor.RED + "false") + ChatColor.GREEN + ")" + ChatColor.GRAY + ", ");
								}
							}
							sender.sendMessage(ChatColor.GOLD + ".oO___[portal list]____Oo.");
							sender.sendMessage(build.toString());
						} else {
							sender.sendMessage(ChatColor.RED + "no portals where found!");
						}
					}
				} else if(args.length == 2) {
					if(args[0].equalsIgnoreCase("world") && args[1].equalsIgnoreCase("list")) {
						StringBuilder build = new StringBuilder();
						for(int i = 0; i < Configuration.getPortalConfig().getWorlds().size(); i++) {
							String wname = Configuration.getPortalConfig().getWorlds().get(i);
							World w = Bukkit.getWorld(wname);
							if(i == Bukkit.getWorlds().size()) {
								build.append(((w instanceof World) ? ChatColor.GREEN + wname : ChatColor.RED + wname));
							} else {
								build.append(((w instanceof World) ? ChatColor.GREEN + wname : ChatColor.RED + wname) + ChatColor.GRAY + ", ");
							}
						}
						sender.sendMessage(ChatColor.GOLD + ".oO___[worlds loaded/unloaded]___Oo.");
						sender.sendMessage(build.toString());
					} else if(args[0].equalsIgnoreCase("load")) {
						if(Configuration.getPortalConfig().getWorlds().contains(args[1])) {
							World world = Bukkit.getWorld(args[1]);
							if(!(world instanceof World)) {
								sender.sendMessage(ChatColor.GREEN + "world " + args[1] + " is now loading ;-)");
								Bukkit.createWorld(new WorldCreator(args[1]));
								sender.sendMessage(ChatColor.GREEN + "world " + args[1] + " has been successfully loaded");
							} else {
								sender.sendMessage(ChatColor.RED + "world is already loaded!");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "world doesn't seems to exist!");
						}
					} else if(args[0].equalsIgnoreCase("unload")) {
						if(Configuration.getPortalConfig().getWorlds().contains(args[1])) {
							World world = Bukkit.getWorld(args[1]);
							if(world instanceof World) {
								sender.sendMessage(ChatColor.GREEN + "world " + args[1] + " is now unloading ;-)");
								Bukkit.unloadWorld(world, true);
								sender.sendMessage(ChatColor.GREEN + "world " + args[1] + " has been successfully unloaded");
							} else {
								sender.sendMessage(ChatColor.RED + "world is already unloaded, or does not exist!");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "world doesn't seems to exist!");
						}
					} else if(args[0].equalsIgnoreCase("create")) {
						File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "portals" + File.separator + args[1] + ".yml");
						if(!f.exists()) {
							PortalSelectedCreateEvent.hash.put(sender.getName(), args[1]);
							sender.sendMessage(ChatColor.GREEN + "now right click the abovest block of the portal, and next followed by the lowest block.");
						} else {
							sender.sendMessage(ChatColor.RED + "this portal name does already exist!");
						}
					} else if(args[0].equalsIgnoreCase("remove")) {
						try {
							Portal portal = Configuration.getPortalConfig().getPortal(args[1]);
							portal.remove();
							sender.sendMessage(ChatColor.GREEN + "you have successfully removed portal " + args[1]);
						} catch(Exception e) {
							sender.sendMessage(ChatColor.RED + "this portal does not exist!");
						}
					}
				} else if(args.length == 3) {
					if(args[0].equalsIgnoreCase("link")) {
						try {
							Portal portal1 = Configuration.getPortalConfig().getPortal(args[1]);
							Portal portal2 = Configuration.getPortalConfig().getPortal(args[2]);
							portal1.linkPortal(portal2.getPortalName(), true);
							sender.sendMessage(ChatColor.GREEN + "you have successfully linked portal " + portal1.getPortalName() + " to " + portal2.getPortalName());
						} catch(Exception e) {
							sender.sendMessage(ChatColor.RED + "one of the portals has a invalid name!");
						}
					}
				}
			} else {
				Warnings.getWarnings(sender).noPermission();
			}
		}
		return false;
	}

}
