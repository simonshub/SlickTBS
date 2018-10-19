/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import game.data.world.map.hex.Hex;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.simon.utils.Log;
import org.simon.utils.SlickUtils;

/**
 *
 * @author emil.simon
 */
public class ResMgr {
    public static ResMgr instance;
    
    public static final String UI_GRFX_PATH = "res/grfx/ui/";
    public static final String POI_GRFX_PATH = "res/grfx/tokens/specials/";
    
    public static final String TOOLTIP_BACKGROUND_IMAGE = "tooltip_bkg.png";
    
    public static TrueTypeFont font_dialog_title;
    public static TrueTypeFont font_dialog_text;
    public static TrueTypeFont font_dialog_response;
    public static TrueTypeFont font_tooltip_text;
    
    public static int screen_res_w = 800;
    public static int screen_res_h = 600;
    
    public static boolean debug_mode = true;
    public static boolean render_political_overlay = true;
    public static boolean render_continents = false;
    public static boolean render_grid = true;
    
    public static String title = "??? WHAT ARE YOU ???";
    
    
    
    private ResMgr () {
        Font f;

        f = new Font("Verdana", Font.BOLD, 32);
        font_dialog_title = new TrueTypeFont (f, true);
        f = new Font("Verdana", Font.PLAIN, 12);
        font_dialog_text = new TrueTypeFont (f, true);
        f = new Font("Verdana", Font.ITALIC, 12);
        font_dialog_response = new TrueTypeFont (f, true);
        f = new Font("Verdana", Font.PLAIN, 8);
        font_tooltip_text = new TrueTypeFont (f, true);
    }
    
    public static void init () throws IOException, FileNotFoundException, IllegalArgumentException, IllegalAccessException {
        instance = new ResMgr ();
        
        try {
            Hex.init();
            
            instance.readSettings();
        } catch (SlickException ex) {
            Log.err(ex);
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
