/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.players.armies;

import game.data.players.unitdesign.UnitDesign;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    
    private boolean canFight () {
        return (composition.keySet().stream().anyMatch( (design) -> (composition.containsKey(design) && composition.get(design)>0) ));
    }
    
    private void takeCasualties (Map<UnitDesign,Integer> casualties, Map<UnitDesign,Integer> routed) {
        Map<UnitDesign,Integer> active_units = new HashMap<> ();
        Set<UnitDesign> all_designs = new HashSet<> ();
        all_designs.addAll(composition.keySet());
        all_designs.addAll(casualties.keySet());
        all_designs.addAll(routed.keySet());
        
        for (UnitDesign design : all_designs) {
            int actual = 0;
            
            if (composition.containsKey(design))
                actual += composition.get(design);
            if (casualties.containsKey(design))
                actual -= casualties.get(design);
            if (routed.containsKey(design))
                actual -= routed.get(design);
            
            if (actual>0) active_units.put(design, actual);
        }
        
        this.composition.clear();
        this.composition.putAll(active_units);
    }
    
    public void doBattle (Army other) {
        // repeat, until someone can't fight anymore
        do {
            
        } while (this.canFight() && other.canFight());
    }
    
}
