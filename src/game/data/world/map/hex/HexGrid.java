/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.world.map.hex;

import game.data.world.map.Camera;
import game.data.world.map.Continent;
import game.data.world.map.TerrainTypeEnum;
import static game.data.world.map.hex.Hex.HEX_GRID_SIZE_X;
import static game.data.world.map.hex.Hex.HEX_GRID_SIZE_Y;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.ResMgr;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.utils.Log;

/**
 *
 * @author emil.simon
 */
public final class HexGrid {
    public static final int DRAW_MARGIN_X = 2;
    public static final int DRAW_MARGIN_Y = 2;
    
    public List<Hex> land;
    public List<Continent> continents;
    
    public int render_counter;
    public int not_render_counter;
    private final int size_x;
    private final int size_y;
    private final List<List<Hex>> grid;
    
    public HexGrid (int size_x, int size_y) {
        this.size_x = size_x;
        this.size_y = size_y;
        this.render_counter = 0;
        
        continents = new ArrayList<> ();
        
        grid = new ArrayList <> ();
        for (int i=0;i<size_y;i++) {
            grid.add(new ArrayList<> ());
            for (int j=0;j<size_x;j++) {
                grid.get(i).add(new Hex (j, i, "none"));
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
            Log.err(ex);
            return null;
        }
    }
    
    public int getSizeX () {
        return size_x;
    }
    
    public int getSizeY () {
        return size_y;
    }
    
    
    
    public List<Hex> getAll () {
        List<Hex> result = new ArrayList<> ();
        for (int y=0;y<size_y;y++) {
            for (int x=0;x<size_x;x++) {
                result.add(get(x,y));
            }
        }
        return result;
    }
    
    public List<Hex> getAllOfType (TerrainTypeEnum type) {
        List<Hex> result = new ArrayList<> ();
        for (int y=0;y<size_y;y++) {
            for (int x=0;x<size_x;x++) {
                if (get(x,y).terrain.equals(type)) {
                    result.add(get(x,y));
                }
            }
        }
        return result;
    }
    
    public List<Hex> getAllNotOfType (TerrainTypeEnum type) {
        List<Hex> result = new ArrayList<> ();
        for (int y=0;y<size_y;y++) {
            for (int x=0;x<size_x;x++) {
                if (!get(x,y).terrain.equals(type)) {
                    result.add(get(x,y));
                }
            }
        }
        return result;
    }
    
    public List<Hex> getAllOfTypes (TerrainTypeEnum... types) {
        List<Hex> result = new ArrayList<> ();
        for (int y=0;y<size_y;y++) {
            for (int x=0;x<size_x;x++) {
                if (!Arrays.asList(types).contains(get(x,y).terrain)) {
                    result.add(get(x,y));
                    break;
                }
            }
        }
        return result;
    }
    
    public List<Hex> getAllNotOfTypes (TerrainTypeEnum... types) {
        List<Hex> result = new ArrayList<> ();
        for (int y=0;y<size_y;y++) {
            for (int x=0;x<size_x;x++) {
                if (!Arrays.asList(types).contains(get(x,y).terrain)) {
                    result.add(get(x,y));
                    break;
                }
            }
        }
        return result;
    }
    
    public Hex getRandomHexOfType (TerrainTypeEnum type) {
        Hex hex = null;
        
        do {
            int x = (int)(Math.random() * size_x);
            int y = (int)(Math.random() * size_y);
            hex = get(x,y);
        } while (hex==null || !hex.terrain.equals(type));
        
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
}
