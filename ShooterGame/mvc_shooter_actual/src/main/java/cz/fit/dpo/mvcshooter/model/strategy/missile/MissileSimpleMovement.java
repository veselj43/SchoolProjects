package cz.fit.dpo.mvcshooter.model.strategy.missile;

import cz.fit.dpo.mvcshooter.model.gameobjects.Missile;
import cz.fit.dpo.mvcshooter.model.movement.Vector;

/**
 *
 * @author Jaroslav Vesely
 */
public class MissileSimpleMovement implements MissileMovementStrategy {
    @Override
    public Vector refreshVector(double g, Missile o) {
        double rad = Math.toRadians(o.getAngle());
        
        return new Vector(
            (o.getForce() * (double)o.getTick() * Math.cos(rad)),
            -(Math.sin(rad)*o.getForce()*o.getTick())
        );
    }
}
