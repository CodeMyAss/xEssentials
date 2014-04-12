package tv.mineinthebox.essentials.instances;

import java.util.Date;

import tv.mineinthebox.essentials.xEssentials;

public class AuctionBidUser implements Comparable<AuctionBidUser> {

	private String user;
	private String productkey;
	private Date date;
	private Double bid;
	
	/**
	 * @author xize
	 * @param user - the bidder
	 * @param productkey - the product key
	 * @param this object will used when they have started a bid
	 * @return AuctionBidUser
	 */
	public AuctionBidUser(String user, String productkey, Double bid) {
		this.user = user;
		this.productkey = productkey;
		this.date = new Date(System.currentTimeMillis());
		this.bid = bid;
	}
	
	/**
	 * @author xize
	 * @param returns the bid the player has made
	 * @return Double
	 */
	public Double getBid() {
		return bid;
	}
	
	/**
	 * @author xize
	 * @param returns the user name
	 * @return String
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * @author xize
	 * @param returns the auctions product key
	 * @return String
	 */
	public String getProductKey() {
		return productkey;
	}
	
	/**
	 * @author xize
	 * @param returns the date, this date also allows the ordering.
	 * @return Date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * @author xize
	 * @param returns the offline player instance
	 * @return xEssentialsOfflinePlayer
	 */
	public xEssentialsOfflinePlayer getOfflinePlayer() {
		return xEssentials.getOfflinePlayer(getUser());
	}
	
	@Override
	public int compareTo(AuctionBidUser o) {
		if(o instanceof AuctionBidUser) {
			AuctionBidUser user = (AuctionBidUser) o;
			return user.getBid().compareTo(o.getBid());
		}
		throw new ClassCastException();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((productkey == null) ? 0 : productkey.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuctionBidUser other = (AuctionBidUser) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (productkey == null) {
			if (other.productkey != null)
				return false;
		} else if (!productkey.equals(other.productkey))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
