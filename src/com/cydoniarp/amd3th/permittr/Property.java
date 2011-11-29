package com.cydoniarp.amd3th.permittr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Property
{
  private Logger log;
  protected Permittr plugin;
  private LinkedHashMap<String, Object> properties = new LinkedHashMap<String, Object>();
  private String filename;
  private String pName;

  public Property(String filename, Permittr plugin)
  {
    this.plugin = plugin;
    this.pName = plugin.pName;
    this.log = plugin.log;
    this.filename = filename;
    File file = new File(filename);

    if (file.exists())
      load();
    else
      save();
  }

  public void load()
  {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(this.filename));

      int cc = 0;
      int lc = 0;
      String line;
      while ((line = br.readLine()) != null)
      {
        if (line.trim().length() == 0)
        {
          continue;
        }
        if ((line.charAt(0) == '#') && (lc > 0))
        {
          int delim = line.indexOf(' ');
          String key = "com" + cc;
          String val = line.substring(delim + 1).trim();
          this.properties.put(key, val);
          cc++;
        }
        else
        {
          if (lc > 0)
          {
            int delim = line.indexOf('=');
            String key = line.substring(0, delim).trim();
            String val = line.substring(delim + 1).trim();
            this.properties.put(key, val);
          }
          lc++;
        }
      } } catch (FileNotFoundException ex) { this.log.log(Level.SEVERE, '[' + this.pName + "]: Couldn't find file " + this.filename, ex);
      return; } catch (IOException ex) { this.log.log(Level.SEVERE, '[' + this.pName + "]: Unable to save " + this.filename, ex);
      return;
    }
    finally {
      try {
        if (br != null)
          br.close();
      }
      catch (IOException ex) {
        this.log.log(Level.SEVERE, '[' + this.pName + "]: Unable to save " + this.filename, ex);
      }
    }
    try
    {
      if (br != null)
        br.close();
    }
    catch (IOException ex) {
      this.log.log(Level.SEVERE, '[' + this.pName + "]: Unable to save " + this.filename, ex);
    }
  }

  public void save()
  {
    BufferedWriter bw = null;
    try
    {
      bw = new BufferedWriter(new FileWriter(this.filename));
      bw.write("# " + this.pName + " Properties File");
      bw.newLine();

      if (this.properties.size() > 0)
      {
        Set<?> set = this.properties.entrySet();
        Iterator<?> i = set.iterator();

        while (i.hasNext())
        {
          Map.Entry<?,?> me = (Map.Entry<?,?>)i.next();
          String key = (String)me.getKey();
          String val = me.getValue().toString();

          if (key.startsWith("com"))
          {
            bw.write("# " + val);
            bw.newLine();
          }
          else {
            bw.write(key + '=' + val);
            bw.newLine();
          }
        }
      } } catch (FileNotFoundException ex) { this.log.log(Level.SEVERE, '[' + this.pName + "]: Couldn't find file " + this.filename, ex);
      return; } catch (IOException ex) { this.log.log(Level.SEVERE, '[' + this.pName + "]: Unable to save " + this.filename, ex);
      return;
    }
    finally {
      try {
        if (bw != null) {
          bw.flush();
          bw.close();
        }
      } catch (IOException ex) {
        this.log.log(Level.SEVERE, '[' + this.pName + "]: Unable to save " + this.filename, ex);
      }
    }
    try
    {
      if (bw != null) {
        bw.flush();
        bw.close();
      }
    } catch (IOException ex) {
      this.log.log(Level.SEVERE, '[' + this.pName + "]: Unable to save " + this.filename, ex);
    }
  }

  public void rebuild(LinkedHashMap<String, Object> newMap)
  {
    this.properties.clear();
    this.properties.putAll(newMap);
    save();
  }

  public boolean match(LinkedHashMap<String, Object> prop)
  {
    return this.properties.keySet().containsAll(prop.keySet());
  }

  public boolean keyExists(String key)
  {
    return this.properties.containsKey(key);
  }

  public boolean isEmpty(String key)
  {
    return this.properties.get(key).toString().isEmpty();
  }

  public void inc(String key)
  {
    this.properties.put(key, String.valueOf(Integer.parseInt((String)this.properties.get(key)) + 1));
  }

  public String getString(String key)
  {
    if (this.properties.containsKey(key)) {
      return (String)this.properties.get(key);
    }
    return "";
  }
  public void setString(String key, String value) {
    this.properties.put(key, value);
  }

  public int getInt(String key)
  {
    if (this.properties.containsKey(key)) {
      return Integer.parseInt((String)this.properties.get(key));
    }
    return 0;
  }
  public void setInt(String key, int value) {
    this.properties.put(key, String.valueOf(value));
    save();
  }

  public double getDouble(String key)
  {
    if (this.properties.containsKey(key)) {
      return Double.parseDouble((String)this.properties.get(key));
    }
    return 0.0D;
  }
  public void setDouble(String key, double value) {
    this.properties.put(key, String.valueOf(value));
  }

  public long getLong(String key)
  {
    if (this.properties.containsKey(key)) {
      return Long.parseLong((String)this.properties.get(key));
    }
    return 0L;
  }
  public void setLong(String key, long value) {
    this.properties.put(key, String.valueOf(value));
  }

  public float getFloat(String key)
  {
    if (this.properties.containsKey(key)) {
      return Float.parseFloat((String)this.properties.get(key));
    }
    return 0.0F;
  }
  public void setFloat(String key, float value) {
    this.properties.put(key, String.valueOf(value));
  }

  public boolean getBoolean(String key)
  {
    if (this.properties.containsKey(key)) {
      return Boolean.parseBoolean((String)this.properties.get(key));
    }
    return false;
  }
  public void setBoolean(String key, boolean value) {
    this.properties.put(key, String.valueOf(value));
  }
}