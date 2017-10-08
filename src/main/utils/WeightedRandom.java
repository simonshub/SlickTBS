/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author XyRoN
 */
public class WeightedRandom {
    
    private class WeightedRegion {
        public List<Location> region;
        public Object value;
        
        public WeightedRegion () {
            region = new ArrayList<> ();
            value = null;
        }
        
        public void add (Location p) {
            region.add(p);
        }
        
        public int getXOfLastPoint () {
            if (region.isEmpty()) return 0;
            else return region.get(region.size()-1).x;
        }
        
        public int getYOfLastPoint () {
            if (region.isEmpty()) return 0;
            else return region.get(region.size()-1).y;
        }
        
        public boolean isApplicableForX (int x) {
            if (region.isEmpty()) return false;
            return (x > region.get(0).x && x <= region.get(region.size()-1).x);
        }
        
        public double getChance (int x) {
            if (region.size()<2) return 0;
            
            for (int i=0;i<region.size()-1;i++) {
                Location p1 = region.get(i);
                Location p2 = region.get(i+1);
                if (p1==null || p2==null) continue;
                
                if (p1.x > x && p2.x <= x) {
                    return ((p2.y-p1.y) * (x-p1.x))/(p2.x-p1.x);
                }
            }
            
            return 0;
        }
    }
    
    
    
    private List<WeightedRegion> regions;
    private WeightedRegion region_to_add;
    
    
    
    public WeightedRandom () {
        regions = new ArrayList<> ();
        region_to_add = null;
    }
    
    public WeightedRandom add (int from_x, int to_x, int y) {
        if (region_to_add == null) region_to_add = new WeightedRegion ();
        
        Location p1 = new Location (from_x, region_to_add.getYOfLastPoint());
        Location p2 = new Location (to_x, y);
        
        region_to_add.add(p1);
        region_to_add.add(p2);
        
        return this;
    }
    
    public WeightedRandom add (int x, int y) {
        if (region_to_add == null) region_to_add = new WeightedRegion ();
        
        Location p = new Location (x,y);
        region_to_add.add(p);
        
        return this;
    }
    
    public WeightedRandom commit () {
        regions.add(region_to_add);
        region_to_add = null;
        
        return this;
    }
    
    public Object roll (int x) {
        double max_roll = 0.0;
        Map<WeightedRegion, Double> chances = new HashMap<> ();
        
        for (WeightedRegion region : regions) {
            if (region.isApplicableForX(x)) {
                chances.put(region, region.getChance(x));
                max_roll += region.getChance(x);
            }
        }
        
        double roll = Math.random() * max_roll;
        double roll_so_far = 0.0;
        for (WeightedRegion region : chances.keySet()) {
            roll_so_far += region.getChance(x);
            
            if (roll <= roll_so_far) {
                return region.value;
            }
        }
        
        return null;
    }
    
    public Object roll () {
        int max_x = 0;
        
        for (WeightedRegion region : regions) {
            if (region.getXOfLastPoint() > max_x)
                max_x = region.getXOfLastPoint();
        }
        
        int rand = (int)(Math.random() * max_x);
        return this.roll(rand);
    }
}
