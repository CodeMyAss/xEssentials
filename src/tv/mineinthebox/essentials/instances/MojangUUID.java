package tv.mineinthebox.essentials.instances;

import org.bukkit.entity.Player;

public class MojangUUID {
    private String id;


    public MojangUUID(Player p) {
            this.id = p.getUniqueId().toString().replace("-", "");
    }
    
    public String getUUID() {
            return id;
    }
}