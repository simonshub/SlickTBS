/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.players;

import java.io.File;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author emil.simon
 */
public class Insignia {
    public Image image;
    public String name;
    
    public Insignia (File file) throws SlickException {
        String path = file.getAbsolutePath();
        String name = file.getName();
        
        this.image = new Image (path);
        this.name = name.substring(path.lastIndexOf("."));
    }
}
