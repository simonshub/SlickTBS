/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.text.SimpleDateFormat;

/**
 *
 * @author emil.simon
 */
public abstract class Consts {
    public static final String SETTINGS_FILE_PATH = "settings.cfg";
    
    public static final String TILES_PATH = "res/data/tiles/";
    public static final String TILES_EXT = ".tile";
    
    public static final String RACES_PATH = "res/data/races/";
    public static final String RACES_EXT = ".race";
    
    public static final String FACTIONS_PATH = "res/data/factions/";
    public static final String FACTIONS_EXT = ".fact";
    
    public static final String DAMAGE_TYPE_PATH = "res/data/attacks/";
    public static final String DAMAGE_TYPE_EXT = ".atk";
    
    public static final String COMBAT_INIT_PATH = "res/data/combat.data";
    
    public static final int MAX_ABILITIES_PER_CHARACTER = 5;
    
    public static final boolean RENDER_DEBUG = true;
    
    public static final boolean RENDER_POIS = true;
    public static final boolean RENDER_FOG_OF_WAR = true;
    public static final boolean RENDER_DEBUG_CAMERA_INFO = true;
    public static final boolean RENDER_DEBUG_RENDERED_HEXES = true;
    
    public static final String LOG_EXTENSION = ".log";
    public static final SimpleDateFormat LOG_TIMESTAMP_FORM = new SimpleDateFormat ("HH:mm:ss");
    public static final SimpleDateFormat LOG_DATE_FORM = new SimpleDateFormat ("dd_MM_");
}
