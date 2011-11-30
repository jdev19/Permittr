package com.cydoniarp.amd3th.permittr;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PerPlayerListener extends PlayerListener {
    
	public PerPlayerListener(){}
	
	public void onPlayerJoin(final PlayerJoinEvent e){
		final Player p = e.getPlayer();
		final String name = p.getName();
	}
}
