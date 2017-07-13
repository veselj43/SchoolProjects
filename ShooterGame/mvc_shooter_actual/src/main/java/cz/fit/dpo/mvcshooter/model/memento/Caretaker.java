package cz.fit.dpo.mvcshooter.model.memento;

import cz.fit.dpo.mvcshooter.model.Model;

/**
 *
 * @author Jaroslav Vesely
 */
public class Caretaker {
	private Memento memento;

	public void saveState(Model m) {
		memento = new Memento(m);
        System.out.println("Game was saved.");
	}

	public void loadState(Model m) {
		if (memento == null) {
			System.out.println("No game save was found.");
			return;
		}
        System.out.println("Loading game save . . .");
		m.load(memento);
        System.out.println("Loaded.");
	}

}
