package tv.mineinthebox.essentials.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import tv.mineinthebox.essentials.Configuration;
import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.events.customEvents.CallAfkSchedulerEvent;
import tv.mineinthebox.essentials.events.customEvents.CallChunkMoveEvent;
import tv.mineinthebox.essentials.events.customEvents.CallEssentialsBroadcastEvent;
import tv.mineinthebox.essentials.events.customEvents.CallEssentialsPlayerMoveEvent;
import tv.mineinthebox.essentials.events.customEvents.CallMojangStatus;
import tv.mineinthebox.essentials.events.customEvents.CallPlayerChatSmilleyEvent;
import tv.mineinthebox.essentials.events.customEvents.CallPlayerHighLightEvent;
import tv.mineinthebox.essentials.events.customEvents.CallPlayerOpenBookEvent;
import tv.mineinthebox.essentials.events.customEvents.CallRssFeedEvent;

public class CustomEventHandler {

	public void startCustomEvents() {
		//this will loadup our custom movement for players
		setListener(new CallChunkMoveEvent());
		setListener(new CallEssentialsPlayerMoveEvent());
		setListener(new CallPlayerOpenBookEvent());
		CallAfkSchedulerEvent scheduler = new CallAfkSchedulerEvent();
		setListener(scheduler);
		scheduler.onStartAfkScheduler();
		if(Configuration.getChatConfig().isRssBroadcastEnabled()) {
			CallRssFeedEvent event = new CallRssFeedEvent();
			event.onChatSentRssBroadcast();
		}
		if(Configuration.getChatConfig().isMojangStatusEnabled()) {
			CallMojangStatus status = new CallMojangStatus();
			status.startMojangCheck();
		}
		if(Configuration.getChatConfig().isChatHighLightEnabled()) {setListener(new CallPlayerHighLightEvent());}
		if(Configuration.getChatConfig().isSmilleysEnabled()) {setListener(new CallPlayerChatSmilleyEvent());}
		if(Configuration.getBroadcastConfig().isBroadcastEnabled()) {CallEssentialsBroadcastEvent.start();}
	}

	public void setListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, xEssentials.getPlugin());
	}

}
