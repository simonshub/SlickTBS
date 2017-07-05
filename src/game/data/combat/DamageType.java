/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.combat;

import java.io.File;
import java.io.IOException;
import main.Consts;
import main.utils.Log;
import main.utils.SlickUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author emil.simon
 */
public enum DamageType {
    PHYSICAL,
    SLASHING, BLUNT, PIERCING,
    FIRE, COLD, ELECTRIC,
    HOLY, DEATH, POISON, ARCANE,
    
    ;
    
    public String name;
    public String img_path;
    
    public Image img;
    
    DamageType () {
        try {
            init ();
        } catch (IOException | SlickException ex) {
            Log.err(ex);
        }
    }
    
    public void init () throws IOException, SlickException {
        img_path = Consts.DAMAGE_TYPE_PATH + this.name().toLowerCase() + Consts.DAMAGE_TYPE_EXT;
        File f = new File (img_path);
        SlickUtils.readObjectFromFile(f, this);
        this.img = new Image (this.img_path);
    }
    
    public static boolean isPhysical (DamageType type) {
        return (type.equals(PHYSICAL) || type.equals(BLUNT) || type.equals(SLASHING) || type.equals(PIERCING));
    }
    
}
