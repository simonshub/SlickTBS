/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.combat;

/**
 *
 * @author XyRoN
 */
public enum EffectTargetEnum {
    SINGLE_ANY      (true,  true,   true,   1.0f),
    SINGLE_ENEMY    (true,  true,   false,  1.0f),
    SINGLE_ALLY     (true,  false,  true,   1.0f),
    
    ALL             (false, true,   true,   1.0f),
    ALL_ENEMY       (false, true,   false,  3.0f),
    ALL_ALLY        (false, false,  true,   4.0f),
    
    ;
    
    public boolean targets_single;
    public boolean targets_enemy;
    public boolean targets_ally;
    
    public float challenge_rating;
    
    EffectTargetEnum (boolean targets_single, boolean targets_enemy, boolean targets_ally, float challenge_rating) {
        this.targets_single = targets_single;
        this.targets_enemy = targets_enemy;
        this.targets_ally = targets_ally;
        this.challenge_rating = challenge_rating;
    }
}
