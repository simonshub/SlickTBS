/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author emil.simon
 */
public abstract class Consts {
    
    public static final String APP_TITLE = "Some Game";
    public static final String APP_SETTINGS_FILE_PATH = "settings.cfg";
    
    public static final String APP_PROPERTY_DELIMITER = "=";
    public static final String APP_COMMENT_DELIMITER = "//";
    
    public static final String GRFX_FILE_EXTENSION = ".png";
    public static final String SOUND_FILE_EXTENSION = ".wav";
    public static final String FONT_FILE_EXTENSION = ".ttf";
    
    public static final String SCRIPT_FILE_EXTENSION = ".js";
    public static final String OBJECT_FILE_EXTENSION = ".json";
    public static final String DATABASE_FILE_EXTENSION = ".db";
    
    public static final String TILES_FILE_EXTENSION = ".tile";
    
    public static final String DEFAULT_FONT = "note_this";
    public static final int DEFAULT_FONT_SIZE = 20;
    
    public static final int ENTITY_OFFSCREEN_DRAW_MARGIN = 128;
    public static final int TILESET_WIDTH = 32;
    public static final int TILESET_HEIGHT = 32;
    
    public static final String CURRENT_VERSION = "DEV";
    public static final String[] SUPPORTED_VERSIONS = { "DEV", "0.1a" };
    
    
    
    private Consts () { }
    
}
