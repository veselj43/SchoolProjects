package cz.fit.dpo.mvcshooter.model;

import cz.fit.dpo.mvcshooter.model.enums.GameMode;
import cz.fit.dpo.mvcshooter.model.enums.CannonMode;
import cz.fit.dpo.mvcshooter.model.factory.*;
import cz.fit.dpo.mvcshooter.model.gameobjects.*;
import cz.fit.dpo.mvcshooter.model.memento.Memento;
import cz.fit.dpo.mvcshooter.model.state.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.Timer;

/**
 *
 * @author Jaroslav Vesely
 */
public class Model implements Observable {
    private double gravity = Config.DEFAULT_GRAVITY;
    
    private ModelInfo modelInfo;
    private Cannon cannon;
    private List<Missile> missiles = new ArrayList();
    private List<Enemy> enemies = new ArrayList();
    private List<Collision> collisions = new ArrayList();
    
    private AbsFactory factory;
    
    private List<Observer> observers = new ArrayList();
    
    private Timer tickTimer;
    private Timer enemyTimer;
    private boolean isPaused;
    
    public Model() {
        modelInfo = new ModelInfo(this);
        cannon = new Cannon();
        setGameMode(Config.GAME_MODE);
        initTimer();
    }
    
    private void initTimer() {
        tickTimer = new Timer(Config.TICK_RATE, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextMotion();
            }
        });
        tickTimer.setRepeats(true);
        
        enemyTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEnemy();
            }
        });
        enemyTimer.setRepeats(true);
        
        tickTimer.start();
        enemyTimer.start();
        isPaused = false;
    }
    
    public void pause() {
        if (isPaused) return;
        tickTimer.stop();
        enemyTimer.stop();
        isPaused = true;
        System.out.println("Game paused.");
    }
    
    public void resume() {
        if (!isPaused) return;
        tickTimer.start();
        enemyTimer.start();
        isPaused = false;
        System.out.println("Game resumed.");
    }
    
    public Memento save() {
        return new Memento(this);
    }
    
    public void load(Memento mem) {
        gravity = mem.getGravity();
        cannon = mem.getCannon();
        modelInfo = mem.getModelInfo();
        missiles = mem.getMissiles();
        enemies = mem.getEnemies();
        collisions = mem.getCollisions();
        factory = mem.getFactory();
        
        nextMotion();
    }
    
    public void nextMotion() {
        refreshCannon();
        refreshMissiles();
        refreshEnemies();
        refreshCollisions();
        checkCollisions();
        
        notifyObservers();
    }
    
    public void refreshCannon() {
        cannon.nextMotion();
    }
    
    public void refreshMissiles() {
        ListIterator<Missile> it = missiles.listIterator();
        while (it.hasNext()) {
            Missile m = it.next();
            m.nextMotion(gravity);
            if (m.isGarbage()) {
                it.remove();
            }
        }
    }
    
    public void refreshEnemies() {
        ListIterator<Enemy> it = enemies.listIterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.nextMotion();
            if (e.isGarbage()) {
                it.remove();
            }
        }
    }
    
    public void refreshCollisions() {
        ListIterator<Collision> it = collisions.listIterator();
        while (it.hasNext()) {
            Collision c = it.next();
            c.nextMotion();
            if (c.isGarbage()) {
                it.remove();
            }
        }
    }

    private void checkCollisions() {
        for (ListIterator<Enemy> eIt = enemies.listIterator(); eIt.hasNext(); ) {
            Enemy enemy = eIt.next();
            for (ListIterator<Missile> mIt = missiles.listIterator(); mIt.hasNext(); ) {
                Missile missile = mIt.next();
                if (missile.collide(enemy)) {
                    collisions.add(new Collision(enemy));
                    modelInfo.score();
                    enemy.setGarbage();
                    missile.setGarbage();
                }
            }
        }
    }
    
    public void setGameMode(AbsFactory f) {
        factory = f;
    }
    
    public void setCannonMode(ShootingState s) {
        cannon.setState(s);
    }
    
    public void toggleGameMode() {
        setGameMode(
            (getGameMode() == GameMode.SIMPLE) ? new RealFactory() : new SimpleFactory()
        );
    }
    
    public void toggleCannonMode() {
        setCannonMode(
            (getCannonMode() == CannonMode.SINGLE) ? new MultipleShoot() : new SingleShoot()
        );
    }
    
    public void fire() {
        missiles.addAll(cannon.fire(factory));
    }
    
    public void aimUp() {
        cannon.aimUp();
    }
    
    public void aimDown() {
        cannon.aimDown();
    }
    
    public void forceUp() {
        cannon.forceUp();
    }
    
    public void forceDown() {
        cannon.forceDown();
    }
    
    public void cannonUp() {
        cannon.moveUp();
    }
    
    public void cannonDown() {
        cannon.moveDown();
    }
    
    public void gravityUp() {
        if (gravity < Config.MAX_GRAVITY)
            gravity += Config.GRAVITY_STEP;
    }
    
    public void gravityDown() {
        if (gravity > -Config.MAX_GRAVITY)
            gravity -= Config.GRAVITY_STEP;
    }

    public void addEnemy() {
        if (enemies.size() < Config.MAX_ENEMIES) {
            enemies.add(factory.createEnemy());
        }
    }
    
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
    
    public boolean isPaused() {
        return isPaused;
    }

    public double getGravity() {
        return gravity;
    }
    
    public Cannon getCannon() {
        return cannon;
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Collision> getCollisions() {
        return collisions;
    }

    public List<Observer> getObservers() {
        return observers;
    }
    
    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    public GameMode getGameMode() {
        return factory.getGameMode();
    }
    
    public CannonMode getCannonMode() {
        return cannon.getState().getCannonMode();
    }

    public AbsFactory getFactory() {
        return factory;
    }
    
    public List<GameObject> getAllGameObjects() {
        List<GameObject> gameObjects = new ArrayList();
        gameObjects.add(getCannon());
        gameObjects.addAll(getCollisions());
        gameObjects.addAll(getMissiles());
        gameObjects.addAll(getEnemies());
        gameObjects.add(getModelInfo());
        return gameObjects;
    }
    
}
