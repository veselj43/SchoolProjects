package weightedsat;

/**
 *
 * @author Jaroslav Vesely
 */
public class WeightedSAT {

    public static void main(String[] args) {
        
        WSATSolver solver;
        
        for (String p : Config.getPaths()) {
            System.out.println(Config.BASE_PATH + p + ":");
            solver = new WSATSolver(Config.BASE_PATH + p + "/");
            System.out.println("");
        }
    }
    
}
