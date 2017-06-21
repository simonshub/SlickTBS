/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import game.data.game.PointOfInterest;
import main.utils.SlickUtils;
import game.data.hex.DirEnum;
import game.data.hex.HexGrid;
import game.data.hex.Hex;
import game.states.PlayingState;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author XyRoN
 */
public abstract class WorldGenerator {
    
    public static final double MAP_LAND_PERCENTAGE = 0.3;
    
    public static int CHAIN_PROPAGATION_RETRY_COUNT = 5;
    
    public static int MOUNTAIN_CHAIN_MIN_LEN = 3;
    public static int MOUNTAIN_CHAIN_MAX_LEN = 40;
    public static int FOREST_RADIAL_MIN_SIZE = 3;
    public static int FOREST_RADIAL_MAX_SIZE = 40;
    public static int WASTELAND_RADIAL_MIN_SIZE = 20;
    public static int WASTELAND_RADIAL_MAX_SIZE = 40;
    
    public static HexGrid GRID;
    
    
    
    public static void setGrid (HexGrid grid) {
        GRID = grid;
    }
    
    
    
    public static int getContinentSize () {
        return (int)(Math.round((GRID.getSizeX()*GRID.getSizeY())/50));
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
        
//        System.out.println("Generating radial of size "+size+", start point ("+starting_point.x+","+starting_point.y+") and type "+to_type.name());
        
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
        
//        System.out.println("Generating chain of length "+length+", start point ("+starting_point.x+","+starting_point.y+") and type "+to_type.name());
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
    
    
    
    public static final void generateMap () {
        System.out.println("Generating map...");
        String loadLabelBase = "... Loading ...\n";
        
        PlayingState.isLoading = true;
        PlayingState.loadLabel = loadLabelBase;
        long start = System.currentTimeMillis();
        
        GRID.continents = new ArrayList<> ();
        GRID.land = new ArrayList<> ();
        
        // set the entire grid to sea, initialization
        PlayingState.loadLabel = loadLabelBase + "Initializing";
            System.out.println(PlayingState.loadLabel);
        for (int y=0;y<GRID.getSizeY();y++) {
            for (int x=0;x<GRID.getSizeX();x++) {
                GRID.get(x, y).terrain = TerrainTypeEnum.SEA;
            }
        }
        
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
            
            PlayingState.loadLabel = loadLabelBase + "Generating landmass #" + String.valueOf(i+1);
            System.out.println(PlayingState.loadLabel);
            
            starting_point.terrain = TerrainTypeEnum.OPEN;
            Continent continent = new Continent (String.valueOf(i+1), starting_point, generateRadial (TerrainTypeEnum.SEA, TerrainTypeEnum.OPEN, starting_point, (int)(getContinentSize()*0.1f), (int)(getContinentSize()*5f)));
            GRID.continents.add(continent);
            GRID.land.addAll(continent.getAll());
            
            continent.generate(GRID);
        }
        
        PlayingState.loadLabel = loadLabelBase + "Cleaning singular hexes";
        System.out.println(PlayingState.loadLabel);
        for (Hex hex : WorldGenerator.GRID.getAllNotOfType(TerrainTypeEnum.SEA)) {
            if (hex.getAllAdjacentOfTypes(GRID, hex.terrain).isEmpty()) {
                Hex adj = hex.getRandomAdjacent(GRID);
                if (adj!=null) hex.terrain = adj.terrain;
            }
        }
        
        PlayingState.loadLabel = loadLabelBase + "Adding transitional hexes";
        System.out.println(PlayingState.loadLabel);
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

        PlayingState.loadLabel = loadLabelBase + "Adding points of interest";
        System.out.println(PlayingState.loadLabel);
        
        int poi_count = 100;
        List<Hex> potential_pois = GRID.getAllNotOfType(TerrainTypeEnum.SEA);
        
        for (int i=0;i<poi_count;i++) {
            Hex hex = potential_pois.get(SlickUtils.randIndex(potential_pois.size()));
            hex.poi = PointOfInterest.getRandom(hex.terrain);
            
            if (hex.poi!=null)
                System.out.println("POI : "+hex.poi.name+" @ ("+hex.x+","+hex.y+")");
        }
        
        PlayingState.isLoading = false;
        System.out.printf("Map generated in %.2f sec", (System.currentTimeMillis()-start)/1000f);
    }
}
