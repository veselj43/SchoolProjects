package cz.fit.dpo.mvcshooter.model.gameobjects;

import cz.fit.dpo.mvcshooter.model.Config;
import cz.fit.dpo.mvcshooter.model.movement.Point;
import cz.fit.dpo.mvcshooter.model.movement.Vector;
import cz.fit.dpo.mvcshooter.view.DrawVisitor;
import cz.fit.dpo.mvcshooter.model.strategy.missile.MissileMovementStrategy;

/**
 *
 * @author Jaroslav Vesely
 */
public class Missile extends GameObject {
    protected MissileMovementStrategy strategy;
    protected Vector v0;
    protected double gravity = 0;
    protected boolean visible = true;
    
    public Missile(Point p, double a, double f, MissileMovementStrategy s) {
        super(p, a ,f);
        strategy = s;
        initVec();
    }
    
    public Missile(Missile m) {
        super(m);
        strategy = m.getStrategy();
        v0 = new Vector(m.getV0());
        gravity = m.getGravity();
        visible = m.getVisible();
    }
    
    @Override
    public Missile copy() {
        return new Missile(this);
    }
    
    protected void initVec() {
        double rad = Math.toRadians(angle);
        v0 = new Vector(
            force * Math.cos(rad),
            force * Math.sin(rad)
        );
    }

    @Override
    public void accept(DrawVisitor v) {
        v.draw(this);
    }
    
    @Override
    public void nextMotion() {
        super.nextMotion();
        vec = strategy.refreshVector(Config.DEFAULT_GRAVITY, this);
        pos = p0.add(vec);
        tick++;
    }
    
    public void nextMotion(double g) {
        super.nextMotion();
        if (g != gravity) {
            gravityChange(gravity);
            gravity = g;
        }
        vec = strategy.refreshVector(g, this);
        pos = p0.add(vec);
        tick++;
    }

    @Override
    public boolean isGarbage() {
        if (isVisible()) {
            visible = true;
            if (life > 0) life = -1;
        }
        else if (visible) {
            visible = false;
            life = Config.INVISIBLE_LIFE;
        }
        return super.isGarbage();
    }
    
    public void gravityChange(double gFrom) {
        v0 = new Vector(
            v0.x,
            v0.y - (gFrom * tick * Config.TIME_SCALE)
        );
        p0 = new Point(pos);
        tick = 0;
    }
    
    public Vector getV0() {
        return v0;
    }

    public MissileMovementStrategy getStrategy() {
        return strategy;
    }

    public double getGravity() {
        return gravity;
    }
    
    public boolean getVisible() {
        return visible;
    }
    
}
