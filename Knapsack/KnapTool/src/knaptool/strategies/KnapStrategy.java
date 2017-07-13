package knaptool.strategies;

import knaptool.Item;

/**
 *
 * @author Jaroslav Vesely
 */
public interface KnapStrategy {
    public int solve(Item[] in, int c);
    
    public int getCounter();
}
