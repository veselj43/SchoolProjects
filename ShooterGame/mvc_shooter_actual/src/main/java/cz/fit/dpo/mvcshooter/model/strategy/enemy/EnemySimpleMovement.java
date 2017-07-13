package cz.fit.dpo.mvcshooter.model.strategy.enemy;

import cz.fit.dpo.mvcshooter.model.gameobjects.Enemy;
import cz.fit.dpo.mvcshooter.model.movement.Vector;

/**
 *
 * @author Jaroslav Vesely
 */
public class EnemySimpleMovement implements EnemyMovementStrategy {
    @Override
    public Vector refreshVector(double g, Enemy o) {
        double rad = Math.toRadians(o.getAngle());
        
        return new Vector(
            (o.getForce() * (double)o.getTick() * Math.cos(rad)),
            -(Math.sin(rad)*o.getForce()*o.getTick())
        );
    }
}
