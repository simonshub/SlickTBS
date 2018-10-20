/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import game.states.MenuState;
import game.states.PlayingState;
import java.io.File;
import java.io.IOException;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.utils.Log;

/**
 *
 * @author emil.simon
 */
public class SlickTBS extends StateBasedGame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Log.log("Starting ...");
        File file = new File ("natives");
        if (file.exists()) {
            switch(LWJGLUtil.getPlatform()) {
                case LWJGLUtil.PLATFORM_WINDOWS:
                    file = new File("native/windows/");
                    break;
                case LWJGLUtil.PLATFORM_LINUX:
                    file = new File("native/linux/");
                    break;
                case LWJGLUtil.PLATFORM_MACOSX:
                    file = new File("native/macosx/");
                    break;
                default:
                    file = new File("native/windows/");
                    break;
            }
            
            Log.log("LWJGL Natives : '"+file.getAbsolutePath()+"'");
            System.setProperty("org.lwjgl.librarypath",file.getAbsolutePath());
        }

        try {
            AppGameContainer agc = new AppGameContainer (new SlickTBS (ResMgr.title));
            agc.setDisplayMode (ResMgr.screen_res_w, ResMgr.screen_res_h, false);
            agc.setTargetFrameRate(60);
            
            agc.start();
        } catch (SlickException ex) {
            Log.err("Couldn't start the game!");
            Log.err(ex);
            System.exit(-1);
        }
    }

    public SlickTBS(String name) {
        super(name);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        try {
            ResMgr.init();
            AppGameContainer app = (AppGameContainer) this.getContainer();
            app.setTitle(ResMgr.title);
        } catch (IOException | IllegalArgumentException | IllegalAccessException ex) {
            Log.err("Couldn't start the game!");
            Log.err(ex);
            System.exit(-1);
        }
            
//        this.addState(new MenuState ());
        this.addState(new PlayingState ());
        
        this.enterState(PlayingState.ID);
    }
    
}
