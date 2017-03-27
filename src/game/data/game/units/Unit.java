/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game.units;

import game.data.map.Hex;

/**
 *
 * @author emil.simon
 */
public abstract class Unit {
    public String name;
    public Hex hex;
    public int owner_id;
    public boolean selected;
    
    public Unit (int owner_id) {
        this.name = "";
        this.owner_id = owner_id;
        this.selected = false;
    }
    
    public void moveTo (Hex hex) {
        this.hex = hex;
    }
}
