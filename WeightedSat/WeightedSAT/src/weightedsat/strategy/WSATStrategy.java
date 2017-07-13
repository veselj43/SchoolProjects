package weightedsat.strategy;

import weightedsat.CNFLogic.CNF;
import weightedsat.Solution;

/**
 *
 * @author Jaroslav Vesely
 */
public interface WSATStrategy {
    public Solution solve(CNF in);
    
    public int getCounter();
}
