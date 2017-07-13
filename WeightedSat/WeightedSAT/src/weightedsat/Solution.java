package weightedsat;

/**
 *
 * @author Jaroslav Vesely
 */
public class Solution extends Configuration {
    protected int weight;

    public Solution(Configuration c, int s, int w) {
        super(c, s);
        weight = w;
    }

    public int getWeight() {
        return weight;
    }
}
