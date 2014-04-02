package tv.mineinthebox.essentials.greylist;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.LogType;

public class GreyListServer implements Runnable {
	
	private int port;

	private volatile Server server;
	private Thread web;
	
	public GreyListServer(int port) {
		this.port = port;
	}

	public void createServer() {
		this.server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		server.addConnector(connector);
		server.setHandler(new GreyListServlet());
		Thread webserverThread = new Thread(this);
		webserverThread.start();
		xEssentials.getPlugin().log("Greylist server listening on port " + port, LogType.INFO);
		this.web = webserverThread;
	}

	public void run() {
		try {
			this.server.start();
			this.server.join();
		} catch (Exception var2) {
			xEssentials.getPlugin().log("Cannot start web server at port " + this.port + "!", LogType.SEVERE);
		}

	}

	@SuppressWarnings("deprecation")
	public void disable() {
		try {
			this.server.stop();
			web.stop();
			xEssentials.getPlugin().log("greylist server has been stopped", LogType.INFO);
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
		GreyListServer other = (GreyListServer) obj;
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
