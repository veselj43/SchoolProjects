package weightedsat.CNFLogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import weightedsat.Config;
import weightedsat.Configuration;

/**
 *
 * @author Jaroslav Vesely
 */
public class CNF {
    private List<Clausule> clausules;
    protected int varCount;
    protected int[] weights;
    
    public CNF(String file) {
        load(file);
    }
    
    private void load(String file) {
        loadCNF(file);
        loadWeights(file);
    }
    
    private void loadCNF(String file) {
        clausules = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line; (line = br.readLine()) != null; ) {
                
                line = line.trim();
                if (line.charAt(0) == 'c') continue;
                if (line.charAt(0) == '%') break;
                
                String[] data = line.split(" ");
                if (line.charAt(0) == 'p') {
                    varCount = Integer.parseInt(data[2]);
                    continue;
                }
                
                clausules.add(new Clausule(data));
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    private void loadWeights(String file) {
        weights = new int[varCount];
//        for (int i = 0; i < varCount; i++) weights[i] = 1;
        
        Path path = Paths.get(file);
        String wFile = path.getParent() + "/" + Config.WEIGHTING_FOLDER + path.getFileName();
        
        File f = new File(wFile);
        if(f.exists() && !f.isDirectory()) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                for(String line; (line = br.readLine()) != null; ) {
                    String[] sWeights = line.split(Config.WEIGHTING_SEPARATOR);
                    for (int i = 0; i < varCount; i++) {
                        weights[i] = Integer.parseInt(sWeights[i]);
                    }
                    break;
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        else {
            String sWeights = "";
            for (int i = 0; i < varCount; i++) {
                weights[i] = (int) (Math.random()*(Config.WEIGHT_TO - Config.WEIGHT_FROM) + Config.WEIGHT_FROM);
                sWeights += Integer.toString(weights[i]);
                if (i < varCount - 1) sWeights += Config.WEIGHTING_SEPARATOR;
            }
            
            try {
                f.createNewFile();
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
                out.write(sWeights);
                out.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
    
    public int check(Configuration conf) {
        int satisfied = 0;
        for (Clausule c : clausules) {
            if (c.check(conf)) satisfied++;
        }
        return satisfied;
    }
    
    public int[] getWeights() {
        return weights;
    }

    public int getVarCount() {
        return varCount;
    }
    
    public int getClausuleCount() {
        return clausules.size();
    }
}


//            try {
//                f.createNewFile();
//                FileWriter writer = new FileWriter(f);
//                
//                for (int i = 0; i < varCount; i++) {
//                    weights[i] = (int) (Math.random()*99 + 1);
//                    writer.write(Integer.toString(weights[i]));
//                    if (i < varCount - 1) writer.write(Config.WEIGHTING_SEPARATOR);
//                }
//                
//                writer.flush();
//                writer.close();
//            } catch (IOException ex) {
//                Logger.getLogger(CNF.class.getName()).log(Level.SEVERE, null, ex);
//            }
