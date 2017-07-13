package weightedsat.CNFLogic;

import java.util.ArrayList;
import java.util.List;
import weightedsat.Configuration;

/**
 *
 * @author Jaroslav Vesely
 */
class Clausule {
    protected List<Literal> literals;
    
    public Clausule(String[] lArray) {
        load(lArray);
    }
    
    private void load(String[] lArray) {
        literals = new ArrayList<>();
        for (String l : lArray) {
            
            int literal = Integer.parseInt(l);
            if (literal == 0) break;
            
            boolean inv = false;
            if (literal < 0) {
                literal *= -1;
                inv = true;
            }
            
            literals.add(new Literal(literal-1, inv));
        }
    }
    
    public boolean check(Configuration c) {
        for (Literal l : literals) {
            if (c.getLiteral(l.getID()) && !l.getInv()) return true;
            if (!c.getLiteral(l.getID()) && l.getInv()) return true;
        }
        return false;
    }
    
}
