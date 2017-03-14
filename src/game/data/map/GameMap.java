/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author emil.simon
 */
public class GameMap {
    public HexGrid grid;
    public Camera camera;
    
    public GameMap (int size_x, int size_y) {
        grid = new HexGrid (size_x, size_y);
        camera = new Camera ();
    }
    
    public void render (GameContainer container, StateBasedGame game, Graphics g) {
        grid.render(camera, container, game, g);
    }
    
    public void update (GameContainer gc, StateBasedGame sbg) {
        camera.update(gc, sbg);
        
        if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            int x = gc.getInput().getMouseX() + camera.x;
            int y = gc.getInput().getMouseY() + camera.y;
            int x_index = (int)(x/Hex.HEX_GRID_SIZE_X);
            int y_index = (int)(y/Hex.HEX_GRID_SIZE_Y);
            
            if (grid.get(x_index, y_index) != null) {
                System.out.println("clicked ["+x_index+","+y_index+"]");
                grid.get(x_index, y_index).setColor(Color.blue);
            }
        }
    }
}
