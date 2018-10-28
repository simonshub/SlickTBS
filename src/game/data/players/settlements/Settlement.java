/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.players.settlements;

import game.data.players.Player;
import game.data.world.map.hex.Hex;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author XyRoN
 */
public class Settlement {
    
    public Player owner;
    
    public final Hex location;
    
    public final Map<Building, Integer> buildings;
    
    
    
    private static Map<Building, Integer> getNewSettlementBuildings () {
        Map<Building, Integer> buildings = new HashMap<> ();
        
        for (Building building : Building.values()) {
            buildings.put(building, building.getStartingLevel());
        }
        
        return buildings;
    }
    
    
    
    public Settlement (Player owner, Hex location) {
        this.owner = owner;
        this.location = location;
        this.buildings = getNewSettlementBuildings();
        this.setHex(location);
    }
    
    private final void setHex (final Hex hex) {
        hex.setSettlement(this);
    }
    
}
