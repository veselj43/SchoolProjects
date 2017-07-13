package cz.fit.dpo.mvcshooter.view;

import cz.fit.dpo.mvcshooter.controller.EventController;
import cz.fit.dpo.mvcshooter.model.Config;
import cz.fit.dpo.mvcshooter.model.Model;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

/**
 *
 * @author Ondrej Stuchlik
 */
public class MainWindow extends JFrame {
    private final boolean keyLog = false;

    public MainWindow() {
        try {
            final Model model = new Model();
            final Canvas view = new Canvas(
                0, 0, 
                Config.CANVAS_WIDTH, Config.CANVAS_HEIGHT, 
                model
            );
            final EventController controller = new EventController(model, view);

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setTitle("MyShooter");
            this.setResizable(false);

            Dimension obrazovka = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(
                  (int) (obrazovka.getWidth() / 2 - 250),
                  (int) (obrazovka.getHeight() / 2 - 250)
            );

            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent evt) {
                    controller.keyPressed(evt);
                    if (keyLog) System.out.println("key pressed: " + evt.getKeyCode() + ": " + KeyEvent.getKeyText(evt.getKeyCode()));
                }
            });
            
            this.add(view);
            this.pack();
        } catch (HeadlessException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
