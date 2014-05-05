package tv.mineinthebox.essentials.events.spleef;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tv.mineinthebox.essentials.xEssentials;

public class CreateArenaEvent implements Listener {

	public static HashMap<String, String> hash = new HashMap<String, String>();

	private static HashMap<String, Block> firstPos = new HashMap<String, Block>();
	private static HashMap<String, Block> secondPos = new HashMap<String, Block>();

	@EventHandler
	public void onCreate(PlayerInteractEvent e) {
		if(e.isCancelled()) {
			return;
		}

		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(hash.containsKey(e.getPlayer().getName())) {
				if(firstPos.containsKey(e.getPlayer().getName())) {
					if(!secondPos.containsKey(e.getPlayer().getName())) {
						e.getPlayer().sendMessage(ChatColor.GRAY + "second block selected");
						secondPos.put(e.getPlayer().getName(), e.getClickedBlock());

						Block pos1 = firstPos.get(e.getPlayer().getName());
						Block pos2 = secondPos.get(e.getPlayer().getName());

						Block[] blocks = getSpleeflBlocks(pos1, pos2);

						Block[] getInnerBlocks = getInnerBlocks(blocks);

						File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "minigames" + File.separator + "spleef" + File.separator + hash.get(e.getPlayer().getName()).toLowerCase() + ".yml");

						if(f.exists()) {
							e.getPlayer().sendMessage(ChatColor.RED + "arena already exist with that name!");
							if(hash.containsKey(e.getPlayer().getName())) {
								if(firstPos.containsKey(e.getPlayer().getName())) {
									firstPos.remove(e.getPlayer().getName());
								}
								if(secondPos.containsKey(e.getPlayer().getName())) {
									secondPos.remove(e.getPlayer().getName());
								}
								hash.remove(e.getPlayer().getName());
							}
							return;
						} else {
							try {
								FileConfiguration con = YamlConfiguration.loadConfiguration(f);
								con.set("arena.name", hash.get(e.getPlayer().getName()));
								List<String> list = new ArrayList<String>();
								for(Block block : getInnerBlocks) {
									block.setType(Material.SNOW_BLOCK);
									String w = block.getWorld().getName();
									int x = block.getX();
									int y = block.getY();
									int z = block.getZ();
									String serialize = w+":"+x+":"+y+":"+z;
									list.add(serialize);
								}
								con.set("arena.snowlayers", list.toArray());
								con.set("arena.isrunning", false);
								con.save(f);
								if(hash.containsKey(e.getPlayer().getName())) {
									if(firstPos.containsKey(e.getPlayer().getName())) {
										firstPos.remove(e.getPlayer().getName());
									}
									if(secondPos.containsKey(e.getPlayer().getName())) {
										secondPos.remove(e.getPlayer().getName());
									}
									hash.remove(e.getPlayer().getName());
								}
								e.getPlayer().sendMessage(ChatColor.RED + "-oOoOo[" + ChatColor.GREEN + "spleef" + ChatColor.RED + "]oOoOo-");
								e.getPlayer().sendMessage(ChatColor.GRAY + "now since you made the spleef arena environment, you now need to add spawnpoints of a minium of 2!");
								e.getPlayer().sendMessage(ChatColor.GRAY + "type \"/spleef add sp <arenaname>\" - to create a spawn point of a player");
								e.getPlayer().sendMessage(ChatColor.GRAY + "or type \"/spleef help\" for more info.");
							} catch(Exception r) {
								r.printStackTrace();
							}
						}
					}
				} else {
					e.getPlayer().sendMessage(ChatColor.GRAY + "first block selected");
					firstPos.put(e.getPlayer().getName(), e.getClickedBlock());
				}
				e.setCancelled(true);
			}	
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(hash.containsKey(e.getPlayer().getName())) {
			if(firstPos.containsKey(e.getPlayer().getName())) {
				firstPos.remove(e.getPlayer().getName());
			}
			if(secondPos.containsKey(e.getPlayer().getName())) {
				secondPos.remove(e.getPlayer().getName());
			}
			hash.remove(e.getPlayer().getName());
		}
	}

	@EventHandler
	public void onQuit(PlayerKickEvent e) {
		if(hash.containsKey(e.getPlayer().getName())) {
			if(firstPos.containsKey(e.getPlayer().getName())) {
				firstPos.remove(e.getPlayer().getName());
			}
			if(secondPos.containsKey(e.getPlayer().getName())) {
				secondPos.remove(e.getPlayer().getName());
			}
			hash.remove(e.getPlayer().getName());
		}
	}


	/**
	 * @author xize
	 * @param returns a specific list of all blocks calculated inside this spleef arena.
	 * @param block1 - the first selection
	 * @param block2 - the second selection
	 * @return Block[]
	 */
	public Block[] getSpleeflBlocks(Block block1, Block block2) {
		List<Block> blocks = new ArrayList<Block>();

		int xr = block1.getX();
		int zr = block2.getZ();

		if((xr-zr) > 0) {
			//its ++ increment
			for(int x = xr; x <= zr; x++) {
				for(int z = zr; zr <= xr; z++) {
					Block block = new Location(block1.getWorld(), x, block1.getY(), z).getBlock();
					blocks.add(block);
				}
			}
		} else if((xr-zr) < 0) {
			//its -- increment
			for(int x = xr; x <= zr; x--) {
				for(int z = zr; zr <= xr; z--) {
					Block block = new Location(block1.getWorld(), x, block1.getY(), z).getBlock();
					blocks.add(block);
				}
			}
		}

		return blocks.toArray(new Block[blocks.size()]);
	}

	public Block[] getInnerBlocks(Block[] blocks) {
		List<Block> blockargs = new ArrayList<Block>(Arrays.asList(blocks));
		for(Block block : blockargs) {
			if(block.getType() != Material.AIR) {
				blockargs.remove(block);
			}
		}
		return blockargs.toArray(new Block[blockargs.size()]);
	}

}
