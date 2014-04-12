package tv.mineinthebox.essentials.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.Sign;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.LogType;
import tv.mineinthebox.essentials.enums.PermissionKey;

public class ProtectionDB {

	public ProtectionDB() {
		File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "databases");
		if(!f.isDirectory()) {
			f.mkdir();
		}
		try {
			Class.forName("org.sqlite.JDBC");
			if(isNewDatabase()) {
				xEssentials.getPlugin().log("there whas no protection database found, creating a new one.", LogType.INFO);
				createTables();
			} else {
				xEssentials.getPlugin().log("connected to the protection database!", LogType.INFO);
			}
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite in craftbukkit, this is probably because you are running a outdated build!", LogType.SEVERE);
		}
	}

	public boolean removeProtectedBlock(Player p, Block block) {
		try {
			Connection con = getConnection();
			if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
				if(isRegistered(block)) {
					if(isOwner(p, block) || p.hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String blockuid = generateBlockUUID(block).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + blockuid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(block.getType() == Material.WALL_SIGN) {
				Block sign = block;
				if(isRegistered(sign)) {
					if(isOwner(p, sign) || p.hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						BlockFace signface = ((Sign)sign.getState().getData()).getAttachedFace();
						String blockuid = generateBlockUUID(sign).toString().replace("-", "");
						String undersignuid = generateBlockUUID(sign.getRelative(signface)).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + blockuid + "'");
						state.executeUpdate("DELETE FROM blocks WHERE id='" + undersignuid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(block.getType() == Material.SIGN_POST) {
				Block sign = block;
				if(isRegistered(sign)) {
					if(isOwner(p, sign) || p.hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String blockuid = generateBlockUUID(sign).toString().replace("-", "");
						String undersignuid = generateBlockUUID(sign.getRelative(BlockFace.DOWN)).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + blockuid + "'");
						state.executeUpdate("DELETE FROM blocks WHERE id='" + undersignuid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(block.getType() == Material.IRON_DOOR_BLOCK) {
				Block door = block;
				if(isRegistered(door)) {
					if(isOwner(p, door) || p.hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String blockuid = generateBlockUUID(door).toString().replace("-", "");
						String underdooruid = generateBlockUUID(door.getRelative(BlockFace.DOWN)).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + blockuid + "'");
						state.executeUpdate("DELETE FROM blocks WHERE id='" + underdooruid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(block.getType() == Material.WOODEN_DOOR) {
				Block door = block;
				if(isRegistered(door)) {
					if(isOwner(p, door) || p.hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String blockuid = generateBlockUUID(door).toString().replace("-", "");
						String underdooruid = generateBlockUUID(door.getRelative(BlockFace.DOWN)).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + blockuid + "'");
						state.executeUpdate("DELETE FROM blocks WHERE id='" + underdooruid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(block.getType() == Material.FURNACE) {
				Block furnace = block;
				if(isRegistered(furnace)) {
					if(isOwner(p, furnace) || p.hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String uid = generateBlockUUID(furnace).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + uid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(block.getType() == Material.JUKEBOX) {
				Block jukebox = block;
				if(isRegistered(jukebox)) {
					if(isOwner(p, jukebox) || p.hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String uid = generateBlockUUID(jukebox).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + uid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(block.getType() == Material.TRAP_DOOR) {
				Block trap = block;
				if(isRegistered(trap)) {
					if(isOwner(p, trap) || p.hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String uid = generateBlockUUID(trap).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + uid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			}
		} catch(Exception r) {
			r.printStackTrace();
		}
		return false;
	}

	public boolean removeProtectedBlock(BlockBreakEvent e) {
		try {
			Connection con = getConnection();
			if(e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.TRAPPED_CHEST) {
				Block block = e.getBlock();
				if(isRegistered(block)) {
					if(isOwner(e.getPlayer(), block) || e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String blockuid = generateBlockUUID(block).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + blockuid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(e.getBlock().getType() == Material.WALL_SIGN) {
				Block sign = e.getBlock();
				if(isRegistered(sign)) {
					if(isOwner(e.getPlayer(), sign) || e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						BlockFace signface = ((Sign)sign.getState().getData()).getAttachedFace();
						String blockuid = generateBlockUUID(sign).toString().replace("-", "");
						String undersignuid = generateBlockUUID(sign.getRelative(signface)).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + blockuid + "'");
						state.executeUpdate("DELETE FROM blocks WHERE id='" + undersignuid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(e.getBlock().getType() == Material.SIGN_POST) {
				Block sign = e.getBlock();
				if(isRegistered(sign)) {
					if(isOwner(e.getPlayer(), sign) || e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String blockuid = generateBlockUUID(sign).toString().replace("-", "");
						String undersignuid = generateBlockUUID(sign.getRelative(BlockFace.DOWN)).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + blockuid + "'");
						state.executeUpdate("DELETE FROM blocks WHERE id='" + undersignuid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(e.getBlock().getType() == Material.IRON_DOOR_BLOCK) {
				Block door = e.getBlock();
				if(isRegistered(door)) {
					if(isOwner(e.getPlayer(), door) || e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String blockuid = generateBlockUUID(door).toString().replace("-", "");
						String underdooruid = generateBlockUUID(door.getRelative(BlockFace.DOWN)).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + blockuid + "'");
						state.executeUpdate("DELETE FROM blocks WHERE id='" + underdooruid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(e.getBlock().getType() == Material.WOODEN_DOOR) {
				Block door = e.getBlock();
				if(isRegistered(door)) {
					if(isOwner(e.getPlayer(), door) || e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String blockuid = generateBlockUUID(door).toString().replace("-", "");
						String underdooruid = generateBlockUUID(door.getRelative(BlockFace.DOWN)).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + blockuid + "'");
						state.executeUpdate("DELETE FROM blocks WHERE id='" + underdooruid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(e.getBlock().getType() == Material.FURNACE) {
				Block furnace = e.getBlock();
				if(isRegistered(furnace)) {
					if(isOwner(e.getPlayer(), furnace) || e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String uid = generateBlockUUID(furnace).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + uid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(e.getBlock().getType() == Material.JUKEBOX) {
				Block jukebox = e.getBlock();
				if(isRegistered(jukebox)) {
					if(isOwner(e.getPlayer(), jukebox) || e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String uid = generateBlockUUID(jukebox).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + uid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			} else if(e.getBlock().getType() == Material.TRAP_DOOR) {
				Block trap = e.getBlock();
				if(isRegistered(trap)) {
					if(isOwner(e.getPlayer(), trap) || e.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						String uid = generateBlockUUID(trap).toString().replace("-", "");
						Statement state = con.createStatement();
						state.executeUpdate("DELETE FROM blocks WHERE id='" + uid + "'");
						state.close();
						con.close();
						return true;
					}
				}
			}
		} catch(Exception r) {
			r.printStackTrace();
		}
		return false;
	}

	/**
	 * @author xize
	 * @param adds protection to specific blocks
	 * @param these can only be apply on chest, trapped chest, wall signs, sign posts, iron door, wooden door, trap door
	 * @return Boolean
	 */
	public boolean addProtectedBlock(String name, Block block) {
		Connection con = getConnection();
		try {
			if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
				Chest attachedChest = getChestAttached(block);
				if(attachedChest instanceof Chest) {
					if(isOwner(name, attachedChest.getBlock())) {
						String uid1 = generateBlockUUID(block).toString().replace("-", "");
						String values1 = setValueString(new String[] {uid1, name, Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
						String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
						Statement state = con.createStatement();
						state.executeUpdate(query1);
						state.close();
						con.close();
						return true;
					}
				} else {
					String uid1 = generateBlockUUID(block).toString().replace("-", "");
					String values1 = setValueString(new String[] {uid1, name, Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
					String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
					Statement state = con.createStatement();
					state.executeUpdate(query1);
					state.close();
					con.close();
					return true;
				}
			} else if(block.getType() == Material.WALL_SIGN) {
				Sign sign = (Sign) block.getState().getData();
				Block behindsign = block.getRelative(sign.getAttachedFace());
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(behindsign).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, name, Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, name, Integer.toString(behindsign.getX()), Integer.toString(behindsign.getY()), Integer.toString(behindsign.getZ()), behindsign.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.SIGN_POST) {
				Block undersign = block.getRelative(BlockFace.DOWN);
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(undersign).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, name, Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, name, Integer.toString(undersign.getX()), Integer.toString(undersign.getY()), Integer.toString(undersign.getZ()), undersign.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.WOODEN_DOOR) {
				Block door = block.getRelative(BlockFace.DOWN);
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(door).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, name, Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, name, Integer.toString(door.getX()), Integer.toString(door.getY()), Integer.toString(door.getZ()), door.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.IRON_DOOR_BLOCK) {
				Block door = block.getRelative(BlockFace.DOWN);
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(door).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, name, Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, name, Integer.toString(door.getX()), Integer.toString(door.getY()), Integer.toString(door.getZ()), door.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.FURNACE) {
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String values = setValueString(new String[] {uid1, name, Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String query = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.JUKEBOX) {
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String values = setValueString(new String[] {uid1, name, Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String query = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.TRAP_DOOR) {
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String values = setValueString(new String[] {uid1, name, Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String query = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query);
				state.close();
				con.close();
				return true;
			}
		} catch(Exception r) {
			r.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @author xize
	 * @param adds protection to specific blocks
	 * @param these can only be apply on chest, trapped chest, wall signs, sign posts, iron door, wooden door, trap door
	 * @return Boolean
	 */
	public boolean addProtectedBlock(Player p, Block block) {
		Connection con = getConnection();
		try {
			if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
				Chest attachedChest = getChestAttached(block);
				if(attachedChest instanceof Chest) {
					if(isOwner(p, attachedChest.getBlock())) {
						String uid1 = generateBlockUUID(block).toString().replace("-", "");
						String values1 = setValueString(new String[] {uid1, p.getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
						String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
						Statement state = con.createStatement();
						state.executeUpdate(query1);
						state.close();
						con.close();
						return true;
					}
				} else {
					String uid1 = generateBlockUUID(block).toString().replace("-", "");
					String values1 = setValueString(new String[] {uid1, p.getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), p.getWorld().getName()});
					String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
					Statement state = con.createStatement();
					state.executeUpdate(query1);
					state.close();
					con.close();
					return true;
				}
			} else if(block.getType() == Material.WALL_SIGN) {
				Sign sign = (Sign) block.getState().getData();
				Block behindsign = block.getRelative(sign.getAttachedFace());
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(behindsign).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, p.getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, p.getName(), Integer.toString(behindsign.getX()), Integer.toString(behindsign.getY()), Integer.toString(behindsign.getZ()), behindsign.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.SIGN_POST) {
				Block undersign = block.getRelative(BlockFace.DOWN);
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(undersign).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, p.getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, p.getName(), Integer.toString(undersign.getX()), Integer.toString(undersign.getY()), Integer.toString(undersign.getZ()), undersign.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.WOODEN_DOOR) {
				Block door = block.getRelative(BlockFace.DOWN);
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(door).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, p.getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, p.getName(), Integer.toString(door.getX()), Integer.toString(door.getY()), Integer.toString(door.getZ()), door.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.IRON_DOOR_BLOCK) {
				Block door = block.getRelative(BlockFace.DOWN);
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(door).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, p.getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, p.getName(), Integer.toString(door.getX()), Integer.toString(door.getY()), Integer.toString(door.getZ()), door.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.FURNACE) {
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String values = setValueString(new String[] {uid1, p.getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String query = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.JUKEBOX) {
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String values = setValueString(new String[] {uid1, p.getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String query = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.TRAP_DOOR) {
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String values = setValueString(new String[] {uid1, p.getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String query = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query);
				state.close();
				con.close();
				return true;
			}
		} catch(Exception r) {
			r.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @author xize
	 * @param adds protection to specific blocks
	 * @param these can only be apply on chest, trapped chest, wall signs, sign posts, iron door, wooden door, trap door
	 * @return Boolean
	 */
	public boolean addProtectedBlock(BlockPlaceEvent e) {
		Block block = e.getBlockPlaced();
		Connection con = getConnection();
		try {
			if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
				Chest attachedChest = getChestAttached(block);
				if(attachedChest instanceof Chest) {
					if(isOwner(e.getPlayer(), attachedChest.getBlock())) {
						String uid1 = generateBlockUUID(block).toString().replace("-", "");
						String values1 = setValueString(new String[] {uid1, e.getPlayer().getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
						String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
						Statement state = con.createStatement();
						state.executeUpdate(query1);
						state.close();
						con.close();
						return true;
					}
				} else {
					String uid1 = generateBlockUUID(block).toString().replace("-", "");
					String values1 = setValueString(new String[] {uid1, e.getPlayer().getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), e.getPlayer().getWorld().getName()});
					String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
					Statement state = con.createStatement();
					state.executeUpdate(query1);
					state.close();
					con.close();
					return true;
				}
			} else if(block.getType() == Material.WALL_SIGN) {
				Sign sign = (Sign) block.getState().getData();
				Block behindsign = block.getRelative(sign.getAttachedFace());
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(behindsign).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, e.getPlayer().getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, e.getPlayer().getName(), Integer.toString(behindsign.getX()), Integer.toString(behindsign.getY()), Integer.toString(behindsign.getZ()), behindsign.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.SIGN_POST) {
				Block undersign = block.getRelative(BlockFace.DOWN);
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(undersign).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, e.getPlayer().getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, e.getPlayer().getName(), Integer.toString(undersign.getX()), Integer.toString(undersign.getY()), Integer.toString(undersign.getZ()), undersign.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.WOODEN_DOOR) {
				Block door = block.getRelative(BlockFace.DOWN);
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(door).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, e.getPlayer().getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, e.getPlayer().getName(), Integer.toString(door.getX()), Integer.toString(door.getY()), Integer.toString(door.getZ()), door.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.IRON_DOOR_BLOCK) {
				Block door = block.getRelative(BlockFace.DOWN);
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String uid2 = generateBlockUUID(door).toString().replace("-", "");
				String values1 = setValueString(new String[] {uid1, e.getPlayer().getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String values2 = setValueString(new String[] {uid2, e.getPlayer().getName(), Integer.toString(door.getX()), Integer.toString(door.getY()), Integer.toString(door.getZ()), door.getWorld().getName()});
				String query1 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values1 + ")";
				String query2 = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values2 + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query1);
				state.executeUpdate(query2);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.FURNACE) {
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String values = setValueString(new String[] {uid1, e.getPlayer().getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String query = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.JUKEBOX) {
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String values = setValueString(new String[] {uid1, e.getPlayer().getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String query = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query);
				state.close();
				con.close();
				return true;
			} else if(block.getType() == Material.TRAP_DOOR) {
				String uid1 = generateBlockUUID(block).toString().replace("-", "");
				String values = setValueString(new String[] {uid1, e.getPlayer().getName(), Integer.toString(block.getX()), Integer.toString(block.getY()), Integer.toString(block.getZ()), block.getWorld().getName()});
				String query = "INSERT INTO blocks(id, username, x, y, z, world) VALUES(" + values + ")";
				Statement state = con.createStatement();
				state.executeUpdate(query);
				state.close();
				con.close();
				return true;
			}
		} catch(Exception r) {
			r.printStackTrace();
		}
		return false;
	}

	/**
	 * @author xize
	 * @param returns the attached chest, if there is no chest attached it will return null
	 * @param block - the main block
	 * @return Chest
	 */
	private Chest getChestAttached(Block block) {
		BlockFace[] faces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
		for(BlockFace face : faces) {
			if(block.getRelative(face).getType() == Material.CHEST || block.getRelative(face).getType() == Material.TRAPPED_CHEST) {
				Chest chest = (Chest) block.getRelative(face).getState();
				return chest;
			}
		}
		return null;
	}

	/**
	 * @author xize
	 * @param update all tables with the new corresponded name.
	 * @param oldname - the old players name
	 * @param newname - the new players name
	 */
	public void updatePlayer(String oldname, String newname) {
		Connection con = getConnection();
		try {
			if(hasProtectedBlocks(oldname)) {
				String query = "UPDATE blocks SET username='" + newname + "' WHERE username='" + oldname + "'";
				Statement state = con.createStatement();
				state.executeUpdate(query);
				state.close();
				con.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author xize
	 * @param returns true whenever the player has protected blocks
	 * @param player - the players name
	 * @return Boolean
	 */
	public boolean hasProtectedBlocks(String player) {
		Connection con = getConnection();
		try {
			String query = "SELECT * FROM blocks WHERE username='" + player + "'";
			Statement state = con.createStatement();
			ResultSet set = state.executeQuery(query);
			if(set.isBeforeFirst()) {
				state.close();
				con.close();
				return true;
			}
		} catch(Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param returns true whenever the block protection belongs to the corresponded player, else false
	 * @param p - the player
	 * @param target - the block which the player hits
	 * @return Boolean
	 */
	public boolean isOwner(Player p, Block target) {
		Connection con = getConnection();
		try {
			String blockuid = generateBlockUUID(target).toString().replace("-", "");
			Statement state = con.createStatement();
			String query = "SELECT * FROM blocks WHERE id='" + blockuid + "'";
			ResultSet set = state.executeQuery(query);
			if(set.next()) {
				if(set.getString("username").equalsIgnoreCase(p.getName())) {
					state.close();
					con.close();
					return true;
				}
			}
		} catch(Exception e) {
			return false;
		}
		return false;
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the block protection belongs to the corresponded player, else false
	 * @param p - the player
	 * @param target - the block which the player hits
	 * @return Boolean
	 */
	public boolean isOwner(String name, Block target) {
		Connection con = getConnection();
		try {
			String blockuid = generateBlockUUID(target).toString().replace("-", "");
			Statement state = con.createStatement();
			String query = "SELECT * FROM blocks WHERE id='" + blockuid + "'";
			ResultSet set = state.executeQuery(query);
			if(set.next()) {
				if(set.getString("username").equalsIgnoreCase(name)) {
					state.close();
					con.close();
					return true;
				}
			}
		} catch(Exception e) {
			return false;
		}
		return false;
	}
	
	/**
	 * @author xize
	 * @param target - block
	 * @return List<String>()
	 */
	public List<String> getOwners(Block target) {
		List<String> list = new ArrayList<String>();
		Connection con = getConnection();
		try {
			String blockuid = generateBlockUUID(target).toString().replace("-", "");
			Statement state = con.createStatement();
			String query = "SELECT * FROM blocks WHERE id='" + blockuid + "'";
			ResultSet set = state.executeQuery(query);
			if(set.next()) {
				list.add(set.getString("username"));
			}
			state.close();
			con.close();
		} catch(Exception e) {
			return list;
		}
		return list;
	}

	/**
	 * @author xize
	 * @param returns true if the block exist in the database, else false
	 * @param target - block
	 * @return Boolean
	 */
	public boolean isRegistered(Block target) {
		Connection con = getConnection();
		try {
			String blockuid = generateBlockUUID(target).toString().replace("-", "");
			Statement state = con.createStatement();
			String query = "SELECT * FROM blocks WHERE id='" + blockuid + "'";
			ResultSet set = state.executeQuery(query);
			if(set.isBeforeFirst()) {
				state.close();
				con.close();
				return true;
			}
		}catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * @author xize
	 * @param creates the tables for the protections
	 */
	private void createTables() {	
		Connection con = getConnection();
		try {
			String table = "CREATE TABLE IF NOT EXISTS `blocks` ("+ 
					"`id` text NOT NULL," +
					"`username` text NOT NULL, " +
					"`x` int NOT NULL, " +
					"`y` int NOT NULL, " +
					"`z` int NOT NULL, " +
					"`world` text NOT NULL, " +
					"PRIMARY KEY (`id`) " +
					")";
			Statement state = con.createStatement();
			state.executeUpdate(table);
			state.close();
			con.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author xize
	 * @param returns true whenever the database is new, else false
	 * @return Boolean
	 */
	private boolean isNewDatabase() {
		Connection con = getConnection();
		try {
			Class.forName("org.sqlite.JDBC");
			Statement state = con.createStatement();
			ResultSet set = state.executeQuery("SELECT * FROM blocks");
			if(set.isBeforeFirst()) {
				state.close();
				con.close();
				return false;
			}
			state.close();
			con.close();
		} catch (SQLException e) {
			return true;
		} catch(ClassNotFoundException e) {
			xEssentials.getPlugin().log("couldn't find sqlite in craftbukkit, this is probably because you are running a outdated build!", LogType.SEVERE);
		}
		return false;
	}

	/**
	 * @author xize
	 * @param this will return a unique id based on the world and coordinates which cannot be duplicated.
	 * @param block - the block represented in minecraft
	 * @return UUID
	 */
	public UUID generateBlockUUID(Block block) {
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		World w = block.getWorld();
		byte[] bytes = (w.getName()+x+y+z).getBytes();
		UUID uid = UUID.nameUUIDFromBytes(bytes);
		return uid;
	}

	/**
	 * @author xize
	 * @param returns the correct syntax for sql for VALUES() in mysql
	 * @return String
	 */
	private String setValueString(String[] args) {
		StringBuilder build = new StringBuilder();
		for(int i = 0; i < args.length; i++) {
			if(i == (args.length-1)) {
				build.append("'" + args[i] + "'");
			} else {
				build.append("'" + args[i] + "',");
			}
		}
		return build.toString();
	}

	/**
	 * @author xize
	 * @param returns the clean Connection object, since Connection likely works as a Iterator with only one use per time, this factory method will solve it.
	 * @return Connection
	 */
	public Connection getConnection() {
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlite:plugins/xEssentials/databases/protection.db");
			con.setAutoCommit(true);
			return con;
		} catch (Exception e) {
			xEssentials.getPlugin().log("couldn't find sqlite in craftbukkit, this is probably because you are running a outdated build!", LogType.SEVERE);
		}
		return null;
	}

}
