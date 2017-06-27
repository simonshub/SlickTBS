/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

import game.data.hex.Hex;
import game.data.hex.HexGrid;
import game.data.map.Continent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.utils.Log;
import main.utils.SlickUtils;

/**
 *
 * @author XyRoN
 */
public class Culture {
    
    public enum CultureTypeEnum {
        
        HEGEMONY        (1.5,   2.0,    0.5,    3.0,
                new String[] { "Hegemony", "Independant States", "Free Cities", "Federation" },
                new String[] { "Hegemony", "City-States", "Citites" },
                new Race[]   { Race.DWARF, Race.HUMAN, Race.ELF, Race.HOBGOBLIN, Race.LIZARDFOLK }
            ),
        EMPIRE          (1.0,   1.0,    2.0,    0.5,
                new String[] { "Empire", "Imperium" },
                new String[] { "Empire", "Autocracy" },
                new Race[]   { Race.DWARF, Race.HUMAN, Race.ELF, Race.HOBGOBLIN }
            ),
        KINGDOM         (1.0,   1.0,    1.0,    1.0,
                new String[] { "Kingdom", "Grand Duchy" },
                new String[] { "Monarchs", "Despots" },
                new Race[]   { Race.DWARF, Race.HUMAN, Race.ELF, Race.HOBGOBLIN }
            ),
        REPUBLIC        (0.5,   0.5,    2.0,    2.0,
                new String[] { "Republic" },
                new String[] { "Republics", "Freefolk" },
                new Race[]   { Race.DWARF, Race.HUMAN, Race.ELF, Race.LIZARDFOLK }
            ),
        CLANS           (1.5,   2.0,    0.5,    100.,
                new String[] { "Clans", "Tribes", "Nation" },
                new String[] { "Clans", "Tribes", "Nation" },
                new Race[]   { Race.DWARF, Race.HUMAN, Race.GOBLIN, Race.HOBGOBLIN, Race.LIZARDFOLK, Race.ORC }
            ),
        HOLY_ORDER      (2.0,   1.0,    1.5,    2.0,
                new String[] { "Faith", "Order", "Temple", "Movement" },
                new String[] { "Faith", "Zealot-States" },
                new Race[]   { Race.DWARF, Race.HUMAN, Race.HOBGOBLIN, Race.LIZARDFOLK }
            ),
        
        ;
        
        public static final int RANDOM_TYPE_MAX_RETRY_COUNT = 200;
        
        public Race[] available_races;
        
        public double spread_preference;
        public double upgrade_preference;
        public double dominance_preference;
        
        public double faction_factor;
        
        public String[] rule_names;
        public String[] culture_names;
        
        CultureTypeEnum (double dominance, double spread, double upgrade, double factions, String[] rule_names, String[] culture_names, Race[] races) {
            this.available_races = races;
            
            this.spread_preference = spread;
            this.upgrade_preference = upgrade;
            this.dominance_preference = dominance;
            
            this.faction_factor = factions;
            
            this.rule_names = rule_names;
            this.culture_names = culture_names;
        }
        
        public static CultureTypeEnum random (Race race) {
            CultureTypeEnum result = CultureTypeEnum.CLANS;
            
            for (int i=0;i<RANDOM_TYPE_MAX_RETRY_COUNT;i++) {
                result = CultureTypeEnum.values()[SlickUtils.randIndex(CultureTypeEnum.values().length)];
                if (Arrays.asList(result.available_races).contains(race))
                    break;
            }
            
            return result;
        }
        
        public String getRuleName () {
            return rule_names[SlickUtils.randIndex(rule_names.length)];
        }
        
        public String getCultureName () {
            return culture_names[SlickUtils.randIndex(culture_names.length)];
        }
        
    }
    
    
    
    public String name;
    public String name_founder;
    public String name_place;
    public String name_type;
    
    public Race race;
    public Hex source_hex;
    public CultureTypeEnum type;
    public Continent source_continent;
    public List<Faction> factions;
    
    public Culture (HexGrid grid, Hex source) {
        source_hex = source;
        source_continent = source_hex.continent;
        
        type = CultureTypeEnum.random(race);
        race = Race.random(source_continent.continent_type);
        
        name_founder = NameGenerator.character(race, false);
        name_place = NameGenerator.place(race);
        name_type = type.getCultureName();
        name = NameGenerator.culture(race, source_continent, type, name_founder, name_place, name_type);
        
        factions = new ArrayList<> ();
        Faction f = new Faction (grid, this);
        f.name = NameGenerator.faction(race, source_continent, type, name_founder, name_place, name_type);
        f.capital.poi.name = name_place;
        factions.add(f);
//        f.addTerritory(source_hex.getAllInRange(grid, 3));
//        source_hex.poi = PointOfInterest.CULTURAL_CENTER;
        
        Log.log("Created culture '"+name+"'; "+race.name+" on "+source_continent.name+" of type "+type.name());
    }
    
}
