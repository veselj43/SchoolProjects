package cz.fit.dpo.mvcshooter.model.strategy.enemy;

import cz.fit.dpo.mvcshooter.model.gameobjects.Enemy;
import cz.fit.dpo.mvcshooter.model.movement.Vector;

/**
 *
 * @author Jaroslav Vesely
 */
public class EnemyRealMovement implements EnemyMovementStrategy {
    @Override
    public Vector refreshVector(double g, Enemy o) {
        double rad = Math.toRadians(o.getAngle());

        double time = o.getTick() * 0.6;
        
        return new Vector(
            (int)(o.getForce() * time * Math.cos(rad)),
            -(int)(Math.sin(rad) * time)
        );
    }
}
