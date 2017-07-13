package knaptool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import knaptool.strategies.*;

/**
 *
 * @author Jaroslav Vesely
 */
public class KnapSolver {
    private static final int        MULTIPLIER  = 1;
    
    private static final boolean    COMPARE     = true;
    private static final double     EPS         = 0.7;
    
    private long start;
    private long end;
    private List<String> files;
    
    private double avgError;
    private double maxError;
    private double avgStateCount;

    public KnapSolver(String path) {
        listFilesForFolder(path);
        prepareMultiple(COMPARE);
    }
    
    private void processLineSpeed(String line) {
        int solution = 0;
        
        Knap problem = new Knap(line, new TabuSearch(2, 4));
        
        for (int i = 0; i < MULTIPLIER; i++) solution = problem.solve();
        avgStateCount += problem.getCounter();
        
//        System.out.println(solution);
    }
    
    private void processLineCompare(String line) {
        double 
            optimal, 
            approx;
        
        Knap compare = new Knap(line, new DynamicByWeight());
        Knap testing = new Knap(line, new TabuSearch(2, 4));
        
        optimal = compare.solve();
        approx = testing.solve();
        double relError = (optimal - approx) / optimal;
        maxError = (maxError < relError) ? relError : maxError;
        avgError += relError;
        
        
//        System.out.println("");
//        System.out.println(compare.getCounter() + " => " + testing.getCounter());
//        System.out.println((int)optimal + " => " + (int)approx);
    }
    
    private void prepareMultiple(boolean compare) {
        
        DecimalFormat f = new DecimalFormat("#0.0000");
        DecimalFormatSymbols decimalFormatSymbols = f.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        f.setDecimalFormatSymbols(decimalFormatSymbols);
        
        for (String s : files) {
            String[] tmp = s.split("\\\\");
            String fileNamePart = tmp[tmp.length - 1];
            System.out.print(fileNamePart + "\t");
            try (BufferedReader br = new BufferedReader(new FileReader(s))) {
                avgError = 0;
                maxError = 0;
                avgStateCount = 0;

                start = getCpuTime();
                
                int n = 0;
                for(String line; (line = br.readLine()) != null; ) {
                    if (compare)
                        processLineCompare(line);
                    else
                        processLineSpeed(line);
                    n++;
                    
//                    if (n > 6) break;
                }

                end = getCpuTime();
                
                if (compare) {
                    avgError /= n;
                    System.out.println(f.format(avgError * 100) + " \t" + f.format(maxError * 100));
                }
                else {
                    avgStateCount /= n;
                    System.out.println((int)Math.round(avgStateCount));
//                    System.out.println("" + (end - start) / MULTIPLIER / 1000);
                }
            
            } catch (IOException ex) {
                System.out.println(ex);
            }
//            break;
        }
    }
    
    // Get CPU time in nanoseconds.
    public long getCpuTime() {
        return System.nanoTime();
    }
    
    private List<String> listFilesForFolder(String folder) {
        files = new ArrayList();
        try(Stream<Path> paths = Files.walk(Paths.get(folder))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
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
