package tv.mineinthebox.essentials.auction.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoginPage {
	
	public String ParseLoginContent() {
		StringBuilder build = new StringBuilder();
		build.append("<div id=\"login\"/>");
			build.append("<form name=\"loginform\" method=\"post\" action=\"\"/>\n" +
						"<input type=\"text\" id=\"userbtn\" name=\"username\" value=\"user\"/>\n" +
						"<input type=\"password\" id=\"pwdbtn\" name=\"password\" value=\"password\"/>\n" +
						"<input type=\"submit\" id=\"submitbtn\" name=\"submit\" value=\"go\"/>\n" +
					"</form>");
		build.append("</div>");
		return build.toString();
	}
	
	public boolean hasTag(File f) {
		try {
			BufferedReader buff = new BufferedReader(new FileReader(f));
			String line;
			try {
				while((line = buff.readLine()) != null) {
					if(line.contains("{login}")) {
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
