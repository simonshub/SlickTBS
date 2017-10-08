/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.combat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Consts;
import main.utils.SlickUtils;

/**
 *
 * @author XyRoN
 */
public class CombatMgr {
    
    private static CombatMgr instance;
    
    public static List<CombatCard> character_lib;
    
    public static final int MAX_MANA_TO_PER_TURN = 5;
    public static final int MAX_STAMINA_TO_PER_TURN = 5;
    
    
    
    public static void init () {
        instance = new CombatMgr ();
        character_lib = new ArrayList<> ();
        
        try {
            File f = new File (Consts.COMBAT_INIT_PATH);
            SlickUtils.readObjectFromFile(f, instance);
        } catch (IOException ex) {
            Logger.getLogger(CombatMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
