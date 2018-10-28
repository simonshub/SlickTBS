/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.players.unitdesign;

/**
 *
 * @author XyRoN
 */
public class UnitDesign {
    
    private WeaponType weapon;
    
    private int armor_level;
    private int shield_level;
    private int weapon_level;
    private int training_level;
    
    private boolean mounted;
    
    // additional values are calculated on get (via method only!), such as;
    //     resource_cost, offense rating,
    //     defense rating, weapon bonuses...
    
}
