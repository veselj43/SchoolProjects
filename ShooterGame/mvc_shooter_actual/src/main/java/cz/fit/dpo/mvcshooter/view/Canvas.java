package cz.fit.dpo.mvcshooter.view;

import cz.fit.dpo.mvcshooter.model.Observer;
import cz.fit.dpo.mvcshooter.model.gameobjects.GameObject;
import cz.fit.dpo.mvcshooter.model.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Ondrej Stuchlik
 */
public class Canvas extends JPanel implements Observer {
    GraphicsDrawer drawer = new GraphicsDrawer();
    Model model;

    public Canvas(int x, int y, int width, int height, Model m) {
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.setLocation(x, y);
        this.setPreferredSize(new Dimension(width,height));
        this.setVisible(true);
        model = m;
        model.addObserver(this);
    }
    
    @Override
    public void update() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawer.setGraphics(g);
        
        for (GameObject go : model.getAllGameObjects()) {
            go.accept(drawer);
        }
    }
    
    public void showHelp() {
        JOptionPane.showMessageDialog(this, 
            "Controls: \n" + 
            "up/down: aim angle \n" + 
            "left/right: force \n" + 
            "space: fire \n" + 
            "w/s: vertical cannon position \n" + 
            "o: toggle cannon mode (state) \n" + 
            "p: toggle game mode (strategy) \n" + 
            "g/t: gravity adjustment \n" + 
            "n/m: pause/resume \n" + 
            "k/l: save/load \n"
        );
    }
    
}
