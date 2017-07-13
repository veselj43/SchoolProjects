package knaptool.strategies;

import knaptool.Item;

/**
 *
 * @author Jaroslav Vesely
 */
public class SimulatedAnnealing implements KnapStrategy {
    protected int capacity;
    protected int solution;
    protected Item[] items;
    protected int equilibrium;
    protected double coolingRate;
    protected double t0;
    protected double tEnd;
    
    protected int counter = 0;

    public SimulatedAnnealing() {
    }
    
    private boolean frozen(double t) {
        return t > tEnd;
    }
    
    private boolean equilibrium(int i) {
        counter++;
        return i < equilibrium;
    }
    
    private double cool(double t) {
        return t * (1 - coolingRate);
    }
    
    private Configuration tryThis(Configuration s, double t) {
        Configuration next;
        
        do {
            next = s.getNeighbor((int)Math.round(Math.random() * (items.length-1)));
        } while (next.getValue() < 0);
        
        if (next.better(s)) {
            return next;
        }
        
        double delta = next.getCost() - s.getCost();
        double x = Math.random();
        
        if (x < Math.pow(Math.E, (delta / t))) {
            return next;
        }
        
        return s;
    }
    
    private Configuration process(Configuration s0) {
        Configuration s = s0;
        Configuration best = s0;
        double t = t0;
        
        while(frozen(t)) {
            for (int i = 0; equilibrium(i); i++) {
                s = tryThis(s, t);
                if (s.better(best)) {
                    best = s;
                }
            }
            t = cool(t);
        }
        
        return best;
    }
    
    @Override
    public int solve(Item[] in, int c) {
        counter = 0;
        capacity = c;
        
        items = new Item[in.length];
        for (int i = 0; i < in.length; i++) {
            items[i] = new Item(in[i]);
        }
        
        t0 = 100;
        tEnd = 10;
        coolingRate = 0.066;
        equilibrium = 5 * items.length;
        
        Configuration start = new Configuration();
        
//        for (int i = 0; i < items.length; i++) {
//            Configuration test = start.getNeighbor(i);
//            if (test.getValue() < 0) break;
//            start = test;
//        }
        
        Configuration best = process(start);
        
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
            final boolean[] compare;
            compare = x.getSelection();
            for (int i = 0; i < selection.length; i++)
                if (selection[i] != compare[i]) return false;
            return true;
        }
        
        public int getValue() {
            if (getWeight() > capacity) return -1;
            return getCost();
        }
        
        public boolean better(Configuration c) {
            return getValue() > c.getValue();
        }
    }

}
