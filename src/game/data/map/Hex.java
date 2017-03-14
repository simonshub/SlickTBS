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
        terrain = ResMgr.TERRAIN_DEFAULT;
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
        if (HEX_GRID_IMG == null)
            return;
        
        g.setColor(color);
        
        if (y%2==0) {
            HEX_GRID_IMG.draw(x*HEX_GRID_SIZE_X+HEX_GRID_SIZE_X/2-cam.x, y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y, HEX_GRID_SIZE_X, HEX_GRID_SIZE_Y);
            g.drawRect(x*HEX_GRID_SIZE_X-cam.x, y*HEX_GRID_SIZE_Y-cam.y, HEX_GRID_SIZE_X, HEX_GRID_SIZE_Y);
        } else {
            HEX_GRID_IMG.draw(x*HEX_GRID_SIZE_X-cam.x, y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y, HEX_GRID_SIZE_X, HEX_GRID_SIZE_Y);
            g.drawRect(x*HEX_GRID_SIZE_X-cam.x, y*HEX_GRID_SIZE_Y-cam.y, HEX_GRID_SIZE_X, HEX_GRID_SIZE_Y);
        }
    }
}
