package cz.fit.dpo.mvcshooter.model.gameobjects;

import cz.fit.dpo.mvcshooter.model.Config;
import cz.fit.dpo.mvcshooter.model.movement.Point;
import cz.fit.dpo.mvcshooter.model.strategy.enemy.EnemyRealMovement;

/**
 *
 * @author Jaroslav Vesely
 */
public class RealEnemy extends Enemy {

    public RealEnemy() {
        super();
    }
    
    public RealEnemy(RealEnemy r) {
        super(r);
    }
    
    @Override
    public RealEnemy copy() {
        return new RealEnemy(this);
    }
    
    @Override
    protected void initStrategy() {
        angle = Math.random() * 360;
        force = Math.random() * 3;
        strategy = new EnemyRealMovement();
    }
    
    @Override
    public void nextMotion() {
        super.nextMotion();
        checkBounce();
    }
    
    protected void checkBounce() {
        boolean change = false;
        if (getPos().x < 0 + Config.COLLISION_MARGIN && (angle > 90 && angle < 270)) {
            angle = -angle + 180;
            change = true;
        }
        else if (getPos().x > Config.CANVAS_WIDTH - Config.COLLISION_MARGIN && (angle < 90 || angle > 270)) {
            angle = -angle + 180;
            change = true;
        }
        if (getPos().y < 0 + Config.COLLISION_MARGIN && (angle > 0 && angle < 180)) {
            angle = -angle;
            change = true;
        }
        else if (getPos().y > Config.CANVAS_HEIGHT - Config.COLLISION_MARGIN && (angle > 180 && angle < 360)) {
            angle = -angle;
            change = true;
        }
        
        if (change) {
            angle %= 360;
            if (angle < 0) angle += 360;
            p0 = new Point(pos);
            tick = 2;
        }
    }
}
