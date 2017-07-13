package cz.fit.dpo.mvcshooter.model.factory;

import cz.fit.dpo.mvcshooter.model.enums.GameMode;
import cz.fit.dpo.mvcshooter.model.gameobjects.Enemy;
import cz.fit.dpo.mvcshooter.model.gameobjects.Missile;
import cz.fit.dpo.mvcshooter.model.movement.Point;

/**
 *
 * @author Jaroslav Vesely
 */
public abstract class AbsFactory {
    
    abstract public Missile createMissile(Point p, double a, double f);
    
    abstract public Enemy createEnemy();
    
    abstract public GameMode getGameMode();
    
    abstract public AbsFactory copy();
}
