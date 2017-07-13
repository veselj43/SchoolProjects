package knaptool.strategies;

import knaptool.Item;

/**
 *
 * @author Jaroslav Vesely
 */
public class DynamicByWeight implements KnapStrategy {
    protected int capacity;
    protected Item solution;
    protected Item[] items;
    protected Item[][] cache;
    
    protected int counter = 0;
    
    public DynamicByWeight() {
    }
    
    // copmutation of the solution
	Item process(int i, int weight) {
		Item y, n, best;

		// trivial
		if (i >= items.length || weight <= 0) return new Item(0, 0);

		// look up in cache
		if (cache[i][weight] != null) return cache[i][weight];
        
        counter++;

		// try both variants
		n = process(i+1, weight);
		y = process(i+1, weight - items[i].getWeight());

		// compare results
		if (weight >= items[i].getWeight() && (y.getCost() + items[i].getCost()) > n.getCost()) 
			best = new Item(y.getWeight() + items[i].getWeight(), y.getCost() + items[i].getCost());
		else
			best = n;

		// save the result
		cache[i][weight] = new Item(best);

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
        
		// alloc matrix
		cache = new Item[items.length][capacity + 1];

		// find the best solution
		solution = process(0, capacity);
        
//        showCache();

		return solution.getCost();
    }
    
    public void showCache() {
        for (int j = capacity; j >= 0; j--) {
            System.out.print(j + "\t");
            for (int i = 0; i < items.length; i++) {
                System.out.print(cache[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    @Override
    public int getCounter() {
        return counter;
    }
    
}
