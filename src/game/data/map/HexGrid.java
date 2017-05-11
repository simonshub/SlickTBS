/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import static game.data.map.Hex.HEX_GRID_SIZE_X;
import static game.data.map.Hex.HEX_GRID_SIZE_Y;
import java.util.ArrayList;
import java.util.List;
import main.Consts;
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
    
    public int render_counter;
    public int not_render_counter;
    private final int size_x;
    private final int size_y;
    private final List<List<Hex>> grid;
    
    public HexGrid (int size_x, int size_y) {
        this.size_x = size_x;
        this.size_y = size_y;
        render_counter = 0;
        
        grid = new ArrayList <> ();
        for (int i=0;i<size_y;i++) {
            grid.add(new ArrayList<> ());
            for (int j=0;j<size_x;j++) {
                grid.get(i).add(new Hex (j, i));
            }
        }
    }
    
    public Hex get (int x, int y) {
        try {
            if (y<0 || y>=size_y) return null;
            
            while (y<0) y+=size_y;
            while (x<0) x+=size_x;
            
            return grid.get(Math.abs(y%size_y)).get(Math.abs(x%size_x));
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }
    
    public int getSizeX () {
        return size_x;
    }
    
    public int getSizeY () {
        return size_y;
    }
    
    public int getNumberOfIslands () {
        return (int)((size_x*size_y)/100);
    }
    
    public boolean satisfiesLandPrecentage (List<Hex> land) {
        return (double)((double)land.size() / (double)(size_x*size_y)) >= Consts.MAP_LAND_PERCENTAGE;
    }
    
    
    
    public List<Hex> getCoastalHexes () {
        List<Hex> result = new ArrayList<> ();
        
        for (int y=0;y<size_y;y++) {
            for (int x=0;x<size_x;x++) {
                if (this.get(x,y).isCoastal(this)) result.add(this.get(x,y));
            }
        }
        
        return result;
    }
    
    public Hex getRandomLandHex () {
        Hex hex = null;
        do {
            int x = (int)(Math.random() * size_x);
            int y = (int)(Math.random() * size_y);
            hex = get(x,y);
            
            if (hex==null) continue;
        } while (hex.terrain.equals(TerrainType.SEA));
        
        return hex;
    }
    
    
    
    public void render (Camera cam, GameContainer container, StateBasedGame game, Graphics g) {
        int start_x_index = (int)(cam.x / Hex.HEX_GRID_SIZE_X)-DRAW_MARGIN_X;
        int start_y_index = (int)(cam.y / (Hex.HEX_GRID_SIZE_Y*3/4))-DRAW_MARGIN_Y;
        render_counter = 0;
        not_render_counter = 0;
        
        for (int i=0;i<ResMgr.screen_res_h/((Hex.HEX_GRID_SIZE_Y*3/4)*cam.zoom)+DRAW_MARGIN_Y*2;i++) {
            for (int j=0;j<ResMgr.screen_res_w/(Hex.HEX_GRID_SIZE_X*cam.zoom)+DRAW_MARGIN_X*2;j++) {
                Hex hex = get(j + start_x_index, i + start_y_index);
                if (hex!=null) {
                    int x = j + start_x_index;
                    int y = i + start_y_index;
                    
                    float x_draw = (x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x)*cam.zoom;
                    float y_draw = (y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y)*cam.zoom;
                    float x_scale = (HEX_GRID_SIZE_X)*cam.zoom;
                    float y_scale = (HEX_GRID_SIZE_Y)*cam.zoom;
                    
                    hex.render(x_draw, y_draw, x_scale, y_scale, container, game, g);
                    render_counter++;
                } else {
                    not_render_counter++;
                }
            }
        }
    }
    
    public void renderMouseShadow (Camera cam, int x, int y) {
        if (get(x,y)!=null) {
            float x_draw = (x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x)*cam.zoom;
            float y_draw = (y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y)*cam.zoom;
            float x_scale = (HEX_GRID_SIZE_X)*cam.zoom;
            float y_scale = (HEX_GRID_SIZE_Y)*cam.zoom;
            
            get(x,y).renderMouseShadow(x_draw, y_draw, x_scale, y_scale);
        }
    }
}
