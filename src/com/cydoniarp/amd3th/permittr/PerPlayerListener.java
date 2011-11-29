package com.cydoniarp.amd3th.permittr;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PerPlayerListener extends PlayerListener {
	protected Permittr plugin;
	
	public PerPlayerListener(Permittr plugin){
		this.plugin = plugin;
	}
	
	public void onPlayerJoin(PlayerJoinEvent event){
		Player plyr = event.getPlayer();
		String name = plyr.getName();
	}
}
