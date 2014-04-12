package tv.mineinthebox.essentials.auction.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AvaterPage {
	
	/**
	 * @author xize
	 * @param user - the user who is logged in
	 * @param isLogged - if the user is logged else, don't print a avater
	 * @return String
	 */
	public String parseAvater(String user, boolean isLogged) {
		StringBuilder build = new StringBuilder();
		if(isLogged) {
			build.append("<div id=\"avater\"/><img src=\"http://skinserver.nl/" + user + ".png\" width=\"94px\" height=\"94px\"/></div>");
		} else { 
			build.append("");
		}
		return build.toString();
	}

	/**
	 * @author xize
	 * @param user - the user on request, this will not be the user who is logged in, but more likely the profile owner of the visited auction
	 * @return String
	 */
	public String parseAvater(String user) {
		StringBuilder build = new StringBuilder();
		build.append("<div id=\"avater\"/><img src=\"http://skinserver.nl/" + user + ".png\" width=\"94px\" height=\"94px\"/></div>");
		return build.toString();
	}

	/**
	 * @author xize
	 * @param f - the mirrored file from the http request.
	 * @param returns true when the {avater} tag has been found else, false
	 * @return Boolean
	 */
	public boolean hasTag(File f) {
		try {
			BufferedReader buff = new BufferedReader(new FileReader(f));
			String line;
			try {
				while((line = buff.readLine()) != null) {
					if(line.contains("{avater}")) {
						buff.close();
						return true;
					}
				}
				buff.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
