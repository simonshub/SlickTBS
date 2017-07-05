/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.world.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.Consts;
import main.utils.Log;
import main.utils.SlickUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author emil.simon
 */
 public enum TerrainTypeEnum {
    OPEN(),
    GRASS(), SAVANNA(), TUNDRA(),
    FOREST(), TROPICAL(),
    SEA(), MARSHES(),
    HILLS(), MOUNTAINS(), 
    DESERT(), WASTES(), ARID(), 
    
    ;
    
    public int move_cost;
    public int military_land_value;
    public int civilian_land_value;
    public Image img;
    public String img_path;
    
    
    
    TerrainTypeEnum () {
        this.img = null;
        this.img_path = "";
        this.move_cost = 1;
        
        try {
            init();
        } catch (IOException | SlickException ex) {
            Log.err(ex);
        }
    }
    
    public void init () throws IOException, SlickException {
        img_path = Consts.TILES_PATH + this.name().toLowerCase() + Consts.TILES_EXT;
        File f = new File (img_path);
        SlickUtils.readObjectFromFile(f, this);
        this.img = new Image (this.img_path);
    }
    
    
    
    public static TerrainTypeEnum[] valuesWithout (TerrainTypeEnum... without) {
        List<TerrainTypeEnum> values = new ArrayList<> (Arrays.asList(TerrainTypeEnum.values()));
        
        for (TerrainTypeEnum type : without) {
            values.remove(type);
        }
        
        TerrainTypeEnum[] results = new TerrainTypeEnum [values.size()];
        values.toArray(results);
        return results;
    }
}
