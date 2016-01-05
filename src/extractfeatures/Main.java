/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractfeatures;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author wangtianxia1
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        ExtractFeatures features = new ExtractFeatures();
//        features.ExtractedAllFeatures();

        
        String inputRootFilePath = "./data";
        File inputRootFile = new File(inputRootFilePath);
        ArrayList<String> path = new ArrayList<>();
        if (!inputRootFile.isDirectory()) {
            System.out.println("Please input a extisted directory.");
        } else {
            TraversalFiles.fileList(inputRootFile, 0, path);
        }

    }

}
