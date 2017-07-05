/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.world.map;

/**
 *
 * @author XyRoN
 */
public enum DifficultyLevel {
    
        VERY_EASY   (2.0,   0.75,   2.0),
        EASY        (1.5,   0.75,   1.5),
        NORMAL      (1.0,   1.0,    1.0),
        HARD        (0.8,   1.2,    1.0),
        VERY_HARD   (0.8,   1.5,    0.75),
        INSANE      (0.5,   2.0,    0.5),
        
        ;
        
        public final double dmg_done_factor;
        public final double dmg_recieved_factor;
        public final double event_luck_factor;
        
        DifficultyLevel (double dmg_done, double dmg_recieved, double luck) {
            dmg_done_factor = dmg_done;
            dmg_recieved_factor = dmg_recieved;
            event_luck_factor = luck;
        }
    }