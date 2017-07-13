package cz.fit.dpo.mvcshooter.model.movement;

/**
 *
 * @author Jaroslav Vesely
 */
abstract public class Abs2DGeometry {
    public double x;
    public double y;
    
    public Abs2DGeometry() {
        this.x = 0;
        this.y = 0;
    }

    public Abs2DGeometry(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    @Override
    public String toString() {
        return x + ", " + y;
    }
}
