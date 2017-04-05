/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.players;

/**
 *
 * @author emil.simon
 */
public class Ability {
    public enum AbilityTrigger { TURN_START, TURN_END, ONCE };
    
    public String description;
    public String display_name;
    
    public String script;
    public int run_counter_base;
    public int run_counter_current;
    public AbilityTrigger trigger;
    
    public Ability () {
    }
}
