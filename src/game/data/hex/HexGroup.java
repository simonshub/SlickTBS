/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.hex;

import game.data.map.TerrainTypeEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author emil.simon
 */
public final class HexGroup {
    
    private List<Hex> hex_list;
    
    
    
    public HexGroup () {
        hex_list = new ArrayList<> ();
    }
    
    public HexGroup (Hex... hexes) {
        hex_list = new ArrayList<> ();
        hex_list.addAll(Arrays.asList(hexes));
    }
    
    public HexGroup (List<Hex> hexes) {
        hex_list = new ArrayList<> ();
        hex_list.addAll(hexes);
    }
    
    
    
    public boolean contains (Hex hex) {
        return hex_list.contains(hex);
    }
    
    public int size () {
        return hex_list.size();
    }
    
    
    
    public Hex get (int index) {
        return hex_list.get(index);
    }
    
    public List<Hex> toList() {
        return hex_list;
    }
    
    public List<Hex> getAllOfTypes(TerrainTypeEnum... types) {
        List<Hex> results = new ArrayList<> ();
        List<TerrainTypeEnum> type_list = Arrays.asList(types);
        
        for (Hex hex : hex_list) {
            if (type_list.contains(hex.terrain))
                results.add(hex);
        }
        
        return results;
    }
    
    public HexGroup removeAllOfTypes(TerrainTypeEnum... types) {
        List<TerrainTypeEnum> type_list = Arrays.asList(types);
        
        for (int i=0;i<hex_list.size();i++) {
            if (type_list.contains(hex_list.get(i).terrain)) {
                hex_list.remove(i);
                --i;
            }
        }
        
        return this;
    }
    
    
    
    public void add (List<Hex> hexes) {
        hex_list.addAll(hexes);
    }
    
    public void add (Hex... hexes) {
        hex_list.addAll(Arrays.asList(hexes));
    }
    
    public void add (HexGroup hexes) {
        hex_list.addAll(hexes.toList());
    }
    
    
    
    public void clear () {
        hex_list.clear();
    }
}
