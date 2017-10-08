/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author emil.simon
 */
public class CombatCard {
    
    public String name;
    public String image_name;
    
    public int health_max, health_cur;
    public float health_modifier = 1.0f;
    public static final float HEALTH_CR_MOD = 0.5f;
    
    public int mana_max, mana_cur;
    public int mana_per_turn () { return mana_max < 1 ? 0 : ( (int) Math.ceil(mana_max / CombatMgr.MAX_MANA_TO_PER_TURN) ); }
    public float mana_modifier = 1.0f;
    public static final float MANA_CR_MOD = 1.5f;
    
    public int stamina_max, stamina_cur;
    public int stamina_per_turn () { return stamina_max < 1 ? 0 : ( (int) Math.ceil(stamina_max / CombatMgr.MAX_STAMINA_TO_PER_TURN) ); }
    public float stamina_modifier = 1.0f;
    public static final float STAMINA_CR_MOD = 1.0f;
    
    public Map<DamageType, Float> resistances;
    public static final float RESISTANCE_CR_MOD = 1.0f;
    
    public List<Ability> abilities;
    public static final float ABILITY_CR_MOD = 1.0f;
    
    
    
    public CombatCard () {
        abilities = new ArrayList<> ();
        resistances = new HashMap<> ();
        for (DamageType type : DamageType.values())
            resistances.put(type, 1.0f);
    }
    
    
    
    public int challenge_rating () {
        return (int)Math.floor(
            health_max *HEALTH_CR_MOD +
            mana_max *MANA_CR_MOD +
            stamina_max *STAMINA_CR_MOD +
            abilities.stream().mapToInt(Ability::challenge_rating).sum() *ABILITY_CR_MOD +
            ( resistances.values().stream().mapToInt(Float::intValue).sum() - resistances.values().size() ) *RESISTANCE_CR_MOD
        );
    }
    
    
    
    public void renderField () {
        
    }
    
    public void renderMouseoverDetails () {
        
    }
    
    public void renderPlayingDetails () {
        
    }
    
}
