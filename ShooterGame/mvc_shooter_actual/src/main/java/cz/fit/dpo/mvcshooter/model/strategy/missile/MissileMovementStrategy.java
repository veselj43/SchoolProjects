package cz.fit.dpo.mvcshooter.model.strategy.missile;

import cz.fit.dpo.mvcshooter.model.gameobjects.Missile;
import cz.fit.dpo.mvcshooter.model.movement.Vector;

/**
 *
 * @author Jaroslav Vesely
 */
public interface MissileMovementStrategy {
    public Vector refreshVector(double g, Missile o);
}
