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
    
    private Hex source;
    private HexGroup hexes;
    
    public List<HexGroup> mountain_list;
    public List<HexGroup> forest_list;
    public List<HexGroup> wastes_list;
    
    private static final double MAX_TARGET_TOTAL_COVERAGE = .95;
    private static final double MIN_TARGET_TOTAL_COVERAGE = .5;
    
    String name = "";
    
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
        
        target_total_coverage = SlickUtils.rand(MIN_TARGET_TOTAL_COVERAGE, MAX_TARGET_TOTAL_COVERAGE);
        
        double mt_factor = Math.round(SlickUtils.rand(0, 10 * (heat*10)));
        double fr_factor = Math.round(SlickUtils.rand(0, 10 * (wetness*10)));
        double wl_factor = Math.round(SlickUtils.rand(0, 10 * (heat*10)));
        
        target_mountain_percentage = (mt_factor / (mt_factor + fr_factor + wl_factor)) * target_total_coverage;
        target_forest_percentage = (fr_factor / (mt_factor + fr_factor + wl_factor)) * target_total_coverage;
        target_wastes_percentage = (wl_factor / (mt_factor + fr_factor + wl_factor)) * target_total_coverage;
        
        System.out.println("Continent "+name+
                "\n | Source: "+source.x+","+source.y+
                "\n | Size: "+hexes.size()+
                "\n | Target coverage: "+target_total_coverage+
                "\n | Mt:"+target_mountain_percentage+", Fr:"+target_forest_percentage+", Wl:"+target_wastes_percentage+
                "\n | heat:"+heat+", wetness:"+wetness+", flatness:"+flatness+", corruption:"+corruption);
    }
    
    private void calculateFeaturePercentages () {
        if (source==null || WorldGenerator.GRID==null) throw new NullPointerException ();
        
        heat = 1.0 - Math.abs((source.y / WorldGenerator.GRID.getSizeY()) - 0.5);
        
        wetness = Math.random();
        flatness = Math.random();
        corruption = Math.random();
        
        color = new Color ((float)heat, (float)wetness, (float)corruption, 0.25f);
    }
    
    
    
    public boolean satisfiesMountainPerc () {
        double mt_count = 0;
        for (HexGroup mt : mountain_list)
            mt_count += mt.size();
        
//        System.out.println("Satisfies mountain perc: "+
//                (mt_count/hexes.size())+">="+target_mountain_percentage+" ("
//                +((mt_count/hexes.size())>=target_mountain_percentage)+")");
        return (mt_count/hexes.size())>=target_mountain_percentage;
    }
    
    public boolean satisfiesWastesPerc () {
        double wl_count = 0;
        for (HexGroup wl : wastes_list)
            wl_count += wl.size();
        
//        System.out.println("Satisfies wastes perc: "+
//                (wl_count/hexes.size())+">="+target_wastes_percentage+" ("
//                +((wl_count/hexes.size())>=target_wastes_percentage)+")");
        return (wl_count/hexes.size())>=target_wastes_percentage;
    }
    
    public boolean satisfiesForestPerc () {
        double fr_count = 0;
        for (HexGroup fr : forest_list)
            fr_count += fr.size();
        
//        System.out.println("Satisfies forest perc: "+
//                (fr_count/hexes.size())+">="+target_forest_percentage+" ("
//                +((fr_count/hexes.size())>=target_forest_percentage)+")");
        return (fr_count/hexes.size())>=target_forest_percentage;
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
            
            List<Hex> chain = generateChain (TerrainTypeEnum.MOUNTAINS, TerrainTypeEnum.OPEN, mt_starting_point, MOUNTAIN_CHAIN_MIN_LEN, MOUNTAIN_CHAIN_MAX_LEN);
            HexGroup mountain_range = new HexGroup (chain);
            mountain_list.add(mountain_range);
            System.out.println("Mountain range "+(mt+1)+" : size("+mountain_range.size()+"), mountains("+mountains().size()+" / "+(hexes.size()*target_mountain_percentage)+" / "+hexes.size()+")");
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
            radial = generateRadial (TerrainTypeEnum.WASTES, TerrainTypeEnum.OPEN, ws_starting_point, WASTELAND_RADIAL_MIN_SIZE, WASTELAND_RADIAL_MAX_SIZE);
            HexGroup waste = new HexGroup (radial);
            wastes_list.add(waste);
            System.out.println("Wasteland "+(ws+1)+" : size("+waste.size()+"), wastes("+wastes().size()+"/"+(hexes.size()*target_wastes_percentage)+" / "+hexes.size()+")");
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
            radial = generateRadial (TerrainTypeEnum.FOREST, TerrainTypeEnum.OPEN, fr_starting_point, FOREST_RADIAL_MIN_SIZE, FOREST_RADIAL_MAX_SIZE);
            HexGroup forest = new HexGroup (radial);
            forest_list.add(forest);
            System.out.println("Forest "+(fr+1)+" : size("+forest.size()+"), forests("+forests().size()+"/"+(hexes.size()*target_forest_percentage)+" / "+hexes.size()+")");
        }
    }
}
