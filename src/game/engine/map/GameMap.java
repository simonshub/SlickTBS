/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.engine.map;

import main.Consts;
import main.Settings;
import game.data.players.Player;
import static game.engine.map.Hex.HEX_GRID_SIZE_X;
import static game.engine.map.Hex.HEX_GRID_SIZE_Y;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.utils.Log;

import com.google.gson.Gson;
import game.data.world.map.WorldGenerator;

/**
 *
 * @author emil.simon
 */
public class GameMap {
    
    public static enum MapSize {
    	
        TINY(32), SMALL(64), MEDIUM(96), LARGE(128), EXTRA_LARGE(192), HUGE(256), EPIC(512)
        
        ;
        
    	public final int size;
        MapSize (int size) { this.size = size; }
        
    }
    
    
    
    public static Gson gson = new Gson ();
    
    public HexGrid grid;
    
    public List<Player> players;
    
    public transient int mouse_shadow_x, mouse_shadow_y;
    
    public transient Hex debug_hex;
    
    
    
    public GameMap (MapSize size, int seed) {
        grid = new HexGrid (size.size*2, size.size);
        mouse_shadow_x = -1;
        mouse_shadow_y = -1;
        debug_hex = null;
        
        players = new ArrayList<> ();
        
        WorldGenerator.setGrid(grid);
        WorldGenerator.generateMap(1.0, seed);
    }
    
    
    
    public boolean save (String filename) {
    	String complete_path = Settings.save_path + filename + Consts.SAVE_FILE_EXTENSION;
    	
    	try {
    		File f = new File (complete_path);
    		
    		if (!f.exists())
    			f.createNewFile();
    		
    		if (!f.exists() || (f.exists() && !f.canWrite()))
    			throw new IOException ("Cannot save map because file-write permission has been denied");
    		
    		String json = gson.toJson(grid);
    		
    		FileWriter writer = new FileWriter (f);
    		writer.write(json);
    		writer.flush();
    		writer.close();
    	} catch (Exception ex) {
    		Log.err(ex);
    		return false;
    	}
    	
    	Log.log("Successfully saved map as '"+filename+"'");
    	return true;
    }
    
    public void render (Camera camera, GameContainer container, StateBasedGame game, Graphics g) {
        grid.render(camera, container, game, g);
        
        if (grid.get(mouse_shadow_x, mouse_shadow_y)!=null) {
            renderMouseShadow(camera, mouse_shadow_x, mouse_shadow_y);
        }
    }
    
    public void update (Camera camera, GameContainer gc, StateBasedGame sbg) {
        camera.update(camera, gc, sbg);
        
        int y = (int)((gc.getInput().getMouseY() - (int)(Hex.HEX_GRID_SIZE_Y*1/8))/camera.zoom) + camera.y;
        int x = (int)((gc.getInput().getMouseX())/camera.zoom) + camera.x;
        int y_index = (int)(y/(Hex.HEX_GRID_SIZE_Y*3/4));
        int x_index = (int)((x+(y_index%2==0?Hex.HEX_GRID_SIZE_X/2:0))/Hex.HEX_GRID_SIZE_X) - (y_index%2==0?1:0) - (x<0?1:0);
        
        mouse_shadow_x = x_index;
        mouse_shadow_y = y_index;
        
        if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            debug_hex = grid.get(x_index, y_index);
        }
    }
    
    public void renderMouseShadow (Camera cam, int x, int y) {
        if (grid.get(x,y)!=null) {
            float x_draw = (x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x)*cam.zoom;
            float y_draw = (y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y)*cam.zoom;
            float x_scale = (HEX_GRID_SIZE_X)*cam.zoom;
            float y_scale = (HEX_GRID_SIZE_Y)*cam.zoom;
            
            grid.get(x,y).renderMouseShadow(x_draw, y_draw, x_scale, y_scale);
        }
    }
}
