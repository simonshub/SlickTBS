/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.Consts;
import main.utils.SlickUtils;

/**
 *
 * @author XyRoN
 */
public abstract class WorldGenerator {
    
    public static double MOUNTAIN_GENERATION_FACTOR = .7;
    public static double FOREST_GENERATION_FACTOR = 2.8;
    
    public static HexGrid GRID;
    
    
    
    public static void setGrid (HexGrid grid) {
        GRID = grid;
    }
    
    
    
    public static int getNumberOfIslands () {
        return (int)((GRID.getSizeX()*GRID.getSizeY())/100);
    }
    
    public static int getNumberOfMountains (List<Hex> land) {
        int num = (int) Math.round((double)((double)land.size() / (double)(GRID.getSizeX()*GRID.getSizeY())) * ((GRID.getSizeX()*GRID.getSizeY())/100) * MOUNTAIN_GENERATION_FACTOR);
        return num;
    }
    
    public static int getNumberOfForests (List<Hex> land) {
        int num = (int) Math.round((double)((double)land.size() / (double)(GRID.getSizeX()*GRID.getSizeY())) * ((GRID.getSizeX()*GRID.getSizeY())/100) * FOREST_GENERATION_FACTOR);
        return num;
    }
    
    public static boolean satisfiesLandPrecentage (List<Hex> land) {
        return (double)((double)land.size() / (double)(GRID.getSizeX()*GRID.getSizeY())) >= Consts.MAP_LAND_PERCENTAGE;
    }
    
    public static Hex getRandomLandHex () {
        Hex hex = null;
        do {
            int x = (int)(Math.random() * GRID.getSizeX());
            int y = (int)(Math.random() * GRID.getSizeY());
            hex = GRID.get(x,y);
            
            if (hex==null) continue;
        } while (hex.terrain.equals(TerrainType.SEA));
        
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
    
    
    
    public static final void generateMap () {
        Random rand = new Random ();
        
        // set the entire grid to sea, initialization
        for (int y=0;y<GRID.getSizeY();y++) {
            for (int x=0;x<GRID.getSizeX();x++) {
                GRID.get(x, y).terrain = TerrainType.SEA;
            }
        }
        
        // make islands
        List<Hex> land = new ArrayList<> ();
        for (int i=0;i<getNumberOfIslands();i++) {
            int x = rand.nextInt(GRID.getSizeX());
            int y = rand.nextInt(GRID.getSizeY());
            
            if (GRID.get(x,y)!=null && GRID.get(x,y).terrain==TerrainType.SEA && !land.contains(GRID.get(x,y))) {
                GRID.get(x,y).terrain = TerrainType.OPEN;
                land.add(GRID.get(x,y));
            } else {
                i--;
            }
        }
        
        // extend random land until you have enough land
        List<Hex> coast;
        while (!satisfiesLandPrecentage(land)) {
            coast = getCoastalHexes();
            int index = rand.nextInt(coast.size());
            if (!coast.get(index).spreadTerrain(GRID, land)) ;
        }
        
        
        // make mountain ranges with hills
        int mountain_ranges = getNumberOfMountains(land);
        int mt_counter=0;
        while (mt_counter < mountain_ranges) {
            int length = (int)(Math.random()*9) + 8;
            List<Hex> mountains = new ArrayList<> ();
            
            Hex hex = GRID.getRandomHexOfType(TerrainType.OPEN);
            if (hex==null) continue;
            mountains.add(hex);
            
            // propagate mountains into a chain
            for (int i=0;i<length;i++) {
                Hex next = hex.getRandomAdjacentOfType(GRID, TerrainType.OPEN);
                if (next==null) { break; }
                hex = next;
                hex.terrain = TerrainType.MOUNTAINS;
                mountains.add(hex);
            }
            
            // propagate hills from random mountains, four times for each mountain
            for (int i=0;i<mountains.size()*4;i++) {
                Hex next = mountains.get(SlickUtils.rand(0,mountains.size()-1)).getRandomAdjacentOfType(GRID, TerrainType.OPEN);
                if (next==null) { continue; }
                next.terrain = TerrainType.HILLS;
            }
            
            mt_counter++;
        }
        
        
        // make forests
        int forests = getNumberOfForests(land);
        int fr_counter=0;
        while (fr_counter < forests) {
            int size = SlickUtils.rand(2, 20);
            List<Hex> forest = new ArrayList<> ();
            
            Hex hex = GRID.getRandomHexOfType(TerrainType.OPEN);
            hex.terrain = TerrainType.FOREST;
            forest.add(hex);
            for (int i=0;i<size;i++) {
                Hex next = forest.get(SlickUtils.rand(0,forest.size()-1)).getRandomAdjacentOfType(GRID, TerrainType.OPEN);
                if (next==null) continue;
                forest.add(next);
                hex = next;
                hex.terrain = TerrainType.FOREST;
            }
            
            fr_counter++;
        }
    }
}
