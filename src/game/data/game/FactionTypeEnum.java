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
import main.utils.Log;
import main.utils.SlickUtils;

/**
 *
 * @author emil.simon
 */
public enum FactionTypeEnum {

    DESPOTIC (),
    HEGEMONIC (),
    IMPERIAL (),
    OLEGARCHIC (),
    RELIGIOUS (),
    TRIBAL (),
    
    ;
    
    public static final int RANDOM_TYPE_MAX_RETRY_COUNT = 200;
    
    public List<Race> available_races;
    public List<String> available_races_name_list;
    
    public double create_village;
    public double create_town;
    public double create_city;
    public double create_outpost;
    public double create_stronghold;
    public double create_castle;
    
    public List<String> rule_titles;
    public List<String> ruler_titles;
    
    
    
    FactionTypeEnum () {
        available_races = new ArrayList<> ();
        
        try {
            init();
        } catch (IOException ex) {
            Log.err(ex);
        }
        
        Log.log("Loaded faction type enum "+this.name());
    }
    
    
    
    public void init () throws IOException {
        String f_path = Consts.FACTIONS_PATH + this.name().toLowerCase() + Consts.FACTIONS_EXT;
        File f = new File (f_path);
        SlickUtils.readObjectFromFile(f, this);
    }
    
    

    public static FactionTypeEnum random (Race race) {
        FactionTypeEnum result = FactionTypeEnum.TRIBAL;

        for (int i=0;i<RANDOM_TYPE_MAX_RETRY_COUNT;i++) {
            result = FactionTypeEnum.values()[SlickUtils.randIndex(FactionTypeEnum.values().length)];
            if (result.available_races.contains(race))
                break;
        }

        return result;
    }

    public String getRuleName () {
        return rule_titles.get(SlickUtils.randIndex(rule_titles.size()));
    }
    
}