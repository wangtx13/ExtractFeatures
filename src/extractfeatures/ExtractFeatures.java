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
        String line_hroster = null;
        String line_hdb = null;
        HashMap<String, Pair<String, String>> hroster = new HashMap();
        int[] pot = new int[4];
        String handCards = "";
        String tableCards = "";

        //hroster
        if (file_hroster.exists()) {
            try {
                try (
                        InputStream in_hroster = new FileInputStream(file_hroster.getPath());
                        BufferedReader reader_hroster = new BufferedReader(new InputStreamReader(in_hroster));) {
                    while ((line_hroster = reader_hroster.readLine()) != null) {
                        String[] split_hroster = line_hroster.split(" ");
                        String timestamp = "";
                        int playersNo = 0;
                        String player = "";
                        String against = "";

                        if (split_hroster.length < 5) {
                            System.out.println("Wrong line.");
                        } else {
                            playersNo = Integer.parseInt(split_hroster[2]);
                            if (playersNo == 2) {
                                timestamp = split_hroster[0];
                                player = split_hroster[3];
                                against = split_hroster[4];
                                hroster.put(timestamp, new Pair(player, against));
                            }
                        }
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(ExtractFeatures.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            System.out.println(file_hroster.getName() + " doesn't exist");
        }

        //hdb
        if (file_hdb.exists()) {
            try {
                try (
                        InputStream in_hdb = new FileInputStream(file_hdb.getPath());
                        BufferedReader reader_hdb = new BufferedReader(new InputStreamReader(in_hdb));) {
                    while ((line_hdb = reader_hdb.readLine()) != null) {
                        String[] split_hdb = line_hdb.split(" ");
                        //找处在hroster中包含某时间戳的行
                        if (hroster.containsKey(split_hdb[0])) {
                            String player = hroster.get(split_hdb[0]).getKey();
                            String against = hroster.get(split_hdb[0]).getValue();
                            System.out.println(player);
                            System.out.println(against);

                            //player and against file
                            File file_player = new File(filePath + "/pdb/pdb.test_" + player);
                            File file_against = new File(filePath + "/pdb/pdb.test_" + against);
                            String line_player = null;
                            String line_against = null;

                            if (file_player.exists() && file_against.exists()) {
                                //read player and against file
                                try {
                                    try (
                                            InputStream in_player = new FileInputStream(file_player.getPath());
                                            BufferedReader reader_player = new BufferedReader(new InputStreamReader(in_player));
                                            InputStream in_against = new FileInputStream(file_against.getPath());
                                            BufferedReader reader_against = new BufferedReader(new InputStreamReader(in_against));) {
                                        while ((line_player = reader_player.readLine()) != null) {
                                            String[] split_player = line_player.split(" ");

                                            //put features except null into player_features
                                            String[] player_features = new String[13];
                                            int index_player = 0;
                                            for (String s : split_player) {
                                                if (!s.equals("")) {
                                                    player_features[index_player] = s;
                                                    ++index_player;
                                                }
                                            }

                                            //player show the hand finally
                                            if (player_features[11] != null && player_features[12] != null) {
                                                //test
//                                                for (String s : player_features) {
//                                                    System.out.println(s);
//                                                }

                                                //find relevant player using timestamp
                                                if (player_features[1].equals(split_hdb[0])) {
                                                    //test
//                                                    for (String s : player_features) {
//                                                        System.out.println(s);
//                                                    }
                                                    
                                                }

                                                //put features except null into hdb_feature
                                                String[] hdb_features = new String[13];
                                                int index = 0;
                                                for (String s : split_hdb) {
                                                    if (!s.equals("")) {
                                                        hdb_features[index] = s;
                                                        ++index;
                                                    }
                                                }

                                                //get pot size
                                                String[] pot0 = hdb_features[4].split("/");
                                                pot[0] = Integer.parseInt(pot0[pot0.length - 1]);
                                                String[] pot1 = hdb_features[5].split("/");
                                                pot[1] = Integer.parseInt(pot1[pot1.length - 1]);
                                                String[] pot2 = hdb_features[6].split("/");
                                                pot[2] = Integer.parseInt(pot2[pot2.length - 1]);
                                                String[] pot3 = hdb_features[7].split("/");
                                                pot[3] = Integer.parseInt(pot3[pot3.length - 1]);

                                                //test pot size
//                                                for (int s : pot) {
//                                                    System.out.println(s);
//                                                }
                                                //get table cards
                                                for (int i = 8; i < 13 && hdb_features[i] != null; ++i) {
                                                    tableCards += hdb_features[i];
                                                    if (i < 12) {
                                                        tableCards += " ";
                                                    }
                                                }
//                                                System.out.println(tableCards);

                                            }

                                        }
                                        while ((line_against = reader_against.readLine()) != null) {

                                        }
                                    }
                                } catch (IOException ex) {
                                    Logger.getLogger(ExtractFeatures.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                System.out.println(file_against.getName() + " doesn't exist");
                            }

                        }

                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(ExtractFeatures.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println(file_hdb.getName() + "doesn't exist");
        }

    }

}
