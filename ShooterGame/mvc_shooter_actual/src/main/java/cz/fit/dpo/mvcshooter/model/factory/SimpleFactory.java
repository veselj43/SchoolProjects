package cz.fit.dpo.mvcshooter.model.factory;

import cz.fit.dpo.mvcshooter.model.enums.GameMode;
import cz.fit.dpo.mvcshooter.model.gameobjects.Enemy;
import cz.fit.dpo.mvcshooter.model.gameobjects.Missile;
import cz.fit.dpo.mvcshooter.model.movement.Point;
import cz.fit.dpo.mvcshooter.model.strategy.missile.MissileSimpleMovement;

/**
 *
 * @author Jaroslav Vesely
 */
public class SimpleFactory extends AbsFactory {

    @Override
    public Missile createMissile(Point p, double a, double f) {
        return new Missile(p, a, f, new MissileSimpleMovement());
    }

    @Override
    public Enemy createEnemy() {
        return new Enemy();
    }
    
    @Override
    public GameMode getGameMode() {
        return GameMode.SIMPLE;
    }
    
    @Override
    public AbsFactory copy() {
        return new SimpleFactory();
    }

}
