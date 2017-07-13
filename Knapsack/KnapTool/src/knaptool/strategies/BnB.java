package knaptool.strategies;

import static java.lang.Math.max;
import knaptool.Item;

/**
 *
 * @author Jaroslav Vesely
 */
public class BnB implements KnapStrategy {
    protected int capacity;
    protected int solution;
    protected int[] partSum;
    protected Item[] items;
    
    protected int counter = 0;

    public BnB() {
    }

    private int process(int i, int weight, int cost) {
        int y, n;
        
        if (weight > capacity) return 0;
        
        if (i >= items.length) return cost;
        
        if (cost + partSum[i] < solution) return 0;
        
        counter++;
        
        y = process(i + 1, weight + items[i].getWeight(), cost + items[i].getCost());
        n = process(i + 1, weight, cost);
        
        cost = max(y, n);
        
        solution = max(solution, cost);
        
        return cost;
    }
    
    @Override
    public int solve(Item[] in, int c) {
        counter = 0;
        capacity = c;
        
        int tmp = 0;
        
        items = new Item[in.length];
        partSum = new int[in.length];
        
        for (int i = in.length - 1; i >= 0; i--) {
            items[i] = new Item(in[i]);
            tmp += items[i].getCost();
            partSum[i] = tmp;
//            counter++;
        }
        
        solution = process(0, 0, 0);
        
        return solution;
    }

    @Override
    public int getCounter() {
        return counter;
    }
    
}
