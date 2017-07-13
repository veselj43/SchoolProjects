package cz.fit.dpo.mvcshooter.model.gameobjects;

import cz.fit.dpo.mvcshooter.model.Config;
import cz.fit.dpo.mvcshooter.model.factory.AbsFactory;
import cz.fit.dpo.mvcshooter.model.movement.Point;
import cz.fit.dpo.mvcshooter.model.movement.Vector;
import cz.fit.dpo.mvcshooter.model.state.*;
import cz.fit.dpo.mvcshooter.view.DrawVisitor;
import java.util.List;

/**
 *
 * @author Jaroslav Vesely
 */
public class Cannon extends GameObject {
    protected ShootingState state;
    
    public Cannon() {
        pos = new Point(20, 225);
        vec = new Vector();
        force = (Config.MIN_FORCE + Config.MAX_FORCE) / 2;
        force -= force % Config.FORCE_STEP;
        setState(new SingleShoot());
    }

    public Cannon(Cannon c) {
        super(c);
        setState(c.getState().copy());
    }
    
    @Override
    public Cannon copy() {
        return new Cannon(this);
    }

    public List<Missile> fire(AbsFactory f) {
        return state.fireMissile(this, f);
    }
    
    @Override
    public void accept(DrawVisitor v) {
        v.draw(this);
    }
    
    public void aimUp() {
        if (angle < Config.MAX_ANGLE) 
            angle += Config.ANGLE_STEP;
    }
    
    public void aimDown() {
        if (angle > -Config.MAX_ANGLE)
            angle -= Config.ANGLE_STEP;
    }
    
    public void forceUp() {
        if (force < Config.MAX_FORCE)
            force += Config.FORCE_STEP;
    }
    
    public void forceDown() {
        if (force > Config.MIN_FORCE)
            force -= Config.FORCE_STEP;
        
    }
    
    public void moveUp() {
        pos.y -= Config.MOVE_STEP;
        if (pos.y < 0 + Config.ENEMY_MARGIN_U) pos.y = 0 + Config.ENEMY_MARGIN_U;
    }
    
    public void moveDown() {
        pos.y += Config.MOVE_STEP;
        if (pos.y > Config.CANVAS_HEIGHT - Config.ENEMY_MARGIN_D) pos.y = Config.CANVAS_HEIGHT - Config.ENEMY_MARGIN_D;
    }
    
    public void setState(ShootingState s) {
        state = s;
    }
    
    @Override
    public String toString() {
        return "Mode: " + state.getCannonMode().toString() + ", Angle: " + angle + ", Force: " + force;
    }

    public ShootingState getState() {
        return state;
    }

}
