/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractfeatures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author wangtianxia1
 */
public class ExtractFeatures {

    public ExtractFeatures() {
    }

    public void ExtractedAllFeatures() {
        String filePath = "./holdem/199504";
        File file_hroster = new File(filePath + "/test_hroster");
        File file_hdb = new File(filePath + "/test_hdb");
        String line_hroster = "";
        String line_hdb = "";
        HashMap<String, Pair<String, String>> hroster = new HashMap();

        //for a line
        String timestamp = "";
        int playersNo = 0;
        String player = "";
        String against = "";
        int[] pot = new int[4];

        //hroster
        try {
            try (
                    InputStream in_hroster = new FileInputStream(file_hroster.getPath());
                    BufferedReader reader_hroster = new BufferedReader(new InputStreamReader(in_hroster));) {
                while ((line_hroster = reader_hroster.readLine()) != null) {
                    String[] line = line_hroster.split(" ");

                    if (line.length < 5) {
                        System.out.println("Wrong line.");
                    } else {
                        playersNo = Integer.parseInt(line[2]);
                        if (playersNo == 2) {
                            timestamp = line[0];
                            player = line[3];
                            against = line[4];
                            hroster.put(timestamp, new Pair(player, against));
                        }
                    }
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(ExtractFeatures.class.getName()).log(Level.SEVERE, null, ex);
        }

        //hdb
        try {
            try (
                    InputStream hdb = new FileInputStream(file_hdb.getPath());
                    BufferedReader reader_hdb = new BufferedReader(new InputStreamReader(hdb));) {
                while ((line_hdb = reader_hdb.readLine()) != null) {
                    String[] line = line_hdb.split(" ");

                    //找处在hroster中包含某时间戳的行
                    if (hroster.containsKey(line[0])) {
                        
                        
                        
                        
                        String[] hdb_features = new String[13];
                        int index = 0;
                        for (String s : line) {
                            if (!s.equals("")) {
                                hdb_features[index] = s;
                                ++index;
                            }
                        }

                        //找出最后能看到底牌的局数，获取每次pot大小
                        if (hdb_features[11] != null && hdb_features[12] != null) {
                            
                            String[] pot0 = hdb_features[4].split("/");
                            pot[0] = Integer.parseInt(pot0[pot0.length-1]);
                            String[] pot1 = hdb_features[5].split("/");
                            pot[1] = Integer.parseInt(pot1[pot1.length-1]);
                            String[] pot2 = hdb_features[6].split("/");
                            pot[2] = Integer.parseInt(pot2[pot2.length-1]);
                            String[] pot3 = hdb_features[7].split("/");
                            pot[3] = Integer.parseInt(pot3[pot3.length-1]);

                            //test
                            for (int s : pot) {
                                System.out.println(s);
                            }
                        }
                    }

                }

            }
        } catch (IOException ex) {
            Logger.getLogger(ExtractFeatures.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
