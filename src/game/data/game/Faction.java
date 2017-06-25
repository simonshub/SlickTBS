/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

import game.data.hex.DirEnum;
import game.data.hex.Hex;
import game.data.hex.HexGrid;
import game.data.map.TerrainTypeEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import main.utils.Log;
import main.utils.SlickUtils;
import org.newdawn.slick.Color;

/**
 *
 * @author XyRoN
 */
public final class Faction {
    
    public static final int MAX_STARTING_POSITION_MOVES = 30;
    public static final int MIN_STARTING_POSITION_MOVES = 10;
    
    public static final TerrainTypeEnum[] ALLOWED_TERRAIN_TYPES = {
                TerrainTypeEnum.GRASS, TerrainTypeEnum.ARID, TerrainTypeEnum.DESERT,
                TerrainTypeEnum.FOREST, TerrainTypeEnum.SAVANNA, TerrainTypeEnum.OPEN,
                TerrainTypeEnum.HILLS, TerrainTypeEnum.MARSHES, TerrainTypeEnum.TUNDRA
            };
    
    public static final TerrainTypeEnum[] IMPASSABLE_TERRAIN_TYPES = {
                TerrainTypeEnum.SEA, TerrainTypeEnum.MOUNTAINS
            };
    
    
    
    public int income;
    
    public Hex capital;
    public Color color;
    public String name;
    public Culture culture;
    public List<Hex> territory;
    
    
    
    public Faction (HexGrid grid, Culture culture) {
        int moves = SlickUtils.rand(MIN_STARTING_POSITION_MOVES, MAX_STARTING_POSITION_MOVES);
        Hex loc = culture.source_hex;
        DirEnum direction = DirEnum.getRandom();
        int dir_mod = 0;
        
        for (int i=0, tries=0;i<moves && tries<20;tries++) {
            Hex adj = loc.getAdjacent(grid, direction);
            
            if (adj==null || Arrays.asList(IMPASSABLE_TERRAIN_TYPES).contains(adj.terrain))
                continue;
            
            loc = adj;
            i += loc.terrain.move_cost;
            dir_mod = SlickUtils.rand(-1, 1);
            
            if (dir_mod == -1)
                direction = DirEnum.getAdjacentClockwise(direction, 1);
            else if (dir_mod == 1)
                direction = DirEnum.getAdjacentCounterClockwise(direction, 1);
        }
        
        this.capital = loc;
        this.capital.poi = PointOfInterest.CAPITOL;
        this.culture = culture;
        this.addTerritory(this.capital.getAllInRange(grid, 1));
        
        this.color = new Color ((float) Math.random(), (float) Math.random(), (float) Math.random(), 0.3f);
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
