/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import main.ResMgr;

/**
 *
 * @author XyRoN
 */
public class Editor {
    public enum State { NONE,TILE,TILE_SPECIAL,UNIT };
    
    public State state;
    public TerrainType paintTile;
    
    public Editor () {
        state = State.NONE;
        paintTile = TerrainType.DEFAULT;
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
}
