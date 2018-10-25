/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.world.map;

import game.data.world.NameGenerator;
import game.data.world.map.hex.DirEnum;
import game.data.world.map.hex.HexGrid;
import game.data.world.map.hex.Hex;
import game.states.PlayingState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.simon.utils.Log;
import org.simon.utils.SlickUtils;

/**
 *
 * @author XyRoN
 */
public abstract class WorldGenerator {
    
    public static final double MAP_LAND_PERCENTAGE = 0.4;
    
    public static int ASSIMILATION_COUNT = 2;
    public static int CULTURE_NO_SETTLE_ZONE_RANGE = 6;
    public static int CHAIN_PROPAGATION_RETRY_COUNT = 10;
    
    public static float CONTINENT_SIZE_TO_POI_COUNT = 0.01f;
    public static float POI_COUNT_OFFSET = 0.01f;
    
    public static float CONTINENT_CULTURES_PER_SIZE = 0.005f;
    
    public static int MAX_RETRY_COUNT = 100000;
    
    public static int ADJACENT_HEX_THRESHOLD_FOR_ASSIMILATION = 3;
    public static TerrainTypeEnum[] ASSIMILATION_NON_SPREAD_TYPES = { TerrainTypeEnum.MOUNTAINS, TerrainTypeEnum.HILLS, TerrainTypeEnum.FOREST, TerrainTypeEnum.TROPICAL };
    
    public static int MOUNTAIN_CHAIN_MIN_LEN = 3;
    public static int MOUNTAIN_CHAIN_MAX_LEN = 40;
    public static int FOREST_RADIAL_MIN_SIZE = 3;
    public static int FOREST_RADIAL_MAX_SIZE = 20;
    public static int WASTELAND_RADIAL_MIN_SIZE = 10;
    public static int WASTELAND_RADIAL_MAX_SIZE = 30;
    
    public static int MIN_HISTORY_ITERATIONS = 3000;
    public static int MAX_HISTORY_ITERATIONS = 4000;
    
    public static HexGrid GRID;
    
    
    
    public static void setGrid (HexGrid grid) {
        GRID = grid;
    }
    
    
    
    public static int getContinentSize (double factor) {
        return (int)((Math.round((GRID.getSizeX()*GRID.getSizeY())/100)) * factor);
    }
    
    public static boolean satisfiesLandPrecentage (List<Hex> land) {
        return (double)((double)land.size() / (double)(GRID.getSizeX()*GRID.getSizeY())) >= MAP_LAND_PERCENTAGE;
    }
    
    
    
    public static Hex getRandomLandHex () {
        Hex hex = null;
        do {
            int x = (int)(Math.random() * GRID.getSizeX());
            int y = (int)(Math.random() * GRID.getSizeY());
            hex = GRID.get(x,y);
        } while (hex==null || hex.terrain.equals(TerrainTypeEnum.SEA));
        
        return hex;
    }
    
    public static Hex getRandomSeaHex () {
        Hex hex = null;
        do {
            int x = (int)(Math.random() * GRID.getSizeX());
            int y = (int)(Math.random() * GRID.getSizeY());
            hex = GRID.get(x,y);
        } while (hex==null || hex.terrain.equals(TerrainTypeEnum.OPEN));
        
        return hex;
    }
    
    public static List<Hex> getCoastalHexes () {
        List<Hex> result = new ArrayList<> ();
        
        for (int y=0;y<GRID.getSizeY();y++) {
            for (int x=0;x<GRID.getSizeX();x++) {
                if (GRID.get(x,y).isCoastal(GRID)) result.add(GRID.get(x,y));
            }
        }
        
        return result;
    }
    
    public static List<Hex> getBorderHexes (TerrainTypeEnum type) {
        List<Hex> result = new ArrayList<> ();
        
        for (int y=0;y<GRID.getSizeY();y++) {
            for (int x=0;x<GRID.getSizeX();x++) {
                if (GRID.get(x,y).isBorder(GRID, type)) result.add(GRID.get(x,y));
            }
        }
        
        return result;
    }
    
    public static List<Hex> getBorderHexes (TerrainTypeEnum type, List<Hex> hexes) {
        if (type==null) return getBorderHexes(hexes);
        
        List<Hex> result = new ArrayList<> ();
        for (Hex hex : hexes) {
            if (hex.isBorder(GRID, type)) result.add(hex);
        }
        
        return result;
    }
    
    public static List<Hex> getBorderHexes (List<Hex> hexes) {
        List<Hex> result = new ArrayList<> ();
        
        for (Hex hex : hexes) {
            if (hex.isBorder(GRID)) result.add(hex);
        }
        
        return result;
    }
    
    
    
    public static final List<Hex> generateRadial (TerrainTypeEnum from_type, TerrainTypeEnum to_type, Hex starting_point, int min_size, int max_size) {
        int size = SlickUtils.rand(min_size, max_size);
        starting_point.terrain = to_type;
        List<Hex> radial = new ArrayList<> ();
        radial.add(starting_point);
        
//        Log.log("Generating radial of size "+size+", start point ("+starting_point.x+","+starting_point.y+") and type "+to_type.name());
        
        for (int i=0;radial.size()<size;i++) {
            List<Hex> radial_border;
            
            if (from_type!=null)
                radial_border = getBorderHexes (from_type, radial);
            else
                radial_border = getBorderHexes (radial);
            
            if (radial_border.isEmpty()) break;
            
            int index = SlickUtils.randIndex(radial_border.size());
            List<Hex> additions;
            if (from_type!=null)
                additions = radial_border.get(index).spreadTerrainToTypes(GRID, from_type);
            else
                additions = radial_border.get(index).spreadTerrain(GRID);
            
            radial.addAll(additions);
        }
        
        return radial;
    }
    
    public static final List<Hex> generateChain (TerrainTypeEnum from_type, TerrainTypeEnum to_type, Hex starting_point, int min_length, int max_length) {
        int length = SlickUtils.rand(min_length, max_length);
        starting_point.terrain = to_type;
        DirEnum direction = DirEnum.getRandom();
        
        List<Hex> chain = new ArrayList<> ();
        chain.add(starting_point);
        Hex head = starting_point;
        
//        Log.log("Generating chain of length "+length+", start point ("+starting_point.x+","+starting_point.y+") and type "+to_type.name());
        int retry_count = 0;
        
        for (int i=0;chain.size()<length;i++) {
            direction = DirEnum.getAdjacents(direction)[SlickUtils.randIndex(3)];
            
            Hex propagation_candidate = head.getAdjacent(GRID, direction);
            if (propagation_candidate==null) {
                if (retry_count<CHAIN_PROPAGATION_RETRY_COUNT) {
                    retry_count++;
                    Hex tmp = head;
                    head = starting_point;
                    starting_point = tmp;
                    direction = direction.opposite();
                    --i;
                    continue;
                } else {
                    break;
                }
            }
            
            if (propagation_candidate.terrain!=TerrainTypeEnum.SEA && // invalid selection, only land hexes can be propagated to
                    (from_type==null || propagation_candidate.terrain==from_type || propagation_candidate.terrain==to_type) // invalid selection, is not of from_type (if it is defined)
                    ) {
                propagation_candidate.terrain = to_type;
                chain.add(propagation_candidate);
                head = propagation_candidate;
            } else if (retry_count<CHAIN_PROPAGATION_RETRY_COUNT) { // try the other end, opposite direction
                retry_count++;
                Hex tmp = head;
                head = starting_point;
                starting_point = tmp;
                direction = direction.opposite();
                --i;
            } else {
                break;
            }
        }
        
        return chain;
    }
    
    
    
    public static final void generateMap (double continent_size_factor, int seed) {
        Log.log("Generating map...");
        String loadLabelBase = "... Loading ...\n";
        
        PlayingState.isLoading = true;
        PlayingState.loadLabel = loadLabelBase;
        long start = System.currentTimeMillis();
        long section_start = 0;
        
        SlickUtils.setSeed(seed);
        
        GRID.continents = new ArrayList<> ();
        GRID.land = new ArrayList<> ();
        
        // set the entire grid to sea, initialization
        Log.log("Initializing...");
        for (int y=0;y<GRID.getSizeY();y++) {
            for (int x=0;x<GRID.getSizeX();x++) {
                GRID.get(x, y).terrain = TerrainTypeEnum.SEA;
            }
        }
        
        Log.log("Generating landmasses");
        for (int i=0;!satisfiesLandPrecentage(GRID.land);i++) {
            Hex starting_point = GRID.get(SlickUtils.randIndex(GRID.getSizeX()), SlickUtils.randIndex(GRID.getSizeY()));
            
            boolean taken = false;
            for (Continent cont : GRID.continents) {
                if (cont.contains(starting_point)) {
                    taken = true;
                    break;
                }
            }
            
            if (taken) {
                --i;
                continue;
            }
            
            Log.log("Generating landmass #" + String.valueOf(i+1));
            section_start = System.currentTimeMillis();
            
            starting_point.terrain = TerrainTypeEnum.OPEN;
            int size = getContinentSize(continent_size_factor);
            Continent continent = new Continent (NameGenerator.continent(), starting_point,
                    generateRadial (TerrainTypeEnum.SEA, TerrainTypeEnum.OPEN, starting_point, (int)(size*0.1f), (int)(size*5f)));
            GRID.continents.add(continent);
            GRID.land.addAll(continent.getAll());
            
            continent.generate(GRID);
            Log.log("Landmass generated in "+(System.currentTimeMillis()-section_start)/1000f+" sec");
        }
        
        Log.log("Adding transitional hexes");
        section_start = System.currentTimeMillis();
        for (Hex hex : WorldGenerator.GRID.getAllNotOfType(TerrainTypeEnum.SEA)) {
            TerrainTypeEnum transition = null;
            
            switch (hex.terrain) {
                case FOREST :
                    transition = TerrainTypeEnum.GRASS;
                    break;
                case TROPICAL :
                    transition = TerrainTypeEnum.GRASS;
                    break;
                case MARSHES :
                    transition = TerrainTypeEnum.GRASS;
                    break;
                case WASTES :
                    transition = TerrainTypeEnum.SAVANNA;
                    break;
                case DESERT :
                    transition = TerrainTypeEnum.SAVANNA;
                    break;
                case ARID :
                    transition = TerrainTypeEnum.SAVANNA;
                    break;
                default :
                    break;
            }
            
            if (transition == null) continue;
            
            List<Hex> adjs = hex.getAllAdjacent(GRID);
            for (Hex adj : adjs) {
                if (!adj.terrain.equals(TerrainTypeEnum.SEA) && !adj.terrain.equals(TerrainTypeEnum.MOUNTAINS)
                        && !adj.terrain.equals(transition) && !adj.terrain.equals(hex.terrain)) {
                    adj.terrain = transition;
                }
            }
        }
        Log.log("Transitional hexes done in "+(System.currentTimeMillis()-section_start)/1000f+" sec");
        
        Log.log("Assimilating adjacent hexes");
        section_start = System.currentTimeMillis();
        List<TerrainTypeEnum> non_spread_types = Arrays.asList(ASSIMILATION_NON_SPREAD_TYPES);
        
        for (int i=0;i < ASSIMILATION_COUNT;i++) {
            List<Hex> assimilable_land = WorldGenerator.GRID.getAllNotOfTypes((TerrainTypeEnum[]) non_spread_types.toArray());
            assimilable_land = (List<Hex>) SlickUtils.shuffleList(assimilable_land);
            for (Hex hex : assimilable_land) {
                if (non_spread_types.contains(hex.terrain))
                    continue;
                List<Hex> adj_list = hex.getAllAdjacent(GRID);

                for (Hex adj : adj_list) {
                    if (non_spread_types.contains(adj.terrain))
                        continue;
                    
                    List<Hex> adj_of_type = hex.getAllAdjacentOfTypes(GRID, adj.terrain);
                    if (adj_of_type.size() >= ADJACENT_HEX_THRESHOLD_FOR_ASSIMILATION) {
                        hex.terrain = adj.terrain;
                        hex.continent = adj.continent;
                        break;
                    }
                }
            }
        }
        
        GRID.land = new ArrayList<> ();
        for (Continent continent : GRID.continents)
            GRID.land.addAll(continent.getAll());
        Log.log("Assimilation done in "+(System.currentTimeMillis()-section_start)/1000f+" sec");
        
        PlayingState.isLoading = false;
        Log.log("Map generated in "+(System.currentTimeMillis()-start)/1000f+" sec");
    }
}
