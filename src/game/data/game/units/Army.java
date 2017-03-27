/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game.units;

import game.data.game.Race;

/**
 *
 * @author XyRoN
 */
public class Army extends Unit {
    public int attack;
    public int defense;
    public int cost;
    public int health;

    public Army(int owner_id, Race r) {
        super(owner_id);
        
        attack = r.army_attack;
        defense = r.army_defense;
        
        
        health = 100;
    }
    
    public void combat (Army other) {
        
    }
}
