package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.simon.utils.Log;
import org.simon.utils.SlickUtils;
import org.simon.utils.consolewindow.ConsoleWindow;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author emil.simon
 */
public class ResourceManager {
    
    private static Map<String, Image> graphics_lib;
    private static Map<String, Sound> sound_lib;
    private static Map<String, Font> font_lib;
    
    private static Map<String, TrueTypeFont> preloaded_fonts;
    
    
    
    private ResourceManager () {  }
    
    
    
    public static void init () {
        long start,end;
        long start_entire = System.currentTimeMillis();
        Log.log("Initializing resources ...");
        
        Log.log("Loading graphics...");
        start = System.currentTimeMillis();
        loadGraphics();
        end = System.currentTimeMillis();
        Log.log("Graphics loaded in "+String.format("%.2f", (end-start)/1000f)+" sec");
        
        Log.log("Loading sounds...");
        start = System.currentTimeMillis();
        loadSounds();
        end = System.currentTimeMillis();
        Log.log("Sounds loaded in "+String.format("%.2f", (end-start)/1000f)+" sec");
        
        Log.log("Loading fonts...");
        start = System.currentTimeMillis();
        loadFonts();
        end = System.currentTimeMillis();
        Log.log("Fonts loaded in "+String.format("%.2f", (end-start)/1000f)+" sec");
        
        long end_entire = System.currentTimeMillis();
        Log.log("Finished loading in "+String.format("%.2f",(end_entire-start_entire)/1000f)+" sec");
    }
    
    private static void loadGraphics () {
        graphics_lib = new HashMap<> ();
        File dump = new File (Settings.grfx_path);
        
        if (!dump.exists()) {
            Log.err("Missing graphics resource dump folder at '"+Settings.grfx_path+"'...");
        } else {
            File[] all_images = SlickUtils.Files.getFileArrayOfExtensionInSubdirs(dump, Consts.GRFX_FILE_EXTENSION);
            for (File img : all_images) {
                try {
                    String path = img.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    String key = path.toLowerCase()
                            .substring(path.indexOf(Settings.grfx_path) + Settings.grfx_path.length())
                            .replaceAll(Consts.GRFX_FILE_EXTENSION, "");
                    
                    Log.log("Loading graphics with name '"+key+"' at path '"+path+"'");
                    
                    Image value = new Image (path, false, Image.FILTER_LINEAR);
                    value.setFilter(Image.FILTER_NEAREST);
                    graphics_lib.put(key, value);
                    
                    Log.log("Loaded graphics with name '"+key+"' at path '"+path+"'");
                } catch (SlickException | IOException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    private static void loadSounds () {
        sound_lib = new HashMap<> ();
        File dump = new File (Settings.sounds_path);
        
        if (!dump.exists()) {
            Log.err("Missing sound resource dump folder at '"+Settings.sounds_path+"'...");
        } else {
            File[] all_sounds = SlickUtils.Files.getFileArrayOfExtensionInSubdirs(dump, Consts.SOUND_FILE_EXTENSION);
            for (File snd : all_sounds) {
                try {
                    String path = snd.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    String key = path.toLowerCase()
                            .substring(path.indexOf(Settings.sounds_path) + Settings.sounds_path.length())
                            .replaceAll(Consts.SOUND_FILE_EXTENSION, "");
                    Sound value = new Sound (path);
                    
                    Log.log("Loaded sound with name '"+key+"' at path '"+path+"'");
                    
                    sound_lib.put(key, value);
                } catch (SlickException | IOException ex) {
                    Log.err(ex);
                }
            }
        }
        
        if (!Settings.test_sound.isEmpty()) {
            if (sound_lib.containsKey(Settings.test_sound)) {
                sound_lib.get(Settings.test_sound).play();
            } else {
                Log.err("Test sound is set, but no sound of name '"+Settings.test_sound+"' was loaded!");
            }
        }
    }
    
    private static void loadFonts () {
        font_lib = new HashMap<> ();
        preloaded_fonts = new HashMap<> ();
        File dump = new File (Settings.fonts_path);
        
        if (!dump.exists()) {
            Log.err("Missing font resource dump folder at '"+Settings.fonts_path+"'...");
        } else {
            File[] all_fonts = SlickUtils.Files.getFileArrayOfExtensionInSubdirs(dump, Consts.FONT_FILE_EXTENSION);
            for (File font : all_fonts) {
                try {
                    String path = font.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    String key = path.substring(path.indexOf(Settings.fonts_path) + Settings.fonts_path.length());
                    key = key.substring(key.indexOf("/")+1);
                    key = key.toLowerCase().replaceAll(Consts.FONT_FILE_EXTENSION, "");
                    Font value = Font.createFont(Font.TRUETYPE_FONT, new File(path));
                    
                    Log.log("Loaded font with name '"+key+"' at path '"+path+"'");
                    
                    font_lib.put(key, value);
                } catch (IOException | FontFormatException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    
    
    public static Image getGraphics (String name) {
        if (name.isEmpty()) return null;
        
        name = name.toLowerCase();
        if (!graphics_lib.containsKey(name)) {
            Log.err("no graphics of name '"+name+"'");
            return null;
        }
        return graphics_lib.get(name);
    }
    
    public static Sound getSound (String name) {
        name = name.toLowerCase();
        if (!sound_lib.containsKey(name)) {
            Log.err("no sound of name '"+name+"'");
            return null;
        }
        return sound_lib.get(name);
    }
    
    public static TrueTypeFont getFont (String name, int size) {
        name = name.toLowerCase();
        String preload_key = name + "_" + String.valueOf(size);
        
        if (preloaded_fonts.containsKey(preload_key))
            return preloaded_fonts.get(preload_key);
        
        TrueTypeFont result;
        if (!font_lib.containsKey(name)) {
            try {
                Font font = new Font (name, Font.PLAIN, size);
                font_lib.put(name, font);
                result = new TrueTypeFont (font, true);
            } catch (RuntimeException ex) {
                Log.err("Unable to resolve font resource '"+name+"'!");
                Log.err(ex);
                return null;
            }
        } else {
            Font resized_font = font_lib.get(name).deriveFont((float) size);
            result = new TrueTypeFont (resized_font, true);
        }
        
        preloaded_fonts.put(preload_key, result);
        return result;
    }
    
    
    
    public static boolean hasGraphics (String name) {
        if (name==null || name.isEmpty()) return false;
        return graphics_lib.containsKey(name);
    }
    
    public static boolean hasSound (String name) {
        if (name==null || name.isEmpty()) return false;
        return sound_lib.containsKey(name);
    }
    
    public static boolean hasFont (String name) {
        if (name==null || name.isEmpty()) return false;
        return font_lib.containsKey(name);
    }
    
    
    
    public static Set<String> getGraphicsKeySet () {
        return graphics_lib.keySet();
    }
    
    public static Set<String> getSoundKeySet () {
        return sound_lib.keySet();
    }
    
    public static Set<String> getFontKeySet () {
        return font_lib.keySet();
    }
    
    
    
    public static List<String> getAllGraphicsStartingWith (String starts_with) {
        String[] all = new String [graphics_lib.size()];
        graphics_lib.keySet().toArray(all);
        List<String> result = new ArrayList<> ();
        
        for (int i=0;i<all.length;i++) {
            String key = all[i];
            if (key.startsWith(starts_with))
                result.add(key);
        }
        
        return result;
    }
    
}
