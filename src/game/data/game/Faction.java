/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

import game.data.hex.Hex;
import game.data.hex.HexGrid;
import game.data.map.TerrainTypeEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import main.utils.SlickUtils;
import org.newdawn.slick.Color;

/**
 *
 * @author XyRoN
 */
public final class Faction {
    
    public static final int MAX_SETTLE_RETRIES = 100;
    
    public static final int MIN_RANGE_BETWEEN_SETTLEMENTS = 4;
    public static final int SPREAD_CANDIDATE_COUNT = 10;
    public static final int LAND_VALUE_SCAN_RANGE = 2;
    public static final int SETTLE_SCAN_RANGE = 10;
    
    public static final int MIN_ACTIONS = 15;
    public static final int MAX_ACTIONS = 30;
    
    public static final TerrainTypeEnum[] ALLOWED_TERRAIN_TYPES = {
                TerrainTypeEnum.GRASS, TerrainTypeEnum.ARID, TerrainTypeEnum.DESERT,
                TerrainTypeEnum.FOREST, TerrainTypeEnum.SAVANNA, TerrainTypeEnum.OPEN,
                TerrainTypeEnum.HILLS, TerrainTypeEnum.MARSHES, TerrainTypeEnum.TUNDRA
            };
    
    public static final TerrainTypeEnum[] IMPASSABLE_TERRAIN_TYPES = {
                TerrainTypeEnum.SEA, TerrainTypeEnum.MOUNTAINS
            };
    
    public static final PointOfInterest[] CIVILIAN_POIS_BY_LEVEL = {
                PointOfInterest.HAMLET, PointOfInterest.VILLAGE, PointOfInterest.TOWN, PointOfInterest.CITY, 
            };
    
    public static final PointOfInterest[] MILITARY_POIS_BY_LEVEL = {
                PointOfInterest.OUTPOST, PointOfInterest.FORT, PointOfInterest.STRONGHOLD, PointOfInterest.CASTLE, 
            };
    
    
    
    public String name;
    
    public RaceEnum race;
    public Hex capital;
    public Color color;
    public List<Hex> territory;
    public List<Hex> settlements;
    
    public FactionTypeEnum type;
    
    
    
    public Faction (HexGrid grid, Hex source) {
        race = RaceEnum.random(source.continent.continent_type);
        type = FactionTypeEnum.random(race);
        
        String name_place = NameGenerator.place(race);
        name = NameGenerator.faction(name_place, type);
        
        for (int retry=0;retry < MAX_SETTLE_RETRIES;retry++) {
            List<Hex> in_range = source.getAllInRange(grid, MIN_RANGE_BETWEEN_SETTLEMENTS);
            boolean is_settleable = true;
            
            for (Hex hex : in_range) {
                if (hex!=null && hex.poi!=null && hex.poi.isSettlement()) {
                    is_settleable = false;
                    break;
                }
            }
            
            if (is_settleable)
                break;
            else
                source = source.continent.getAll().get(SlickUtils.randIndex(source.continent.size()));
        }
        
        capital = source;
        capital.poi = new PointOfInterest(type.military_capital ? Faction.MILITARY_POIS_BY_LEVEL[1] : Faction.CIVILIAN_POIS_BY_LEVEL[1]);
        capital.poi.name = name_place;
        this.addTerritory(this.capital.getAllInRange(grid, 2));
        
        settlements = new ArrayList<> ();
        settlements.add(capital);
        
        this.color = new Color ((float) Math.random(), (float) Math.random(), (float) Math.random(), 0.75f);
        
    }
    
    
    
    public void resetTerritory () {
        this.territory = new ArrayList<> ();
    }
    
    public void addTerritory (Hex hex) {
        if (this.territory == null)
            this.territory = new ArrayList<> ();
        
        this.territory.add(hex);
        hex.owner = this;
    }
    
    public void addTerritory (Collection<Hex> hexes) {
        if (this.territory == null)
            this.territory = new ArrayList<> ();
        
        for (Hex hex : hexes) {
            if (hex==null) continue;
            
            territory.add(hex);
            hex.owner = this;
        }
    }
    
    
    
    public void spread (HexGrid grid, Hex source) {
        PointOfInterest settlement = new PointOfInterest (nextSettlementType());
        boolean is_military = settlement.isMilitary();
        
        List<Hex> in_range = source.getAllInRange(grid, SETTLE_SCAN_RANGE);
        List<Hex> candidates = new ArrayList<> ();
        
        for (int i=0;i<in_range.size();i++) {
            if (in_range.get(i)==null || Arrays.asList(IMPASSABLE_TERRAIN_TYPES).contains(in_range.get(i).terrain)) {
                in_range.remove(i);
                i--;
                continue;
            }
            
            List<Hex> check_in_range = in_range.get(i).getAllInRange(grid, MIN_RANGE_BETWEEN_SETTLEMENTS);
            boolean is_settleable = true;
            
            for (Hex hex : check_in_range) {
                if (hex!=null && hex.poi!=null && hex.poi.isSettlement()) {
                    is_settleable = false;
                    break;
                }
            }
            
            if (!is_settleable) {
                in_range.remove(i);
                i--;
            }
        }
        
        for (int candidate_count=0;candidate_count < SPREAD_CANDIDATE_COUNT;candidate_count++) {
            int max_land_value = -2;
            Hex max = null;
            
            for (int i=0;i<in_range.size();i++) {
                int land_value = getLandValue(grid, in_range.get(i), is_military);
                if (land_value > max_land_value) {
                    max = in_range.get(i);
                    max_land_value = land_value;
                }
            }
            
            if (max != null)
                candidates.add(max);
        }
        
        if (candidates.isEmpty())
            return;
        
        candidates.get(SlickUtils.randIndex(candidates.size())).poi = settlement;
        this.addTerritory(candidates.get(SlickUtils.randIndex(candidates.size())).getAllInRange(grid, 2));
    }
    
    public void upgrade (HexGrid grid, Hex source) {
        List<Hex> potential = new ArrayList<> (settlements);
        
        for (int i=0;i<potential.size();i++) {
            if (getSettlementLevel(potential.get(i)) < 0 || getSettlementLevel(potential.get(i)) >= 3) {
                potential.remove(i);
                i--;
            }
        }
        
        if (potential.isEmpty()) {
            spread(grid, source);
            return;
        }
        
        Hex target = potential.get(SlickUtils.randIndex(potential.size()));
        if (target.poi.isMilitary())
            target.poi = new PointOfInterest (Faction.MILITARY_POIS_BY_LEVEL[ getSettlementLevel(target)+1 ]);
        else
            target.poi = new PointOfInterest (Faction.CIVILIAN_POIS_BY_LEVEL[ getSettlementLevel(target)+1 ]);
    }
    
    
    
    public PointOfInterest nextSettlementType () {
        double mil_count=0.0;
        double civ_count=0.0;
        
        for (Hex hex : settlements) {
            if (hex.poi.isMilitary())
                mil_count+=1.0;
            else if (hex.poi.isCivilian())
                civ_count+=1.0;
        }
        
        // check whether the ratio dictates a next military or civilian settlement
        boolean military = false;
        if (civ_count>0)
            military = (mil_count / civ_count) < (type.composition_military / type.composition_civilian);
        
        PointOfInterest poi_to_add=null;
        if (military)
            poi_to_add = Faction.MILITARY_POIS_BY_LEVEL[0];
        else
            poi_to_add = Faction.CIVILIAN_POIS_BY_LEVEL[0];
        return poi_to_add;
    }
    
    public Hex randomSettlement () {
        if (settlements==null || settlements.isEmpty())
            return null;
        
        return settlements.get(SlickUtils.randIndex(settlements.size()));
    }
    
    public int getSettlementLevel (Hex hex) {
        PointOfInterest poi = hex.poi;
        if (poi==null) return -1;
        
        if (poi.isMilitary()) {
            for (int i=0 ; i<Faction.MILITARY_POIS_BY_LEVEL.length ; i++) {
                if (poi.is(Faction.MILITARY_POIS_BY_LEVEL[i]))
                    return i;
            }
        } else {
            for (int i=0 ; i<Faction.CIVILIAN_POIS_BY_LEVEL.length ; i++) {
                if (poi.is(Faction.CIVILIAN_POIS_BY_LEVEL[i]))
                    return i;
            }
        }
        
        return 0;
    }
    
    public List<Hex> getMilitarySettlements () {
        List<Hex> results = new ArrayList<> ();
        
        for (Hex settlement : settlements)
            if (settlement.poi.isMilitary())
                results.add(settlement);
        
        return results;
    }
    
    public List<Hex> getCivilianSettlements () {
        List<Hex> results = new ArrayList<> ();
        
        for (Hex settlement : settlements)
            if (settlement.poi.isCivilian())
                results.add(settlement);
        
        return results;
    }
    
    
    
    public static int getLandValue (HexGrid grid, Hex hex, boolean is_military) {
        if (hex==null || Arrays.asList(Faction.IMPASSABLE_TERRAIN_TYPES).contains(hex.terrain))
            return -1;
        
        int value = 0;
        List<Hex> neighbourhood = hex.getAllInRange(grid, LAND_VALUE_SCAN_RANGE);
        
        for (Hex neigh : neighbourhood) {
            if (neigh == null) continue;
            
            if (is_military) {
                value += neigh.terrain.military_land_value;
            } else {
                value += neigh.terrain.civilian_land_value;
            }
        }
        
        return value;
    }
    
}
