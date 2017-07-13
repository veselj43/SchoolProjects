package knaptool.strategies;

import java.util.Arrays;
import knaptool.Item;

/**
 *
 * @author Jaroslav Vesely
 */
public class Heuristic implements KnapStrategy {
    protected int capacity;
    protected int solution;
    protected Item[] items;
    
    protected int counter = 0;

    public Heuristic() {
    }
    
    private Double calculateRatio(Item i) {
        return (double)i.getCost()/i.getWeight();
    }

    @Override
    public int solve(Item[] in, int c) {
        counter = 0;
        capacity = c;
        
        items = new Item[in.length];
        for (int i = 0; i < in.length; i++) {
            items[i] = new Item(in[i]);
        }
        
        Arrays.sort(items, (Item i1, Item i2) -> calculateRatio(i2).compareTo(calculateRatio(i1)));
        
        int aCost = 0;
        int aWeight = 0;
        
        for (Item item : items) {
			if (item.getWeight() + aWeight <= capacity) {
				// we can add this item to bag
				aWeight += item.getWeight();
				aCost += item.getCost();
			}
            counter++;
		}
        
        return aCost;
    }

    @Override
    public int getCounter() {
        return counter;
    }

}
