package cz.fit.dpo.mvcshooter.model;

/**
 *
 * @author Jaroslav Vesely
 */
public interface Observable {    
    public void addObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObservers();
}
