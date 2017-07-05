/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.world;

import game.data.world.map.TerrainTypeEnum;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.ResMgr;
import main.utils.Log;
import main.utils.SlickUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author emil.simon
 */
public class PointOfInterest {
    
    // HOSTILE DUNGEONS
    
    public static final PointOfInterest GRAVEYARD = new PointOfInterest
                        (true,  "Graveyard",        "dungeon_crypt.png",        1.0f,1.0f,1.0f,     "A haunted graveyard.",
                         TerrainTypeEnum.ARID, TerrainTypeEnum.DESERT, TerrainTypeEnum.SAVANNA, TerrainTypeEnum.TROPICAL);
    
    public static final PointOfInterest ABANDONED_TOWER = new PointOfInterest
                        (true,  "Dungeon",          "dungeon_dungeon.png",      1.0f,1.0f,1.0f,     "An old, ruined tower.",
                         TerrainTypeEnum.ARID, TerrainTypeEnum.SAVANNA);
    
    public static final PointOfInterest RUINED_FORT = new PointOfInterest
                        (true,  "Ruined Fort",      "dungeon_ruins.png",        1.0f,1.0f,1.0f,     "A forgotten fortress or stronghold.",
                         TerrainTypeEnum.ARID, TerrainTypeEnum.DESERT, TerrainTypeEnum.SAVANNA);
    
    public static final PointOfInterest GHOST_TOWN = new PointOfInterest
                        (true,  "Ghost Town",       "dungeon_haunted_house.png",1.0f,1.0f,1.0f,     "An abandoned town, village or hamlet.",
                         TerrainTypeEnum.ARID, TerrainTypeEnum.DESERT, TerrainTypeEnum.SAVANNA);
    
    public static final PointOfInterest CAVE = new PointOfInterest
                        (true,  "Cave",             "dungeon_cave.png",         1.0f,1.0f,1.0f,     "A shallow cave, sometimes leading underground.",
                         TerrainTypeEnum.valuesWithout(TerrainTypeEnum.HILLS, TerrainTypeEnum.TROPICAL, TerrainTypeEnum.SAVANNA));
    
    public static final PointOfInterest CAVERN = new PointOfInterest
                        (true,  "Cavern",           "dungeon_cave.png",         1.0f,1.0f,1.0f,     "A huge system of caves, sometimes leading underground.",
                         TerrainTypeEnum.valuesWithout(TerrainTypeEnum.HILLS));
    
    public static final PointOfInterest ABANDONED_MINE = new PointOfInterest
                        (true,  "Abandoned Mine",   "dungeon_mine.png",         1.0f,1.0f,1.0f,     "An abandoned mine.",
                         TerrainTypeEnum.SAVANNA);
    
    
    
    
    // CIVILIAN SETTLEMENTS
    
    public static final PointOfInterest HAMLET = new PointOfInterest
                        (false, "Hamlet",           "hamlet.png",               1.0f,1.0f,1.0f,     "This is a small hamlet, consisting of sparse rough housing and a few services..",
                         null);
    
    public static final PointOfInterest VILLAGE = new PointOfInterest
                        (false, "Village",          "village.png",              1.0f,1.0f,1.0f,     "This is a village.",
                         null);
    
    public static final PointOfInterest TOWN = new PointOfInterest
                        (false, "Town",             "town.png",                 1.0f,1.0f,1.0f,     "This is a town.",
                         null);
    
    public static final PointOfInterest CITY = new PointOfInterest
                        (false, "City",             "city.png",                 1.0f,1.0f,1.0f,     "This is a city.",
                         null);
    
    public static final PointOfInterest CAPITOL = new PointOfInterest
                        (false, "Capitol",          "capitol.png",              1.0f,1.0f,1.0f,     "This is the ruling seat of a faction, a center of great power.",
                         null);
    
    
    
    
    // MILITARY SETTLEMENTS
    
    public static final PointOfInterest OUTPOST = new PointOfInterest
                        (false, "Outpost",          "outpost.png",              1.0f,1.0f,1.0f,     "This is a small outpost, which produces small patrols.",
                         null);
    
    public static final PointOfInterest FORT = new PointOfInterest
                        (false, "Fort",             "fort.png",                 1.0f,1.0f,1.0f,     "This is a fortified army camp, which produces patrols on a regular basis.",
                         null);
    
    public static final PointOfInterest STRONGHOLD = new PointOfInterest
                        (false, "Stronghold",       "citadel.png",                 1.0f,1.0f,1.0f,     "This is a fortified stronghold, which is used as a source of power and influence.",
                         null);
    
    public static final PointOfInterest CASTLE = new PointOfInterest
                        (false, "Castle",           "citadel.png",              1.0f,1.0f,1.0f,     "This is a walled castle, a central administrative, military and regal location.",
                         null);
    
    public static final PointOfInterest CITADEL = new PointOfInterest
                        (false, "Citadel",          "citadel.png",              1.0f,1.0f,1.0f,     "This is the ruling seat of a faction, a center of great power.",
                         null);
    
    ;
    
    public static final PointOfInterest[] SETTLEMENT_TYPES = {
        HAMLET, VILLAGE, TOWN, CITY, CAPITOL,
        OUTPOST, FORT, STRONGHOLD, CASTLE, CITADEL,
    };
    
    public static final PointOfInterest[] CIVILIAN_SETTLEMENT_TYPES = {
        HAMLET, VILLAGE, TOWN, CITY, CAPITOL,
    };
    
    public static final PointOfInterest[] MILITARY_SETTLEMENT_TYPES = {
        OUTPOST, FORT, STRONGHOLD, CASTLE, CITADEL,
    };
    
    private static final float POI_ICON_SCALE_FACTOR = 0.8f;
    
    public boolean can_be_generated;
    
    public Image icon;
    public Color color;
    public String name;
    public String description;
    public PointOfInterest parent;
    public List<TerrainTypeEnum> allowed_terrain_types;
    
    
    
    public PointOfInterest (PointOfInterest parent) {
        this.parent = parent;
        this.can_be_generated = parent.can_be_generated;
        this.icon = parent.icon;
        this.color = parent.color;
        this.name = parent.name;
        this.description = parent.description;
        this.allowed_terrain_types = new ArrayList<> (parent.allowed_terrain_types);
    }
    
    public PointOfInterest (PointOfInterest parent, String name) {
        this(parent);
        this.name = name;
    }
    
    private PointOfInterest (boolean can_be_generated, String name, String icon, float r, float g, float b, String description, TerrainTypeEnum... not_allowed_terrains) {
        try {
            this.parent = null;
            
            this.can_be_generated = can_be_generated;
            this.color = new Color (r,g,b,1.0f);
            this.icon = new Image (ResMgr.POI_GRFX_PATH + icon);
            this.name = name;
            this.description = description;
        
            allowed_terrain_types = new ArrayList<> ();
            allowed_terrain_types.add(TerrainTypeEnum.SEA);
            allowed_terrain_types.add(TerrainTypeEnum.MOUNTAINS);
            allowed_terrain_types.addAll(Arrays.asList(TerrainTypeEnum.values()));
            
            if (not_allowed_terrains != null)
                for (TerrainTypeEnum not_allowed : not_allowed_terrains)
                    allowed_terrain_types.remove(not_allowed);
        } catch (SlickException ex) {
            Log.err(ex);
        }
    }
    
    
    
    public static PointOfInterest[] values () {
        List<PointOfInterest> result = new ArrayList<> ();
        Field[] fields = PointOfInterest.class.getDeclaredFields();
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers()) && f.getType().equals(PointOfInterest.class)) {
                try {
                    result.add((PointOfInterest) f.get(null));
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Log.err(ex);
                }
            }
        }
        
        PointOfInterest[] result_array = new PointOfInterest [result.size()];
        result.toArray(result_array);
        return result_array;
    }
    
    public static PointOfInterest valueOf (String name) {
        try {
            return (PointOfInterest) PointOfInterest.class.getField(name.toUpperCase()).get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Log.err(ex);
        }
        
        return null;
    }
    
    public static PointOfInterest getRandom (TerrainTypeEnum for_terrain) {
        List<PointOfInterest> potential = new ArrayList<> ();
        potential.addAll(Arrays.asList(PointOfInterest.values()));
        
        for (int i=0;i<potential.size();i++) {
            if (!potential.get(i).allowed_terrain_types.contains(for_terrain) || !potential.get(i).can_be_generated) {
                potential.remove(i);
                i--;
            }
        }
        
        if (potential.isEmpty()) return null;
        
        return potential.get(SlickUtils.randIndex(potential.size()));
    }
    
    public boolean is (PointOfInterest parent) {
        if (this.parent == null)
            return this == parent;
        
        return this.parent == parent;
    }
    
    public boolean isSettlement () {
        List<PointOfInterest> settlements = Arrays.asList(SETTLEMENT_TYPES);
        return settlements.contains(this) || settlements.contains(this.parent);
    }
    
    public boolean isMilitary () {
        List<PointOfInterest> settlements = Arrays.asList(MILITARY_SETTLEMENT_TYPES);
        return settlements.contains(this) || settlements.contains(this.parent);
    }
    
    public boolean isCivilian () {
        List<PointOfInterest> settlements = Arrays.asList(CIVILIAN_SETTLEMENT_TYPES);
        return settlements.contains(this) || settlements.contains(this.parent);
    }
    
    public List<PointOfInterest> getSettlements () {
        return Arrays.asList(SETTLEMENT_TYPES);
    }
    
    public void render (float x, float y, float scale_x, float scale_y) {
        float margin_x = (scale_x * (1.0f - POI_ICON_SCALE_FACTOR)) / 2.0f;
        float margin_y = (scale_y * (1.0f - POI_ICON_SCALE_FACTOR)) / 2.0f;
        
        this.icon.draw(x+margin_x, y+margin_y, scale_x*POI_ICON_SCALE_FACTOR, scale_y*POI_ICON_SCALE_FACTOR, this.color);
    }
}