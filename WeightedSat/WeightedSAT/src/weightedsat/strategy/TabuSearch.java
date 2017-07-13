package weightedsat.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import weightedsat.CNFLogic.CNF;
import weightedsat.Config;
import weightedsat.Configuration;
import weightedsat.Solution;

/**
 *
 * @author Jaroslav Vesely
 */
public class TabuSearch implements WSATStrategy {
    protected int capacity;
    protected Configuration s0;
    protected CNF formula;
    protected double multiplierTabuSize;
    protected double dividerTabuSize2;
    protected double multiplierLimit;
    
    protected int counter = 0;

    public TabuSearch() {
        init(
            Config.DEFAULT_TABU_LIMIT,
            Config.DEFAULT_TABU_STATE_LIST_SIZE,
            Config.DEFAULT_TABU_CHANGES_SIZE
        );
    }

    public TabuSearch(double ml, double mts, double dts2) {
        init(ml, mts, dts2);
    }
    
    protected void init(double ml, double mts, double dts2) {
        multiplierLimit = ml;
        multiplierTabuSize = mts;
        dividerTabuSize2 = dts2;
    }
    
    private void initSolve(CNF in) {
        formula = in;
        counter = 0;
    }
    
    private int getWeight(Configuration c) {
        return c.getWeight(formula.getWeights());
    }
    
    private int getResultWeight(Configuration c) {
        return (formula.check(c) < formula.getClausuleCount()) ? 0 : getWeight(c);
    }
    
    private int fitness(Configuration c) {
        if (c == null) return -1;
        int trueClauses = formula.check(c);
        if (trueClauses < formula.getClausuleCount()) return trueClauses;
        return formula.getClausuleCount() + getWeight(c);
    }

    private int compareIndexesOf(Queue<Configuration> queue, Configuration c1, Configuration c2) {
        for (Configuration i : queue) {
            if (i.equals(c1)) return 1;
            if (i.equals(c2)) return -1;
        }
        return 0;
    }

    private boolean contains(Queue<Configuration> queue, Configuration c) {
        for (Configuration i : queue) {
            if (i.equals(c)) return true;
        }
        return false;
    }

    private boolean containsSearch(HashSet<String> set, Configuration c) {
        return set.contains(c.toString());
    }
    
    private Configuration process(int limit, int maxTabuSize, int maxTabuSize2) {
        Configuration s = s0;
        Configuration sBest = s;
        Queue<Configuration> tabuStates = new LinkedList<>();
        HashSet<String> tabuStatesSearch = new HashSet();
        List<Integer> tabuChanges = new ArrayList();
        
        for (int n = 0; n < limit; n++) {
            Configuration bestCandidate = null;
            Configuration tabuBestCandidate = null;
            int bestCandidateIndex = -1;
            int tabuBestCandidateIndex = -1;
            
            for (int i = 0; i < formula.getVarCount(); i++) {
                counter++;
                
                Configuration sCandidate = s.getNeighbor(i);
                if (tabuChanges.contains(i) && fitness(sCandidate) < fitness(sBest)) continue;
                
                if (fitness(sCandidate) >= fitness(bestCandidate)) {
                    if (!containsSearch(tabuStatesSearch, sCandidate)) {
                        bestCandidate = sCandidate;
                        bestCandidateIndex = i;
                    }
                    else if (compareIndexesOf(tabuStates, sCandidate, tabuBestCandidate) > 0) {
                        tabuBestCandidate = sCandidate;
                        tabuBestCandidateIndex = i;
                    }
                }
            }
            
            if (bestCandidate == null) {
                if (tabuBestCandidate == null) {
                    System.out.println("------------");
                    break;
                }
                s = tabuBestCandidate;
                bestCandidateIndex = tabuBestCandidateIndex;
            }
            else {
                s = bestCandidate;
            }
            if (fitness(s) > fitness(sBest)) {
                sBest = s;
//                if (formula.check(sBest) == formula.getClausuleCount()) {
//                    System.out.println(
//                        (counter - tabuChanges.get(tabuChanges.size()-1)) + "\t" + 
//                        getWeight(sBest)
//                    );
//                }
            }
            
            tabuStates.add(s);
            tabuStatesSearch.add(s.toString());
            tabuChanges.add(bestCandidateIndex);
            if (tabuStates.size() > maxTabuSize) {
                tabuStatesSearch.remove(
                    tabuStates.remove().toString()
                );
            }
            if (tabuChanges.size() > maxTabuSize2) {
                tabuChanges.remove(0);
            }
        }
        
        if (formula.check(sBest) == formula.getClausuleCount()) {
            System.out.println(counter + "\t" + getResultWeight(sBest));
        }
        else {
            System.out.println(counter + "\t" + fitness(sBest));
        }
        
        return sBest;
    }
    
    @Override
    public Solution solve(CNF in) {
        initSolve(in);
        
        int limit = (int) (formula.getClausuleCount() * multiplierLimit);
        int maxTabuSize = (int) (formula.getVarCount() * multiplierTabuSize);
        int maxTabuSize2 = (int) (formula.getVarCount() / dividerTabuSize2);
        
        s0 = new Configuration(formula.getVarCount());
        
        Configuration best = process(limit, maxTabuSize, maxTabuSize2);
        
//        System.out.println("== " + formula.check(best) + " <= " + formula.getClausuleCount() + " ==");
        
        return new Solution(
            best, 
            formula.getVarCount(), 
            getResultWeight(best)
        );
    }

    @Override
    public int getCounter() {
        return counter;
    }    
}
