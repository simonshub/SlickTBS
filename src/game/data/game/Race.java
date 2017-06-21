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
import org.newdawn.slick.SlickException;

/**
 *
 * @author emil.simon
 */
public enum Race {
    ORC ,
    HUMAN ,
    ELF ,
    DWARF ,
    GOBLIN ,
    
    ;
    
    public boolean playable;
    
    public int str_mod;
    public int agi_mod;
    public int dex_mod;
    public int vit_mod;
    public int int_mod;
    public int cha_mod;
    
    public String name;
    public String plural;
    public String adjective;
    
    public List<String> prefixes;
    public List<String> suffixes;
    public List<String> last_names;
    public List<String> first_names;
    
    Race () {
        prefixes = new ArrayList<> ();
        suffixes = new ArrayList<> ();
        last_names = new ArrayList<> ();
        first_names = new ArrayList<> ();
        
        try {
            init();
        } catch (IOException | SlickException ex) {
            ex.printStackTrace();
        }
    }
    
    public void init () throws IOException, SlickException {
        String f_path = Consts.RACES_PATH+this.name().toLowerCase()+".tile";
        File f = new File (f_path);
        SlickUtils.readObjectFromFile(f, this);
    }
}
