package weightedsat;

import weightedsat.CNFLogic.CNF;
import weightedsat.strategy.WSATStrategy;

/**
 *
 * @author Jaroslav Vesely
 */
public class WSAT {
    protected WSATStrategy strategy;
    protected CNF cnf;
    
    WSAT(String file, WSATStrategy s) {
        cnf = new CNF(file);
        strategy = s;
    }
    
    public Solution solve() {
        return strategy.solve(cnf);
    }
    
}
