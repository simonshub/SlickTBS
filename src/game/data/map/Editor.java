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
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author XyRoN
 */
public class Editor {
    public enum State { NONE,TILE,TILE_SPECIAL,UNIT };
    
    public State state;
    public TerrainType paintTile;
    public boolean show;
        
    private final int MENU_ITEM_SIZE = 32;
    
    public Editor () {
        state = State.NONE;
        paintTile = TerrainType.DEFAULT;
        show = false;
    }
    
    public void nextTile () {
        boolean next=false;
        for (int i=0;i<TerrainType.values().length;i++) {
            if (next) { paintTile = TerrainType.values()[i]; break; }
            next = TerrainType.values()[i].equals(paintTile);
        }
    }
    
    public void prevTile () {
        boolean next=false;
        for (int i=TerrainType.values().length-1;i>=0;i--) {
            if (next) { paintTile = TerrainType.values()[i]; break; }
            next = TerrainType.values()[i].equals(paintTile);
        }
    }
    
    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) {
        if (!show) return;
        
        if (state.equals(State.TILE)) {
            int height = MENU_ITEM_SIZE * TerrainType.values().length;
            int width = ResMgr.screen_res_w/4;
            int y = 0;

            g.setColor(Color.black);
            g.fillRect(0, 0, width, height);
            for (TerrainType terrain : TerrainType.values()) {
                if (terrain.img!=null) {
                    if (paintTile.equals(terrain)) g.setColor(Color.blue);
                    else g.setColor(Color.red);
                    g.drawRect(0, y, width, MENU_ITEM_SIZE);

                    terrain.img.draw(0,y,MENU_ITEM_SIZE,MENU_ITEM_SIZE);
                    g.setColor(Color.white);
                    g.drawString(terrain.name(), MENU_ITEM_SIZE, y+(MENU_ITEM_SIZE/4));
                    y+=MENU_ITEM_SIZE;
                }
            }
        }
    }
    
    public boolean update (GameContainer gc, StateBasedGame sbg) {
        if (!show) return false;
        
        int x = gc.getInput().getMouseX();
        int y = gc.getInput().getMouseY();
        
        if (x>=0 && x<=(ResMgr.screen_res_w/4) && y>=0 && y<=(MENU_ITEM_SIZE*TerrainType.values().length)) {
            if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                int index = y / MENU_ITEM_SIZE;
                if (index>=0 && index<TerrainType.values().length) {
                    this.paintTile = TerrainType.values()[index];
                    System.out.println("Selected "+paintTile.name());
                }
            }
            return true;
        }
        
        return false;
    }
}
