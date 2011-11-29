package com.cydoniarp.amd3th.permittr;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PerBlockListener extends BlockListener{
	public Permittr plugin;
	
	public PerBlockListener(Permittr plugin){
		this.plugin = plugin;
	}
	
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		Material mat = event.getBlock().getType();
		int ID = mat.getId();
		if(Permittr.permit.hasId(ID)){
			String name = Permittr.permit.get(key)
			if(Permittr.permit.get("amount") < 1){
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You need a permit to do that. Use /permit list to see available permits.");
			}else{
				
			}
		}
	}
	
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		Material mat = event.getBlock().getType();
		
	}
}
