package cz.fit.dpo.mvcshooter.model;

import cz.fit.dpo.mvcshooter.model.factory.*;
import cz.fit.dpo.mvcshooter.model.state.*;

/**
 *
 * @author Jaroslav Vesely
 */
public class Config {
    public static final AbsFactory GAME_MODE = new RealFactory();
    public static final ShootingState CANNON_MODE = new SingleShoot();
    
    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 600;
    
    public static final int TICK_RATE = 15;
    public static final double TIME_SCALE = 0.5;
    
    public static final int MOVE_STEP = 10;
    public static final double FORCE_STEP = 3;
    public static final double ANGLE_STEP = 5;
    public static final double MULTIPLE_ANGLE_STEP = 15;
    public static final double MAX_ANGLE = 75;
    public static final double MAX_FORCE = 30;
    public static final double MIN_FORCE = FORCE_STEP;
    
    public static final int ENEMY_LIFE = -1;
    public static final int MAX_ENEMIES = 10;
    public static final int ENEMY_MARGIN_L = 200;
    public static final int ENEMY_MARGIN_R = 50;
    public static final int ENEMY_MARGIN_U = 50;
    public static final int ENEMY_MARGIN_D = 100;
        
    public static final int COLLISION_MARGIN = 20;
    public static final int COLLISION_LIFE = 5;
    
    public static final double DEFAULT_GRAVITY = 1;
    public static final double GRAVITY_STEP = 0.5;
    public static final double MAX_GRAVITY = 5;
    
    public static final int INVISIBLE_LIFE = 100;

    
//    public static final int ;
    
    
}
