/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractfeatures;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author apple
 */
public class TraversalFiles {

    //traversal all files
    public static void fileList(File inputFile, int node, ArrayList<String> path) {

        node++;
        File[] files = inputFile.listFiles();
        if (!inputFile.exists()) {
            System.out.println("File doesn't exist!");
        } else if (node <= 2 && inputFile.isDirectory()) {
            path.add(inputFile.getName());

            for (File f : files) {
                //不处理 .DS_Store
                if (!f.getName().equals(".DS_Store")) {
//                    for (int i = 0; i < node - 1; i++) {
//                        System.out.print(" ");
//                    }

                    //处理data下两层的日期文件夹中的文件
                    if (node == 2) {
                        String filePath = f.getPath();
                        System.out.print("|-" + filePath);
                        ExtractFeatures features = new ExtractFeatures();
                        HashMap<String, Features> featuresFromOneSet = new HashMap();
                        featuresFromOneSet = features.ExtractedAllFeatures(filePath);  
                        WriteFeatures writeFeatures = new WriteFeatures();
                        writeFeatures.write(filePath, featuresFromOneSet);
                        System.out.println(" => extracted");
                    }

                    fileList(f, node, path);
                }

            }
            path.remove(node - 1);

        }
    }

}
