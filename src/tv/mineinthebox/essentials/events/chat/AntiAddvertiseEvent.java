package tv.mineinthebox.essentials.events.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class AntiAddvertiseEvent implements Listener {
	
	@EventHandler
	public void onAntiAddvertise(AsyncPlayerChatEvent e) {
		xEssentialsPlayer xp = xEssentials.get(e.getPlayer().getName());
		e.setMessage(ipcheck(e.getMessage(), xp));
	}
	
	public String ipcheck(String a, xEssentialsPlayer xp) {
		String pattern = "\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})?";

		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(a);
		while (matcher.find()) {
			xp.setPermBanned("You are banned for addvertising.", "CONSOLE");
			xp.getPlayer().kickPlayer("You are banned for addvertising.");
		    return a.toString().replace(matcher.group(), "***.***.***:****");
		}
		if(a.toLowerCase().contains("no-ip.org")) {
			xp.setPermBanned("You are banned for addvertising.", "CONSOLE");
			xp.getPlayer().kickPlayer("You are banned for addvertising.");
			return a.toString().toLowerCase().replace("no-ip.org", "*****.***");
		} else if(a.toLowerCase().contains("kicks-ass.net")) {
			xp.setPermBanned("You are banned for addvertising.", "CONSOLE");
			xp.getPlayer().kickPlayer("You are banned for addvertising.");
			return a.toString().toLowerCase().replace("kicks-ass.net", "*********.***");
		}
		String url = "mc\\.([a-z0-9])+\\.(.*?)";
		Pattern compile = Pattern.compile(url.toLowerCase());
		Matcher match = compile.matcher(a.toLowerCase());
		while(match.find()) {
			//log.info(match.group());
			xp.setPermBanned("You are banned for addvertising.", "CONSOLE");
			xp.getPlayer().kickPlayer("You are banned for addvertising.");
			return a.toString().toLowerCase().replace(match.group().toString(), "mc.*****.***");
		}
		String nfoServers = "(.*?)\\.nfoservers\\.com";
		Pattern compileNfo = Pattern.compile(nfoServers.toLowerCase());
		Matcher matchNfo = compileNfo.matcher(a.toLowerCase());
		while(matchNfo.find()) {
			//log.info(match.group());
			xp.setPermBanned("You are banned for addvertising.", "CONSOLE");
			xp.getPlayer().kickPlayer("You are banned for addvertising.");
			return a.toString().toLowerCase().replace(matchNfo.group().toString(), "***.*****.***");
		}
		return a;
	}

}
