package tv.mineinthebox.essentials.commands;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

public class CmdTest {

	private Object getPrivateField(Object object, String field)throws SecurityException,

	NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

		Class<?> clazz = object.getClass();

		Field objectField = clazz.getDeclaredField(field);

		objectField.setAccessible(true);

		Object result = objectField.get(object);

		objectField.setAccessible(false);

		return result;

	}

	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("test")) {
			sender.sendMessage("key:values");
			try {
				Object result = getPrivateField(Bukkit.getServer().getPluginManager(), "commandMap");
				SimpleCommandMap commandMap = (SimpleCommandMap) result;
				Object map = getPrivateField(commandMap, "knownCommands");
				@SuppressWarnings("unchecked")
				HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
				Iterator<Entry<String, Command>> it = knownCommands.entrySet().iterator();
				while(it.hasNext()) {
					Entry<String, Command> entry = it.next();
					if(entry.getValue().getName().equalsIgnoreCase("money")) {
						sender.sendMessage("==|key|value|==");
						sender.sendMessage("alias: " + cmd.getAliases().toString());
						sender.sendMessage("key: " + entry.getKey() + " value: " + entry.getValue());
						sender.sendMessage("aliases: " + entry.getValue().getAliases().toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
