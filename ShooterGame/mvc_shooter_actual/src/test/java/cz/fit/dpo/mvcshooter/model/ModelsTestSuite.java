package cz.fit.dpo.mvcshooter.model;

import cz.fit.dpo.mvcshooter.model.gameobjects.*;
import cz.fit.dpo.mvcshooter.model.movement.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Jaroslav Vesely
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({VectorTest.class, CannonTest.class, CollisionTest.class, NumberOfGameObjectsTest.class})
public class ModelsTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
