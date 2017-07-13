package cz.fit.dpo.mvcshooter.model.movement;

import cz.fit.dpo.mvcshooter.model.gameobjects.GameObject;

/**
 *
 * @author Jaroslav Vesely
 */
public class Point extends Abs2DGeometry {

    public Point() {
        super();
    }

    public Point(double x, double y) {
        super(x, y);
    }

    public Point(Point p) {
        x = p.getX();
        y = p.getY();
    }

    public Point(GameObject o) {
        x = o.getX();
        y = o.getY();
    }
    
    public Point add(Point p) {
        return new Point(x + p.getX(), y + p.getY());
    }
    
    public Point add(Vector v) {
        return new Point(x + v.getX(), y + v.getY());
    }
    
}
