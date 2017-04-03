/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

import main.ResMgr;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author emil.simon
 */
public class Camera {
    private int click_x, click_y;
    private int freeze_x, freeze_y;
    
    public int x, y;
    public float zoom;
    public boolean grip;
    
    public Camera (GameContainer gc) {
        x = 0;
        y = 0;
        zoom = 1f;
        grip = false;
        
        gc.getInput().addMouseListener(new MouseListener () {
            @Override
            public void mouseWheelMoved(int i) {
                zoom += i*0.0001f;
            }
            @Override
            public void mouseClicked(int i, int i1, int i2, int i3) { }
            @Override
            public void mousePressed(int i, int i1, int i2) { }
            @Override
            public void mouseReleased(int i, int i1, int i2) { }
            @Override
            public void mouseMoved(int i, int i1, int i2, int i3) { }
            @Override
            public void mouseDragged(int i, int i1, int i2, int i3) { }
            @Override
            public void setInput(Input input) { }
            @Override
            public boolean isAcceptingInput() { return true; }
            @Override
            public void inputEnded() { }
            @Override
            public void inputStarted() { }
        });
    }
    
    public void update (Camera cam, GameContainer gc, StateBasedGame sbg) {
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
            this.x = (int) (freeze_x + (click_x - gc.getInput().getMouseX())/zoom);
            this.y = (int) (freeze_y + (click_y - gc.getInput().getMouseY())/zoom);
        }
    }
}
