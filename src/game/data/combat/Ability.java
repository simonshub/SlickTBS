/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.combat;

import java.util.Map;

/**
 *
 * @author emil.simon
 */
public class Ability {
    
    public String name;
    public String text;
    
    public EffectTargetEnum target;
    public Map<DamageType, Integer> damage;
    
    public int challenge_rating () {
        int sum = 0;
        sum = damage.values().stream().map((dmg) -> dmg).reduce(sum, Integer::sum);
        sum *= target.challenge_rating;
        
        return sum;
    }
    
}
