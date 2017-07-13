package knaptool;

/**
 *
 * @author Jaroslav Vesely
 */
public class Item {
    protected int weight;
    protected int cost;
    
    public Item() {
        weight = -1;
        cost = -1;
    }
    
    public Item(int w, int c) {
        weight = w;
        cost = c;
    }
    
    public Item(Item x) {
        weight = x.getWeight();
        cost = x.getCost();
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCost() {
        return cost;
    }

    public int getWeight() {
        return weight;
    }
    
    @Override
    public String toString() {
        return "" + cost + ", " + weight;
    }
}
