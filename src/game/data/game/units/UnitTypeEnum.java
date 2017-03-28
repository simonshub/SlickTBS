/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game.units;

import java.io.File;
import java.io.IOException;
import main.Consts;
import main.utils.SlickUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author XyRoN
 */
public enum UnitTypeEnum {
    WORKER, APPRENTICE,
    HUMAN_ARMY, ORC_ARMY, HOBGOBLIN_ARMY, LIZARDFOLK_ARMY, DWARF_ARMY, ELF_ARMY, GOBLIN_ARMY,
    NEUTRAL_UNDEAD, NEUTRAL_SPECTRES, NEUTRAL_DEMON, NEUTRAL_VAMPIRE, NEUTRAL_DRAGON, NEUTRAL_TREANTS
    ;
    
    public Image token;
    public String token_path;
    
    public Image portrait;
    public String portrait_path;
    
    public String display_name;
    public String display_description;
    
    public boolean is_military;
    
    public int cost;          // 10 Power per point
    
    // MILITARY UNITS ONLY !
    
    public int attack;        // Number of six-sided dice to roll when determining damage. 1 die per point.
    public int defense;       // Number of damage ignored whenever damage is dealt. 1 die per point.
    public int health_total;  // Total number of damage the unit can take before dying. 10 HP per point.
    
    
    
    UnitTypeEnum () {
        this.token = null;
        this.token_path = "";
        this.portrait = null;
        this.portrait_path = "";
        
        this.display_name = "";
        this.display_description = "";
        
        this.is_military = false;
        this.cost = 0;
        this.attack = 0;
        this.defense = 0;
        this.health_total = 0;
        
        try {
            init();
        } catch (IOException | SlickException ex) {
            ex.printStackTrace();
        }
    }
    
    public void init () throws IOException, SlickException {
        String file_path = Consts.UNITS_PATH+this.name().toLowerCase()+".unit";
        File f = new File (file_path);
        SlickUtils.readObjectFromFile(f, this);
        this.token = new Image (this.token_path);
        this.portrait = new Image (this.portrait_path);
    }
}
