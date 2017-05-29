/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import game.data.map.DirEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.utils.SlickUtils;
import org.newdawn.slick.Color;

/**
 *
 * @author XyRoN
 */
public abstract class WorldGenerator {
    
    public static double MOUNTAIN_GENERATION_FACTOR = .7;
    public static double FOREST_GENERATION_FACTOR = 2.8;
    public static double WASTELAND_GENERATION_FACTOR = .5;
    
    public static final double MAP_LAND_PERCENTAGE = 0.4;
    public static final double MAP_CONTINENT_MOUNTAIN_PERCENTAGE = 0.025;
    public static final double MAP_CONTINENT_FOREST_PERCENTAGE = 0.180;
    public static final double MAP_CONTINENT_WASTELAND_PERCENTAGE = 0.025;
    
    public static int CHAIN_PROPAGATION_RETRY_COUNT = 4;
    
    public static double FOREST_SPARSE_CHANCE = 0.33;
    
    public static int MOUNTAIN_CHAIN_MIN_LEN = 4;
    public static int MOUNTAIN_CHAIN_MAX_LEN = 20;
    public static int FOREST_RADIAL_MIN_SIZE = 2;
    public static int FOREST_RADIAL_MAX_SIZE = 40;
    public static int WASTELAND_RADIAL_MIN_SIZE = 10;
    public static int WASTELAND_RADIAL_MAX_SIZE = 40;
    
    public static List<Continent> CONTINENTS;
    
    public static HexGrid GRID;
    
    
    
    public static void setGrid (HexGrid grid) {
        GRID = grid;
    }
    
    
    
    public static int getContinentSize () {
        return (int)(Math.round((GRID.getSizeX()*GRID.getSizeY())/30));
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
        List<Hex> result = new ArrayList<> ();
        
        if (type==null) return getBorderHexes(hexes);
        
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
    
    
    
    public static final List<Hex> generateRadial (boolean incl, TerrainTypeEnum to_type, TerrainTypeEnum from_type, Hex starting_point, int min_size, int max_size) {
        int size = SlickUtils.rand(min_size, max_size);
        List<Hex> radial = new ArrayList<> ();
        radial.add(starting_point);
        
//        System.out.println("Generating radial of size "+size+", start point ("+starting_point.x+","+starting_point.y+") and type "+to_type.name());
        
        for (int i=0;radial.size()<size;i++) {
            List<Hex> radial_border = getBorderHexes (from_type, radial);
            if (radial_border.isEmpty()) break;
            
            int index = SlickUtils.randIndex(radial_border.size());
            List<Hex> additions;
            if (incl)
                additions = radial_border.get(index).spreadTerrainIncl(GRID, from_type);
            else
                additions = radial_border.get(index).spreadTerrain(GRID);
            radial.addAll(additions);
        }
        
        return radial;
    }
    
    public static final List<Hex> generateChain (TerrainTypeEnum to_type, TerrainTypeEnum from_type, Hex starting_point, int min_length, int max_length) {
        int length = SlickUtils.rand(min_length, max_length);
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
                    --i;
                    System.out.println("Retry!");
                    continue;
                } else {
                    break;
                }
            }
            
            if (!propagation_candidate.isCoastal(GRID) && // invalid selection, chains cannot propagate to coastal tiles
                    propagation_candidate.terrain!=TerrainTypeEnum.SEA && // invalid selection, only land hexes can be propagated to
                    (from_type==null || propagation_candidate.terrain==from_type) // invalid selection, is not of from_type
                    ) {
                propagation_candidate.terrain = to_type;
                chain.add(propagation_candidate);
                head = propagation_candidate;
            } else if (retry_count<CHAIN_PROPAGATION_RETRY_COUNT) { // try the other end, opposite direction
                retry_count++;
                Hex tmp = head;
                head = starting_point;
                starting_point = tmp;
                --i;
//                System.out.println("Retry!");
            } else {
                break;
            }
        }
        
        return chain;
    }
    
    
    
    public static final void generateMap () {
        System.out.println("Generating map...");
        CONTINENTS = new ArrayList<> ();
        
        // set the entire grid to sea, initialization
        for (int y=0;y<GRID.getSizeY();y++) {
            for (int x=0;x<GRID.getSizeX();x++) {
                GRID.get(x, y).terrain = TerrainTypeEnum.SEA;
            }
        }
        
        List<Hex> land = new ArrayList<> ();
        
        for (int i=0;!satisfiesLandPrecentage(land);i++) {
            Hex starting_point = GRID.get(SlickUtils.randIndex(GRID.getSizeX()), SlickUtils.randIndex(GRID.getSizeY()));
            
            boolean taken = false;
            for (Continent cont : CONTINENTS) {
                if (cont.contains(starting_point)) {
                    taken = true;
                    break;
                }
            }
            if (taken) {
                --i;
                continue;
            }
            
            starting_point.terrain = TerrainTypeEnum.OPEN;
            land.add(starting_point);
            Continent continent = new Continent (starting_point.x+";"+starting_point.y, starting_point, generateRadial (false, TerrainTypeEnum.OPEN, TerrainTypeEnum.SEA, starting_point, (int)(getContinentSize()*0.25f), (int)(getContinentSize()*5f)));
            System.out.println("Continent "+continent.name+" created, size: "+continent.size());
            
            for (Hex cont_hex : continent.getAll()) {
                land.add(cont_hex);
                cont_hex.continent = continent;
            }
            CONTINENTS.add(continent);
            
            continent.generate(GRID);
        }
    }
}
