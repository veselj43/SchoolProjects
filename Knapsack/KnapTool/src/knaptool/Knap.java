package knaptool;

import knaptool.strategies.KnapStrategy;

/**
 *
 * @author Jaroslav Vesely
 */
public class Knap {
    protected int solution;
    protected int capacity;
    Item[] items;
    KnapStrategy strategy;
    
    private int counter;

    public Knap(String line, KnapStrategy s) {
        strategy = s;
        read(line);
    }
    
    public int solve() {
        int sol = strategy.solve(items, capacity);
        counter = strategy.getCounter();
        return sol;
    }
    
    protected void read(String line) {
        int i, id, n;
        
        String[] data = line.split(" ");
        
        i = 0;
        
        id = Integer.parseInt(data[i++]);
        n = Integer.parseInt(data[i++]);
        capacity = Integer.parseInt(data[i++]);
        
        items = new Item[n];
        
        for (int k = 0; k < n; k++) {
            items[k] = new Item(
                Integer.parseInt(data[i++]),
                Integer.parseInt(data[i++])
            );
        }
    }

    public int getCounter() {
        return counter;
    }
    
}
