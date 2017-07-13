package cz.fit.dpo.mvcshooter.model.gameobjects;

import cz.fit.dpo.mvcshooter.model.Config;
import cz.fit.dpo.mvcshooter.model.movement.Point;
import cz.fit.dpo.mvcshooter.model.movement.Vector;
import cz.fit.dpo.mvcshooter.view.DrawVisitor;

/**
 *
 * @author Jaroslav Vesely
 */
public class Collision extends GameObject {
    
    public Collision(Enemy e) {
        pos = new Point(e.getPos());
        vec = new Vector();
        life = Config.COLLISION_LIFE;
    }
    
    public Collision(Collision c) {
        super(c);
    }
    
    @Override
    public Collision copy() {
        return new Collision(this);
    }

    @Override
    public void accept(DrawVisitor v) {
        v.draw(this);
    }

}
