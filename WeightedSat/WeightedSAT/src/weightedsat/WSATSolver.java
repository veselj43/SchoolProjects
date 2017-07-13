package weightedsat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import weightedsat.enums.ComputationStates;
import weightedsat.strategy.TabuSearch;

/**
 *
 * @author Jaroslav Vesely
 */
public class WSATSolver {
    private List<String> files;
    private HashMap<String,Solution> solutions;
    private HashMap<String,Solution> solutions2;
    
    public WSATSolver(String p) {
        listFilesForFolder(p);
        compute();
        showResults();
    }
    
    private void processFile(String file) {
        
        WSAT problem = new WSAT(file, new TabuSearch());
        solutions.put(file, problem.solve());
        
        if (Config.STATE == ComputationStates.COMPARE) {
            WSAT problem2 = new WSAT(file, new TabuSearch(10, 20, 4));
            solutions2.put(file, problem2.solve());
        }
        
    }
    
    private String getFileName(String s) {
        String[] tmp = s.split("\\\\");
        return tmp[tmp.length - 1];
    }
    
    private void compute() {
        solutions = new HashMap<>();
        solutions2 = new HashMap<>();
        
        int n = -1;
        
        Iterator<String> it = files.iterator();
		while (it.hasNext()) {
            String s = it.next();
            n++;
            if (n < Config.FILE_FROM) {
                it.remove();
                continue;
            }
            if (n == Config.FILE_FROM + Config.FILE_LIMIT) {
                it.remove();
                break;
            }
//            System.out.print(getFileName(s) + "\n");
            processFile(s);
        }
        while(it.hasNext()) {
            it.next();
            it.remove();
        }
    }
    
    public void showResults() {
        String noRes = "";
        int noResCount = 0;
        for (String f : files) {
            System.out.print(getFileName(f) + "\t");
            switch (Config.STATE) { 
                case SIMPLE:
                    System.out.println(solutions.get(f).getWeight());
                    if (solutions.get(f).getWeight() == 0) {
                        noRes = f;
                        noResCount++;
                    }
                    break;
                case COMPARE:
                    System.out.println((solutions2.get(f).getWeight() - solutions.get(f).getWeight()));
                    break;
            }
        }
        System.out.println("\nnot solved:");
        System.out.println(noRes + "\n(" + noResCount + ")");
    }
    
    private List<String> listFilesForFolder(String folder) {
        files = new ArrayList();
        String weightsFolderString = Config.WEIGHTING_FOLDER.substring(0, Config.WEIGHTING_FOLDER.length()-1);
        try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath) && !filePath.getParent().endsWith(weightsFolderString)) {
                    files.add(filePath.toString());
                }
            });
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
        return files;
    }
}
