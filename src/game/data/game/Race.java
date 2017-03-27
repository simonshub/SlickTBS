/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.Consts;
import main.utils.SlickUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author XyRoN
 */
public enum Race {
    HUMAN       (),
    ELF         (),
    DWARF       (),
    ORC         (),
    GOBLIN      (),
    HOBGOBLIN   (),
    LIZARDFOLK  (),
    ;
    
    public int army_attack, army_defense, army_cost, army_health;
    public String army_name, army_description, army_special;
    
    public int apprentice_power, apprentice_knowledge;
    public String apprentice_description, apprentice_name_list;
    public List<String> apprentice_names;
    
    public Image town_img;
    public String town_img_path;
    public Image army_portrait;
    public String army_portrait_path;
    public Image apprentice_portrait;
    public String apprentice_portrait_path;
    
    
    Race () {
        this.town_img = null;
        this.town_img_path = "";
        this.army_portrait = null;
        this.army_portrait_path = "";
        this.apprentice_portrait = null;
        this.apprentice_portrait_path = "";
        this.apprentice_names = new ArrayList<> ();
        
        try {
            init();
        } catch (IOException | SlickException ex) {
            ex.printStackTrace();
        }
    }
    
    public void init () throws IOException, SlickException {
        File f = new File (Consts.RACES_PATH+this.name().toLowerCase()+".race");
        SlickUtils.readObjectFromFile(f, this);
        
        this.army_cost = (army_attack+army_defense)*3 + army_health;
        for (String name : apprentice_name_list.split(Consts.NAME_LIST_DELIMITER)) {
            this.apprentice_names.add(name.trim());
        }
        
        this.town_img = new Image (this.town_img_path);
        this.army_portrait = new Image (this.army_portrait_path);
        this.apprentice_portrait = new Image (this.apprentice_portrait_path);
    }
}
