package com.cydoniarp.amd3th.permittr;

import java.io.File;
import java.io.IOException;
import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Permittr extends JavaPlugin{
	protected static Logger log;
	protected HashMap<String, Permit> permits = new HashMap<String, Permit>();
	protected Property player;
	protected static String name;
	protected static String version;
	protected static String df;
	protected String players;
	protected String permitd;
	protected String logs;
	protected pCom worker;
	public static Permit permit;
	
	private void mkDir(String...d) {
        for (String f : d) {
            try {
                new File(f).mkdir();
            } catch (SecurityException e) {
                log.severe('['+name+"] Failed to create folder: "+f);
                e.printStackTrace();
            }
        }
    }
	
	// ON DISABLE
	public void onDisable() {
        log.info("["+name+"] v"+version+" has been disabled");
    }

	// ON ENABLE
	public void onEnable(){
	    log = getServer().getLogger();
		
		// Register events
		PluginManager pm = getServer().getPluginManager();
		final PerBlockListener bl = new PerBlockListener();
		pm.registerEvent(Type.PLAYER_JOIN, new PerPlayerListener(), Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_BREAK, bl, Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_PLACE, bl, Priority.Normal, this);
		PluginDescriptionFile pdf = getDescription();
		name = pdf.getName();
		version = pdf.getVersion();
		df = getDataFolder().toString();
		
		log.info("["+name+"] v"+pdf.getVersion()+" has been enabled.");
		
		// Make folders as needed
		mkDir(df, logs, permitd, players);
		
		//COMMANDS
		this.worker = new pCom(this);
		getCommand("permit").setExecutor(new CommandExecutor(){
			public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
				if (sender instanceof Player){
					Player pl = ((Player)sender);
					/* Not sure what was happening here...
					if(args.length == 4){
						if (args[0].equalsIgnoreCase("create") && player.){
							//CREATE CODE
							String pName = args[1];
							int am = Integer.parseInt(args[2]);
							int pr = Integer.parseInt(args[3]);
							Permittr.this.worker.permCreate(pl, pName, am, pr);
							return true;
						}
					}
					*/
					if(args.length == 3){
						if (args[0].equalsIgnoreCase("add") && pl.hasPermission("permit.add")){
							//ADD CODE
							String pName = args[1];
							String it = args[2];
							Permittr.this.worker.permAdd(pl, pName, it);
							return true;
						}
					}
					/* Fix this up too
					if(args.length == 2){
						if (args[0].equalsIgnoreCase("buy") && pl.hasPermission("permit.buy")){
							//BUY CODE
							String pName = args[1];
							int price = Permittr.this.worker.priceGet(pName);
							int am = Permittr.this.worker.durGet(pName);
							if(account.getBalance() >= price){
								if (player.keyExists(pName)){
									int cDur = player.getInt(pName);
									int nDur = cDur + am;
									player.setInt(pName, nDur);
									account.subtract(price);
									((Player)sender).sendMessage(ChatColor.GRAY+"Time has been edded to your "+ChatColor.GOLD+pName+ChatColor.GRAY+" permit.");
									return true;
								}else{
									player.setInt(pName, am);
									((Player)sender).sendMessage(ChatColor.GRAY+"You now have a "+ChatColor.GOLD+pName+ChatColor.GRAY+" permit.");
									account.subtract(am);
									return true;
								}
								
							}else{
								((Player)sender).sendMessage(ChatColor.RED+"You do not have enough money to purchase that permit.");
							}
						}
						if (args[0].equalsIgnoreCase("check") && pl.hasPermission("permit.check")){
							//CHECK CODE
							String pName = args[1];
							Permittr.this.worker.permCheck(pl, pName);
							return true;
						}
					}
					*/
					if(args.length == 1){
						if (args[0].equalsIgnoreCase("list") && pl.hasPermission("permit.list")){
							//LIST CODE
							Permittr.this.worker.permList(pl);
							return true;
						}
					}
				}
				return false;
			}
		});
	}
}
