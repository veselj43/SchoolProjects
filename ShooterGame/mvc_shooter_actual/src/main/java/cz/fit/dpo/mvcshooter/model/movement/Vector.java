package cz.fit.dpo.mvcshooter.model.movement;

/**
 *
 * @author Jaroslav Vesely
 */
public class Vector extends Abs2DGeometry {

    public Vector() {
        super();
    }

    public Vector(double x, double y) {
        super(x, y);
    }

    public Vector(Vector v) {
        this.x = v.getX();
        this.y = v.getY();
    }

    public Vector(Point p1, Point p2) {
        this.x = p1.getX() - p2.getX();
        this.y = p1.getY() - p2.getY();
    }
    
    public Vector add(Vector v) {
        return new Vector(x + v.getX(), y + v.getY());
    }
    
}
