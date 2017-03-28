/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game.units;

import game.data.map.Hex;
import java.util.Random;

/**
 *
 * @author emil.simon
 */
public class Unit {
    public Hex hex;
    public UnitTypeEnum unit_type;
    
    public String display_name;
    public String display_description;
    
    public boolean is_military;
    
    public int owner_id;
    public int health_current;
    public int cost;          // 10 Power per point
    
    // MILITARY UNITS ONLY !
    
    public int attack;        // Number of six-sided dice to roll when determining damage. 1 die per point.
    public int defense;       // Number of damage ignored whenever damage is dealt. 1 die per point.
    public int health_total;  // Total number of damage the unit can take before dying. 10 HP per point.
    
    
    
    public Unit (int owner_id, UnitTypeEnum type) {
        this.owner_id = owner_id;
        this.unit_type = type;
        setAllValues(type);
    }
    
    public void moveTo (Hex hex) {
        this.hex = hex;
    }
    
    public final void setAllValues (UnitTypeEnum type) {
        this.attack = type.attack;
        this.cost = type.cost;
        this.defense = type.defense;
        this.display_description = type.display_description;
        this.display_name = type.display_name;
        this.health_total = type.health_total;
        this.health_current = type.health_total;
        this.is_military = type.is_military;
    }
    
    public void attack (Unit target) {
        Random rand = new Random ();
        
        for (int i=0;i<unit_type.attack;i++) {
            
        }
    }
}
