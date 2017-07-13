package cz.fit.dpo.mvcshooter.model;

import cz.fit.dpo.mvcshooter.view.DrawVisitor;

/**
 *
 * @author Jaroslav Vesely
 */
public interface DrawElement {
    public void accept(DrawVisitor v);
}
