package weightedsat.CNFLogic;

/**
 *
 * @author Jaroslav Vesely
 */
public class Literal {
    protected int id;
    protected boolean inv;
    
    public Literal(int name, boolean i) {
        id = name;
        inv = i;
    }
    
    public int getID() {
        return id;
    }
    
    public boolean getInv() {
        return inv;
    }
}
