/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

import game.data.hex.Hex;
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
    
    public List<RaceEnum> available_races;
    
    public String capital_type_name;
    public PointOfInterest capital_type;
    
    public double composition_civilian;
    public double composition_hamlet;
    public double composition_village;
    public double composition_town;
    public double composition_city;
    
    public double composition_military;
    public double composition_outpost;
    public double composition_fort;
    public double composition_stronghold;
    public double composition_castle;
    
    public List<String> rule_titles;
    public List<String> ruler_titles;
    
    
    
    FactionTypeEnum () {
        available_races = new ArrayList<> ();
        
        try {
            init();
        } catch (IOException ex) {
            Log.err(ex);
        }
        
        capital_type = PointOfInterest.valueOf(capital_type_name);
        
        Log.log("Loaded faction type enum "+this.name());
    }
    
    
    
    public void init () throws IOException {
        String f_path = Consts.FACTIONS_PATH + this.name().toLowerCase() + Consts.FACTIONS_EXT;
        File f = new File (f_path);
        SlickUtils.readObjectFromFile(f, this);
    }
    
    

    public static FactionTypeEnum random (RaceEnum race) {
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
    
    public Integer getComposition (Faction f, PointOfInterest poi) {
        double mil_count=0.0;
        double civ_count=0.0;
        
        for (Hex hex : f.settlements) {
            if (hex.poi.isMilitary())
                mil_count+=1.0;
            else if (hex.poi.isCivilian())
                civ_count+=1.0;
        }
        
        // check whether the ratio dictates a next military or civilian settlement
        boolean military = (mil_count / civ_count) < (f.type.composition_military / f.type.composition_civilian);
        PointOfInterest poi_to_add = null;
        
        return 0;
    }
    
}
