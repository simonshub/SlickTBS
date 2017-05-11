/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import java.util.ArrayList;
import java.util.List;
import main.ResMgr;
import main.utils.Point;
import main.utils.SlickUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author emil.simon
 */
public class Hex {
    public static final String HEX_FOG_OF_WAR_IMG_PATH = "res/grfx/fog.png";
    public static final String HEX_OVERLAY_IMG_PATH = "res/grfx/overlay.png";
    public static final String HEX_GRID_IMG_PATH = "res/grfx/hex_blue.png";
    public static final int HEX_GRID_SIZE_X = 64;
    public static final int HEX_GRID_SIZE_Y = 64;
    
    public static Image HEX_FOG_OF_WAR_IMG;
    public static Image HEX_OVERLAY_IMG;
    public static Image HEX_GRID_IMG;
    
    public boolean river;
    
    public int x, y;
    public Color color;
    public FogOfWar fog_of_war;
    public TerrainType terrain;
    
    public enum DirEnum { UPPER_RIGHT(1,-1), RIGHT(1,0), LOWER_RIGHT(1,1), LOWER_LEFT(-1,1), LEFT(-1,0), UPPER_LEFT(-1,-1);
        public int x_offset, y_offset;
        
        DirEnum(int x, int y) { x_offset=x; y_offset=y; }
        
        public static DirEnum getRandom() {
            int index = (int)(Math.random() * DirEnum.values().length);
            return DirEnum.values()[index];
        }
    };
    
    
    
    public static void init () throws SlickException {
        HEX_GRID_IMG = new Image (HEX_GRID_IMG_PATH);
        HEX_OVERLAY_IMG = new Image (HEX_OVERLAY_IMG_PATH);
        HEX_FOG_OF_WAR_IMG = new Image (HEX_FOG_OF_WAR_IMG_PATH);
    }
    
    public Hex (int x, int y) {
        this.x = x;
        this.y = y;
        terrain = TerrainType.DEFAULT;
        color = new Color (1f,1f,1f,0f);
        river = false;
        fog_of_war = FogOfWar.VISIBLE;
    }
    
    
    
    public void setLocation (int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setColor (Color col) {
        color = col;
    }
    
    public Point getMapCoordsCenter () {
        Point p = new Point ();
        p.x = x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0) + (int)(HEX_GRID_SIZE_X/2);
        p.y = y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y) + (int)(HEX_GRID_SIZE_Y/2);
        return p;
    }
    
    public Point getScreenCoordsCenter (Camera cam) {
        Point p = new Point ();
        p.x = (int) ((x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x + (int)(HEX_GRID_SIZE_X/2))*cam.zoom);
        p.y = (int) ((y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y + (int)(HEX_GRID_SIZE_Y/2))*cam.zoom);
        return p;
    }
    
    public void render (float x_draw, float y_draw, float x_scale, float y_scale, GameContainer container, StateBasedGame game, Graphics g) {
        if (terrain != null && terrain.img != null && fog_of_war!=FogOfWar.HIDDEN)
            terrain.img.draw(x_draw, y_draw, x_scale, y_scale);
        
        if (HEX_FOG_OF_WAR_IMG != null && fog_of_war.level != 0)
            HEX_FOG_OF_WAR_IMG.draw(x_draw, y_draw, x_scale, y_scale, new Color (1f,1f,1f,fog_of_war.level/3));
        
        if (HEX_GRID_IMG == null)
            return;
        
        if (ResMgr.render_grid)
            HEX_GRID_IMG.draw(x_draw, y_draw, x_scale, y_scale);
    }
    
    public void renderMouseShadow (float x_draw, float y_draw, float x_scale, float y_scale) {
        if (HEX_OVERLAY_IMG != null)
            HEX_OVERLAY_IMG.draw(x_draw, y_draw, x_scale, y_scale, new Color (0f,0f,0f,0.2f));
    }
    
    
    
    public Hex getAdjacent (HexGrid grid, DirEnum direction) {
        return grid.get(x+direction.x_offset, y+direction.y_offset);
    }
    
    public Hex getRandomAdjacent (HexGrid grid) {
        return getAdjacent(grid, DirEnum.getRandom());
    }
    
    public Hex getRandomAdjacentOfType (HexGrid grid, TerrainType type) {
        List<Hex> result = new ArrayList<> ();
        for (DirEnum dir : DirEnum.values())
            if (this.getAdjacent(grid, dir)!=null && this.getAdjacent(grid, dir).terrain.equals(type))
                result.add(this.getAdjacent(grid, dir));
        
        if (result.isEmpty())
            return null;
        
        int index = (int)(Math.random()*result.size());
        return result.get(index);
    }
    
    public List<Hex> getAllAdjacent (HexGrid grid) {
        List<Hex> result = new ArrayList<> ();
        for (DirEnum dir : DirEnum.values())
            if (this.getAdjacent(grid, dir)!=null) result.add(this.getAdjacent(grid, dir));
        return result;
    }
    
    public boolean isCoastal (HexGrid grid) {
        for (DirEnum dir : DirEnum.values())
            if (getAdjacent(grid,dir)!=null && getAdjacent(grid,dir).terrain.equals(TerrainType.SEA) && !terrain.equals(TerrainType.SEA)) return true;
        return false;
    }
    
    public boolean spreadTerrain (HexGrid grid, List<Hex> land) {
        DirEnum[] enums = DirEnum.values();
        SlickUtils.shuffleArray(enums);
        for (DirEnum dir : enums) {
            if (getAdjacent(grid, dir)!=null && (getAdjacent(grid, dir).terrain != terrain)) {
                getAdjacent(grid, dir).terrain = terrain;
                if (terrain!=TerrainType.SEA) land.add(getAdjacent(grid, dir));
                return true;
            }
        }
        
        return false;
    }
}
