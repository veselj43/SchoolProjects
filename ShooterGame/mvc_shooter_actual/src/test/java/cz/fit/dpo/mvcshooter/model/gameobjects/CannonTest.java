/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fit.dpo.mvcshooter.model.gameobjects;

import cz.fit.dpo.mvcshooter.model.factory.AbsFactory;
import cz.fit.dpo.mvcshooter.model.factory.SimpleFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jaroslav Vesely
 */
public class CannonTest {
    Cannon cannon;
    
    public CannonTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        cannon = new Cannon();
    }
    
    @After
    public void tearDown() {
        System.out.println("-------------");
    }
    
    @Test
    public void testCopy() {
        System.out.println("copy:");
        
        String expResult, result;
        Cannon cannonCopy;
        
        cannonCopy = cannon.copy();
        expResult = cannon.toString();
        result = cannonCopy.toString();
        System.out.println("assert1:\t" + expResult);
        System.out.println("assert2:\t" + result);
        
        assertEquals(expResult, result);
        
        cannonCopy = cannon.copy();
        cannon.forceUp();
        expResult = cannon.toString();
        result = cannonCopy.toString();
        System.out.println("assert3:\t" + result);
        
        assertFalse(result.equals(expResult));
        
        cannonCopy = cannon.copy();
        cannon.aimDown();
        expResult = cannon.toString();
        result = cannonCopy.toString();
        System.out.println("assert4:\t" + result);
        
        assertFalse(result.equals(expResult));
    }
    
    @Test
    public void testFire() {
        System.out.println("fire:");
        
        AbsFactory f = new SimpleFactory();
        
        String expResult = cannon.getPos().toString();
        String result = cannon.fire(f).get(0).getPos().toString();
        System.out.println("result: \t" + result);
        System.out.println("expected:\t" + expResult);
        
        assertEquals(expResult, result);
    }
    
}
