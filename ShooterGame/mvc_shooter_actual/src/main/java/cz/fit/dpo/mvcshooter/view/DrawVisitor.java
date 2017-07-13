package cz.fit.dpo.mvcshooter.view;

import cz.fit.dpo.mvcshooter.model.gameobjects.Cannon;
import cz.fit.dpo.mvcshooter.model.gameobjects.Collision;
import cz.fit.dpo.mvcshooter.model.gameobjects.Enemy;
import cz.fit.dpo.mvcshooter.model.gameobjects.Missile;
import cz.fit.dpo.mvcshooter.model.gameobjects.ModelInfo;

/**
 *
 * @author Jaroslav Vesely
 */
public interface DrawVisitor {
    public void draw(Cannon cannon);
    public void draw(Missile missile);
    public void draw(Enemy enemy);
    public void draw(Collision collision);
    public void draw(ModelInfo modelinfo);
}
