/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.states;

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

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        gameMap = new GameMap (container, 100, 100);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        gameMap.render(container, game, g);
        
        if (Consts.RENDER_DEBUG) {
            int y = 32;
            g.setColor(Color.white);
            
            g.drawString("Debug Mode", 0, y); y+=24;
            if (Consts.RENDER_DEBUG_CAMERA_INFO)
                g.drawString("Camera: "+String.format("% 05d",gameMap.camera.x)+","+String.format("% 05d",gameMap.camera.y)+"("+String.format("%.2f",gameMap.camera.zoom)+"z)", 0, y); y+=24;
            if (Consts.RENDER_DEBUG_RENDERED_HEXES)
                g.drawString("Rendered hexes: "+gameMap.grid.counter, 0, y); y+=24;
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        gameMap.update(container, game);
        
        if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
            gameMap.camera.x--;
        }
        if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
            gameMap.camera.x++;
        }
        if (container.getInput().isKeyDown(Input.KEY_UP)) {
            gameMap.camera.y--;
        }
        if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
            gameMap.camera.y++;
        }
    }
    
}
