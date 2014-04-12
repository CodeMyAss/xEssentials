package tv.mineinthebox.essentials.auction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bukkit.entity.Player;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.auction.pages.AvaterPage;
import tv.mineinthebox.essentials.auction.pages.ContentPage;
import tv.mineinthebox.essentials.auction.pages.LoginPage;
import tv.mineinthebox.essentials.auction.pages.UserPage;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.instances.xEssentialsOfflinePlayer;
import tv.mineinthebox.essentials.utils.Crypt;

public class AuctionContentHandler extends AbstractHandler {

	/*
	 * these are the reminders for myself, the url calls which are important for myself.
	 * 
	 *   - /index.html - the index where the {content} tag behaves as a last recent submitted items catalog
	 *   when logged in the player can also place a bid, however the auction owner cannot and instead a delete button appears.
	 *   
	 *   - /login.html - a simple login system can be used and is called {login}
	 *   
	 *   -/login.html?user=someuser&password=somepass - is called in a $_POST way, and will validate the existence of a username, and password
	 *   
	 *   - /logout.html - logs the player out
	 *   
	 *   - /user.html?player={player} - shows all items the player sells in the auction
	 *   
	 *   - /placebid.html?bid={player name}&productid={UUID} - fires the order
	 *   
	 *   - /remove.html?delproduct={player name}&productid={UUID}
	 *   
	 */

	private final AvaterPage avater = new AvaterPage();
	private final ContentPage content = new ContentPage();
	private final UserPage usercontent = new UserPage();
	private final LoginPage logincontent = new LoginPage();

	public synchronized void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if(request.getRequestURI().equalsIgnoreCase("/") || request.getRequestURI().equalsIgnoreCase("index.html")) {
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(200);
			baseRequest.setHandled(true);
			File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "auction" + File.separator + "index.html");
			if(content.hasTag(f)) {
				response.getWriter().write(parseContent(f, request.getCookies(), null));
			} else {
				response.getWriter().write("there is no tag {content} found on this page, please read the documentation.");
			}
		} else if(request.getRequestURI().equalsIgnoreCase("login.html") || request.getRequestURI().startsWith("login.html?user=")) {
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(200);
			baseRequest.setHandled(true);
			if(isLoggedIn(request.getCookies())) {
				response.getWriter().write("you are already logged in! <a href=\"javascript:window.history(-1)\"/>click here to go back</a>");
				return;
			}
			if(request.getRequestURI().equalsIgnoreCase("login.html")) {
				File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "auction" + File.separator + "login.html");
				if(logincontent.hasTag(f)) {
					response.getWriter().write(parseContent(f, request.getCookies(), null));
				} else {
					response.getWriter().write("there is no tag {login} found on this page, please read the documentation.");
				}
			} else {
				String user = request.getParameter("user");
				String pass = request.getParameter("password");
				
				if(xEssentials.isEssentialsPlayer(user)) {
					xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(user);
					if(off.hasAuctionPassword()) {
						try {
							if(Crypt.CryptToSaltedSha512(pass).equalsIgnoreCase(off.getAuctionPassword())) {
								Cookie cookie = new Cookie(user, off.getAuctionSession());
								response.addCookie(cookie);
								response.sendRedirect("index.html");
							} else {
								response.getWriter().write("invalid password!");
							}
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchProviderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						response.getWriter().write("this user is not registered!");
					}
				} else {
					response.getWriter().write("this user does not exist!");
				}
			}
		} else if(request.getRequestURI().startsWith("/user/")) {
			String user = request.getRequestURI().substring("/user/".length()).replace("/", "");
			if(xEssentials.isEssentialsPlayer(user)) {
				
			}
		} else if(request.getRequestURI().startsWith("/placebid=")) {

		} else if(request.getRequestURI().startsWith("/remove=")) {

		} else if(request.getRequestURI().endsWith(".js") || request.getRequestURI().endsWith(".css") || request.getRequestURI().endsWith(".jpg") || request.getRequestURI().endsWith(".gif") || request.getRequestURI().endsWith(".bmp") || request.getRequestURI().endsWith(".png")) {
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(200);
			baseRequest.setHandled(true);
		} else {
			response.setStatus(HttpStatus.NOT_FOUND_404);
		}
	}

	/*if(response.getStatus() == HttpServletResponse.SC_OK) {	
	if(request.getRequestURI().equalsIgnoreCase("/index.html") || request.getRequestURI().equalsIgnoreCase("/")) {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(200);
		baseRequest.setHandled(true);
		File f = new File(xEssentials.getPlugin().getDataFolder() + File.separator + "auction" + File.separator + "index.html");
		if(f.exists()) {
			if(hasContentTag(f)) {
				response.getWriter().write(getModifiedPage(f, baseRequest.getCookies()));
			} else {
				response.getWriter().write("no {content} tag has been found, this software can only be used for auctions not normal websites.");
			}
		} else {
			response.getWriter().write("no index found, please make sure you add a index.html with {content} !");
		}
	} else if(request.getRequestURI().endsWith(".css") || request.getRequestURI().endsWith(".js") || request.getRequestURI().endsWith(".jpg") || request.getRequestURI().endsWith(".png") || request.getRequestURI().endsWith(".bmp") || request.getRequestURI().endsWith(".gif")) {
		response.setContentType("text/html;charset=utf-8");
	}
}
	 */

	/**
	 * @author xize
	 * @param f - the file, also known as the webpage
	 * @return  String - the modified page, with the replacement for {content}
	 */
	public String parseContent(File f, Cookie[] cookies, String vistedUser) {
		Cookie cookie = cookies[0];
		boolean loggedin = false;
		boolean isAdmin = false;
		if(cookie instanceof Cookie) {
			if(xEssentials.contains(cookie.getName())) {
				xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(cookie.getName());
				if(off.getAuctionSession().equalsIgnoreCase(cookie.getValue())) {
					loggedin = true;
					if(off.getPlayer() instanceof Player) {
						isAdmin = off.getPlayer().hasPermission(PermissionKey.IS_ADMIN.getPermission());
					}
				}
			}
		}
		try {
			BufferedReader buff = new BufferedReader(new FileReader(f));
			String line;
			StringBuilder build = new StringBuilder();
			try {
				while((line = buff.readLine()) != null) {
					if(line.contains("{content}")) {
						line = line.replaceAll("\\{content\\}", content.parseRecentContent(loggedin, isAdmin));
					}
					if(line.contains("{usercontent}")) {
						line = line.replaceAll("\\{usercontent\\}", usercontent.parseUserContent(vistedUser, loggedin, isAdmin));
					}
					if(line.contains("{login}")) {
						line = line.replaceAll("\\{login\\}", logincontent.ParseLoginContent());
					}
					if(line.contains("{avater}")) {
						line = line.replaceAll("\\{avater\\}", avater.parseAvater(vistedUser, loggedin));
					}
					build.append(line);
				}
				buff.close();
				return build.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isLoggedIn(Cookie[] cookies) {
		Cookie cookie = cookies[0];
		if(cookie instanceof Cookie) {
			if(xEssentials.isEssentialsPlayer(cookie.getName())) {
				xEssentialsOfflinePlayer off = xEssentials.getOfflinePlayer(cookie.getName());
				if(off.hasAuctionPassword()) {
					if(off.getAuctionSession().equalsIgnoreCase(cookie.getValue())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
