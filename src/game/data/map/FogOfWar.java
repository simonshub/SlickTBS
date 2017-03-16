/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

/**
 *
 * @author emil.simon
 */
public enum FogOfWar {
    HIDDEN(3), DISCOVERED(2), REVEALED(1), VISIBLE(0);
    
    public int level;
    
    FogOfWar (int level) {
        this.level = level;
    }
};
