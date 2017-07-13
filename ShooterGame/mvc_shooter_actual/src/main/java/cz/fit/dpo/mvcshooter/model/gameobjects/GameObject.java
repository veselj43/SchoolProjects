package cz.fit.dpo.mvcshooter.model.gameobjects;

import cz.fit.dpo.mvcshooter.model.Config;
import cz.fit.dpo.mvcshooter.model.DrawElement;
import cz.fit.dpo.mvcshooter.model.movement.Point;
import cz.fit.dpo.mvcshooter.model.movement.Vector;

/**
 *
 * @author Jaroslav Vesely
 */
abstract public class GameObject implements DrawElement {
    protected long tick = 0;
    protected int life = -1;
    protected double angle = 0;
    protected double force = 0;
    protected Point p0;
    protected Point pos;
    protected Vector vec;
    
    public GameObject() {
        p0 = pos = new Point();
        vec = new Vector();
    }
    
    public GameObject(Point p) {
        p0 = new Point(p);
        pos = p0;
        vec = new Vector();
    }
    
    public GameObject(Point p, double a, double f) {
        p0 = new Point(p);
        pos = p0;
        vec = new Vector();
        angle = a;
        force = f;
    }
    
    public GameObject(GameObject o) {
        tick = o.getTick();
        life = o.getLife();
        angle = o.getAngle();
        force = o.getForce();
        p0 = new Point(o.getP0());
        pos = new Point(o.getPos());
        vec = new Vector(o.getVec());
    }
    
    public void nextMotion() {
        if (life > 0) life--;
    }

    public boolean isGarbage() {
        return life == 0;
    }
    
    public boolean isVisible() {
        return !(pos.x < 0 || pos.y < 0 || pos.x > Config.CANVAS_WIDTH || pos.y > Config.CANVAS_HEIGHT);
    }
    
    public boolean collide(GameObject o) {
        return (
            Math.abs(getX() - o.getX()) < Config.COLLISION_MARGIN && 
            Math.abs(getY() - o.getY()) < Config.COLLISION_MARGIN
        );
    }
    
    public void setPos(Point p) {
        pos = p;
    }
    
    public void setGarbage() {
        life = 0;
    }
    
    public Point getP0() {
        return p0;
    }
    
    public Point getPos() {
        return pos;
    }
    
    public Vector getVec() {
        return vec;
    }
    
    public int getX() {
        return (int)pos.x;
    }
    
    public int getY() {
        return (int)pos.y;
    }

    public double getAngle() {
        return angle;
    }

    public double getForce() {
        return force;
    }

    public long getTick() {
        return tick;
    }

    public int getLife() {
        return life;
    }
    
    abstract public GameObject copy();
    
}
