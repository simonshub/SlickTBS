/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import org.simon.utils.Log;

/**
 *
 * @author emil.simon
 */
public class Settings {
    
    private Settings () { }
    
    
    
    public static int screen_width = 1280;
    public static int screen_height = 960;
    
    public static int max_particle_count = 1000;
    
    public static boolean debug_mode = true;
    public static boolean debug_gui = true;
    
    public static String savegame_path = "saves/";
    
    public static String default_tooltip_background_image = "pixel";
    
    public static String default_world_generation_template = "continents";
    public static String inventory_background_image = "icons/backpack";
    public static String test_sound = "wauw";
    
    
    
    public static void load () {
        File file = new File (Consts.APP_SETTINGS_FILE_PATH);
        
        if (file.exists()) {
            try {
                InputStream is = new FileInputStream (file);
                Properties props = new Properties();
                props.load(is);
                
                Field[] fields = Settings.class.getDeclaredFields();
                for (Field f : fields) {
                    if (!props.containsKey(f.getName())) continue;
                    
                    if (f.getType() == int.class) {
                        f.set(null, Integer.parseInt(props.getProperty(f.getName())));
                    } else if (f.getType() == float.class) {
                        f.set(null, Float.parseFloat(props.getProperty(f.getName())));
                    } else if (f.getType() == boolean.class) {
                        f.set(null, Boolean.parseBoolean(props.getProperty(f.getName())));
                    } else {
                        f.set(null, props.getProperty(f.getName()));
                    }
                }
            } catch (IOException | IllegalAccessException | NumberFormatException ex) {
                Log.err("error while reading settings file at '"+Consts.APP_SETTINGS_FILE_PATH+"'");
                Log.err(ex);
            }
        } else {
            try {
                OutputStream os = new FileOutputStream (file);
                Properties props = new Properties();
                
                Field[] fields = Settings.class.getDeclaredFields();
                for (Field f : fields) {
                    props.setProperty(f.getName(), f.get(null).toString());
                }
                
                props.store(os, null);
            } catch (IOException | IllegalAccessException ex) {
                Log.err("error while trying to create default settings file at '"+Consts.APP_SETTINGS_FILE_PATH+"'");
                Log.err(ex);
            }
        }
    }
    
}
