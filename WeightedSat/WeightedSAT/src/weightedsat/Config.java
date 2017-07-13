package weightedsat;

import java.util.ArrayList;
import java.util.List;
import weightedsat.enums.ComputationStates;

/**
 *
 * @author Jaroslav Vesely
 */
public class Config {
    public static final ComputationStates STATE = ComputationStates.SIMPLE;
    
    public static final int FILE_FROM = 20;
    public static final int FILE_LIMIT = 15;
    
    public static final int WEIGHT_FROM = 1;
    public static final int WEIGHT_TO = 100;
    
    public static final String WEIGHTING_FOLDER = "weights/";
    public static final String WEIGHTING_SEPARATOR = ",";
    
    public static final String BASE_PATH = "../instances";
    
    public static List<String> getPaths() {
        List<String> paths = new ArrayList<>();
        
//        paths.add("/ukazka.cnf");

//        paths.add("/uf20-91/");
//        uf20-0160.cnf
//        uf20-0119.cnf
//        uf20-0777.cnf
//        uf50-0986.cnf
        paths.add("/uf75-325/");
        
        return paths;
    }
    
    // multiplied with number of clauses in the formula
    public static final double DEFAULT_TABU_LIMIT = 30;
    
    // multiplied with number of variables
    public static final double DEFAULT_TABU_STATE_LIST_SIZE = 20;
    
    // divides the number of variables
    public static final double DEFAULT_TABU_CHANGES_SIZE = 4;
    
}
