package knaptool;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jaroslav Vesely
 */
public class KnapTool {
    private static int state = 1;
    
    public static void main(String[] args) {
        
        KnapSolver knapSolver;
        String basePath = "../knap/in";
        List<String> testPaths = new ArrayList<>();
        
        switch (state) {
            case 0:
                testPaths.add("/knap_40.inst.dat");
                break;
            case 1:
                testPaths.add("");
                break;
            case 2:
                basePath = "../knap/gen/";
                testPaths.add("W");
                testPaths.add("C");
                testPaths.add("m");
                testPaths.add("k_d+1");
                testPaths.add("k_d-1");
                break;
        }
        
        for (String p : testPaths) {
            System.out.println(basePath + p + ":");
            knapSolver = new KnapSolver(basePath + p + "/");
            System.out.println("");
        }
        
    }
    
}
