/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import main.ResMgr;
import main.SlickUtils;
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
    public Editor editor;
    
    public GameMap (GameContainer gc, int size_x, int size_y) {
        editor = null;
        camera = new Camera (gc);
        grid = new HexGrid (size_x, size_y);
        
        if (ResMgr.edit_mode) {
            editor = new Editor ();
        }
    }
    
    public void render (GameContainer container, StateBasedGame game, Graphics g) {
        grid.render(camera, container, game, g);
    }
    
    public void update (GameContainer gc, StateBasedGame sbg) {
        camera.update(camera, gc, sbg);
        
        int y = (int)((gc.getInput().getMouseY() - (int)(Hex.HEX_GRID_SIZE_Y*1/8))/camera.zoom) + camera.y;
        int x = (int)((gc.getInput().getMouseX())/camera.zoom) + camera.x;
        int y_index = (int)(y/(Hex.HEX_GRID_SIZE_Y*3/4));
        int x_index = (int)((x+(y_index%2==0?Hex.HEX_GRID_SIZE_X/2:0))/Hex.HEX_GRID_SIZE_X) - (y_index%2==0?1:0);
        
        if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            // something
        }
        
        if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (grid.get(x_index, y_index) != null && editor != null && ResMgr.edit_mode) {
                switch (editor.state) {
                    case TILE :
                        grid.get(x_index, y_index).terrain = editor.paintTile;
                        System.out.println("Painting "+SlickUtils.getFileName(editor.paintTile.img_path)+" to tile at "+x_index+","+y_index);
                        break;
                    case TILE_SPECIAL :
                        break;
                    case UNIT :
                        break;
                    case NONE :
                        break;
                    default :
                        break;
                }
            }
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_2) && editor.state==Editor.State.TILE) {
            editor.nextTile();
            System.out.println("Tile Painter: "+editor.paintTile.name());
        }
        if (gc.getInput().isKeyPressed(Input.KEY_1) && editor.state==Editor.State.TILE) {
            editor.prevTile();
            System.out.println("Tile Painter: "+editor.paintTile.name());
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_T) && (gc.getInput().isKeyDown(Input.KEY_LCONTROL) || gc.getInput().isKeyDown(Input.KEY_RCONTROL))) {
            editor.state = Editor.State.TILE;
            System.out.println("Entered state: TILE");
        } else if (gc.getInput().isKeyPressed(Input.KEY_S) && (gc.getInput().isKeyDown(Input.KEY_LCONTROL) || gc.getInput().isKeyDown(Input.KEY_RCONTROL))) {
            editor.state = Editor.State.TILE_SPECIAL;
            System.out.println("Entered state: TILE_SPECIAL");
        } else if (gc.getInput().isKeyPressed(Input.KEY_D) && (gc.getInput().isKeyDown(Input.KEY_LCONTROL) || gc.getInput().isKeyDown(Input.KEY_RCONTROL))) {
            editor.state = Editor.State.UNIT;
            System.out.println("Entered state: UNIT");
        } else if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            editor.state = Editor.State.NONE;
            System.out.println("Entered state: NONE");
        }
    }
}
