package cz.fit.dpo.mvcshooter.controller;

import cz.fit.dpo.mvcshooter.model.Model;
import cz.fit.dpo.mvcshooter.model.memento.Caretaker;
import cz.fit.dpo.mvcshooter.view.Canvas;
import java.awt.event.KeyEvent;

/**
 *
 * @author Jaroslav Vesely
 */
public class EventController {
    protected Model model;
    protected Canvas view;
    protected Caretaker caretaker;
    
    public EventController(Model m, Canvas v) {
        model = m;
        view = v;
        caretaker = new Caretaker();
    }
    
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        // events available in both paused or running states
        switch (keyCode) {
            case KeyEvent.VK_H:
                view.showHelp();
                break;
            case KeyEvent.VK_K:
                caretaker.saveState(model);
                break;
            case KeyEvent.VK_L:
                caretaker.loadState(model);
                break;
        }
        
        if (model.isPaused()) {
            if (keyCode == KeyEvent.VK_M) model.resume();
        }
        else {
            switch (keyCode) {
                case KeyEvent.VK_SPACE:
                    model.fire();
                    break;
                case KeyEvent.VK_UP:
                    model.aimUp();
                    break;
                case KeyEvent.VK_DOWN:
                    model.aimDown();
                    break;
                case KeyEvent.VK_LEFT:
                    model.forceDown();
                    break;
                case KeyEvent.VK_RIGHT:
                    model.forceUp();
                    break;
                case KeyEvent.VK_W:
                    model.cannonUp();
                    break;
                case KeyEvent.VK_S:
                    model.cannonDown();
                    break;
                case KeyEvent.VK_G:
                    model.gravityUp();
                    break;
                case KeyEvent.VK_T:
                    model.gravityDown();
                    break;
                case KeyEvent.VK_P:
                    model.toggleGameMode();
                    break;
                case KeyEvent.VK_O:
                    model.toggleCannonMode();
                    break;
                case KeyEvent.VK_N:
                    model.pause();
                    break;
            }
        }
    }
}
