package com.cydoniarp.amd3th.permittr;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.nijiko.coelho.iConomy.iConomy;

public class PluginListener extends ServerListener{
	public PluginListener() { }
	
	public void onPluginEnabled(PluginEnableEvent event){
		if(Permittr.getiConomy() == null){
			Plugin iConomy = Permittr.getBukkitServer().getPluginManager().getPlugin("iConomy");
			
			if(iConomy != null){
				if(iConomy.isEnabled()){
					Permittr.setiConomy((iConomy)iConomy);
					System.out.println("[Permittr] Successfully linked with iConomy.");
				}
			}
		}
	}

}
