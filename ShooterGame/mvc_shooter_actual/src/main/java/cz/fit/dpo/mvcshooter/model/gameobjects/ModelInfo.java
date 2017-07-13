package cz.fit.dpo.mvcshooter.model.gameobjects;

import cz.fit.dpo.mvcshooter.model.Model;
import cz.fit.dpo.mvcshooter.view.DrawVisitor;

/**
 *
 * @author Jaroslav Vesely
 */
public class ModelInfo extends GameObject {
    protected Model model;
    protected int score;

    public ModelInfo(Model m) {
        model = m;
        score = 0;
    }

    public ModelInfo(ModelInfo mi) {
        model = mi.model;
        score = mi.getScore();
    }
    
    @Override
    public ModelInfo copy() {
        return new ModelInfo(this);
    }

    @Override
    public void accept(DrawVisitor v) {
        v.draw(this);
    }
    
    public void score() {
        score++;
    }
    
    public String getInfo() {
        return 
            model.getGameMode().toString() + ", " + 
            model.getCannon().toString() + ", " + 
            "Gravity: " + model.getGravity() + ", " + 
            "Score: " + getScore();
    }

    public int getScore() {
        return score;
    }
    
}
