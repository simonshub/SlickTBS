/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.engine.tokens;

import game.data.players.Player;
import game.engine.map.GameMap;
import game.engine.map.Hex;

/**
 *
 * @author XyRoN
 */
public class Token <T> {
    
    public final GameMap parent;
    
    public T object;
    
    public Hex location = null;
    public Player owner = null;
    
    
    
    public Token (final GameMap parent, final Player owner, T object, int x, int y) {
        this.parent = parent;
        this.owner = owner;
        this.object = object;
        this.location = parent.grid.get(x, y);
    }
    
    
    
    public void render () {
        
    }
    
}
