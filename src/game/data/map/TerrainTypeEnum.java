/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import java.io.File;
import java.io.IOException;
import main.Consts;
import main.utils.SlickUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author emil.simon
 */
public enum TerrainTypeEnum {
    DEFAULT,
    OPEN, TUNDRA,
    FOREST, TROPICAL,
    SEA, MARSHES,
    HILLS, MOUNTAINS, 
    DESERT, WASTES, ARID, 
    
    ;
    
    public Image img;
    public String img_path;
    
    
    
    TerrainTypeEnum () {
        this.img = null;
        this.img_path = "";
        
        try {
            init();
        } catch (IOException | SlickException ex) {
            ex.printStackTrace();
        }
    }
    
    public void init () throws IOException, SlickException {
        img_path = Consts.TILES_PATH+this.name().toLowerCase()+".tile";
        File f = new File (img_path);
        SlickUtils.readObjectFromFile(f, this);
        this.img = new Image (this.img_path);
    }
}
