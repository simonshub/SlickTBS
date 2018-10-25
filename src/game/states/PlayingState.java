/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.states;

import game.data.world.map.Camera;
import game.data.world.map.GameMap;
import main.Consts;
import main.Settings;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.utils.SlickUtils;

/**
 *
 * @author emil.simon
 */
public class PlayingState extends BasicGameState {
    public static final int ID = 1;
    
    public static String loadLabel;
    public static boolean isLoading;
    
    public static boolean is_night=false;
    public static float night_overlay=0f;
    
    public GameMap gameMap;
    public Camera camera;
    
    
    
    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        camera = new Camera (container);
        isLoading = true;
        loadLabel = "";
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        if (isLoading) {
            g.drawString(loadLabel, (container.getWidth()/2) - (g.getFont().getWidth(loadLabel)/2), (container.getHeight()/2) - (g.getFont().getHeight(loadLabel)/2));
        } else {
            gameMap.render(camera, container, game, g);
            
            // draw night-time overlay
            g.setColor(new Color (0f, 0f, .5f, night_overlay));
            g.fillRect(0, 0, container.getWidth(), container.getHeight());

            if (Settings.debug_mode) {
                int y = 32;
                g.setColor(Color.white);

                g.drawString("Debug Mode", 0, y); y+=24;
                if (Settings.debug_mode) {
                    g.drawString("Camera: "+String.format("% 05d",camera.x)+","+String.format("% 05d",camera.y)+"("+String.format("%.2f",camera.zoom)+"z)", 0, y); y+=24;
                    g.drawString("Rendered hexes: "+gameMap.grid.render_counter, 0, y); y+=24;
                }
                if (gameMap.debug_hex != null) {
                    g.drawString("Hex: "+gameMap.debug_hex.x+","+gameMap.debug_hex.y, 0, y); y+=24;
                    g.drawString("Type: "+gameMap.debug_hex.terrain.name(), 0, y); y+=24;
                    if (gameMap.debug_hex.continent != null) {
                         y+=24;
                        g.drawString("Continent: "+gameMap.debug_hex.continent.name+" ("+SlickUtils.Strings.beautifyString(gameMap.debug_hex.continent.continent_type.name())+")", 0, y); y+=24;
                        g.drawString("Ht/Wt/Cr: "+gameMap.debug_hex.continent.getColor().r+"/"+gameMap.debug_hex.continent.getColor().g+"/"+gameMap.debug_hex.continent.getColor().b, 0, y); y+=24;
                    }
                }
            }
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (gameMap==null) {
            this.gameMap = new GameMap (GameMap.MapSize.MEDIUM, 100);
            return;
        }
        
        gameMap.update(camera, container, game);
        
        if (is_night && night_overlay<0.3f) {
            night_overlay = Math.min(night_overlay + (delta/10000f), 0.3f);
        } else if (!is_night && night_overlay>0f) {
            night_overlay = Math.max(night_overlay - (delta/10000f), 0f);
        }
        
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
            // MAP GENERATOR TEST
            gameMap = new GameMap (GameMap.MapSize.MEDIUM, new Random().nextInt());
        }
        
        if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
            // MAP GENERATOR TEST
            gameMap = new GameMap (GameMap.MapSize.MEDIUM, 100);
        }
        
        if (container.getInput().isKeyPressed(Input.KEY_S)) {
            // MAP GENERATOR TEST
            gameMap.save("save");
        }
        
        if (container.getInput().isKeyPressed(Input.KEY_X)) {
            // NIGHT TIME TEST
            is_night = !is_night;
        }
        
        if (container.getInput().isKeyPressed(Input.KEY_N)) {
            // NAME GENERATOR TEST
        }
    }
    
}
