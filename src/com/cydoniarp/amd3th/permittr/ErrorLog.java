package com.cydoniarp.amd3th.permittr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ErrorLog {
	private Logger log;
	protected Permittr plugin;
	private LinkedHashMap<String, Object> output = new LinkedHashMap<String, Object>();
	private String file;
	private String pName;
	
	public ErrorLog(String file, Permittr plugin){
		this.plugin = plugin;
		this.pName = plugin.pName;
		this.log = plugin.log;
		this.file = file;
		
		if(!(new File(file).exists())){
			save();
		}
	}
	
	public void save(){
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(file));
			bw.write("# "+pName+" Error Log");
			bw.newLine();
			
			if(output.size() > 0){
				Set<?> set = output.entrySet();
				Iterator<?> i = set.iterator();
				
				while(i.hasNext()){
					Map.Entry<?, ?> me = (Map.Entry<?, ?>)i.next();
					String key = (String)me.getKey();
					String val = me.getValue().toString();
					
					if(key.startsWith("com")){
						bw.write("# "+val);
						bw.newLine();
					}else{
						bw.write(key+": "+val);
						bw.newLine();
					}
				}
			}
		} catch (FileNotFoundException ex){
			log.log(Level.SEVERE, '['+pName+"]: couldn't find file "+file, ex);
			return;
		} catch (IOException ex){
			log.log(Level.SEVERE, '['+pName+"]: Unable to save "+file, ex);
			return;
		} finally{
			try{
				if(bw != null){
					bw.flush();
					bw.close();
				}
			} catch (IOException ex){
				log.log(Level.SEVERE, '['+pName+"]: Unable to save "+file, ex);
			}
		}
	}
	
	public void setString(String key, String value){
		output.put(key, value);
	}
	
	public void setInt(String key, int value){
		output.put(key, String.valueOf(value));
		save();
	}
	
	public void setDouble(String key, double value){
		output.put(key, String.valueOf(value));
	}
	
	public void setLong(String key, long value){
		output.put(key, String.valueOf(value));
	}
	
	public void setFloat(String key, float value){
		output.put(key, String.valueOf(value));
	}
	
	public void setBoolean(String key, boolean value){
		output.put(key, String.valueOf(value));
	}
}
