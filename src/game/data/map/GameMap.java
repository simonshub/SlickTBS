/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.utils.SlickUtils;
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
        
        generateMap();
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
        int x_index = (int)((x+(y_index%2==0?Hex.HEX_GRID_SIZE_X/2:0))/Hex.HEX_GRID_SIZE_X) - (y_index%2==0?1:0) - (x<0?1:0);
        
        mouse_shadow_x = x_index;
        mouse_shadow_y = y_index;
    }
    
    
    
    public final void generateMap () {
        Random rand = new Random ();
        
        // set the entire grid to sea, initialization
        for (int y=0;y<grid.getSizeY();y++) {
            for (int x=0;x<grid.getSizeX();x++) {
                grid.get(x, y).terrain = TerrainType.SEA;
            }
        }
        
        // make islands
        List<Hex> land = new ArrayList<> ();
        for (int i=0;i<grid.getNumberOfIslands();i++) {
            int x = rand.nextInt(grid.getSizeX());
            int y = rand.nextInt(grid.getSizeY());
            
            if (grid.get(x,y)!=null && grid.get(x,y).terrain==TerrainType.SEA && !land.contains(grid.get(x,y))) {
                grid.get(x,y).terrain = TerrainType.OPEN;
                land.add(grid.get(x,y));
            } else {
                i--;
            }
        }
        
        // extend random land until you have enough land
        List<Hex> coast;
        while (!grid.satisfiesLandPrecentage(land)) {
            coast = grid.getCoastalHexes();
            int index = rand.nextInt(coast.size());
            if (!coast.get(index).spreadTerrain(grid, land)) ;
        }
        
        
        // make mountain ranges with hills
        int mountain_ranges = (int)(Math.random()*3) + 2;
        int mt_counter=0;
        while (mt_counter < mountain_ranges) {
            int length = (int)(Math.random()*10) + 6;
            int index = SlickUtils.rand(0,land.size()); // starting point
            
            Hex hex = land.get(index);
            if (hex==null) continue;
            
            for (int i=0;i<length;i++) {
                hex.terrain = TerrainType.MOUNTAINS;
                hex = hex.getRandomAdjacentOfType(grid, TerrainType.OPEN);
            }
            
            mt_counter++;
        }
    }
}
