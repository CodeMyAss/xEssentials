package tv.mineinthebox.essentials.commands;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.Warnings;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.utils.TPS;

public class CmdxEssentials implements Runnable {

	private CommandSender sender;
	private Thread lwcthread;
	private Long timestart;
	private Long timeend;

	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("xEssentials")) {
			if(sender.hasPermission(PermissionKey.CMD_XESSENTIALS.getPermission())) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + ".oO___[xEssentials version " + xEssentials.getPlugin().getDescription().getVersion() + "___Oo.");
					sender.sendMessage(ChatColor.GREEN + "this plugin is written by Xeph0re AKA xize ");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials reload " + ChatColor.WHITE + ": reloads the plugin");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials help " + ChatColor.WHITE + ": shows help");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials tps " + ChatColor.WHITE + ": shows tps of the server");
					sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials convert <type> " + ChatColor.WHITE + ": current only supports lwc as type");
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("help")) {
						sender.sendMessage(ChatColor.GOLD + ".oO___[xEssentials version " + xEssentials.getPlugin().getDescription().getVersion() + "___Oo.");
						sender.sendMessage(ChatColor.GREEN + "this plugin is written by Xeph0re AKA xize ");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials reload " + ChatColor.WHITE + ": reloads the plugin");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials help " + ChatColor.WHITE + ": shows help");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials tps " + ChatColor.WHITE + ": shows tps of the server");
						sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/xEssentials convert <type> " + ChatColor.WHITE + ": current only supports lwc as type");
					} else if(args[0].equalsIgnoreCase("reload")) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&3xEssentials&2]&f " + ChatColor.GRAY + "reloading xEssentials version " + xEssentials.getPlugin().getDescription().getVersion()));
						Configuration.reloadConfiguration();
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&3xEssentials&2]&f " + ChatColor.GRAY + "reload completed!"));
					} else if(args[0].equalsIgnoreCase("tps")) {
						if(sender.hasPermission(PermissionKey.CMD_TPS.getPermission())) {
							ChatColor tpsColor = null;
							sender.sendMessage(ChatColor.GOLD + ".oO___[tps]___Oo.");
							sender.sendMessage(ChatColor.GRAY + "Gc max: " + TPS.garbageCollectorMax() + "mb");
							sender.sendMessage(ChatColor.GRAY + "Free memory: " + TPS.getFreeMemory() + "mb");
							sender.sendMessage(ChatColor.GRAY + "Max memory " + TPS.getMemoryMax() + "mb");
							if(TPS.getServerTicks() < 15) {
								tpsColor = ChatColor.GREEN;
							} else if(TPS.getServerTicks() > 15 && TPS.getServerTicks() < 25) {
								tpsColor = ChatColor.YELLOW;
							} else if(TPS.getServerTicks() > 25) {
								tpsColor = ChatColor.RED;
							}
							sender.sendMessage(ChatColor.GRAY + "ticks: " + tpsColor + TPS.getServerTicks());
						} else {
							Warnings.getWarnings(sender).noPermission();
						}
					}
				} else if(args.length == 2) {
					if(args[0].equalsIgnoreCase("convert")) {
						if(args[1].equalsIgnoreCase("lwc")) {
							if(Configuration.getProtectionConfig().isProtectionEnabled()) {
								File dir = new File("plugins" + File.separator + "LWC");
								if(dir.isDirectory()) {
									File db = new File("plugins" + File.separator + "LWC" + File.separator + "lwc.db");
									if(db.exists()) {
										sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&3xEssentials&2]&f " + ChatColor.GRAY + "starting importing lwc database contents in our own database..."));
										sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&3xEssentials&2]&f " + ChatColor.GRAY + "setting up a new workers thread, against server crashing."));
										timestart = System.currentTimeMillis();
										this.sender = sender;
										lwcthread = new Thread(this);
										lwcthread.start();
										sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&3xEssentials&2]&f " + ChatColor.GRAY + "thread has been activated, now waiting for response, this could take up to 30 minutes depending on the size of the database."));
									} else {
										sender.sendMessage(ChatColor.RED + "could not find any LWC sqlite database");
									}
								} else {
									sender.sendMessage(ChatColor.RED + "could not find any LWC folder.");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "protection system is not enabled!");
							}
						}
					}
				}
			} else {
				Warnings.getWarnings(sender).noPermission();
			}
		}
		return false;
	}

	@Override
	public void run() {
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlite:plugins/LWC/lwc.db");
			Statement state = con.createStatement();
			String query = "SELECT * FROM lwc_protections";
			ResultSet set = state.executeQuery(query);
			while(set.next()) {
				String owner = set.getString("owner");
				int x = set.getInt("x");
				int y = set.getInt("y");
				int z = set.getInt("z");
				World world = Bukkit.getWorld(set.getString("world"));
				if(world instanceof World) {
					Block block = world.getBlockAt(x, y, z);
					xEssentials.getProtectionDatabase().register(owner, block);
				}
			}
			state.close();
			set.close();
			con.close();
			timeend = System.currentTimeMillis();
			if(sender instanceof CommandSender) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&3xEssentials&2]&f " + ChatColor.GRAY + "lwc database has successfully been intergrated, into our xEssentials system."));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&3xEssentials&2]&f " + ChatColor.GRAY + "thread took currently " + (timeend-timestart) + "ms."));
			}
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
