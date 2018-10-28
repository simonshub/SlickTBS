/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.players.armies;

import game.data.players.unitdesign.UnitDesign;
import java.util.HashMap;
import java.util.Map;
import org.simon.utils.Log;

/**
 *
 * @author XyRoN
 */
public class Army {
    
    private final Map<UnitDesign,Integer> composition;
    
    
    
    public Army () {
        composition = new HashMap<> ();
    }
    
    
    
    public void add (UnitDesign design, int n) {
        int old_n = 0;
        
        if (composition.containsKey(design))
            old_n = composition.get(design);
        
        int new_n = Math.max(0, old_n+n);
        composition.put(design, new_n);
    }
    
    public boolean isEmpty () {
        return composition.keySet().stream().allMatch
                // if all design matches this condition (which checks whether it is present in this army), then it is empty !
                ( (d) -> composition.get(d)!=null && composition.get(d)>0 );
    }
    
    public Army split (Map<UnitDesign,Integer> what) {
        Army army = new Army ();
        
        for (UnitDesign design : what.keySet()) {
            if ( !composition.containsKey(design) || (composition.get(design)==null) || (composition.get(design)==0) ) {
                Log.err("Error while splitting army; tried to remove "+what.get(design)+" "+design+", but no such unit present in composition!");
            } else {
                // here we get the actual number of units that we will split off from the main army
                int actual_num = Math.min(
                                    composition.get(design), // maximum allowed, from composition
                                    what.get(design)         // what we want to split off
                                );
                // we use the add method because it does the calculations and checks
                this.add(design, -actual_num);
                army.add(design, actual_num);
            }
        }
        
        return army;
    }
    
}
