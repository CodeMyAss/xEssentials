package tv.mineinthebox.essentials.instances;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import tv.mineinthebox.essentials.xEssentials;

public class MarketItem extends ItemStack implements Serializable {
	
	private static final long serialVersionUID = 7237293935062333120L;
	
	private Double money;
	private Long date;
	private String owner;
	private String imagePath;
	
	/**
	 * @author xize
	 * @param custom ItemStack which works better for shops!
	 * @param stack - the ItemStack
	 * @return MarketItem
	 */
	public MarketItem(ItemStack stack) {
		super(stack);
		this.imagePath = "images/"+stack.getType().name().toLowerCase()+","+stack.getDurability()+".png";
	}
	
	/**
	 * @author xize
	 * @param sets how much money this cost!
	 * @return void
	 */
	public void setCosts(Double money) {
		this.money = money;
	}
	
	/**
	 * @author xize
	 * @param returns the how much a item costs
	 * @return Double
	 */
	public Double getMoney() {
		return money;
	}
	
	/**
	 * @author xize
	 * @param date - the date when this item is created
	 * @return void
	 */
	public void setCreationDate(Date date) {
		this.date = date.getTime();
	}
	
	/**
	 * @author xize
	 * @param returns the date whenever the item is created
	 * @return Date
	 */
	public Long getCreationDate() {
		return date;
	}
	
	/**
	 * @author xize
	 * @param off - the offline player instance
	 * @return void
	 */
	public void setOwner(String uuid) {
		this.owner = uuid;
	}
	
	/**
	 * @author xize
	 * @param returns the owner of this item being sold
	 * @return xEssentialsOfflinePlayer
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * @author xize
	 * @param returns the product id through a UUID
	 * @return UUID
	 */
	public String getProductId() {
		return UUID.nameUUIDFromBytes((owner+date).getBytes()).toString().replace("-", "");
	}
	
	/**
	 * @author xize
	 * @param returns the image icon of this item
	 * @return String
	 */
	public String getItemIconImage() {
		return imagePath;
	}
	
	/**
	 * @author xize
	 * @param returns true whenever the player/auction holder is online
	 * @return Boolean
	 */
	public boolean isPlayerOnline() {
		return xEssentials.contains(owner);
	}
	
	/**
	 * @author xize
	 * @param returns the offlineplayer
	 * @return xEssentialsOfflinePlayer
	 */
	public xEssentialsOfflinePlayer getOfflinePlayer() {
		return xEssentials.getOfflinePlayer(owner);
	}
	
	/**
	 * @author xize
	 * @param returns the xEssentialsPLayer
	 * @return xEssentialsPlayer
	 */
	public xEssentialsPlayer getPlayer() {
		return xEssentials.get(owner);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MarketItem other = (MarketItem) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (imagePath == null) {
			if (other.imagePath != null)
				return false;
		} else if (!imagePath.equals(other.imagePath))
			return false;
		if (money == null) {
			if (other.money != null)
				return false;
		} else if (!money.equals(other.money))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		return "MarketItem [money=" + money + ", date=" + date + ", owner="
				+ owner + ", imagePath=" + imagePath + ", getMoney()="
				+ getMoney() + ", getCreationDate()=" + getCreationDate()
				+ ", getOwner()=" + getOwner() + ", getProductId()="
				+ getProductId() + ", getItemIconImage()=" + getItemIconImage()
				+ ", isPlayerOnline()=" + isPlayerOnline()
				+ ", getOfflinePlayer()=" + getOfflinePlayer()
				+ ", getPlayer()=" + getPlayer() + ", getType()=" + getType()
				+ ", getTypeId()=" + getTypeId() + ", getAmount()="
				+ getAmount() + ", getData()=" + getData()
				+ ", getDurability()=" + getDurability()
				+ ", getMaxStackSize()=" + getMaxStackSize() + ", toString()="
				+ super.toString() + ", hashCode()=" + hashCode()
				+ ", getEnchantments()=" + getEnchantments() + ", serialize()="
				+ serialize() + ", getItemMeta()=" + getItemMeta()
				+ ", hasItemMeta()=" + hasItemMeta() + ", getClass()="
				+ getClass() + "]";
	}
	
	
	
}
