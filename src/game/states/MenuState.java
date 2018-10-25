/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.Settings;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author emil.simon
 */
public class MenuState extends BasicGameState {
    public static final int ID = 2;
    
    public class Point {
        public float x,y;
        public float angle_of_next;
        
        public Point (float x, float y) {
            this.x=x; this.y=y;
        }
        public Point offset (float x, float y) {
            return new Point (this.x+x, this.y+y);
        }
        public Point polarOffset (float angle, float dist) {
            angle_of_next=angle;
            return new Point ((float)(this.x-Math.cos(angle)*dist), (float)(this.y-Math.sin(angle)*dist)); }
    }
    
    int cam_x = 0;
    int cam_y = 0;
    
    public Image bkg;
    public Image img;
    public Polygon shape;
    public Random rand;
    
    public int road_step = 50;
    public int road_width = 50;
    public float road_max_angular_offset = (float) (1/6 * 2 * Math.PI);
    public List<Point> road_points;
    
    public void generatePoly () {
        road_points = new ArrayList<> ();
        Point start = new Point (rand.nextInt(Settings.screen_width), 0);
        Point prev = start;
        prev.angle_of_next = (float) (3/2 * Math.PI);
        road_points.add(start);
        for (;road_points.get(road_points.size()-1).y < Settings.screen_height && road_points.size() < 10;) {
            Point p = prev.polarOffset(prev.angle_of_next+(rand.nextFloat()*road_max_angular_offset - road_max_angular_offset/2), road_step);
            road_points.add(p);
            prev = p;
        }
        
        shape = new Polygon ();
        for (int i=0;i<road_points.size();i++) {
            Point p = road_points.get(i).polarOffset((float) (road_points.get(i).angle_of_next+Math.PI/2), road_width);
            shape.addPoint(p.x, p.y);
        }
        for (int i=road_points.size()-1;i>=0;i--) {
            Point p = road_points.get(i).polarOffset((float) (road_points.get(i).angle_of_next-Math.PI/2), road_width);
            shape.addPoint(p.x, p.y);
        }
        shape.setClosed(true);
        System.out.println("Generated poly!");
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        bkg = new Image ("grass.png");
        img = new Image ("gravely.png");
        rand = new Random ();
        
        generatePoly();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.texture(new Rectangle(0,0, Settings.screen_width,Settings.screen_height), bkg);
        Transform t = Transform.createTranslateTransform(-cam_x, -cam_y);
        g.texture(shape.transform(t), img, false);
        
        for (int i=1;i<road_points.size();i++) {
            g.setColor(Color.blue);
            Point p1 = road_points.get(i-1);
            Point p2 = road_points.get(i);
            g.drawLine(p1.x-cam_x,p1.y-cam_y , p2.x-cam_x,p2.y-cam_y);
            
            g.setColor(Color.red);
            Point e1 = p2.polarOffset((float) (road_points.get(i).angle_of_next+(Math.PI/2)), road_width);
            Point e2 = p2.polarOffset((float) (road_points.get(i).angle_of_next-(Math.PI/2)), road_width);
            g.drawLine(p2.x-cam_x,p2.y-cam_y, e1.x-cam_x,e1.y-cam_y);
            g.drawLine(p2.x-cam_x,p2.y-cam_y, e2.x-cam_x,e2.y-cam_y);
            g.fillRect(e1.x-cam_x,e1.y-cam_y, 4,4);
            g.fillRect(e2.x-cam_x,e2.y-cam_y, 4,4);
            g.setColor(Color.yellow);
            g.drawLine(p1.x-cam_x,p1.y-cam_y, e1.x-cam_x,e1.y-cam_y);
            g.drawLine(p1.x-cam_x,p1.y-cam_y, e2.x-cam_x,e2.y-cam_y);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (container.getInput().isKeyPressed(Input.KEY_RETURN))
            game.enterState(PlayingState.ID);
        
        if (container.getInput().isKeyPressed(Input.KEY_SPACE))
            generatePoly();
        
        if (container.getInput().isKeyDown(Input.KEY_UP))
            cam_y-=3;
        
        if (container.getInput().isKeyDown(Input.KEY_DOWN))
            cam_y+=3;
        
        if (container.getInput().isKeyDown(Input.KEY_LEFT))
            cam_x-=3;
        
        if (container.getInput().isKeyDown(Input.KEY_RIGHT))
            cam_x+=3;
    }
    
}
