/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import main.utils.SlickUtils;
import game.data.map.Hex;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.newdawn.slick.SlickException;

/**
 *
 * @author emil.simon
 */
public class ResMgr {
    public static ResMgr instance;
    
    public static int screen_res_w = 800;
    public static int screen_res_h = 600;
    public static boolean debug_mode = true;
    public static boolean render_grid = true;
    public static boolean render_continous = false;
    public static boolean render_levels = true;
    public static boolean render_units = true;
    public static boolean render_token_backgrounds = true;
    public static boolean render_tokens = true;
    public static boolean render_tile_specials = true;
    
    public static String title = "??? WHAT ARE YOU ???";
    
    
    
    private ResMgr () {
        
    }
    
    public static void init () throws IOException, FileNotFoundException, IllegalArgumentException, IllegalAccessException {
        instance = new ResMgr ();
        
        try {
            Hex.init();
            instance.readSettings();
        } catch (SlickException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    
    
    public void readSettings () throws FileNotFoundException, IOException, IllegalArgumentException, IllegalAccessException {
        File settings = new File (Consts.SETTINGS_FILE_PATH);
        
        if (!settings.exists())
            SlickUtils.writeObjectToFile(settings, this);
        else
            SlickUtils.readObjectFromFile(settings, this);
    }
}
