/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import static game.data.map.WorldGenerator.FOREST_RADIAL_MAX_SIZE;
import static game.data.map.WorldGenerator.FOREST_RADIAL_MIN_SIZE;
import static game.data.map.WorldGenerator.MOUNTAIN_CHAIN_MAX_LEN;
import static game.data.map.WorldGenerator.MOUNTAIN_CHAIN_MIN_LEN;
import static game.data.map.WorldGenerator.WASTELAND_RADIAL_MAX_SIZE;
import static game.data.map.WorldGenerator.WASTELAND_RADIAL_MIN_SIZE;
import static game.data.map.WorldGenerator.generateChain;
import static game.data.map.WorldGenerator.generateRadial;
import java.util.ArrayList;
import java.util.List;
import main.utils.SlickUtils;
import org.newdawn.slick.Color;

/**
 *
 * @author emil.simon
 */
public class Continent {
    
    private HexGroup hexes;
    
    public List<HexGroup> mountain_list;
    public List<HexGroup> forest_list;
    public List<HexGroup> wastes_list;
    
    private static final double MAX_TARGET_TOTAL_COVERAGE = .95;
    private static final double MIN_TARGET_TOTAL_COVERAGE = .5;
    
    private static final double MAX_TARGET_MOUNTAIN_PERCENTAGE = 10;
    private static final double MAX_TARGET_FOREST_PERCENTAGE = 30;
    private static final double MAX_TARGET_WASTES_PERCENTAGE = 20;
    
    private static final double MIN_TARGET_MOUNTAIN_PERCENTAGE = 1;
    private static final double MIN_TARGET_FOREST_PERCENTAGE = 1;
    private static final double MIN_TARGET_WASTES_PERCENTAGE = 1;
    
    String name = "";
    
    private double target_total_coverage = 0.;
    private double target_mountain_percentage = 0.;
    private double target_forest_percentage = 0.;
    private double target_wastes_percentage = 0.;
    
    private Color color;
    private Color border_color;
    
    
    
    public Continent (String name) {
        this();
        this.name = name;
        hexes = new HexGroup ();
        color = new Color ((float)Math.random(), (float)Math.random(), (float)Math.random(), 0.25f);
        border_color = new Color (color.r, color.g, color.b, 0.1f);
        calculateTargetPrecentages();
    }
    
    public Continent (String name, List<Hex> hexes) {
        this();
        this.name = name;
        this.hexes = new HexGroup (hexes);
        color = new Color ((float)Math.random(), (float)Math.random(), (float)Math.random(), 0.25f);
        border_color = new Color (color.r, color.g, color.b, 0.1f);
        calculateTargetPrecentages();
    }
    
    private Continent () {
        mountain_list = new ArrayList<> ();
        forest_list = new ArrayList<> ();
        wastes_list = new ArrayList<> ();
    }
    
    
    
    public List<Hex> getAll () {
        return hexes.getAll();
    }
    
    public HexGroup getAllAsGroup () {
        return hexes;
    }
    
    public HexGroup mountains () {
        HexGroup mts = new HexGroup ();
        for (HexGroup grp : mountain_list)
            mts.add(grp);
        
        return mts;
    }
    
    
    
    public void setHexes (List<Hex> hex_list) {
        hexes.clear();
        hexes.add(hex_list);
    }
    
    
    
    public boolean contains (Hex hex) {
        return hexes.contains(hex);
    }
    
    public int size () {
        return hexes.size();
    }
    
    
    
    private void calculateTargetPrecentages () {
        target_total_coverage = SlickUtils.rand(MIN_TARGET_TOTAL_COVERAGE, MAX_TARGET_TOTAL_COVERAGE);
        
        int mt_factor = SlickUtils.rand(0, 10);
        int fr_factor = SlickUtils.rand(0, 10);
        int wl_factor = SlickUtils.rand(0, 10);
        
        target_mountain_percentage = (mt_factor / (mt_factor + fr_factor + wl_factor)) * target_total_coverage;
        target_forest_percentage = (fr_factor / (mt_factor + fr_factor + wl_factor)) * target_total_coverage;
        target_wastes_percentage = (wl_factor / (mt_factor + fr_factor + wl_factor)) * target_total_coverage;
        
        System.out.println("Continent "+name+" (Mt:"+target_mountain_percentage+", Fr:"+target_forest_percentage+", Wl:"+target_wastes_percentage+" | Target coverage: "+target_total_coverage+")");
    }
    
    
    
    public boolean satisfiesMountainPerc () {
        int mt_count = 0;
        for (HexGroup mt : mountain_list)
            mt_count += mt.size();
        
        return (mt_count/hexes.size())>=target_mountain_percentage;
    }
    
    public boolean satisfiesForestPerc () {
        int fr_count = 0;
        for (HexGroup fr : forest_list)
            fr_count += fr.size();
        
        return (fr_count/hexes.size())>=target_forest_percentage;
    }
    
    public boolean satisfiesWastesPerc () {
        int wl_count = 0;
        for (HexGroup wl : wastes_list)
            wl_count += wl.size();
        
        return (wl_count/hexes.size())>=target_wastes_percentage;
    }
    
    
    
    public void generate (HexGrid grid) {
        // generate mountain ranges
        for (int mt=0;!satisfiesMountainPerc();mt++) {
            Hex mt_starting_point = hexes.get(SlickUtils.randIndex(hexes.size()));
            if (mt_starting_point.terrain!=TerrainTypeEnum.OPEN) {
                --mt; // try again !
                continue;
            }
            
            mt_starting_point.terrain = TerrainTypeEnum.MOUNTAINS;
            
            List<Hex> chain = generateChain (TerrainTypeEnum.MOUNTAINS, TerrainTypeEnum.OPEN, mt_starting_point, MOUNTAIN_CHAIN_MIN_LEN, MOUNTAIN_CHAIN_MAX_LEN, 0.75, 0.75, 0);
            HexGroup mountain_range = new HexGroup (chain);
            mountain_list.add(mountain_range);
            System.out.println("Mountain range "+(mt+1)+" created, size: "+mountain_range.size());
        }
        // propagate some hills
        Hex[] mt = new Hex [mountains().size()];
        mountains().getAll().toArray(mt);
        SlickUtils.shuffleArray(mt);
        for (Hex hex : mt) {
            hex.propagate(grid, TerrainTypeEnum.OPEN, TerrainTypeEnum.HILLS);
        }
        
        // generate wastelands
        for (int ws=0;!satisfiesWastesPerc();ws++) {
            Hex ws_starting_point = hexes.get(SlickUtils.randIndex(hexes.size()));
            if (ws_starting_point.terrain!=TerrainTypeEnum.OPEN) {
                --ws; // try again !
                continue;
            }

            ws_starting_point.terrain = TerrainTypeEnum.WASTES;

            List<Hex> radial;
            radial = generateRadial (true, TerrainTypeEnum.WASTES, TerrainTypeEnum.OPEN, ws_starting_point, WASTELAND_RADIAL_MIN_SIZE, WASTELAND_RADIAL_MAX_SIZE);
            HexGroup waste = new HexGroup (radial);
            wastes_list.add(waste);
            System.out.println("Wasteland "+(ws+1)+" created, size: "+waste.size());
        }
        
        // generate forests
        for (int fr=0;!satisfiesForestPerc();fr++) {
            Hex fr_starting_point = hexes.get(SlickUtils.randIndex(hexes.size()));
            if (fr_starting_point.terrain!=TerrainTypeEnum.OPEN) {
                --fr; // try again !
                continue;
            }

            fr_starting_point.terrain = TerrainTypeEnum.FOREST;

            List<Hex> radial;
            radial = generateRadial (true, TerrainTypeEnum.FOREST, TerrainTypeEnum.OPEN, fr_starting_point, FOREST_RADIAL_MIN_SIZE, FOREST_RADIAL_MAX_SIZE);
            HexGroup forest = new HexGroup (radial);
            forest_list.add(forest);
            System.out.println("Forest "+(fr+1)+" created, size: "+forest.size());
        }
    }
}
