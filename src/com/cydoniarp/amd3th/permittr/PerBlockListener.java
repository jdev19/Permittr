package com.cydoniarp.amd3th.permittr;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PerBlockListener extends BlockListener{
    
	public PerBlockListener(){}
	
	public void onBlockPlace(final BlockPlaceEvent e){
		final Player p = e.getPlayer();
		Material mat = e.getBlock().getType();
		int ID = mat.getId();
		if(Permittr.permit.hasId(ID)){
			String name = Permittr.permit.get(key);
			if(Permittr.permit.get("amount") < 1){
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You need a permit to do that. Use /permit list to see available permits.");
			}else{
				// I dunno chief...
			}
		}
	}
	
	public void onBlockBreak(final BlockBreakEvent e){
		final Player p = e.getPlayer();
		Material material = e.getBlock().getType();
	}
}
