package cz.fit.dpo.mvcshooter.model.strategy.missile;

import cz.fit.dpo.mvcshooter.model.Config;
import cz.fit.dpo.mvcshooter.model.gameobjects.Missile;
import cz.fit.dpo.mvcshooter.model.movement.Vector;

/**
 *
 * @author Jaroslav Vesely
 */
public class MissileRealMovement implements MissileMovementStrategy {
    @Override
    public Vector refreshVector(double g, Missile o) {
        double time = o.getTick() * Config.TIME_SCALE;
        
        return new Vector(
            (o.getV0().x * time), 
            -((o.getV0().y * time) - (g * 0.5 * time * time))
        );
    }
}
