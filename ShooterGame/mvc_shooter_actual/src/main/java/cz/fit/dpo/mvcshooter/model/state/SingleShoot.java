package cz.fit.dpo.mvcshooter.model.state;

import cz.fit.dpo.mvcshooter.model.enums.CannonMode;
import cz.fit.dpo.mvcshooter.model.factory.AbsFactory;
import cz.fit.dpo.mvcshooter.model.gameobjects.Cannon;
import cz.fit.dpo.mvcshooter.model.gameobjects.Missile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jaroslav Vesely
 */
public class SingleShoot implements ShootingState {

    @Override
    public List<Missile> fireMissile(Cannon c, AbsFactory f) {
        List<Missile> created = new ArrayList();
        created.add(f.createMissile(c.getPos(), c.getAngle(), c.getForce()));
        return created;
    }
    
    @Override
    public CannonMode getCannonMode() {
        return CannonMode.SINGLE;
    }

    @Override
    public ShootingState copy() {
        return new SingleShoot();
    }
    
}
