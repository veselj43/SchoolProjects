package cz.fit.dpo.mvcshooter.model.strategy.enemy;

import cz.fit.dpo.mvcshooter.model.gameobjects.Enemy;
import cz.fit.dpo.mvcshooter.model.movement.Vector;

/**
 *
 * @author Jaroslav Vesely
 */
public interface EnemyMovementStrategy {
    public Vector refreshVector(double gravity, Enemy o);
}
