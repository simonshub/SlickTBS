/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import game.data.game.TerrainType;
import game.data.game.units.Unit;
import java.util.List;
import main.ResMgr;
import main.utils.Point;
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
    public List<Unit> units;
    public FogOfWar fog_of_war;
    public TerrainType terrain;
    
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
    
    public void render (Camera cam, GameContainer container, StateBasedGame game, Graphics g) {
        if (terrain != null && terrain.img != null && fog_of_war!=FogOfWar.HIDDEN) {
            terrain.img.draw(
                             (x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x)*cam.zoom,
                             (y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y)*cam.zoom,
                             (HEX_GRID_SIZE_X)*cam.zoom,
                             (HEX_GRID_SIZE_Y)*cam.zoom
                            );
        }
        
        if (HEX_FOG_OF_WAR_IMG != null) {
            HEX_FOG_OF_WAR_IMG.draw(
                             (x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x)*cam.zoom,
                             (y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y)*cam.zoom,
                             (HEX_GRID_SIZE_X)*cam.zoom,
                             (HEX_GRID_SIZE_Y)*cam.zoom,
                             new Color (1f,1f,1f,fog_of_war.level/3)
                            );
        }
        
        if (HEX_GRID_IMG == null)
            return;
        
        if (ResMgr.render_grid) HEX_GRID_IMG.draw(
                                 (x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x)*cam.zoom,
                                 (y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y)*cam.zoom,
                                 (HEX_GRID_SIZE_X)*cam.zoom,
                                 (HEX_GRID_SIZE_Y)*cam.zoom
                                );
        
    }
    
    public void renderMouseShadow (Camera cam) {
        if (HEX_OVERLAY_IMG != null) {
            HEX_OVERLAY_IMG.draw(
                             (x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x)*cam.zoom,
                             (y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y)*cam.zoom,
                             (HEX_GRID_SIZE_X)*cam.zoom,
                             (HEX_GRID_SIZE_Y)*cam.zoom,
                             new Color (0f,0f,0f,0.2f)
                            );
        }
    }
}
