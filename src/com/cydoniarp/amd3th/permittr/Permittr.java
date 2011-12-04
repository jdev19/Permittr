package com.cydoniarp.amd3th.permittr;

import java.awt.List;
import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Event.Priority;
//import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Permittr extends JavaPlugin {
    protected HashMap<String, Configuration> permits = new HashMap<String, Configuration>();
    protected static Configuration config;
    protected static FileConfiguration perms;
    protected static String version;
    protected static String name;
    protected static Logger log;
    protected static String df;
    protected String pmf;
    
    private void mkDirs(final String...d) {
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
        log.info("["+name+"] v"+version+" has been enabled.");
        
        perms = getConfig();
        // Create logger
        log = getServer().getLogger();
        
        // Config
        
        
        // Register events
        final PluginManager pm = getServer().getPluginManager();
        //final PerBlockListener bl = new PerBlockListener();
        pm.registerEvent(Type.PLAYER_JOIN, new PerPlayerListener(), Priority.Normal, this);
        //pm.registerEvent(Type.BLOCK_BREAK, bl, Priority.Normal, this);
        //pm.registerEvent(Type.BLOCK_PLACE, bl, Priority.Normal, this);
        name = getDescription().getName();
        version = getDescription().getVersion();
        df = getDataFolder().toString();
        
        // Make folders as needed
        mkDirs(df);
        
        //COMMANDS
        getCommand("permit").setExecutor(new CommandExecutor(){
            public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
                if (sender instanceof Player){
                    final Player p = ((Player)sender);
                    /* Create a permit					*/
                    
                    if (args[0].equalsIgnoreCase("create") && p.hasPermission("permit.create")){
                        //CREATE CODE
                        final String pName = args[1];
                        final int am = Integer.parseInt(args[2]);
                        final int pr = Integer.parseInt(args[3]);
                        final ArrayList<Integer> ids = new ArrayList<Integer>();
                        final String[] arguments = new String[args.length-4];
                        for (int i = 4, j = 0; i < args.length-4; i++, j++) {
                            arguments[j] = args[i];
                        }
                        
                        perms.set(pName+".amount", am);
                        perms.set(pName+".price", pr);
                        perms.set(pName+".IDs", ids);
                        ((Player)sender).sendMessage(ChatColor.GREEN+"Permit "+ChatColor.WHITE+pName+ChatColor.GREEN+" has been created.");
                        return true;
                    }
                    
                    
                    
                    if (args[0].equalsIgnoreCase("add") && p.hasPermission("permit.add")){
                        //ADD CODE
                        final String pName = args[1];
                        final String[] ids = args[2].split(",");
                        //final List idList = new List();
                        for (String id : ids){
                            perms.getList(pName+".IDs").add(id);
                        }
                        return true;
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
					if(args.length == 1){
						if (args[0].equalsIgnoreCase("list") && pl.hasPermission("permit.list")){
							//LIST CODE
							Permittr.this.worker.permList(pl);
							return true;
						}
					}
                     */
                }
                return false;
            }
        });
    }
}
