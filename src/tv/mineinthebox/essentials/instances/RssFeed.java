package tv.mineinthebox.essentials.instances;

public class RssFeed {
	
	private String title;
	private String author;
	private String link;
	
	/**
	 * @author xize
	 * @param title
	 * @param author
	 * @param link
	 * @return rssFeed
	 */
	public RssFeed(String title, String author, String link) {
		this.title = title;
		this.author = author;
		this.link = link;
	}
	
	/**
	 * @author xize
	 * @param get the title of this new home page feed!
	 * @return String
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @author xize
	 * @param get the authors name of the thread
	 * @return String
	 */
	public String getAuthor() {
		return author;
	}
	
	/**
	 * @author xize
	 * @param get the full thread url
	 * @return String
	 */
	public String getLink() {
		return link;
	}

}
