/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import game.data.hex.HexGrid;
import game.data.hex.Hex;
import static game.data.hex.Hex.HEX_GRID_SIZE_X;
import static game.data.hex.Hex.HEX_GRID_SIZE_Y;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author emil.simon
 */
public class GameMap {
    
    public enum MapSize {
        TINY(64), SMALL(96), MEDIUM(128), LARGE(192), HUGE(256), EPIC(512)
        ;
        public final int size;
        MapSize (int size) { this.size = size; }
    }
    
    
    
    public HexGrid grid;
    
    public int mouse_shadow_x, mouse_shadow_y;
    
    public Hex debug_hex;
    
    
    
    public GameMap (MapSize size, DifficultyLevel diff) {
        grid = new HexGrid (size.size*2, size.size);
        mouse_shadow_x = -1;
        mouse_shadow_y = -1;
        debug_hex = null;
        
        WorldGenerator.setGrid(grid);
        WorldGenerator.generateMap(1.0, 1.0);
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
