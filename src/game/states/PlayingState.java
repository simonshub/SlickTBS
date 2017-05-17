/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.states;

import game.data.map.Camera;
import game.data.map.GameMap;
import main.Consts;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author emil.simon
 */
public class PlayingState extends BasicGameState {
    public static final int ID = 1;
    
    public GameMap gameMap;
    public Camera camera;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        camera = new Camera (container);
        gameMap = new GameMap (80, 80);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        gameMap.render(camera, container, game, g);
        
        if (Consts.RENDER_DEBUG) {
            int y = 32;
            g.setColor(Color.white);
            
            g.drawString("Debug Mode", 0, y); y+=24;
            if (Consts.RENDER_DEBUG_CAMERA_INFO) {
                g.drawString("Camera: "+String.format("% 05d",camera.x)+","+String.format("% 05d",camera.y)+"("+String.format("%.2f",camera.zoom)+"z)", 0, y); y+=24;
            }
            if (Consts.RENDER_DEBUG_RENDERED_HEXES) {
                g.drawString("Rendered hexes: "+gameMap.grid.render_counter, 0, y); y+=24;
                g.drawString("Not rendered hexes: "+gameMap.grid.not_render_counter, 0, y); y+=24;
            }
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        gameMap.update(camera, container, game);
        
        if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
            camera.x--;
        }
        if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
            camera.x++;
        }
        if (container.getInput().isKeyDown(Input.KEY_UP)) {
            camera.y--;
        }
        if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
            camera.y++;
        }
        
        if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            gameMap = new GameMap (32, 32);
        }
    }
    
}
