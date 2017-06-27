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
import java.util.Collection;
import java.util.List;
import main.utils.SlickUtils;
import org.newdawn.slick.Color;

/**
 *
 * @author XyRoN
 */
public final class Faction {
    
    public static final int MAX_SETTLE_RETRIES = 200;
    public static final int MIN_RANGE_BETWEEN_SETTLEMENTS = 4;
    
    public static final TerrainTypeEnum[] ALLOWED_TERRAIN_TYPES = {
                TerrainTypeEnum.GRASS, TerrainTypeEnum.ARID, TerrainTypeEnum.DESERT,
                TerrainTypeEnum.FOREST, TerrainTypeEnum.SAVANNA, TerrainTypeEnum.OPEN,
                TerrainTypeEnum.HILLS, TerrainTypeEnum.MARSHES, TerrainTypeEnum.TUNDRA
            };
    
    public static final TerrainTypeEnum[] IMPASSABLE_TERRAIN_TYPES = {
                TerrainTypeEnum.SEA, TerrainTypeEnum.MOUNTAINS
            };
    
    
    
    public String name;
    
    public RaceEnum race;
    public Hex capital;
    public Color color;
    public List<Hex> territory;
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
        capital.poi = new PointOfInterest(type.capital_type);
        capital.poi.name = name_place;
        this.addTerritory(this.capital.getAllInRange(grid, 2));
        
        this.color = new Color ((float) Math.random(), (float) Math.random(), (float) Math.random(), 0.5f);
        
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
    
}
