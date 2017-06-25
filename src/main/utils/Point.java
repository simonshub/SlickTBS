/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.utils;

/**
 *
 * @author emil.simon
 */
public class Point {
    public int x,y;
    
    public Point () {
        this.x = 0;
        this.y = 0;
    }
    
    public Point (int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public double getDistance (Point p) {
        return Math.sqrt(Math.pow(x-p.x,2) + Math.pow(y-p.y,2));
    }
}
