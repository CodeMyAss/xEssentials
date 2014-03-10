package tv.mineinthebox.essentials.greylist;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.GreyListCause;
import tv.mineinthebox.essentials.enums.HookEnum;
import tv.mineinthebox.essentials.events.customEvents.OfflinePlayerGreyListedEvent;
import tv.mineinthebox.essentials.events.customEvents.PlayerGreyListedEvent;
import tv.mineinthebox.essentials.hook.BPermissionsHook;
import tv.mineinthebox.essentials.hook.GroupManagerHook;
import tv.mineinthebox.essentials.hook.PermissionsExHook;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;

public class GreyListServlet extends AbstractHandler {

	/*
	 this is a example how the greylist works through php, we can use the json which gets returned by jetty in this way.
	 currently we have 3 possible return types:
	 
	 success = when the player change from group,
	 greylisted = when the player already is greylisted
	 notexist = if the player has never played before

	 php callback example:

	 <?php
		$string = file_get_contents("http://127.0.0.1:8001/adduser/Xeph0re");
		$json = json_decode($string, true);
		$args = $json["xEssentials"]["response"];
		if($args == "success") {
			echo "player has been promoted";
		} else if($args == "greylisted") {
			echo "player already is greylisted";
		} else if($args == "notexist") {
			echo "player has never played before";
		}
	?>
	 */

	public synchronized void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException	{
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		if(request.getRequestURI().startsWith("/adduser/")) {
			if(request.getRequestURI().length() > "/adduser/".length()) {
				String user = request.getRequestURI().substring("/adduser/".length());
				if(xEssentials.isEssentialsPlayer(user)) {
					xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(user);
					if(!off.isGreyListed()) {
						response.setContentType("application/json");
						response.getWriter().write("{\"xEssentials\": {\"response\": \"success\"}}");
						off.setGreyListed(true);
						if(off.getPlayer() instanceof Player) {
							off.getPlayer().sendMessage(ChatColor.GREEN + "you are successfully promoted to " + Configuration.getGrayListConfig().getGroup());
							if(Bukkit.getPluginManager().isPluginEnabled("bPermissions")) {
								String oldGroup = BPermissionsHook.getGroup(user);
								String newgroup = Configuration.getGrayListConfig().getGroup();
								BPermissionsHook.setGroup(user, Configuration.getGrayListConfig().getGroup());
								Bukkit.getPluginManager().callEvent(new PlayerGreyListedEvent(off.getPlayer(), newgroup, oldGroup, HookEnum.BPERMISSIONS, GreyListCause.SITE));
							} else if(Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
								String oldGroup = PermissionsExHook.getGroup(user);
								String newgroup = Configuration.getGrayListConfig().getGroup();
								PermissionsExHook.setGroup(user, Configuration.getGrayListConfig().getGroup());
								Bukkit.getPluginManager().callEvent(new PlayerGreyListedEvent(off.getPlayer(), newgroup, oldGroup, HookEnum.PERMISSIONSEX, GreyListCause.SITE));
							} else if(Bukkit.getPluginManager().isPluginEnabled("GroupManager")) {
								String oldGroup = GroupManagerHook.getGroup(user);
								String newgroup = Configuration.getGrayListConfig().getGroup();
								GroupManagerHook.setGroup(user, Configuration.getGrayListConfig().getGroup());
								Bukkit.getPluginManager().callEvent(new PlayerGreyListedEvent(off.getPlayer(), newgroup, oldGroup, HookEnum.GROUPMANAGER, GreyListCause.SITE));
							}
						} else {
							if(Bukkit.getPluginManager().isPluginEnabled("bPermissions")) {
								String oldGroup = BPermissionsHook.getGroup(user);
								String newgroup = Configuration.getGrayListConfig().getGroup();
								BPermissionsHook.setGroup(user, Configuration.getGrayListConfig().getGroup());
								Bukkit.getPluginManager().callEvent(new OfflinePlayerGreyListedEvent(user, newgroup, oldGroup, HookEnum.BPERMISSIONS, GreyListCause.SITE));
							} else if(Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
								String oldGroup = PermissionsExHook.getGroup(user);
								String newgroup = Configuration.getGrayListConfig().getGroup();
								PermissionsExHook.setGroup(user, Configuration.getGrayListConfig().getGroup());
								Bukkit.getPluginManager().callEvent(new OfflinePlayerGreyListedEvent(user, newgroup, oldGroup, HookEnum.PERMISSIONSEX, GreyListCause.SITE));
							} else if(Bukkit.getPluginManager().isPluginEnabled("GroupManager")) {
								String oldGroup = GroupManagerHook.getGroup(user);
								String newgroup = Configuration.getGrayListConfig().getGroup();
								GroupManagerHook.setGroup(user, Configuration.getGrayListConfig().getGroup());
								Bukkit.getPluginManager().callEvent(new OfflinePlayerGreyListedEvent(user, newgroup, oldGroup, HookEnum.GROUPMANAGER, GreyListCause.SITE));
							}
						}
					} else {
						response.setContentType("application/json");
						response.getWriter().write("{\"xEssentials\": {\"response\": \"greylisted\"}}");
					}
				} else {
					response.setContentType("application/json");
					response.getWriter().write("{\"xEssentials\": {\"response\": \"notexist\"}}");
				}
			} else {
				response.getWriter().write("invalid greylist url.");
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}

