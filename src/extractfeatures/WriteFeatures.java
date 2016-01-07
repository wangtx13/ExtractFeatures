/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractfeatures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import texas.holdem.hand.evaluator.Hand;
import texas.holdem.hand.evaluator.HandEvaluator;
import texas.holdem.hand.evaluator.PotentialEvaluator;

/**
 *
 * @author yangxiaohan
 */
public class WriteFeatures {

    public WriteFeatures() {
    }

    public void write(String originalPath, HashMap<String, Features> featuresFromOneSet) {

        String[] split_originalPath = originalPath.split("/");
        String fileName = split_originalPath[split_originalPath.length - 2] + "_"
                + split_originalPath[split_originalPath.length - 1];
        String extractedFilePath = "./b_output/" + fileName + ".txt";

        //create output file for extracted comments
        File extractedFeaturesFile = new File(extractedFilePath);
        try {
            if (extractedFeaturesFile.createNewFile()) {
                System.out.println();
                System.out.println("Create successful: " + extractedFeaturesFile.getName());
            }
        } catch (IOException ex) {
            Logger.getLogger(WriteFeatures.class.getName()).log(Level.SEVERE, null, ex);
        }

        StringBuffer allFeatures = new StringBuffer();

        Iterator<Map.Entry<String, Features>> it = featuresFromOneSet.entrySet().iterator();
        while (it.hasNext()) {
            Features features = new Features();
            Map.Entry<String, Features> entry = it.next();
//            String key = entry.getKey();
            features = entry.getValue();
//            System.out.println(key);
//
            //get features
            String name = features.getName();
            String myHand1 = features.getMyHand1();
            String myHand2 = features.getMyHand2();
            String tableCards1 = features.getTableCards1();
            String tableCards2 = features.getTableCards2();
            String tableCards3 = features.getTableCards3();
            String tableCards4 = features.getTableCards4();
            String tableCards5 = features.getTableCards5();
            int currentValue = features.getCurrentValue();
            double potentialValue = features.getPotentialValue();
            double shortAggIndex = features.getShortAggIndex();
            double longAggIndex = features.getLongAggIndex();
            int pot = features.getPot();
            int cost = features.getCost();
            String decision = features.getDecision();

            allFeatures.append(name);
            allFeatures.append(" ");
            allFeatures.append(myHand1);
            allFeatures.append(" ");
            allFeatures.append(myHand2);
            allFeatures.append(" ");
            allFeatures.append(tableCards1);
            allFeatures.append(" ");
            allFeatures.append(tableCards2);
            allFeatures.append(" ");
            allFeatures.append(tableCards3);
            allFeatures.append(" ");
            allFeatures.append(tableCards4);
            allFeatures.append(" ");
            allFeatures.append(tableCards5);
            allFeatures.append(" ");
            allFeatures.append(currentValue);
            allFeatures.append(" ");
            allFeatures.append(potentialValue);
            allFeatures.append(" ");
            allFeatures.append(shortAggIndex);
            allFeatures.append(" ");
            allFeatures.append(longAggIndex);
            allFeatures.append(" ");
            allFeatures.append(pot);
            allFeatures.append(" ");
            allFeatures.append(cost);
            allFeatures.append(" ");
            allFeatures.append(decision);
            allFeatures.append("\n");

        }

        //write
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(extractedFeaturesFile.getPath()))) {
                writer.write(allFeatures.toString());
            }

        } catch (IOException ex) {
            Logger.getLogger(WriteFeatures.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
