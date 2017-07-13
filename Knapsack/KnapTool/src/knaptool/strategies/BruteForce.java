package knaptool.strategies;

import static java.lang.Math.max;
import knaptool.Item;

/**
 *
 * @author Jaroslav Vesely
 */
public class BruteForce implements KnapStrategy {
    protected int capacity;
    protected int solution;
    protected Item[] items;
    
    protected int counter = 0;

    public BruteForce() {
    }
    
    private int process(int i, int weight, int cost) {
        int y, n;
        
        if (i >= items.length) return (weight > capacity) ? 0 : cost;
        
        counter++;
        
        y = process(i + 1, weight + items[i].getWeight(), cost + items[i].getCost());
        n = process(i + 1, weight, cost);
        
        return max(y, n);
    }
    
    @Override
    public int solve(Item[] in, int c) {
        counter = 0;
        capacity = c;
        
        items = new Item[in.length];
        for (int i = 0; i < in.length; i++) {
            items[i] = new Item(in[i]);
        }
        
        solution = process(0, 0, 0);
        
        return solution;
    }

    @Override
    public int getCounter() {
        return counter;
    }

}
