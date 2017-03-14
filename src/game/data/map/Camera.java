/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author emil.simon
 */
public class Camera {
    private int click_x, click_y;
    private int freeze_x, freeze_y;
    
    public int x, y;
    public boolean grip;
    
    public Camera () {
        x = 0;
        y = 0;
        grip = false;
    }
    
    public void update (GameContainer gc, StateBasedGame sbg) {
        if (!gc.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON) && grip) {
            grip = false;
            click_x = 0;
            click_y = 0;
            freeze_x = 0;
            freeze_y = 0;
        }
        
        if (gc.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON) && !grip) {
            grip = true;
            click_x = gc.getInput().getMouseX();
            click_y = gc.getInput().getMouseY();
            freeze_x = this.x;
            freeze_y = this.y;
        }
        
        if (grip && gc.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
            this.x = freeze_x + click_x - gc.getInput().getMouseX();
            this.y = freeze_y + click_y - gc.getInput().getMouseY();
        }
    }
}
