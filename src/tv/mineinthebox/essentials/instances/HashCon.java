package tv.mineinthebox.essentials.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import tv.mineinthebox.essentials.utils.xEssentialsDB;

public class HashCon extends HashMap<String, Object> {

	private static final long serialVersionUID = 2231019440630840876L;
	
	/*
	 * ################################
	 * #     HashCon made by xize     #
	 * ################################
	 *   a fusion/bridge between a HashMap, and a YamlConfiguration and sqlite.
	 *   since I didn't wanted to break much of the old backend I decided to make a fusion between these objects.
	 *   the idea is to make the system exactly working as how YamlConfiguration worked for me but then through a HashMap.
	 *   this will help me better manage new features and still hold the more use friendly system, with sqlite as backend.
	 */

	private xEssentialsDB db;
	
	public HashCon(xEssentialsDB db) {
		super(db.getMap());
		this.db = db;
	}
	
	public boolean contains(String obj) { 
		return containsKey(obj);
	}
	
	public boolean isSet(String obj) {
		return containsKey(obj);
	}
	
	public String getString(String obj) {
		return (String)get(obj);
	}
	
	public int getInt(String obj) {
		return (Integer)get(obj);
	}
	
	public Long getLong(String obj) {
		return (Long)get(obj);
	}
	
	public Double getDouble(String obj) {
		return (Double)get(obj);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getList(String obj) {
		return (List<Object>)get(obj);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getStringList(String obj) {
		return (List<String>)get(obj);
	}
	
	public Boolean getBoolean(String obj) {
		return (Boolean)get(obj);
	}
	
	public List<String> getConfigurationSection(String key, boolean showkeys) {
		List<String> list = new ArrayList<String>();
		if(showkeys) {
			Iterator<Entry<String, Object>> it = entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, Object> entry = it.next();
				if(entry.getKey().startsWith(key)) {
					list.add(entry.getKey());
				}
			}
		} else {
			Iterator<Entry<String, Object>> it = entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, Object> entry = it.next();
				if(entry.getKey().startsWith(key)) {
					list.add(entry.getKey().replace(key+".", ""));
				}
			}
		}
		return list;
	}
	
	public void set(String key, Object obj) {
		if(obj == null) {
			remove(key);
		} else {
			put(key, obj);	
		}
	}
	
	public void save() {
		db.saveMap(this);
	}
	
	public void load() {
		clear();
		super.putAll(db.getMap());
	}


}
