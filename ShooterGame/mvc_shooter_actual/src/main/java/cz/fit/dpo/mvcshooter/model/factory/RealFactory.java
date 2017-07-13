package cz.fit.dpo.mvcshooter.model.factory;

import cz.fit.dpo.mvcshooter.model.enums.GameMode;
import cz.fit.dpo.mvcshooter.model.gameobjects.Enemy;
import cz.fit.dpo.mvcshooter.model.gameobjects.Missile;
import cz.fit.dpo.mvcshooter.model.gameobjects.RealEnemy;
import cz.fit.dpo.mvcshooter.model.movement.Point;
import cz.fit.dpo.mvcshooter.model.strategy.missile.MissileRealMovement;

/**
 *
 * @author Jaroslav Vesely
 */
public class RealFactory extends AbsFactory {

    @Override
    public Missile createMissile(Point p, double a, double f) {
        return new Missile(p, a, f, new MissileRealMovement());
    }

    @Override
    public Enemy createEnemy() {
        return new RealEnemy();
    }
    
    @Override
    public GameMode getGameMode() {
        return GameMode.REALISTIC;
    }
    
    @Override
    public AbsFactory copy() {
        return new RealFactory();
    }
    
}
