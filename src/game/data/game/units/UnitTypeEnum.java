/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game.units;

import org.newdawn.slick.Image;

/**
 *
 * @author XyRoN
 */
public enum UnitTypeEnum {
    WORKER, APPRENTICE,
    HUMAN_ARMY, ORC_ARMY, HOBGOBLIN_ARMY, LIZARDFOLK_ARMY, DWARF_ARMY, ELF_ARMY, GOBLIN_ARMY,
    HOSTILE_UNDEAD, HOSTILE_SPECTRES, HOSTILE_DEMON, HOSTILE_VAMPIRE, HOSTILE_DRAGON, HOSTILE_
    ;
    
    public Image token;
    public String token_path;
    
    public Image portrait;
    public String portrait_path;
    
    public String display_name;
    public String display_description;
    
    public int attack;        // Number of six-sided dice to roll when determining damage. 1 per point.
    public int defense;       // Number of damage ignored whenever damage is dealt. 1 per point.
    public int health_total;  // Total number of damage the unit can take before dying. 10 hp per point.
    
    public int cost;
}
