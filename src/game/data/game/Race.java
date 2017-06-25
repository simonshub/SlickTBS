/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

import game.data.map.ContinentTypeEnum;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.Consts;
import main.utils.Log;
import main.utils.SlickUtils;
import org.newdawn.slick.SlickException;

/**
 *
 * @author emil.simon
 */
public enum Race {
    ORC         (ContinentTypeEnum.BARRENS, ContinentTypeEnum.DESERT, ContinentTypeEnum.MOUNTAINOUS),
    HUMAN       (ContinentTypeEnum.VALLEY, ContinentTypeEnum.WOODLANDS, ContinentTypeEnum.FLAT_STEPPES, ContinentTypeEnum.DESERT, ContinentTypeEnum.MOUNTAINOUS),
    ELF         (ContinentTypeEnum.JUNGLE, ContinentTypeEnum.VALLEY, ContinentTypeEnum.WOODLANDS),
    DWARF       (ContinentTypeEnum.BARRENS, ContinentTypeEnum.WOODLANDS, ContinentTypeEnum.MOUNTAINOUS, ContinentTypeEnum.VALLEY),
    GOBLIN      (ContinentTypeEnum.BARRENS, ContinentTypeEnum.DESERT, ContinentTypeEnum.MOUNTAINOUS, ContinentTypeEnum.WOODLANDS, ContinentTypeEnum.FLAT_STEPPES),
    HOBGOBLIN   (ContinentTypeEnum.FLAT_STEPPES, ContinentTypeEnum.BARRENS, ContinentTypeEnum.DESERT, ContinentTypeEnum.MOUNTAINOUS),
    LIZARDFOLK  (ContinentTypeEnum.JUNGLE, ContinentTypeEnum.BARRENS, ContinentTypeEnum.VALLEY),
    
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
    
    public List<String> male_name_blocks;    // Format: [rule]-[possible letter]|[possible_letter]|[possible_letter] etc...
    public List<String> female_name_blocks;  // Format: [rule]-[possible letter]|[possible_letter]|[possible_letter] etc...
    public List<String> place_name_blocks;   // Format: [rule]-[possible letter]|[possible_letter]|[possible_letter] etc...
    public List<String> faction_name_blocks; // Format: [rule]-[possible letter]|[possible_letter]|[possible_letter] etc...
    public List<String> culture_name_blocks; // Format: [rule]-[possible letter]|[possible_letter]|[possible_letter] etc...
    
    public ContinentTypeEnum[] allowed_types;
    
    Race (ContinentTypeEnum... allowed_types) {
        this.allowed_types = allowed_types;
        
        male_name_blocks = new ArrayList<> ();
        female_name_blocks = new ArrayList<> ();
        place_name_blocks = new ArrayList<> ();
        faction_name_blocks = new ArrayList<> ();
        culture_name_blocks = new ArrayList<> ();
        
        try {
            init();
        } catch (IOException | SlickException ex) {
            Log.err(ex);
        }
        
        Log.log("Loaded race enum "+this.name());
    }
    
    public void init () throws IOException, SlickException {
        String f_path = Consts.RACES_PATH+this.name().toLowerCase()+".race";
        File f = new File (f_path);
        SlickUtils.readObjectFromFile(f, this);
    }
    
    public static Race random () {
        return Race.values()[SlickUtils.randIndex(Race.values().length)];
    }
    
    public static Race random (ContinentTypeEnum continent) {
        List<Race> possible_results = new ArrayList<> ();
        
        for (Race race : Race.values()) {
            if (Arrays.asList(race.allowed_types).contains(continent))
                possible_results.add(race);
        }
        
        return possible_results.get(SlickUtils.randIndex(possible_results.size()));
    }
}
