package tv.mineinthebox.essentials.configurations;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.enums.ConfigEnum;

public class PlayerConfig {
	
	/**
	 * @author xize
	 * @param when enabled players have different inventories per game-mode
	 * @return boolean
	 */
	public boolean isSeperatedInventorysEnabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "useSeperatedInventorys");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param when enabled the inventories of players get saved for /invsee (offline checking)
	 * @return boolean
	 */
	public boolean isSaveInventoryEnabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "savePlayerInventory");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param when enabled players are automatic god-mode while in afk, mobs will not target you.
	 * @return boolean
	 */
	public boolean isGodModeInAfkEnabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "godmodeInAfk");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param when enabled players will hear a steve hurt sound from minecraft classic note that in 1.7.2 this sound has been removed, mojang may plans again to readd it according their tweets.
	 * @return boolean
	 * @deprecated
	 */
	public boolean isSteveHurtSoundEnabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "steve-hurt-sound");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param when true the hunger is cancelled
	 * @return Boolean
	 */
	public boolean isHungerCancelled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "hunger");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param when true the players keeps the items in his inventory even while being death!
	 * @return Boolean
	 */
	public boolean isKeepInventoryOnDeathEnabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "keepinv");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param when enabled hostile entitys can steal players their head when the player died due the cause of them
	 * @return boolean
	 */
	public boolean isCanEntityStealHatOnPlayersDeath() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "entitysCanUseHeadOnPlayerDeath");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param when enabled we can use realistic glass this feature means that velocity's can break glass.
	 * @return boolean
	 */
	public boolean isRealisticGlassEnabled() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "enableRealisticGlass");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the option is enabled and players are allowed to set custom homes till the max
	 * @return boolean
	 */
	public boolean canUseMoreHomes() {
		Boolean bol = (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "canDefaultUseMoreHomes");
		return bol;
	}
	
	/**
	 * @author xize
	 * @param returns the amount of homes a default player is allowed to set
	 * @return int
	 */
	public int getMaxHomesAllowed() {
		int i = (Integer) Configuration.getConfigValue(ConfigEnum.PLAYER, "maxHomes");
		return i;
	}
	
	/**
	 * @author xize
	 * @param returns true whenever portal creation is disabled
	 * @return Boolean
	 */
	public boolean isPortalsDisabled() {
		return (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "DisablePortals");
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the custom size of portals is disabled!
	 * @return Boolean
	 */
	public boolean isCustomPortalSizeDisabled() {
		return (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "DisableCustomSize");
	}
	
	/**
	 * @author xize
	 * @param whenever if it is disabled, no achievements get broadcasted!
	 * @return boolean
	 */
	public boolean isBroadcastAchievementsEnabled() {
		return (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "PlayerAchievements");
	}
	
	/**
	 * @author xize
	 * @param returns true when enabled otherwise false
	 * @return booelan
	 */
	public boolean isAutoAnvilEnabled() {
		return (Boolean) Configuration.getConfigValue(ConfigEnum.PLAYER, "anvil");
	}

}
