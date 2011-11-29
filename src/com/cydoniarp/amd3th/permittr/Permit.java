package com.cydoniarp.amd3th.permittr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Permit {
	private Permittr plugin;
	private String permit;
	private String pName;
	private Logger log;
	private String fn;
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	public Permit(String name, Permittr plugin){
		this.permit = name;
		this.log = plugin.log;
		this.plugin = plugin;
		this.pName = plugin.pName;
		this.fn = plugin.pf+"/permits/"+name+".pr";
		
		if (new File(fn).exists()) load(); else save();
	}
	
	public void load() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fn),"UTF-8"));
			String line;
			int lc = 0; // # of lines
			
			// While there are lines to read
			while ((line = br.readLine()) != null) {
				if (line.trim().length() == 0) {
					// Line's empty, skip it
					lc++;
					continue;
				}
				// Not the first line of the file
				if (lc > 0) {
					int delim = 0;
					while ((delim = line.indexOf('=')) != -1) {
						String key = line.substring(0, delim).trim();
						String val = line.substring(delim+1).trim();
						if (key.equalsIgnoreCase("ids")) {
							ArrayList<Integer> i = new ArrayList<Integer>();
							for (String s : val.split(",")) {
								i.add(Integer.parseInt(s));
							}
							data.put(key, i);
							break;
						}
						data.put(key, Integer.parseInt(val));
						break;
					}
				}
				lc++;
			}
		} catch (FileNotFoundException ex) {
			log.log(Level.SEVERE, '['+pName+"]: Couldn't find file "+fn, ex);
			return;
		} catch (IOException ex) {
			log.log(Level.SEVERE, '['+pName+"]: Unable to save "+fn, ex);
			return;
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				log.log(Level.SEVERE, '['+pName+"]: Unable to save "+fn, ex);
			}
		}
	}
	
	public void save(){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn), "UTF-8"));
			bw.write("# "+pName+" Permit File");
			bw.newLine();
			
			if (data.size() > 0){
				Set<?> set = data.entrySet();
				Iterator<?> it = set.iterator();
				
				while (it.hasNext()){
					Map.Entry<?, ?> me = (Map.Entry<?, ?>)it.next();
					String key = (String)me.getKey();
					if (key.equalsIgnoreCase("ids")){
						@SuppressWarnings("unchecked")
						ArrayList<Integer> val = (ArrayList<Integer>)me.getKey();
						String v = "";
						for (int i = 0; i < val.size(); i++){
							v += (i == 0) ? val.get(i).toString() : ","+val.get(i);
						}
						bw.write(key+'='+v);
						bw.newLine();
					}else{
						String val = me.getValue().toString();
						bw.write(key+'='+val);
						bw.newLine();
					}
				}
			}
		}catch(FileNotFoundException ex){
			log.log(Level.SEVERE, '['+pName+"]: Couldn't find file "+fn, ex);
			return;
		}catch(IOException ex){
			log.log(Level.SEVERE, '['+pName+"]: Unable to save "+fn, ex);
			return;
		}finally{
			try{
				if (bw != null){
					bw.flush();
					bw.close();
				}
			}catch(IOException ex){
				log.log(Level.SEVERE, '['+pName+"]: Unable to save "+fn, ex);
			}
		}
	}
	
	public void set(String key, Object val){
		data.put(key, val);
	}
	
	public int get(String key){
		return (key.equalsIgnoreCase("amount")) ? Integer.parseInt(data.get("amount").toString()) : Integer.parseInt(data.get("price").toString());
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> getIds(){
		return (ArrayList<Integer>)data.get("ids");
	}
	
	public boolean hasId(int ID){
		@SuppressWarnings("unchecked")
		ArrayList<Integer> ids = (ArrayList<Integer>)data.get("ids");
		if(ids.contains(ID)){
			return true;
		}
		return false;
	}
	
	public void addId(int ID){
		@SuppressWarnings("unchecked")
		ArrayList<Integer> ids = (ArrayList<Integer>)data.get("ids");
		ids.add(ID);
		data.put("ids", ids);
	}
	
	public void setAll(int amount, int price, ArrayList<Integer> ids){
		data.put("amount", amount);
		data.put("price", price);
		data.put("ids", ids);
		save();
	}
	
	public void dec(String key){
		data.put(key, String.valueOf(Integer.parseInt((String)data.get(key))-1));
	}
}
