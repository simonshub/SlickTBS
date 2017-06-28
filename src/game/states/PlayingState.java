/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.states;

import game.data.game.Faction;
import game.data.game.NameGenerator;
import game.data.game.RaceEnum;
import game.data.map.Camera;
import game.data.map.DifficultyLevel;
import game.data.map.GameMap;
import main.Consts;
import main.utils.Log;
import main.utils.SlickUtils;
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
    
    public static String loadLabel;
    public static boolean isLoading;
    
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

            if (Consts.RENDER_DEBUG) {
                int y = 32;
                g.setColor(Color.white);

                g.drawString("Debug Mode", 0, y); y+=24;
                if (Consts.RENDER_DEBUG_CAMERA_INFO) {
                    g.drawString("Camera: "+String.format("% 05d",camera.x)+","+String.format("% 05d",camera.y)+"("+String.format("%.2f",camera.zoom)+"z)", 0, y); y+=24;
                }
                if (Consts.RENDER_DEBUG_RENDERED_HEXES) {
                    g.drawString("Rendered hexes: "+gameMap.grid.render_counter, 0, y); y+=24;
    //                g.drawString("Not rendered hexes: "+gameMap.grid.not_render_counter, 0, y); y+=24;
                }
                if (gameMap.debug_hex != null) {
                    g.drawString("Hex: "+gameMap.debug_hex.x+","+gameMap.debug_hex.y, 0, y); y+=24;
                    g.drawString("Type: "+gameMap.debug_hex.terrain.name(), 0, y); y+=24;
                    g.drawString("Military Land Value: "+Faction.getLandValue(gameMap.grid, gameMap.debug_hex, true), 0, y); y+=24;
                    g.drawString("Civilian Land Value: "+Faction.getLandValue(gameMap.grid, gameMap.debug_hex, false), 0, y); y+=24;
                    if (gameMap.debug_hex.continent != null) {
                         y+=24;
                        g.drawString("Continent: "+gameMap.debug_hex.continent.name+" ("+SlickUtils.beautifyString(gameMap.debug_hex.continent.continent_type.name())+")", 0, y); y+=24;
                        g.drawString("Ht/Wt/Cr: "+gameMap.debug_hex.continent.getColor().r+"/"+gameMap.debug_hex.continent.getColor().g+"/"+gameMap.debug_hex.continent.getColor().b, 0, y); y+=24;
                    }
                    if (gameMap.debug_hex.poi != null) {
                         y+=24;
                        g.drawString("Point of Interest: "+gameMap.debug_hex.poi.name+((gameMap.debug_hex.poi.parent!=null) ? " ("+gameMap.debug_hex.poi.parent.name+")" : ""), 0, y); y+=24;
                        g.drawString("Description: "+gameMap.debug_hex.poi.description, 0, y); y+=24;
                    }
                    if (gameMap.debug_hex.owner != null) {
                         y+=24;
                        g.drawString("Owner: "+gameMap.debug_hex.owner.name, 0, y); y+=24;
                        g.drawString("Race: "+gameMap.debug_hex.owner.race.name, 0, y); y+=24;
                        g.drawString("Type: "+gameMap.debug_hex.owner.type.name(), 0, y); y+=24;
                        g.drawString("Settlements: "+gameMap.debug_hex.owner.settlements.size(), 0, y); y+=24;
                        g.drawString("Civilian: "+gameMap.debug_hex.owner.getCivilianSettlements().size()+" | Military: "+gameMap.debug_hex.owner.getMilitarySettlements().size(), 24, y); y+=24;
                        g.drawString("Owner Territory: "+gameMap.debug_hex.owner.territory.size(), 0, y); y+=24;
                    }
                }
            }
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (gameMap==null) {
            this.gameMap = new GameMap (GameMap.MapSize.MEDIUM, DifficultyLevel.NORMAL);
            return;
        }
        
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
            gameMap = new GameMap (GameMap.MapSize.MEDIUM, DifficultyLevel.NORMAL);
        }
        
        if (container.getInput().isKeyPressed(Input.KEY_N)) {
            String[] names = new String [10];
            String[] places = new String [10];
            
            for (int i=0;i<10;i++) {
                RaceEnum race = RaceEnum.random();
                boolean female = Math.random()>=0.5;
                names[i] = race.name+", "+(female?"female":"male")+" : "+NameGenerator.character(race, female);
            }
            for (int i=0;i<10;i++) {
                RaceEnum race = RaceEnum.random();
                places[i] = race.name+" : "+NameGenerator.place(race);
            }
            
            Log.log("Character name examples:\n\t"+SlickUtils.getArrayAsStringList(names, "\n\t"));
            Log.log("Place name examples:\n\t"+SlickUtils.getArrayAsStringList(places, "\n\t"));
        }
    }
    
}
