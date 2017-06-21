/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

import game.data.map.TerrainTypeEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.ResMgr;
import main.utils.SlickUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author emil.simon
 */
public enum PointOfInterest {
    OLD_CRYPT           ("Old Crypt",       "dungeon_crypt.png",           "An abandoned crypt. Level 2 dungeon.",
                         TerrainTypeEnum.ARID, TerrainTypeEnum.DESERT, TerrainTypeEnum.SAVANNA, TerrainTypeEnum.TROPICAL, TerrainTypeEnum.SEA), 
    ABANDONED_TOWER     ("Abandoned Tower", "dungeon_dungeon.png",         "An old, ruined tower. Level 3 dungeon.",
                         TerrainTypeEnum.ARID, TerrainTypeEnum.DESERT, TerrainTypeEnum.SAVANNA, TerrainTypeEnum.SEA), 
    RUINED_FORT         ("Ruined Fort",     "dungeon_ruins.png",           "A forgotten fortress or stronghold. Level 4 dungeon.",
                         TerrainTypeEnum.ARID, TerrainTypeEnum.DESERT, TerrainTypeEnum.SAVANNA, TerrainTypeEnum.SEA), 
    
    ;
    
    private static final float POI_ICON_SCALE_FACTOR = 0.8f;
    
    public Image icon;
    public String name;
    public String description;
    public List<TerrainTypeEnum> allowed_terrain_types;
    
    PointOfInterest (String name, String icon, String description, TerrainTypeEnum... not_allowed_terrains) {
        try {
            this.icon = new Image (ResMgr.POI_GRFX_PATH + icon);
            this.name = name;
            this.description = description;
        
            allowed_terrain_types = new ArrayList<> ();
            allowed_terrain_types.addAll(Arrays.asList(TerrainTypeEnum.values()));
            for (TerrainTypeEnum not_allowed : not_allowed_terrains)
                allowed_terrain_types.remove(not_allowed);
        } catch (SlickException ex) {
            ex.printStackTrace();
        }
    }
    
    public static PointOfInterest getRandom (TerrainTypeEnum for_terrain) {
        List<PointOfInterest> potential = new ArrayList<> ();
        potential.addAll(Arrays.asList(PointOfInterest.values()));
        
        for (int i=0;i<potential.size();i++) {
            if (!potential.get(i).allowed_terrain_types.contains(for_terrain)) {
                potential.remove(i);
                i--;
            }
        }
        
        if (potential.isEmpty()) return null;
        
        return potential.get(SlickUtils.randIndex(potential.size()));
    }
    
    public void render (float x, float y, float scale_x, float scale_y) {
        float margin_x = (scale_x * (1.0f - POI_ICON_SCALE_FACTOR)) / 2.0f;
        float margin_y = (scale_y * (1.0f - POI_ICON_SCALE_FACTOR)) / 2.0f;
        
        this.icon.draw(x+margin_x, y+margin_y, scale_x*POI_ICON_SCALE_FACTOR, scale_y*POI_ICON_SCALE_FACTOR);
    }
}
