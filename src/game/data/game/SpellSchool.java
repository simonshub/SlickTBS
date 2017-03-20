/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

import java.util.HashMap;
import org.newdawn.slick.Image;

/**
 *
 * @author XyRoN
 */
public enum SpellSchool {
    ELEMENTAL, BLACK, WHITE, BLOOD, ELDRITCH;
    
    public Image img;
    public String img_path;
    public HashMap<String,Spell> spell_list;
    
    SpellSchool () {
        this.img = null;
        this.img_path = "";
        this.spell_list = new HashMap<> ();
    }
    
    public void registerSpell (Spell spell) {
        this.spell_list.put(spell.name, spell);
    }
}
