package cz.fit.dpo.mvcshooter.model.state;

import cz.fit.dpo.mvcshooter.model.enums.CannonMode;
import cz.fit.dpo.mvcshooter.model.factory.AbsFactory;
import cz.fit.dpo.mvcshooter.model.gameobjects.Cannon;
import cz.fit.dpo.mvcshooter.model.gameobjects.Missile;
import java.util.List;

/**
 *
 * @author Jaroslav Vesely
 */
public interface ShootingState {
    public List<Missile> fireMissile(Cannon c, AbsFactory f);
    
    public CannonMode getCannonMode();
    
    public ShootingState copy();
}
