package cz.fit.dpo.mvcshooter.view;

import cz.fit.dpo.mvcshooter.model.gameobjects.Collision;
import cz.fit.dpo.mvcshooter.model.gameobjects.Missile;
import cz.fit.dpo.mvcshooter.model.gameobjects.Cannon;
import cz.fit.dpo.mvcshooter.model.gameobjects.Enemy;
import cz.fit.dpo.mvcshooter.model.gameobjects.ModelInfo;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Ondrej Stuchlik
 */
public class GraphicsDrawer implements DrawVisitor {
    private BufferedImage cannonImage;
    private BufferedImage enemyImage1;
    private BufferedImage enemyImage2;
    private BufferedImage missileImage;
    private BufferedImage collisionImage;
 
    Graphics g;

    public GraphicsDrawer() {
        try {
            cannonImage = ImageIO.read(getClass().getResourceAsStream("/images/cannon.png"));
            enemyImage1 = ImageIO.read(getClass().getResourceAsStream("/images/enemy1.png"));
            enemyImage2 = ImageIO.read(getClass().getResourceAsStream("/images/enemy2.png"));
            missileImage = ImageIO.read(getClass().getResourceAsStream("/images/missile.png"));
            collisionImage = ImageIO.read(getClass().getResourceAsStream("/images/collision.png"));
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    public void setGraphics(Graphics graphics) {
        g = graphics;
    }
    
    @Override
    public void draw(Cannon cannon) {
        g.drawImage(cannonImage, 
              cannon.getX() - cannonImage.getWidth()/2, 
              cannon.getY() - cannonImage.getHeight()/2, null
        );
    }
    
    @Override
    public void draw(Missile missile) {
        g.drawImage(missileImage, 
              missile.getX() - missileImage.getWidth()/2, 
              missile.getY() - missileImage.getHeight()/2, null
        );
    }
    
    @Override
    public void draw(Enemy enemy) {
        BufferedImage tmp = (enemy.getEnemyStyle() == 1) ? enemyImage1 : enemyImage2;
        g.drawImage(tmp, 
              enemy.getX() - tmp.getWidth()/2, 
              enemy.getY() - tmp.getHeight()/2, null
        );
    }
    
    @Override
    public void draw(Collision collision) {
        BufferedImage tmp = collisionImage;
        g.drawImage(tmp, 
              collision.getX() - tmp.getWidth()/2, 
              collision.getY() - tmp.getHeight()/2, null
        );
    }
    
    @Override
    public void draw(ModelInfo info) {
        String tmp = info.getInfo();
        g.drawChars(tmp.toCharArray(), 0, tmp.length(), 5, 15);
    }

}
