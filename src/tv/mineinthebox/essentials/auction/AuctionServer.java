package tv.mineinthebox.essentials.auction;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.LogType;

public class AuctionServer implements Runnable {

	private int port;

	private volatile Server server;
	private Thread web;

	public AuctionServer(int port) {
		this.port = port;
	}

	public void createServer() {
		 this.server = new Server();
	      SelectChannelConnector connector = new SelectChannelConnector();
	      connector.setPort(this.port);
	      this.server.addConnector(connector);
	      ResourceHandler resource_handler = new ResourceHandler();
	      resource_handler.setCacheControl("max-age=0,public");
	      resource_handler.setDirectoriesListed(false);
	      resource_handler.setWelcomeFiles(new String[]{"index.html"});
	      resource_handler.setResourceBase("./plugins/xEssentials/auction/");
	      HandlerList handlers = new HandlerList();
	      handlers.setHandlers(new Handler[]{resource_handler, new AuctionContentHandler()});
	      this.server.setHandler(handlers);
	      this.web = new Thread(this);
	      this.web.start();
	}

	/**
	 * @author xize
	 * @param runs the new thread
	 */
	public void run() {
		try {
			this.server.start();
			this.server.join();
		} catch (Exception var2) {
			xEssentials.getPlugin().log("Cannot start web server at port " + this.port + "!", LogType.SEVERE);
		}

	}

	/**
	 * @author xize
	 * @param shutdown the server
	 * @return void
	 */
	@SuppressWarnings("deprecation")
	public void disable() {
		try {
			this.server.stop();
			web.stop();
			xEssentials.getPlugin().log("auction web server has been stopped", LogType.INFO);
		} catch (Exception var2) {
			xEssentials.getPlugin().log("Cannot stop web server at port " + this.port + "!", LogType.SEVERE);
		}

	}

	/**
	 * @author xize
	 * @param returns true whenever the server runs
	 * @return boolean
	 */
	public boolean isRunning() {
		if(server == null) {
			return false;
		}
		return server.isRunning();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + port;
		result = prime * result + ((server == null) ? 0 : server.hashCode());
		result = prime * result + ((web == null) ? 0 : web.hashCode());
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
		AuctionServer other = (AuctionServer) obj;
		if (port != other.port)
			return false;
		if (server == null) {
			if (other.server != null)
				return false;
		} else if (!server.equals(other.server))
			return false;
		if (web == null) {
			if (other.web != null)
				return false;
		} else if (!web.equals(other.web))
			return false;
		return true;
	}

}
