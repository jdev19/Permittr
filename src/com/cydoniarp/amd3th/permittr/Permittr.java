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
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;


public class Permittr extends JavaPlugin{
	private final PerPlayerListener pListener = new PerPlayerListener(this);
	private final PerBlockListener bListener = new PerBlockListener(this);
	public Logger log = Logger.getLogger("Minecraft");
	public HashMap<String, Permit> permits = new HashMap<String, Permit>();
	public Property player;
	public String pName;
	public File pf;
	public String players;
	public String permitd;
	public String logs;
	private pCom worker;
	public static Permit permit;


	public String getCanonPath(String dir){
		String cp = null;
		try{
			cp = new File(dir).getCanonicalPath();
		}catch (IOException e){
			e.printStackTrace();
		}
		return cp;
	}

	public String getCanonFile(String file){
		String cf = null;
		try{
			cf = new File(file).getCanonicalFile().toString();
		}catch (IOException e){
			e.printStackTrace();
		}
		return cf;
	}

	//iConomy
	private static PluginListener PluginListener = null;
	private static iConomy iConomy = null;
	private static Server Server = null;

	//ON ENABLE
	public void onEnable(){
		//iConomy
		Server = getServer();
		PluginListener = new PluginListener();
		//Event Registration
		Server.getPluginManager().registerEvent(Event.Type.PLUGIN_ENABLE, PluginListener, Priority.Monitor, this);
		
		//Main onEnable
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.pListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, this.bListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, this.bListener, Event.Priority.Normal, this);
		PluginDescriptionFile pdf = getDescription();
		this.pName = pdf.getName();
		pf = getDataFolder();
		
		log.info("["+this.pName+"] v"+pdf.getVersion()+" has been enabled.");
		
		//Make data directory if doesn't exist
		if (!pf.isDirectory()){
			pf.mkdir();
		}
		// Make error logs directory if it doesn't exist
		if(!(new File(logs).isDirectory())){
			new File(logs).mkdir();
		}
		// Make permit directory if it doesn't exist
		if(!(new File(permitd).isDirectory())){
			new File(permitd).mkdir();
		}
		//Make player directory if it doesn't exist
		if(!(new File(players).isDirectory())){
			new File(players).mkdir();
		}
		
		//Permissions
		setupPermissions();
		//COMMANDS
		this.worker = new pCom(this);
		getCommand("permit").setExecutor(new CommandExecutor(){
			public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
				Account account = iConomy.getBank().getAccount(((Player)sender).getName());
				if (sender instanceof Player){
					Player pl = ((Player)sender);
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
					if(args.length == 3){
						if (args[0].equalsIgnoreCase("add") && Permissions.has(pl, "permit.add")){
							//ADD CODE
							String pName = args[1];
							String it = args[2];
							Permittr.this.worker.permAdd(pl, pName, it);
							return true;
						}
					}
					if(args.length == 2){
						if (args[0].equalsIgnoreCase("buy") && Permissions.has(pl, "permit.buy")){
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
						if (args[0].equalsIgnoreCase("check") && Permissions.has(pl, "permit.check")){
							//CHECK CODE
							String pName = args[1];
							Permittr.this.worker.permCheck(pl, pName);
							return true;
						}
					}
					if(args.length == 1){
						if (args[0].equalsIgnoreCase("list") && Permissions.has(pl, "permit.list")){
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
	
	private void setupPermissions(){
		Plugin test = getServer().getPluginManager().getPlugin("Permissions");
		PluginDescriptionFile pdf = getDescription();
		if(Permissions == null){
			if (test != null){
				Permissions = ((Permissions)test).getHandler();
			} else {
				log.info('['+this.pName+"]v"+pdf.getVersion()+": Permissions system not detected, plugin shutting down.");
				getServer().getPluginManager().disablePlugin(this);
			}
		}
	}
	
	public static Server getBukkitServer(){
		return Server;
	}

	public static iConomy getiConomy(){
		return iConomy;
	}

	public static boolean setiConomy(iConomy plugin){
		if(iConomy == null){
			iConomy = plugin;
		}else{
			return false;
		}
		return true;
	}

	public void onDisable() {
		PluginDescriptionFile pdf = getDescription();
		log.info("["+this.pName+"] v"+pdf.getVersion() + " has been disabled");
	}

}
