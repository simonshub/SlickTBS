/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.players.unitdesign;

import java.util.Objects;

/**
 *
 * @author XyRoN
 */
public class UnitDesign {
    
    private final long uuid;
    
    private String display_name;
    
    private WeaponType weapon;
    
    private int armor_level;
    private int shield_level;
    private int weapon_level;
    private int training_level;
    
    private boolean mounted;
    
    // additional values are calculated on get (via method only!), such as;
    //     resource_cost, offense rating,
    //     defense rating, weapon bonuses...
    
    
    
    public UnitDesign () {
        uuid=System.currentTimeMillis();
    }
    
    
    
    @Override
    public boolean equals (Object obj) {
        if (obj instanceof UnitDesign) {
            UnitDesign other = (UnitDesign) obj;
            return  this.uuid == other.uuid &&
                    this.display_name.equals(other.display_name) &&
                    this.mounted == other.mounted &&
                    this.weapon == other.weapon &&
                    this.armor_level == other.armor_level &&
                    this.shield_level == other.shield_level &&
                    this.weapon_level == other.weapon_level &&
                    this.training_level == other.training_level;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.uuid ^ (this.uuid >>> 32));
        hash = 37 * hash + Objects.hashCode(this.display_name);
        hash = 37 * hash + Objects.hashCode(this.weapon);
        hash = 37 * hash + this.armor_level;
        hash = 37 * hash + this.shield_level;
        hash = 37 * hash + this.weapon_level;
        hash = 37 * hash + this.training_level;
        hash = 37 * hash + (this.mounted ? 1 : 0);
        return hash;
    }
    
}
