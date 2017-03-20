/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

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
public class Spell {
    String name;
    String img_path;
    String display_name;
    String spell_script_code;
    String parent_school_name;
    
    Image img;
    SpellSchool parent_school;
    
    public Spell (String spell_name) {
        this.name = spell_name;
        this.img_path = Consts.SPELLS_PATH+name.toLowerCase()+".spl";
        
        try {
            this.init();
        } catch (IOException | SlickException ex) {
            ex.printStackTrace();
        }
    }
    
    private void init () throws IOException, SlickException {
        File f = new File (img_path);
        SlickUtils.readObjectFromFile(f, this);
        
        this.img = new Image (this.img_path);
        parent_school = SpellSchool.valueOf(parent_school_name.trim().toUpperCase());
        parent_school.registerSpell(this);
    }
}
