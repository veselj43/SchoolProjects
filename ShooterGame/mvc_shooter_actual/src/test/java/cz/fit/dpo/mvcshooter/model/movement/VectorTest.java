/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fit.dpo.mvcshooter.model.movement;

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
public class VectorTest {
    
    public VectorTest() {
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
    public void testVectorFromTwoPointsConstructor() {
        System.out.println("VectorFromTwoPointsConstructor:");
        
        Vector result = new Vector(
            new Point(1,2),
            new Point(5,4)
        );
        Vector expResult = new Vector(-4,-2);
        
        System.out.println("result: \t" + result);
        System.out.println("expected: \t" + expResult);
        
        assertEquals(expResult.toString(), result.toString());
    }
    
    @Test
    public void testAdd() {
        System.out.println("add:");
        
        Vector v1 = new Vector(-1,2);
        Vector v2 = new Vector(4,10);
        
        Vector expResult = new Vector(3,12);
        Vector result = v1.add(v2);
        
        System.out.println("result: \t" + result);
        System.out.println("expected: \t" + expResult);
        
        assertEquals(expResult.toString(), result.toString());
    }
    
}
