package cz.fit.dpo.mvcshooter.model.gameobjects;

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
public class CollisionTest {
    
    public CollisionTest() {
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
    public void testConstcuctorFromEnemy() {
        System.out.println("Constructor( Enemy ):");
        
        Enemy e = new Enemy();
        Collision instance = new Collision(e);
        
        String expResult = e.getPos().toString();
        String result = instance.getPos().toString();
        
        System.out.println("result: \t" + result);
        System.out.println("expected: \t" + expResult);
        
        assertEquals(expResult, result);
    }
    
}
