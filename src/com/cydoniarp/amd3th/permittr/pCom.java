package com.cydoniarp.amd3th.permittr;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.nijiko.coelho.iConomy.system.Account;

public class pCom {
	protected Permittr plugin;
	public pCom(Permittr plugin){
		this.plugin = plugin;
	}
	
	public void permCreate(Player player, String pName, int amount, int price){
		Permit permit = new Permit(pName, plugin);
		permit.set("amount", amount);
		permit.set("price", price);
		}
	public void permAdd(Player player, String pName, String item){
		//Convert Material Name to ID
		int id = 0;
		for (Material m : Material.values()){
			if(item.equalsIgnoreCase(m.toString().replace("_", ""))){
				id = m.getId();
				break;
			}
		}
		Permit permit = new Permit(pName, plugin);
		permit.addId(id);
	}
	public void permCheck(Player player, String pName){
		
	}
	public void permList(Player player){
		Permit permit = new Permit();
		permit.
	}
	public int priceGet(String pName){
		Permit permit = new Permit(pName, plugin);
		int price = permit.get("price");
		return price;
	}
	public int durGet(String pName){
		Permit permit = new Permit(pName, plugin);
		int amount = permit.get("amount");
		return amount;
	}
}
