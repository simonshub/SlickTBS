/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game.units;

/**
 *
 * @author emil.simon
 */
public abstract class Unit {
    public String name;
    public int owner_id;
    public int x, y;
    public boolean selected;
    
    public Unit (int owner_id) {
        this.name = "";
        this.owner_id = owner_id;
        this.x = 0;
        this.y = 0;
        this.selected = false;
    }
}
