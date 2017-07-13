package cz.fit.dpo.mvcshooter.model.gameobjects;

import cz.fit.dpo.mvcshooter.model.Config;
import cz.fit.dpo.mvcshooter.model.movement.Point;
import cz.fit.dpo.mvcshooter.model.strategy.enemy.EnemySimpleMovement;
import cz.fit.dpo.mvcshooter.view.DrawVisitor;
import cz.fit.dpo.mvcshooter.model.strategy.enemy.EnemyMovementStrategy;

/**
 *
 * @author Jaroslav Vesely
 */
public class Enemy extends GameObject {
    protected EnemyMovementStrategy strategy;
    protected int enemyStyle;
    
    public Enemy() {
        getRandPos();
        life = Config.ENEMY_LIFE;
        enemyStyle = (int)(Math.random() * 2);
        initStrategy();
    }
    
    public Enemy(Enemy e) {
        super(e);
        strategy = e.getStrategy();
        enemyStyle = e.getEnemyStyle();
    }
    
    @Override
    public Enemy copy() {
        return new Enemy(this);
    }
    
    protected void initStrategy() {
        strategy = new EnemySimpleMovement();
    }
    
    protected void getRandPos() {
        int xRange = Config.CANVAS_WIDTH - Config.ENEMY_MARGIN_L - Config.ENEMY_MARGIN_R;
        int yRange = Config.CANVAS_HEIGHT - Config.ENEMY_MARGIN_U - Config.ENEMY_MARGIN_D;
        
        p0 = new Point(
            Config.ENEMY_MARGIN_L + (int)(Math.random() * xRange),
            Config.ENEMY_MARGIN_U + (int)(Math.random() * yRange)
        );
        pos = new Point(p0);
    }

    @Override
    public void accept(DrawVisitor v) {
        v.draw(this);
    }
    
    @Override
    public void nextMotion() {
        super.nextMotion();
        vec = strategy.refreshVector(Config.DEFAULT_GRAVITY, this);
        pos = p0.add(vec);
        tick++;
    }

    @Override
    public boolean isGarbage() {
        return (!isVisible() || super.isGarbage());
    }

    public int getEnemyStyle() {
        return enemyStyle;
    }

    public EnemyMovementStrategy getStrategy() {
        return strategy;
    }
    
}
