package cz.fit.dpo.mvcshooter.controller;

import cz.fit.dpo.mvcshooter.model.Model;
import java.awt.event.KeyEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Jaroslav Vesely
 */
public class EventControllerTest {
    
    public EventControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        System.out.println("-------------");
    }
    
    @Test
    public void testKeyPressed() {
        System.out.println("keyPressed:");
        
        Model m = mock(Model.class);
        
        KeyEvent W_key = mock(KeyEvent.class);
        when(W_key.getKeyCode()).thenReturn(KeyEvent.VK_W);
        
        KeyEvent SPACE_key = mock(KeyEvent.class);
        when(SPACE_key.getKeyCode()).thenReturn(KeyEvent.VK_SPACE);
        
        EventController controller = new EventController(m, null);
        
        controller.keyPressed(W_key);
        
        verify(m, times(1)).cannonUp();
        
        controller.keyPressed(SPACE_key);
        
        verify(m, times(1)).fire();
    }
    
}
