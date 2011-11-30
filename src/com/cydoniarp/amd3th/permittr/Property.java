package com.cydoniarp.amd3th.permittr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Property {
    private LinkedHashMap<String, String> props = new LinkedHashMap<String, String>();
    private Map<String, String> keys = new HashMap<String, String>();
    private String file;
    private final String pname = Permittr.name;
    private final Logger log = Permittr.log;

    public Property(final String file) {
        if (file.equals(null)) return;
        this.file = file;
        if (new File(file).exists()) {
            load();
        }
    }
    
    // Load data from file into ordered HashMap
    public Property load() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String line;
            byte cc = 0; // # of comments
            int delim;
            
            // While there are lines to read (auto-breaks)
            while ((line = br.readLine()) != null) {
                // Is a comment, store it
                if (line.charAt(0) == '#') {
                    props.put("#"+cc, line.substring(line.indexOf(' ')+1).trim());
                    cc++;
                    continue;
                }
                // Isn't a comment, store the key and value
                while ((delim = line.indexOf('=')) != -1) {
                    final String key = line.substring(0, delim).trim();
                    props.put(key.toLowerCase(), line.substring(delim+1).trim());
                    keys.put(key.toLowerCase(), key);
                    break;
                }
            }
        } catch (final FileNotFoundException e) {
            log.log(Level.SEVERE, '['+pname+"]: Couldn't find file "+file, e);
        } catch (final IOException e) {
            log.log(Level.SEVERE, '['+pname+"]: Unable to save "+file, e);
        } finally {
            // Close the reader
            try {
                if (null != br) {
                    br.close();
                }
            } catch (final IOException e) {
                log.log(Level.SEVERE, '['+pname+"]: Unable to save "+file, e);
            }
        }
        return this;
    }
    
    // Save data from LinkedHashMap to file
    public Property save() {
        BufferedWriter bw = null;
        try {
            // Construct the BufferedWriter object
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
            
            // Save all the props one at a time, only if there's data to write
            if (props.size() > 0) {
                // Grab all the entries and create an iterator to run through them all
                final Iterator<Map.Entry<String, String>> i = props.entrySet().iterator();
                
                // While there's data to iterate through..
                while (i.hasNext()) {
                    // Map the entry and save the key and value as variables
                    final String key = keys.get(i.next().getKey()); // Case-preserved key
                    final String val = i.next().getValue();
                    
                    // If it starts with "#", it's a comment so write it as such
                    if (key.charAt(0) == '#') {
                        // Writing a comment to the file
                        bw.write("# "+val);
                        bw.newLine();
                        continue;
                    }
                    // Otherwise write the key and value pair as key=value
                    bw.write(key+'='+val);
                    bw.newLine();
                }
            }
        } catch (final FileNotFoundException e) {
            log.log(Level.SEVERE, '['+pname+"]: Couldn't find file "+file, e);
        } catch (final IOException e) {
            log.log(Level.SEVERE, '['+pname+"]: Unable to save "+file, e);
        } finally {
            // Close the BufferedWriter
            try {
                if (null != bw) {
                    bw.close();
                }
            } catch (final IOException e) {
                log.log(Level.SEVERE, '['+pname+"]: Unable to close buffer for "+file, e);
            }
        }
        return this;
    }
    
    // Return current map
    public List<Map<String, String>> copy() {
        List<Map<String, String>> o = new ArrayList<Map<String, String>>();
        o.add(props);
        o.add(keys);
        return o;
    }
    
    // Convert current map to specified map
    public Property paste(final List<Map<String, String>> list) {
        props.putAll(list.get(0));
        keys.putAll(list.get(1));
        return this;
    }
    
    // Grab all keys
    public Set<String> getKeys() {
        return props.keySet();
    }
    
    // Extend ability to clear all mappings
    public Property clear() {
        props.clear();
        keys.clear();
        return this;
    }
    
    // Remove the key. Extends native removal
    public Property remove(final String key) {
        props.remove(key.toLowerCase());
        keys.remove(key.toLowerCase());
        return this;
    }
    
    // Check if the key exists or not
    public boolean keyExists(final String key) {
        return props.containsKey(key.toLowerCase());
    }
    
    // Check if the key no value
    public boolean isEmpty(final String key) {
        return props.get(key.toLowerCase()).isEmpty();
    }
    
    // Set property value
    public Property set(final String key, final Object value) {
        props.put(key.toLowerCase(), String.valueOf(value));
        keys.put(key.toLowerCase(), key);
        return this;
    }
    
    // Get property value as a String
    public String getString(final String key) {
        return props.containsKey(key.toLowerCase()) ? props.get(key.toLowerCase()) : "";
    }
    
    // Get property value as a boolean
    public boolean getBool(final String key) {
        return props.containsKey(key.toLowerCase()) ? Boolean.parseBoolean(props.get(key.toLowerCase())) : false;
    }
    
    // Get property value as an int
    public int getInt(final String key) {
        return props.containsKey(key.toLowerCase()) ? Integer.parseInt(props.get(key.toLowerCase())) : 0;
    }
    
    // Get property value as a double
    public double getDouble(final String key) {
        return props.containsKey(key.toLowerCase()) ? Double.parseDouble(props.get(key.toLowerCase())) : 0.0D;
    }
}