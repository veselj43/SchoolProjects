/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fit.dpo.mvcshooter.model;

import cz.fit.dpo.mvcshooter.model.gameobjects.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Jaroslav Vesely
 */
public class NumberOfGameObjectsTest  {
    
    public NumberOfGameObjectsTest() {
    }

    @Test
	public void gameObjectsTest() {
		Model model = mock(Model.class);

		ArrayList<Enemy> enemies = new ArrayList();
		for (int i = 0; i < 6; i++) {
			Enemy e = mock(Enemy.class);
			enemies.add(e);
		}
		when(model.getEnemies()).thenReturn(enemies);

		ArrayList<Missile> missiles = new ArrayList();
		for (int i = 0; i < 5; i++) {
			Missile m = mock(Missile.class);
			missiles.add(m);
		}
		when(model.getMissiles()).thenReturn(missiles);

		when(model.getCollisions()).thenReturn(new ArrayList());

		ModelInfo modelInfo = mock(ModelInfo.class);
		when(model.getModelInfo()).thenReturn(modelInfo);

		Cannon cannon = mock(Cannon.class);
		when(model.getCannon()).thenReturn(cannon);

		when(model.getAllGameObjects()).thenCallRealMethod();

		assertEquals(13, model.getAllGameObjects().size());
	}
}
