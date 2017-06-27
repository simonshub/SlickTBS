/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import game.data.game.RaceEnum;
import game.data.hex.HexGroup;
import game.data.hex.HexGrid;
import game.data.hex.Hex;
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
import main.utils.Log;
import main.utils.SlickUtils;
import org.newdawn.slick.Color;

/**
 *
 * @author emil.simon
 */
public class Continent {
    
    private Hex source;
    private HexGroup hexes;
    
    public String name = "";
    
    public ContinentTypeEnum continent_type;
    
    public List<HexGroup> mountain_list;
    public List<HexGroup> forest_list;
    public List<HexGroup> wastes_list;
    
    private static final double MAX_TARGET_TOTAL_COVERAGE = 1.25;
    private static final double MIN_TARGET_TOTAL_COVERAGE = 0.75;
    
    private double target_total_coverage = 0.;
    private double target_mountain_percentage = 0.;
    private double target_forest_percentage = 0.;
    private double target_wastes_percentage = 0.;
    
    private double heat = 0.;
    private double wetness = 0.;
    private double flatness = 0.;
    private double corruption = 0.;
    
    private Color color;
    
    
    
    public Continent (String name, Hex source) {
        this();
        this.name = name;
        this.source = source;
        hexes = new HexGroup ();
        calculateTargetPercentages();
    }
    
    public Continent (String name, Hex source, List<Hex> hexes) {
        this();
        this.name = name;
        this.setHexes(source, hexes);
        calculateTargetPercentages();
    }
    
    private Continent () {
        continent_type = ContinentTypeEnum.getRandom();
        mountain_list = new ArrayList<> ();
        forest_list = new ArrayList<> ();
        wastes_list = new ArrayList<> ();
    }
    
    
    
    public List<Hex> getAll () {
        return hexes.toList();
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
    
    public HexGroup forests () {
        HexGroup frs = new HexGroup ();
        for (HexGroup grp : forest_list)
            frs.add(grp);
        
        return frs;
    }
    
    public HexGroup wastes () {
        HexGroup wls = new HexGroup ();
        for (HexGroup grp : wastes_list)
            wls.add(grp);
        
        return wls;
    }
    
    
    
    public final void setHexes (Hex source, List<Hex> hex_list) {
        hexes = new HexGroup ();
        if (hex_list!=null)
            hexes.add(hex_list);
        
        if (hex_list!=null) {
            for (Hex hex : hex_list) {
                hex.continent = this;
            }
        }
        
        this.source = source;
        source.continent = this;
    }
    
    public Color getColor () {
        return color;
    }
    
    
    
    public boolean contains (Hex hex) {
        return hexes.contains(hex);
    }
    
    public int size () {
        return hexes.size();
    }
    
    
    
    private void calculateTargetPercentages () {
        calculateFeaturePercentages();
        
//        double mt_factor = Math.round(SlickUtils.rand(0, 10 * (heat*10)));
//        double fr_factor = Math.round(SlickUtils.rand(0, 10 * (wetness*10)));
//        double wl_factor = Math.round(SlickUtils.rand(0, 10 * (heat*10)));
        
        target_total_coverage = this.continent_type.coverage * SlickUtils.rand(MIN_TARGET_TOTAL_COVERAGE, MAX_TARGET_TOTAL_COVERAGE);
        
        double mt_factor = this.continent_type.mt_factor;
        double fr_factor = this.continent_type.fr_factor;
        double ws_factor = this.continent_type.ws_factor;
        
        target_mountain_percentage = (mt_factor / (mt_factor + fr_factor + ws_factor)) * target_total_coverage;
        target_forest_percentage = (fr_factor / (mt_factor + fr_factor + ws_factor)) * target_total_coverage;
        target_wastes_percentage = (ws_factor / (mt_factor + fr_factor + ws_factor)) * target_total_coverage;
        
        Log.log("Continent "+name, "Source: "+source.x+","+source.y, "Size: "+hexes.size(), "Target coverage: "+target_total_coverage, 
                "Mt:"+target_mountain_percentage+", Fr:"+target_forest_percentage+", Wl:"+target_wastes_percentage,
                "heat:"+heat+", wetness:"+wetness+", flatness:"+flatness+", corruption:"+corruption);
    }
    
    private void calculateFeaturePercentages () {
        if (source==null || WorldGenerator.GRID==null) throw new NullPointerException ();
        
        heat = 1.0 - Math.abs((source.y / WorldGenerator.GRID.getSizeY()) - 0.5);
        
        wetness = Math.random();
        flatness = Math.random();
        corruption = Math.random();
        
        color = new Color ((float)corruption, (float)heat, (float)wetness, 0.2f);
    }
    
    
    
    public boolean satisfiesMountainPerc () {
        double mt_count = 0;
        for (HexGroup mt : mountain_list)
            mt_count += mt.size();
        
        mt_count*=2.; // increase mt_count to get less mountains
        return (mt_count/hexes.size())>=target_mountain_percentage;
    }
    
    public boolean satisfiesWastesPerc () {
        double wl_count = 0;
        for (HexGroup wl : wastes_list)
            wl_count += wl.size();
        
        wl_count*=1.; // increase wl_count to get less wastes
        return (wl_count/hexes.size())>=target_wastes_percentage;
    }
    
    public boolean satisfiesForestPerc () {
        double fr_count = 0;
        for (HexGroup fr : forest_list)
            fr_count += fr.size();
        
        fr_count*=1.; // increase fr_count to get less forests
        return (fr_count/hexes.size())>=target_forest_percentage;
    }
    
    
    
    public void generate (HexGrid grid) {
        int retry_max_count = WorldGenerator.MAX_RETRY_COUNT, retry;
        
        // generate mountain ranges
        retry = 0;
        for (int mt=0;!satisfiesMountainPerc() && (retry < retry_max_count);mt++) {
            Hex mt_starting_point = hexes.get(SlickUtils.randIndex(hexes.size()));
            if (mt_starting_point.terrain!=TerrainTypeEnum.OPEN) {
                --mt; // try again !
                retry++;
                continue;
            }
            
            List<Hex> chain = generateChain (TerrainTypeEnum.OPEN, TerrainTypeEnum.MOUNTAINS, mt_starting_point, MOUNTAIN_CHAIN_MIN_LEN, MOUNTAIN_CHAIN_MAX_LEN);
            HexGroup mountain_range = new HexGroup (chain);
            mountain_list.add(mountain_range);
//            System.out.println("Mountain range "+(mt+1)+" : size("+mountain_range.size()+"), mountains("+mountains().size()+" / "+(hexes.size()*target_mountain_percentage)+" / "+hexes.size()+")");
        }
        // propagate some hills
        Hex[] mt = new Hex [mountains().size()];
        mountains().toList().toArray(mt);
        SlickUtils.shuffleArray(mt);
        for (Hex hex : mt) {
            hex.propagate(grid, TerrainTypeEnum.OPEN, TerrainTypeEnum.HILLS);
        }
        
        // generate wastelands
        retry = 0;
        for (int ws=0;!satisfiesWastesPerc() && (retry < retry_max_count);ws++) {
            Hex ws_starting_point = hexes.get(SlickUtils.randIndex(hexes.size()));
            if (ws_starting_point.terrain!=TerrainTypeEnum.OPEN) {
                --ws; // try again !
                retry++;
                continue;
            }

            List<Hex> radial;
            radial = generateRadial (TerrainTypeEnum.OPEN, continent_type.wasteland, ws_starting_point, WASTELAND_RADIAL_MIN_SIZE, WASTELAND_RADIAL_MAX_SIZE);
            HexGroup waste = new HexGroup (radial);
            wastes_list.add(waste);
//            System.out.println("Wasteland "+(ws+1)+" : size("+waste.size()+"), wastes("+wastes().size()+"/"+(hexes.size()*target_wastes_percentage)+" / "+hexes.size()+")");
        }
        
        // generate forests
        retry = 0;
        for (int fr=0;!satisfiesForestPerc() && (retry < retry_max_count);fr++) {
            Hex fr_starting_point = hexes.get(SlickUtils.randIndex(hexes.size()));
            if (fr_starting_point.terrain!=TerrainTypeEnum.OPEN) {
                --fr; // try again !
                retry++;
                continue;
            }

            List<Hex> radial;
            radial = generateRadial (TerrainTypeEnum.OPEN, continent_type.forest, fr_starting_point, FOREST_RADIAL_MIN_SIZE, FOREST_RADIAL_MAX_SIZE);
            HexGroup forest = new HexGroup (radial);
            forest_list.add(forest);
//            System.out.println("Forest "+(fr+1)+" : size("+forest.size()+"), forests("+forests().size()+"/"+(hexes.size()*target_forest_percentage)+" / "+hexes.size()+")");
        }
        
        // convert remaining open terrain to continent type terrain, if it's different to the default OPEN terrain
        if (continent_type.open != TerrainTypeEnum.OPEN) {
            for (Hex hex : hexes.getAllOfTypes(TerrainTypeEnum.OPEN)) {
                hex.terrain = continent_type.open;
            }
        }
    }
}
