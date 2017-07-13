package cz.fit.dpo.mvcshooter.model.memento;

import cz.fit.dpo.mvcshooter.model.Model;
import cz.fit.dpo.mvcshooter.model.factory.AbsFactory;
import cz.fit.dpo.mvcshooter.model.gameobjects.*;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Jaroslav Vesely
 */
public class Memento {
    protected double gravity;
    
    private final ModelInfo modelInfo;
    private final Cannon cannon;
    private final List<Missile> missiles;
    private final List<Enemy> enemies;
    private final List<Collision> collisions;
    
    private final AbsFactory factory;
    
    public Memento(Model m) {
        gravity = m.getGravity();
        
        modelInfo = m.getModelInfo().copy();
        cannon = m.getCannon().copy();
        
        missiles = new ArrayList();
        for (Missile o : m.getMissiles()) 
            missiles.add(o.copy());
        
        enemies = new ArrayList();
        for (Enemy o : m.getEnemies()) 
            enemies.add(o.copy());
        
        collisions = new ArrayList();
        for (Collision o : m.getCollisions()) 
            collisions.add(o.copy());
        
        factory = m.getFactory().copy();
    }

    public double getGravity() {
        return gravity;
    }

    public ModelInfo getModelInfo() {
        return modelInfo.copy();
    }

    public Cannon getCannon() {
        return cannon.copy();
    }
    
    public List<Missile> getMissiles() {
        List<Missile> newMissiles = new ArrayList();
        for (Missile o : missiles) 
            newMissiles.add(o.copy());
        return newMissiles;
    }
    
    public List<Enemy> getEnemies() {
        List<Enemy> newEnemies = new ArrayList();
        for (Enemy o : enemies) 
            newEnemies.add(o.copy());
        return newEnemies;
    }

    public List<Collision> getCollisions() {
        List<Collision> newCollisions = new ArrayList();
        for (Collision o : collisions) 
            newCollisions.add(o.copy());
        return newCollisions;
    }

    public AbsFactory getFactory() {
        return factory.copy();
    }
    
}
