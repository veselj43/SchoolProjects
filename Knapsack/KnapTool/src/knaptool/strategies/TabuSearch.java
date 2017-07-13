package knaptool.strategies;

import java.util.ArrayList;
import java.util.List;
import knaptool.Item;

/**
 *
 * @author Jaroslav Vesely
 */
public class TabuSearch implements KnapStrategy {
    protected int capacity;
    protected Configuration s0;
    protected Item[] items;
    protected int maxItemCost;
    protected double multiplierTabuSize;
    protected double multiplierLimit;
    
    protected int counter = 0;

    public TabuSearch() {
        init(2, 2.5);
    }

    public TabuSearch(double mts, double ml) {
        init(mts, ml);
    }
    
    protected void init(double mts, double ml) {
        multiplierTabuSize = mts;
        multiplierLimit = ml;
    }
    
    private void initSolve(int c) {
        counter = 0;
        capacity = c;
        maxItemCost = 0;
    }
    
    private int fitness(Configuration c) {
        if (c == null || c.getWeight() > capacity) return -1;
        return c.getCost();
    }

    private int compareIndexesOf(List<Configuration> list, Configuration c1, Configuration c2) {
        for (Configuration i : list) {
            if (i.equals(c1)) return 1;
            if (i.equals(c2)) return -1;
        }
        return 0;
    }

    private boolean contains(List<Configuration> list, Configuration c) {
        for (Configuration i : list) {
            if (i.equals(c)) return true;
        }
        return false;
    }
    
    private Configuration process(int limit, int maxTabuSize) {
        Configuration s = s0;
        Configuration sBest = s;
        List<Configuration> tabuStates = new ArrayList();
        List<Integer> tabuChanges = new ArrayList();
        
        int maxTabuSize2 = (int) items.length / 5;
        
//        System.out.println(counter + "\t" + sBest.getCost());
        
        for (int n = 0; n < limit; n++) {
            Configuration bestCandidate = null;
            Configuration tabuBestCandidate = null;
            int bestCandidateIndex = -1;
            int tabuBestCandidateIndex = -1;
            
            for (int i = 0; i < items.length; i++) {
                counter++;
                
                Configuration sCandidate = s.getNeighbor(i);
                if (fitness(sCandidate) < 0) continue;
                if (tabuChanges.contains(i) && fitness(sCandidate) < fitness(sBest)) continue;
                
                if (fitness(sCandidate) > fitness(bestCandidate)) {
                    if (!contains(tabuStates, sCandidate)) {
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
//                    System.out.println("===========");
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
//                System.out.println(counter + "\t" + sBest.getCost());
            }
            
            tabuStates.add(s);
            tabuChanges.add(bestCandidateIndex);
            if (tabuStates.size() > maxTabuSize) {
                tabuStates.remove(0);
            }
            if (tabuChanges.size() > maxTabuSize2) {
                tabuChanges.remove(0);
            }
        }
        
//        System.out.println(counter + "\t" + sBest.getCost());
        
        return sBest;
    }
    
    @Override
    public int solve(Item[] in, int c) {
        initSolve(c);
        
        items = new Item[in.length];
        for (int i = 0; i < in.length; i++) {
            items[i] = new Item(in[i]);
        }
        
        for (Item i : items) {
            if (i.getCost() > maxItemCost) maxItemCost = i.getCost();
        }
        
        int limit = (int) (items.length * multiplierLimit);
        int maxTabuSize = (int) (items.length * multiplierTabuSize);
        
        s0 = new Configuration();
        
//        for (int i = 0; i < items.length; i++) {
//            Configuration test = s0.getNeighbor(i);
//            if (fitness(test) < 0) break;
//            s0 = test;
//        }
        
        Configuration best = process(limit, maxTabuSize);
        
//        System.out.println("best: " + best.toString());
        
        return best.getCost();
    }

    @Override
    public int getCounter() {
        return counter;
    }
    
    public class Configuration {
        private boolean[] selection;
        
        public Configuration() {
            selection = new boolean[items.length];
            for (int i = 0; i < items.length; i++)
                selection[i] = false;
        }
        
        public Configuration(Configuration c) {
            selection = new boolean[items.length];
            System.arraycopy(c.getSelection(), 0, selection, 0, items.length);
        }
        
        public Configuration copy() {
            return new Configuration(this);
        }
        
        public Configuration changeOn(int position) {
            selection[position] = !selection[position];
            return this;
        }
        
        public Configuration getNeighbor(int position) {
            return copy().changeOn(position);
        }
        
        public int getWeight() {
            int weight = 0;
            for (int i = 0; i < items.length; i++) if (selection[i]) weight += items[i].getWeight();
            return weight;
        }

        private int getCost() {
            int cost = 0;
            for (int i = 0; i < items.length; i++) if (selection[i]) cost += items[i].getCost();
            return cost;
        }

        public boolean[] getSelection() {
            return selection;
        }

        @Override
        public String toString() {
            String str = "";
            boolean first = true;
            for (boolean x : selection) {
                if (first) first = false;
                else str += ", ";
                str += (x) ? "1" : "0";
            }
            return str;
        }

        public boolean equals(Configuration x) {
            if (x == null) return false;
            final boolean[] compare;
            compare = x.getSelection();
            for (int i = 0; i < selection.length; i++)
                if (selection[i] != compare[i]) return false;
            return true;
        }
    }

}
