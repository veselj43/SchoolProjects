package weightedsat;

/**
 *
 * @author Jaroslav Vesely
 */
public class Configuration {
    private boolean[] selection;
    
    public Configuration(int s) {
        selection = new boolean[s];
        for (int i = 0; i < s; i++)
            selection[i] = false;
    }

    public Configuration(Configuration c, int s) {
        selection = new boolean[s];
        System.arraycopy(c.getSelection(), 0, selection, 0, s);
    }

    public Configuration copy() {
        return new Configuration(this, selection.length);
    }

    public Configuration changeOn(int position) {
        selection[position] = !selection[position];
        return this;
    }

    public Configuration getNeighbor(int position) {
        return copy().changeOn(position);
    }

    public int getWeight(int[] weights) {
        int weight = 0;
        for (int i = 0; i < selection.length; i++) if (selection[i]) weight += weights[i];
        return weight;
    }

    public boolean[] getSelection() {
        return selection;
    }
    
    public boolean getLiteral(int i) {
        return selection[i];
    }

    @Override
    public String toString() {
        String str = "";
        for (boolean x : selection) {
            str += (x) ? "1" : "0";
        }
        return str;
    }

    public boolean equals(Configuration x) {
        if (x == null) return false;
        final boolean[] compare;
        compare = x.getSelection();
        for (int i = 0; i < selection.length; i++)
            if (selection[i] != compare[i]) return false;
        return true;
    }
}
