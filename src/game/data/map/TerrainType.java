/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.SlickUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author emil.simon
 */
public enum TerrainType {
    DEFAULT, OPEN, HILLS, MOUNTAINS, TUNDRA, DESERT, MARSHES, TROPICAL, SEA, FOREST, URBAN, ARID;
    
    public Image img;
    public String img_path;
    
    public float soft_mod;
    public float hard_mod;
    
    public float atk_mod;
    public float def_mod;
    
    TerrainType () {
        this.img = null;
        this.img_path = "";
        
        try {
            init();
        } catch (IOException | SlickException ex) {
            ex.printStackTrace();
        }
    }
    
    public void init () throws IOException, SlickException {
        String path = "res/data/tiles/"+this.name().toLowerCase()+".tile";
        File f = new File (path);
        SlickUtils.readObjectFromFile(f, this);
        this.img = new Image (this.img_path);
    }
}
