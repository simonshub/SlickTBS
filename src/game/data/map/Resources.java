/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import java.io.File;
import java.io.IOException;
import main.Consts;
import main.SlickUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author XyRoN
 */
public enum Resources {
    POWER, GOLD, POPULATION, FOOD, MATERIALS;
    
    public Image img;
    public String img_path;
    
    Resources () {
        this.img = null;
        this.img_path = "";
        
        try {
            init();
        } catch (IOException | SlickException ex) {
            ex.printStackTrace();
        }
    }
    
    public void init () throws IOException, SlickException {
        String path = Consts.TILES_PATH+this.name().toLowerCase()+".tile";
        File f = new File (path);
        SlickUtils.readObjectFromFile(f, this);
        this.img = new Image (this.img_path);
    }
}
