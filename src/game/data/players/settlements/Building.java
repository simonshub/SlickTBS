/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.players.settlements;

import java.util.function.Function;

/**
 *
 * @author XyRoN
 */
public enum Building {
    
    FARMS (
            "Farmland", 10, 1,
            (lvl) -> lvl*10,
            (lvl) -> lvl*5
        ),
    INDUSTRY (
            "Industry", 10, 1,
            (lvl) -> lvl*5,
            (lvl) -> lvl*10
        ),
    RESIDENCES (
            "Residence", 10, 1,
            (lvl) -> lvl*5,
            (lvl) -> lvl*5
        ),
    FORTIFICATIONS (
            "Fortifications", 10, 0,
            (lvl) -> lvl*5,
            (lvl) -> lvl*20
        ),
    
    
    BARRACKS (
            "Barracks", 5, 0,
            (lvl) -> lvl*10,
            (lvl) -> lvl*15
        ),
    TRAINING_GROUNDS (
            "Training Grounds", 5, 0,
            (lvl) -> lvl*15,
            (lvl) -> lvl*5
        ),
    
    
    TEMPLE (
            "Temple", 3, 0,
            (lvl) -> lvl*25,
            (lvl) -> lvl*15
        ),
    LIBRARY (
            "Library", 1, 0,
            (lvl) -> lvl*100,
            (lvl) -> lvl*75
        ),
    UNIVERSITY (
            "University", 1, 0,
            (lvl) -> lvl*150,
            (lvl) -> lvl*100
        ),
    WAR_ACADEMY (
            "War Academy", 1, 0,
            (lvl) -> lvl*100,
            (lvl) -> lvl*150
        ),
    
    ;
    
    private final String display_name;
    
    private final int max_level;
    private final int starting_level;
    
    private final Function<Integer, Integer> gold_cost;
    private final Function<Integer, Integer> resource_cost;
    
    private Building (String display_name, int max_level, int starting_level, Function<Integer,Integer> gold_cost, Function<Integer,Integer> resource_cost) {
        this.display_name = display_name;
        
        this.max_level = max_level;
        this.starting_level = starting_level;
        
        this.gold_cost = gold_cost;
        this.resource_cost = resource_cost;
    }
    
    public int getGoldCost (int for_level) {
        return gold_cost.apply(for_level);
    }
    
    public int getResourceCost (int for_level) {
        return resource_cost.apply(for_level);
    }
    
    public int getStartingLevel () {
        return starting_level;
    }
    
    public boolean isMaxed (int current_level) {
        return current_level >= max_level;
    }
    
    @Override
    public String toString () {
        return display_name;
    }
    
}
