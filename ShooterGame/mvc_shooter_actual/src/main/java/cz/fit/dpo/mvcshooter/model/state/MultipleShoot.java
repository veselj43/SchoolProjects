package cz.fit.dpo.mvcshooter.model.state;

import cz.fit.dpo.mvcshooter.model.Config;
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
public class MultipleShoot implements ShootingState {

    @Override
    public List<Missile> fireMissile(Cannon c, AbsFactory f) {
        List<Missile> created = new ArrayList();
        int n = 1;
        for (int k = -n; k <= n; k++) {
            created.add(f.createMissile(c.getPos(), c.getAngle() + k * Config.MULTIPLE_ANGLE_STEP, c.getForce()));
        }
        return created;
    }
    
    @Override
    public CannonMode getCannonMode() {
        return CannonMode.MULTIPLE;
    }

    @Override
    public ShootingState copy() {
        return new MultipleShoot();
    }

}
