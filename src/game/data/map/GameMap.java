/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author emil.simon
 */
public class GameMap {
    public HexGrid grid;
    public Camera camera;
    public int mouse_shadow_x, mouse_shadow_y;
    
    public GameMap (GameContainer gc, int size_x, int size_y) {
        camera = new Camera (gc);
        grid = new HexGrid (size_x, size_y);
        mouse_shadow_x = -1;
        mouse_shadow_y = -1;
    }
    
    public void render (GameContainer container, StateBasedGame game, Graphics g) {
        grid.render(camera, container, game, g);
        
        if (grid.get(mouse_shadow_x, mouse_shadow_y)!=null) {
            grid.renderMouseShadow(camera, mouse_shadow_x, mouse_shadow_y);
        }
    }
    
    public void update (GameContainer gc, StateBasedGame sbg) {
        camera.update(camera, gc, sbg);
        
        int y = (int)((gc.getInput().getMouseY() - (int)(Hex.HEX_GRID_SIZE_Y*1/8))/camera.zoom) + camera.y;
        int x = (int)((gc.getInput().getMouseX())/camera.zoom) + camera.x;
        int y_index = (int)(y/(Hex.HEX_GRID_SIZE_Y*3/4));
        int x_index = (int)((x+(y_index%2==0?Hex.HEX_GRID_SIZE_X/2:0))/Hex.HEX_GRID_SIZE_X) - (y_index%2==0?1:0);
        
        if (grid.get(x_index, y_index)!=null) {
            mouse_shadow_x = x_index;
            mouse_shadow_y = y_index;
        } else {
            mouse_shadow_x = -1;
            mouse_shadow_y = -1;
        }
    }
    
    
    
    public void generateMap (float land_perc, float specials_factor, int players) {
        
    }
}
