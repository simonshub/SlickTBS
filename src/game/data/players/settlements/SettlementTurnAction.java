/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.players.settlements;

import java.util.function.Predicate;

/**
 *
 * @author XyRoN
 */
public enum SettlementTurnAction {
    
    ADVANCE_TECHNOLOGY,     // tick down one turn on selected tech
    
    BUILD_FARMS,            // increase food income
    BUILD_INDUSTRY,         // increase resource and gold/pop income
    BUILD_RESIDENCES,       // increase pop, and consequently gold income
    BUILD_FORTIFICATIONS,   // increase settlement fortification level
    
    BUILD_BARRACKS,         // provides a number of pops for recruitment (so you don't have to spend active pops)
    BUILD_TRAINING_GROUNDS, // allows training of more and better units, increases in speed
    
    BUILD_BLACKSMITH,       // increases power of all units created here
    BUILD_ARMORSMITH,       // increases armor rating of all units created here
    BUILD_WEAPONSMITH,      // increases weapon rating of all units created here
    
    BUILD_TEMPLE,           // special, increase pop, gold/pop income and resource income
    BUILD_LIBRARY,          // special, advance tech action advances by 2 instead of 1
    BUILD_UNIVERSITY,       // special, advance tech action advances by 3 instead of 2
    BUILD_WAR_ACADEMY,      // special, increases training speed and increases training level of all built units by 1
    
    ;
        
    private Predicate<Settlement> requirement;
    
}
