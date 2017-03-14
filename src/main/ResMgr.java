/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import game.data.map.Hex;
import game.data.map.TerrainType;
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
    
    public static TerrainType TERRAIN_DEFAULT;
    public static TerrainType TERRAIN_PLAINS;
    public static TerrainType TERRAIN_FOREST;
    public static TerrainType TERRAIN_MOUNTAINS;
    public static TerrainType TERRAIN_HILLS;
    public static TerrainType TERRAIN_SEA;
    public static TerrainType TERRAIN_MARSHES;
    public static TerrainType TERRAIN_DESERT;
    
    public static int screen_res_w = 800;
    public static int screen_res_h = 600;
    public static String title = "WW II";
    
    
    
    private ResMgr () {
        TERRAIN_DEFAULT = new TerrainType ();
    }
    
    public static void init () throws IOException, FileNotFoundException, IllegalArgumentException, IllegalAccessException {
        instance = new ResMgr ();
        instance.readSettings();
        
        try {
            Hex.init();
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
