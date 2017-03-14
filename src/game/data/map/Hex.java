/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import main.ResMgr;
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
    public static final String HEX_GRID_IMG_PATH = "res/grfx/hex.png";
    public static final int HEX_GRID_SIZE_X = 64;
    public static final int HEX_GRID_SIZE_Y = 64;
    
    public static Image HEX_GRID_IMG;
    
    public int x, y;
    public Color color;
    public TerrainType terrain;
    
    public static void init () throws SlickException {
        HEX_GRID_IMG = new Image (HEX_GRID_IMG_PATH);
    }
    
    
    
    public Hex (int x, int y) {
        this.x = x;
        this.y = y;
        terrain = TerrainType.DEFAULT;
        color = new Color (1f,1f,1f,0f);
    }
    
    public void setLocation (int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setColor (Color col) {
        color = col;
    }
    
    public void render (Camera cam, GameContainer container, StateBasedGame game, Graphics g) {
        if (terrain != null && terrain.img != null) {
            terrain.img.draw(
                             (x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x)*cam.zoom,
                             (y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y)*cam.zoom,
                             (HEX_GRID_SIZE_X)*cam.zoom,
                             (HEX_GRID_SIZE_Y)*cam.zoom
                            );
        }
        
        if (HEX_GRID_IMG == null)
            return;
        
//        g.setColor(color);
//        g.drawRect( (x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x)*cam.zoom,
//                    (y*(HEX_GRID_SIZE_Y*3/4)-cam.y + (int)(Hex.HEX_GRID_SIZE_Y*1/8))*cam.zoom,
//                    (HEX_GRID_SIZE_X)*cam.zoom,
//                    (HEX_GRID_SIZE_Y*3/4)*cam.zoom);
        
        if (ResMgr.render_grid) HEX_GRID_IMG.draw(
                                 (x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x)*cam.zoom,
                                 (y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y)*cam.zoom,
                                 (HEX_GRID_SIZE_X)*cam.zoom,
                                 (HEX_GRID_SIZE_Y)*cam.zoom
                                );
    }
    
    // DEBUG
    public void redify () {
        this.color = new Color (1f,0f,0f,this.color.a+0.1f);
    }
}
