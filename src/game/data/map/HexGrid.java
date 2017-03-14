/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import java.util.ArrayList;
import java.util.List;
import main.ResMgr;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author emil.simon
 */
public class HexGrid {
    public static final int DRAW_MARGIN_X = 2;
    public static final int DRAW_MARGIN_Y = 2;
    
    public int counter;
    private final int size_x;
    private final int size_y;
    private final List<List<Hex>> grid;
    
    public HexGrid (int size_x, int size_y) {
        this.size_x = size_x;
        this.size_y = size_y;
        counter = 0;
        
        grid = new ArrayList <> ();
        for (int i=0;i<size_y;i++) {
            grid.add(new ArrayList<> ());
            for (int j=0;j<size_x;j++) {
                grid.get(i).add(new Hex (j, i));
            }
        }
    }
    
    public Hex get (int x, int y) {
        if (y>=0 && y<size_y && x>=0 && x<size_x)
            return grid.get(y).get(x);
        return null;
    }
    
    public void render (Camera cam, GameContainer container, StateBasedGame game, Graphics g) {
        int start_x_index = (int)(cam.x / Hex.HEX_GRID_SIZE_X)-DRAW_MARGIN_X;
        int start_y_index = (int)(cam.y / (Hex.HEX_GRID_SIZE_Y*3/4))-DRAW_MARGIN_Y;
        counter = 0;
        
        for (int i=0;i<ResMgr.screen_res_h/((Hex.HEX_GRID_SIZE_Y*3/4)*cam.zoom)+DRAW_MARGIN_Y*2;i++) {
            for (int j=0;j<ResMgr.screen_res_w/(Hex.HEX_GRID_SIZE_X*cam.zoom)+DRAW_MARGIN_X*2;j++) {
                Hex hex = get(j + start_x_index, i + start_y_index);
                if (hex!=null) { hex.render(cam, container, game, g); counter++; }
            }
        }
    }
}
