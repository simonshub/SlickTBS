/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

/**
 *
 * @author emil.simon
 */
public class Infrastructure {
    public int road_level;
    public int fort_level;
    public int factory_level;
    public int airfield_level;
    
    public int road_max_level;
    public int fort_max_level;
    public int factory_max_level;
    public int airfield_max_level;
    
    public Infrastructure () {
        road_max_level = 5;
        fort_max_level = 5;
        factory_max_level = 5;
        airfield_max_level = 5;
    }
}
