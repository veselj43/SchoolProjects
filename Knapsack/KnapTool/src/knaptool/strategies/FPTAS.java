package knaptool.strategies;

import knaptool.Item;

/**
 *
 * @author Jaroslav Vesely
 */
public class FPTAS implements KnapStrategy {
    protected double eps;
    protected int capacity;
    protected int solution;
    protected Item[] in;
    protected Item[] items;
    protected int[][] cache;
    
    protected int counter = 0;
    
    public FPTAS() {
    }
    
    public FPTAS(double eps) {
        setEps(eps);
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    // copmutation of the solution
	protected void process(int i, int cost, int weight) {
        
		if (cache[i][cost] <= weight && cache[i][cost] != -1) return;

		cache[i][cost] = weight;

		if (cost > solution && weight <= capacity) {
			solution = cost;
		}

		if (items.length <= i) return;
        
        counter++;

		// try both variants
		process(i+1, cost + items[i].getCost(), weight + items[i].getWeight());
		process(i+1, cost, weight);

	}

	protected int reconstructSolution() {
		int i, c, sol_cost;
		i = items.length;
		c = solution;
		sol_cost = 0;

		while (i > 0) {
            counter++;
			if (cache[i][c] == cache[i-1][c]) {
				i--;
			}
			else {
				sol_cost += in[i-1].getCost();
				c -= items[i-1].getCost();
				i--;
			}
		}

		return sol_cost;
	}
    
    @Override
    public int solve(final Item[] in, int c) {
        int sum, maxCost; 
        double divider;
        
        sum = maxCost = 0;
        counter = 0;
        capacity = c;
        this.in = in;
        
        items = new Item[in.length];
        for (int i = 0; i < in.length; i++) {
            items[i] = new Item(in[i]);
            counter++;
			maxCost = (maxCost < in[i].getCost()) ? in[i].getCost() : maxCost;
        }
        
        divider = eps * maxCost / items.length;
        if (divider < 1) divider = 1;
        
        for (Item i : items) {
            i.setCost((int) Math.floor((double)i.getCost() / divider));
            sum += i.getCost();
            counter++;
        }
        
        // alloc matrix
		cache = new int[items.length+1][sum + 1];
		for (int i = 0; i < items.length + 1; i++) {
			for (int j = 0; j < sum + 1; j++) {
				cache[i][j] = -1;
			}
		}
		
		// find the best solution
		process(0, 0, 0);
        
        // showCache();
		
		solution = reconstructSolution();

		return solution;
    }
    
    public void showCache() {
        for (int j = cache[0].length - 1; j >= 0; j--) {
            System.out.print(j + "\t");
            for (int i = 0; i < items.length + 1; i++) {
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
