/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.world.map.hex;

import game.data.world.Faction;
import game.data.world.PointOfInterest;
import game.data.world.map.Camera;
import game.data.world.map.Continent;
import game.data.world.map.FogOfWar;
import game.data.world.map.TerrainTypeEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import main.Consts;
import main.ResMgr;
import main.utils.Point;
import main.utils.SlickUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author emil.simon
 */
public final class Hex {
    
    public static final int HEX_GRID_SIZE_X = 64;
    public static final int HEX_GRID_SIZE_Y = 64;
    
    public static Image HEX_FOG_OF_WAR_IMG;
    public static Image HEX_OVERLAY_IMG;
    public static Image HEX_GRID_IMG;
    public static final String HEX_FOG_OF_WAR_IMG_PATH = "res/grfx/fog.png";
    public static final String HEX_OVERLAY_IMG_PATH = "res/grfx/overlay.png";
    public static final String HEX_GRID_IMG_PATH = "res/grfx/hex_blue.png";
    
    public static Image HEX_COAST_SANDY_UL_IMG;
    public static Image HEX_COAST_SANDY_UR_IMG;
    public static Image HEX_COAST_SANDY_L_IMG;
    public static Image HEX_COAST_SANDY_R_IMG;
    public static Image HEX_COAST_SANDY_DL_IMG;
    public static Image HEX_COAST_SANDY_DR_IMG;
    public static Image HEX_COAST_CLIFF_UL_IMG;
    public static Image HEX_COAST_CLIFF_UR_IMG;
    public static Image HEX_COAST_CLIFF_L_IMG;
    public static Image HEX_COAST_CLIFF_R_IMG;
    public static Image HEX_COAST_CLIFF_DL_IMG;
    public static Image HEX_COAST_CLIFF_DR_IMG;
    public static String HEX_COAST_SANDY_UL_IMG_PATH = "res/grfx/coast/coast_sandy_ul.png";
    public static String HEX_COAST_SANDY_UR_IMG_PATH = "res/grfx/coast/coast_sandy_ur.png";
    public static String HEX_COAST_SANDY_L_IMG_PATH = "res/grfx/coast/coast_sandy_l.png";
    public static String HEX_COAST_SANDY_R_IMG_PATH = "res/grfx/coast/coast_sandy_r.png";
    public static String HEX_COAST_SANDY_DL_IMG_PATH = "res/grfx/coast/coast_sandy_dl.png";
    public static String HEX_COAST_SANDY_DR_IMG_PATH = "res/grfx/coast/coast_sandy_dr.png";
    public static String HEX_COAST_CLIFF_UL_IMG_PATH = "res/grfx/coast/coast_cliff_ul.png";
    public static String HEX_COAST_CLIFF_UR_IMG_PATH = "res/grfx/coast/coast_cliff_ur.png";
    public static String HEX_COAST_CLIFF_L_IMG_PATH = "res/grfx/coast/coast_cliff_l.png";
    public static String HEX_COAST_CLIFF_R_IMG_PATH = "res/grfx/coast/coast_cliff_r.png";
    public static String HEX_COAST_CLIFF_DL_IMG_PATH = "res/grfx/coast/coast_cliff_dl.png";
    public static String HEX_COAST_CLIFF_DR_IMG_PATH = "res/grfx/coast/coast_cliff_dr.png";
    
    public boolean river;
    public boolean coast_is_cliff;
    public boolean coastal_ul;
    public boolean coastal_ur;
    public boolean coastal_l;
    public boolean coastal_r;
    public boolean coastal_dl;
    public boolean coastal_dr;
    
    public int x, y;
    public Continent continent;
    public FogOfWar fog_of_war;
    public TerrainTypeEnum terrain;
    public PointOfInterest poi;
    public Faction owner;
    
    
    
    public static void init () throws SlickException {
        HEX_GRID_IMG = new Image (HEX_GRID_IMG_PATH);
        HEX_OVERLAY_IMG = new Image (HEX_OVERLAY_IMG_PATH);
        HEX_FOG_OF_WAR_IMG = new Image (HEX_FOG_OF_WAR_IMG_PATH);
        
//        HEX_COAST_SANDY_UL_IMG = new Image (HEX_COAST_SANDY_UL_IMG_PATH);
//        HEX_COAST_SANDY_UR_IMG = new Image (HEX_COAST_SANDY_UR_IMG_PATH);
//        HEX_COAST_SANDY_L_IMG = new Image (HEX_COAST_SANDY_L_IMG_PATH);
//        HEX_COAST_SANDY_R_IMG = new Image (HEX_COAST_SANDY_R_IMG_PATH);
//        HEX_COAST_SANDY_DL_IMG = new Image (HEX_COAST_SANDY_DL_IMG_PATH);
//        HEX_COAST_SANDY_DR_IMG = new Image (HEX_COAST_SANDY_DR_IMG_PATH);
//        
//        HEX_COAST_CLIFF_UL_IMG = new Image (HEX_COAST_CLIFF_UL_IMG_PATH);
//        HEX_COAST_CLIFF_UR_IMG = new Image (HEX_COAST_CLIFF_UR_IMG_PATH);
//        HEX_COAST_CLIFF_L_IMG = new Image (HEX_COAST_CLIFF_L_IMG_PATH);
//        HEX_COAST_CLIFF_R_IMG = new Image (HEX_COAST_CLIFF_R_IMG_PATH);
//        HEX_COAST_CLIFF_DL_IMG = new Image (HEX_COAST_CLIFF_DL_IMG_PATH);
//        HEX_COAST_CLIFF_DR_IMG = new Image (HEX_COAST_CLIFF_DR_IMG_PATH);
    }
    
    public Hex (int x, int y, String continent_name) {
        this.x = x;
        this.y = y;
        this.continent = null;
        
        terrain = TerrainTypeEnum.OPEN;
        river = false;
        fog_of_war = FogOfWar.VISIBLE;
        poi = null;
        
        coastal_ul = false;
        coastal_ur = false;
        coastal_l = false;
        coastal_r = false;
        coastal_dl = false;
        coastal_dr = false;
    }
    
    
    
    public void setLocation (int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setPointOfInterest (PointOfInterest poi) {
        this.poi = poi;
    }
    
    public Point getMapCoordsCenter () {
        Point p = new Point ();
        p.x = x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0) + (int)(HEX_GRID_SIZE_X/2);
        p.y = y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y) + (int)(HEX_GRID_SIZE_Y/2);
        return p;
    }
    
    public Point getScreenCoordsCenter (Camera cam) {
        Point p = new Point ();
        p.x = (int) ((x*HEX_GRID_SIZE_X+(y%2==0?HEX_GRID_SIZE_X/2:0)-cam.x + (int)(HEX_GRID_SIZE_X/2))*cam.zoom);
        p.y = (int) ((y*HEX_GRID_SIZE_Y-(HEX_GRID_SIZE_Y/4*y)-cam.y + (int)(HEX_GRID_SIZE_Y/2))*cam.zoom);
        return p;
    }
    
    public void render (float x_draw, float y_draw, float x_scale, float y_scale, GameContainer container, StateBasedGame game, Graphics g) {
        if (terrain != null && terrain.img != null && fog_of_war!=FogOfWar.HIDDEN)
            terrain.img.draw(x_draw, y_draw, x_scale, y_scale);
        
        if (HEX_FOG_OF_WAR_IMG != null && fog_of_war.level != 0 && Consts.RENDER_FOG_OF_WAR)
            HEX_FOG_OF_WAR_IMG.draw(x_draw, y_draw, x_scale, y_scale, new Color (1f,1f,1f,fog_of_war.level/3));
        
        if (HEX_GRID_IMG == null)
            return;
        
        if (ResMgr.render_political_overlay && owner!=null && owner.color!=null)
            HEX_OVERLAY_IMG.draw(x_draw, y_draw, x_scale, y_scale, owner.color);
        
        if (ResMgr.render_continents && continent!=null && continent.getColor()!=null)
            HEX_OVERLAY_IMG.draw(x_draw, y_draw, x_scale, y_scale, continent.getColor());
        
        if (ResMgr.render_grid)
            HEX_GRID_IMG.draw(x_draw, y_draw, x_scale, y_scale);
        
        if (poi != null && Consts.RENDER_POIS)
            poi.render(x_draw, y_draw, x_scale, y_scale);
    }
    
    public void renderMouseShadow (float x_draw, float y_draw, float x_scale, float y_scale) {
        if (HEX_OVERLAY_IMG != null)
            HEX_OVERLAY_IMG.draw(x_draw, y_draw, x_scale, y_scale, new Color (0f,0f,0f,0.2f));
    }
    
    
    
    public List<Hex> getAllInRange (HexGrid grid, int range) {
        Set<Hex> results = new HashSet<> ();
        range += 1;
        
        if (y%2==0)
            for (int step_y=0;step_y<range;step_y++) {
                for (int step_x=0;step_x<range - (int)Math.ceil(step_y/2);step_x++) {
                    results.add(grid.get(x + step_x, y + step_y));
                    results.add(grid.get(x + step_x, y - step_y));
                    results.add(grid.get(x - step_x + (step_y%2), y + step_y));
                    results.add(grid.get(x - step_x + (step_y%2), y - step_y));
                }
            }
        else
            for (int step_y=0;step_y<range;step_y++) {
                for (int step_x=0;step_x<range - (int)Math.ceil(step_y/2);step_x++) {
                    results.add(grid.get(x + step_x - (step_y%2), y + step_y));
                    results.add(grid.get(x + step_x - (step_y%2), y - step_y));
                    results.add(grid.get(x - step_x, y + step_y));
                    results.add(grid.get(x - step_x, y - step_y));
                }
            }
        
        return new ArrayList<> (results);
    }
    
    public Hex getAdjacent (HexGrid grid, DirEnum direction) {
        if (y%2==0)
            return grid.get(x+direction.even_x_offset, y+direction.even_y_offset);
        else
            return grid.get(x+direction.odd_x_offset, y+direction.odd_y_offset);
    }
    
    public Hex getRandomAdjacent (HexGrid grid) {
        return getAdjacent(grid, DirEnum.getRandom());
    }
    
    public Hex getRandomAdjacentOfType (HexGrid grid, TerrainTypeEnum type) {
        List<Hex> result = new ArrayList<> ();
        for (DirEnum dir : DirEnum.values())
            if (this.getAdjacent(grid, dir)!=null && this.getAdjacent(grid, dir).terrain.equals(type))
                result.add(this.getAdjacent(grid, dir));
        
        if (result.isEmpty())
            return null;
        
        int index = (int)(Math.random()*result.size());
        return result.get(index);
    }
    
    public Hex getRandomAdjacentOfTypes (HexGrid grid, TerrainTypeEnum... types) {
        List<Hex> result = new ArrayList<> ();
        for (DirEnum dir : DirEnum.values()) {
            Hex adj = this.getAdjacent(grid, dir);
            if (adj!=null && SlickUtils.equalsAnyInArray(types, adj.terrain))
                result.add(adj);
        }
        
        if (result.isEmpty())
            return null;
        
        int index = (int)(Math.random()*result.size());
        return result.get(index);
    }
    
    public List<Hex> getAllAdjacent (HexGrid grid) {
        List<Hex> result = new ArrayList<> ();
        for (DirEnum dir : DirEnum.values())
            if (this.getAdjacent(grid, dir)!=null) result.add(this.getAdjacent(grid, dir));
        return result;
    }
    
    public List<Hex> getAllAdjacentOfTypes (HexGrid grid, TerrainTypeEnum... types) {
        List<TerrainTypeEnum> allowed_types = Arrays.asList(types);
        List<Hex> result = new ArrayList<> ();
        for (DirEnum dir : DirEnum.values()) {
            Hex adj = this.getAdjacent(grid, dir);
            if (adj!=null && allowed_types.contains(adj.terrain)) result.add(adj);
        }
        return result;
    }
    
    
    
    public boolean isCoastal (HexGrid grid) {
        return isBorder(grid, TerrainTypeEnum.SEA);
    }
    
    public boolean isBorder (HexGrid grid) {
//        for (DirEnum dir : DirEnum.values())
//            if (getAdjacent(grid,dir)!=null && !getAdjacent(grid,dir).terrain.equals(terrain)) return true;
//        return false;
        List<Hex> adj_list = getAllAdjacent(grid);
        for (Hex adj : adj_list) {
            if (!adj.terrain.equals(this.terrain)) return true;
        }
        return false;
    }
    
    public boolean isBorder (HexGrid grid, TerrainTypeEnum for_type) {
//        for (DirEnum dir : DirEnum.values())
//            if (getAdjacent(grid,dir)!=null && getAdjacent(grid,dir).terrain.equals(type) && !terrain.equals(type)) return true;
//        return false;
        List<Hex> adj_list = getAllAdjacent(grid);
        for (Hex adj : adj_list) {
            if (adj.terrain.equals(for_type)) return true;
        }
        return false;
    }
    
    
    
    public List<Hex> spreadTerrainExceptTypes (HexGrid grid, TerrainTypeEnum... exclude_types) {
        DirEnum[] enums = DirEnum.values();
        List<TerrainTypeEnum> exclude_list;
        
        if (exclude_types!=null) {
            exclude_list = Arrays.asList(exclude_types);
        } else {
            exclude_list = new ArrayList<> ();
        }
        
        List<Hex> additions = new ArrayList<> ();
        
        for (DirEnum dir : enums) {
            Hex adj = getAdjacent(grid, dir);
            if (adj!=null && !exclude_list.contains(adj.terrain)) {
                adj.terrain = this.terrain;
                additions.add(adj);
            }
        }
        
        return additions;
    }
    
    public List<Hex> spreadTerrainToTypes (HexGrid grid, TerrainTypeEnum... include_types) {
        DirEnum[] enums = DirEnum.values();
        List<TerrainTypeEnum> include_list;
        
        if (include_types!=null) {
            include_list = Arrays.asList(include_types);
        } else {
            include_list = new ArrayList<> ();
        }
        
        List<Hex> additions = new ArrayList<> ();
        
        for (DirEnum dir : enums) {
            Hex adj = getAdjacent(grid, dir);
            if (adj!=null && include_list.contains(adj.terrain)) {
                adj.terrain = this.terrain;
                additions.add(adj);
            }
        }
        
        return additions;
    }
    
    public List<Hex> spreadTerrain (HexGrid grid) {
        return spreadTerrainExceptTypes(grid, (TerrainTypeEnum[]) null);
    }
    
    public List<Hex> propagate (HexGrid grid, TerrainTypeEnum from_type, TerrainTypeEnum to_type) {
        List<Hex> adj = this.getAllAdjacentOfTypes (grid, from_type);
        List<Hex> candidates = new ArrayList<> ();
        
        for (int count=0;count<adj.size();count++) {
            int index = SlickUtils.randIndex(adj.size());
            candidates.add(adj.get(index));
            adj.remove(index);
        }
        
        for (Hex hex : candidates)
            hex.terrain = to_type;
        
        return candidates;
    }
}
