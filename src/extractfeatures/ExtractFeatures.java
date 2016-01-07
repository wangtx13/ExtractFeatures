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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import texas.holdem.hand.evaluator.Hand;
import texas.holdem.hand.evaluator.HandEvaluator;
import texas.holdem.hand.evaluator.PotentialEvaluator;

/**
 *
 * @author wangtianxia1
 */
public class ExtractFeatures {

    private HashMap<String, Pair<String, String>> hroster = new HashMap();
    private HashMap<String, Double> totalAggValue_map = new HashMap();
    private HashMap<String, Features> featuresFromOneSet = new HashMap();
    private String name;
    private String identify;
    private int cost_flap;
    private int cost_turn;
    private int cost_river;
    private int pot_flap;
    private int pot_turn;
    private int pot_river;
//    private int pot_showdn;
    private String handCards1;
    private String handCards2;
    private String tableCards1;
    private String tableCards2;
    private String tableCards3;
    private String tableCards4;
    private String tableCards5;
    private String decision_pre;
    private String decision_flap;
    private String decision_turn;
    private String decision_river;
    private double shortAggIndex;
    private double longAggIndex;
    private int currentValue;
    private double potentialValue;

    public ExtractFeatures() {
    }

    public HashMap<String, Features> ExtractedAllFeatures(String filePath) {
//        String filePath = "./holdem/199504";
        File file_hroster = new File(filePath + "/hroster");
        File file_hdb = new File(filePath + "/hdb");

        String[] split_originalPath = filePath.split("/");
        String className = split_originalPath[split_originalPath.length - 2];

        String line_hroster = null;
        String line_hdb = null;

        //hroster
        if (file_hroster.exists()) {
            try {
                try (
                        InputStream in_hroster = new FileInputStream(file_hroster.getPath());
                        BufferedReader reader_hroster = new BufferedReader(new InputStreamReader(in_hroster));) {
                    while ((line_hroster = reader_hroster.readLine()) != null) {
                        String[] split_hroster = line_hroster.split(" ");

                        String[] feature_hroster = new String[4];
                        int index_hroster = 0;
                        for (int i = 0; i < split_hroster.length && index_hroster < 4; ++i) {
                            if (!split_hroster[i].equals("")) {
                                feature_hroster[index_hroster] = split_hroster[i];
                                ++index_hroster;
                            }

                        }

                        String timestamp_hroster = "";
                        int playersNo = 0;
                        String player = "";
                        String against = "";

                        if (split_hroster.length < 5) {
                            System.out.println(" Wrong line: " + line_hroster);
                        } else {

                            if (feature_hroster[1].length() < 4) {
                                playersNo = Integer.parseInt(feature_hroster[1]);
                            }
//                            System.out.println(feature_hroster[1]);
//                            System.out.println(playersNo);
                            if (playersNo == 2) {
                                timestamp_hroster = feature_hroster[0];
                                player = feature_hroster[2];
                                against = feature_hroster[3];
                                hroster.put(timestamp_hroster, new Pair(player, against));
                            }
                        }
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(ExtractFeatures.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            System.out.println(" " + file_hroster.getName() + " doesn't exist");
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
                            String relevantPlayer = hroster.get(split_hdb[0]).getKey();
                            String relevantAgainst = hroster.get(split_hdb[0]).getValue();

                            //player and against file
                            File file_player = new File(filePath + "/pdb/pdb." + relevantPlayer);
                            File file_against = new File(filePath + "/pdb/pdb." + relevantAgainst);
//                            String line_player = null;
//                            String line_against = null;

                            String currentCards = "";
                            Features features = new Features();
                            boolean emptyHand_player = extractInfoFromPlayer(file_player, split_hdb, relevantPlayer);
                            if (!emptyHand_player) {
                                extractInfoFromHdb(split_hdb);

                                int pot0 = calculatePot0(className);
                                currentCards = tansforCards(handCards1 + " " + handCards2, null);
                                currentValue = calculateCurretnValue(currentCards);
                                potentialValue = calculatePotentialValue(currentCards);
                                features = putIntoFeatures(handCards1, handCards2, null,
                                        null, null, null, null, currentValue, potentialValue, pot0, 0, decision_pre);
                                featuresFromOneSet.put(identify + "_pre", features);

                                currentCards = tansforCards(handCards1 + " " + handCards2,
                                        tableCards1 + " " + tableCards2 + " " + tableCards3);
                                currentValue = calculateCurretnValue(currentCards);
                                potentialValue = calculatePotentialValue(currentCards);
                                //10-20post_flap和cost_flap无变化
                                //20-40
//                                pot_flap = pot_flap / 2;
//                                cost_flap = cost_flap / 2;
                                //50-100
                                //20-40
//                                pot_flap = pot_flap/5;
//                                cost_flap = cost_flap/5;
                                pot_flap = calculatePotAndCost(className, pot_flap);
                                cost_flap = calculatePotAndCost(className, cost_flap);
                                features = putIntoFeatures(handCards1, handCards2, tableCards1,
                                        tableCards2, tableCards3, null, null,
                                        currentValue, potentialValue, pot_flap, cost_flap, decision_flap);
                                featuresFromOneSet.put(identify + "_flap", features);

                                currentCards = tansforCards(handCards1 + " " + handCards2,
                                        tableCards1 + " " + tableCards2 + " " + tableCards3 + " " + tableCards4);
                                currentValue = calculateCurretnValue(currentCards);
                                potentialValue = calculatePotentialValue(currentCards);
                                //10-20post_turn和cost_turn无变化
                                //20-40
//                                pot_turn = pot_turn/2;
//                                cost_turn = cost_turn/2;
                                //50-100
                                //20-40
//                                pot_turn = pot_turn/5;
//                                cost_turn = cost_turn/5;
                                pot_turn = calculatePotAndCost(className, pot_turn);
                                cost_turn = calculatePotAndCost(className, cost_turn);
                                features = putIntoFeatures(handCards1, handCards2, tableCards1,
                                        tableCards2, tableCards3, tableCards4, null,
                                        currentValue, potentialValue, pot_turn, cost_turn, decision_turn);
                                featuresFromOneSet.put(identify + "_turn", features);

                                currentCards = tansforCards(handCards1 + " " + handCards2,
                                        tableCards1 + " " + tableCards2 + " " + tableCards3 + " " + tableCards4 + " " + tableCards5);
                                currentValue = calculateCurretnValue(currentCards);
                                potentialValue = calculatePotentialValue(currentCards);
                                //10-20post_river和cost_river无变化
                                //20-40
//                                pot_river = pot_river / 2;
//                                cost_river = cost_river / 2;
                                //50-100
                                //20-40
//                                pot_turn = pot_turn/5;
//                                cost_turn = cost_turn/5;
                                pot_river = calculatePotAndCost(className, pot_river);
                                cost_river = calculatePotAndCost(className, cost_river);
                                features = putIntoFeatures(handCards1, handCards2, tableCards1,
                                        tableCards2, tableCards3, tableCards4, tableCards5,
                                        currentValue, potentialValue, pot_river, cost_river, decision_river);
                                featuresFromOneSet.put(identify + "_river", features);

//                                Iterator<Map.Entry<String, Features>> it = featuresFromOneSet.entrySet().iterator();
//                                while (it.hasNext()) {
//                                    Map.Entry<String, Features> entry = it.next();
//                                    int[] test = new int[4];
//                                            test = entry.getValue().getPot();
//                                    System.out.println(test);
//                                    for (int s : test) {
//                                        System.out.println("pot:" + s);
//                                    }
//                                    System.out.println("t: " + entry.getValue().getTableCards());
//                                }
//                                System.out.println(featuresFromOneSet.get(identify));
                            }

                            boolean emptyHand_against = extractInfoFromPlayer(file_against, split_hdb, relevantAgainst);
                            if (!emptyHand_against) {
                                extractInfoFromHdb(split_hdb);

                                int pot0 = calculatePot0(className);
                                currentCards = tansforCards(handCards1 + " " + handCards2, null);
                                currentValue = calculateCurretnValue(currentCards);
                                potentialValue = calculatePotentialValue(currentCards);
                                features = putIntoFeatures(handCards1, handCards2, null,
                                        null, null, null, null, currentValue, potentialValue, pot0, 0, decision_pre);
                                featuresFromOneSet.put(identify + "_pre", features);

                                currentCards = tansforCards(handCards1 + " " + handCards2,
                                        tableCards1 + " " + tableCards2 + " " + tableCards3);
                                currentValue = calculateCurretnValue(currentCards);
                                potentialValue = calculatePotentialValue(currentCards);
                                //10-20post_flap和cost_flap无变化
                                //20-40
//                                pot_flap = pot_flap / 2;
//                                cost_flap = cost_flap / 2;
                                //50-100
                                //20-40
//                                pot_flap = pot_flap/5;
//                                cost_flap = cost_flap/5;
                                pot_flap = calculatePotAndCost(className, pot_flap);
                                cost_flap = calculatePotAndCost(className, cost_flap);
                                features = putIntoFeatures(handCards1, handCards2, tableCards1,
                                        tableCards2, tableCards3, null, null,
                                        currentValue, potentialValue, pot_flap, cost_flap, decision_flap);
                                featuresFromOneSet.put(identify + "_flap", features);

                                currentCards = tansforCards(handCards1 + " " + handCards2,
                                        tableCards1 + " " + tableCards2 + " " + tableCards3 + " " + tableCards4);
                                currentValue = calculateCurretnValue(currentCards);
                                potentialValue = calculatePotentialValue(currentCards);
                                //10-20post_turn和cost_turn无变化
                                //20-40
//                                pot_turn = pot_turn / 2;
//                                cost_turn = cost_turn / 2;
                                //50-100
                                //20-40
//                                pot_turn = pot_turn/5;
//                                cost_turn = cost_turn/5;
                                pot_turn = calculatePotAndCost(className, pot_turn);
                                cost_turn = calculatePotAndCost(className, cost_turn);
                                features = putIntoFeatures(handCards1, handCards2, tableCards1,
                                        tableCards2, tableCards3, tableCards4, null,
                                        currentValue, potentialValue, pot_turn, cost_turn, decision_turn);
                                featuresFromOneSet.put(identify + "_turn", features);

                                currentCards = tansforCards(handCards1 + " " + handCards2,
                                        tableCards1 + " " + tableCards2 + " " + tableCards3 + " " + tableCards4 + " " + tableCards5);
                                currentValue = calculateCurretnValue(currentCards);
                                potentialValue = calculatePotentialValue(currentCards);
                                //10-20post_river和cost_river无变化
                                //20-40
//                                pot_river = pot_river / 2;
//                                cost_river = cost_river / 2;
                                //50-100
                                //20-40
//                                pot_turn = pot_turn/5;
//                                cost_turn = cost_turn/5;
                                pot_river = calculatePotAndCost(className, pot_river);
                                cost_river = calculatePotAndCost(className, cost_river);
                                features = putIntoFeatures(handCards1, handCards2, tableCards1,
                                        tableCards2, tableCards3, tableCards4, tableCards5,
                                        currentValue, potentialValue, pot_river, cost_river, decision_river);
                                featuresFromOneSet.put(identify + "_river", features);

                            }

                        }

                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(ExtractFeatures.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println(" " + file_hdb.getName() + " doesn't exist");
        }

        //test
//        System.out.println("Features: ");
//        Iterator<Map.Entry<String, Features>> it = featuresFromOneSet.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, Features> entry = it.next();
//            System.out.println();
//            System.out.println(entry.getKey());
//            System.out.println("h: " + entry.getValue().getMyHand());
//            System.out.println("t: " + entry.getValue().getTableCards());
//            System.out.println("cv: " + entry.getValue().getCurrentValue());
//            System.out.println("pv: " + entry.getValue().getPotentialValue());
//            System.out.println("pot: " + entry.getValue().getPot());
//            System.out.println("d: " + entry.getValue().getDecision());
//        }
        return featuresFromOneSet;

    }

    private boolean extractInfoFromPlayer(File file, String[] split_hdb, String player) {
        String line_player = null;
        boolean emptyHand = true;
        if (file.exists()) {
            //read player file
            try {
                try (
                        InputStream in_player = new FileInputStream(file.getPath());
                        BufferedReader reader_player = new BufferedReader(new InputStreamReader(in_player));) {

                    //save timestamp and actions for player
                    ArrayList<String[]> actions_list = new ArrayList();
                    int line_index = 0;
                    ArrayList<Integer> needCalculateAgg = new ArrayList();
                    while ((line_player = reader_player.readLine()) != null) {
                        String[] split_player = line_player.split(" ");

                        //put features except null into player_features
                        String[] player_features = new String[13];
                        int index_player = 0;
                        for (String s : split_player) {
                            if (!s.equals("")) {
                                if (index_player < 13) {
//                                    System.out.println(s);
                                    player_features[index_player] = s;
                                    ++index_player;
//                                } else {
//                                    System.out.println(line_player);
                                }
                            }
                        }
                        
                        //玩家的操作，也就是决定
                        String actions = player_features[4] + " " + player_features[5] + " " + player_features[6] + " " + player_features[7];

                        //timestamp，相应的玩家操作String
                        String[] oneAction = new String[2];
                        oneAction[0] = player_features[1];//timestamp
                        oneAction[1] = actions;//action
                        actions_list.add(oneAction);
//                      System.out.println(actions_list.get(line_index)[0] + " " + actions_list.get(line_index)[1]);

                        //find relevant player by timestamp                     
                        if (player_features[1]!=null && player_features[1].equals(split_hdb[0])) {
                            //the round of player shows the hand finally手牌不为空
                            if (player_features[11] != null && player_features[12] != null) {
                                //要找的那一行手牌不为空
                                emptyHand = false;

                                name = player;
                                identify = player_features[1] + "_" + player;
//                                System.out.println(identify);
//                                System.out.println(player);
                                decision_pre = player_features[4];//pre flap
                                decision_flap = player_features[5];//flap
                                decision_turn = player_features[6];//turn
                                decision_river = player_features[7];//river
//                                System.out.println("decisions: " + decisions);
                                handCards1 = player_features[11];
                                handCards2 = player_features[12];
//                                System.out.println("handCards: " + handCards);
                                //record index number where need to calculate the aggressive index
                                needCalculateAgg.add(line_index);
                            }

                        }
                        //next line
                        ++line_index;

                    }

                    if (!actions_list.isEmpty() && !needCalculateAgg.isEmpty()) {
                        //calculate nedded short term aggressive index
                        Iterator it_short = needCalculateAgg.iterator();
                        double tenAggIndex = 0;
                        while (it_short.hasNext()) {
                            int index = Integer.parseInt(String.valueOf(it_short.next()));
                            for (int i = index; i < actions_list.size() && i <= index + 10; ++i) {
//                          System.out.println(actions_list.get(i)[0] + " " + actions_list.get(i)[1]);
                                double aggIndex = calculateAggIndex(actions_list.get(i)[1]);
                                tenAggIndex += aggIndex;
//                          System.out.println(aggIndex);
                            }
                            shortAggIndex = tenAggIndex / 10;
//                            System.out.println("shortAggIndex: " + shortAggIndex);
                        }

                        //calculate long term aggressive index and save
                        longAggIndex = 0;
                        double totalAggIndex = 0;
                        if (!totalAggValue_map.containsKey(player)) {
                            //Iterator??
                            for (int i = 0; i < actions_list.size(); ++i) {
                                double aggIndex = calculateAggIndex(actions_list.get(i)[1].toString());
                                totalAggIndex += aggIndex;
                            }
                            longAggIndex = totalAggIndex / actions_list.size();
//                                            System.out.println(actions_list.size());
                            totalAggValue_map.put(player, longAggIndex);

                        } else {
                            longAggIndex = totalAggValue_map.get(player);
                        }
//                        System.out.println("longAggIndex: " + longAggIndex);

                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(ExtractFeatures.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println(" " + file.getName() + " doesn't exist");
        }

        return emptyHand;
    }

    private void extractInfoFromHdb(String[] split_hdb) {

        //时间戳不变
        //put features except null into hdb_feature
        String[] hdb_features = new String[13];
        int index = 0;
        for (String s : split_hdb) {
            if (!s.equals("")) {
                hdb_features[index] = s;
                ++index;
            }
        }

//        System.out.println();
//        System.out.println(split_hdb[0]);
        //get pot size
        String[] pot0Sr = hdb_features[4].split("/");
        pot_flap = Integer.parseInt(pot0Sr[pot0Sr.length - 1]);
        cost_flap = pot_flap;

        String[] pot1Sr = hdb_features[5].split("/");
        pot_turn = Integer.parseInt(pot1Sr[pot1Sr.length - 1]);
        cost_turn = pot_turn - pot_flap;

        String[] pot2Sr = hdb_features[6].split("/");
        pot_river = Integer.parseInt(pot2Sr[pot2Sr.length - 1]);
        cost_river = pot_river - pot_turn;

//        String[] pot3Sr = hdb_features[7].split("/");
//        int p3 = Integer.parseInt(pot3Sr[pot3Sr.length - 1]);
//        pot_showdn = p3 - p2;
        //test pot size   
//        System.out.println(pot_flap);
//        System.out.println(pot_turn);
//        System.out.println(pot_river);
//        System.out.println(pot3);
        //test cost
//        System.out.println(cost_flap);
//        System.out.println(cost_turn);
//        System.out.println(cost_river);
        //get table cards
        tableCards1 = hdb_features[8];
        tableCards2 = hdb_features[9];
        tableCards3 = hdb_features[10];
        tableCards4 = hdb_features[11];
        tableCards5 = hdb_features[12];

//        System.out.println(tableCards);
    }

//    private String[] splitDecision(String c1, String c2, String c3, String decision) {
//        String[] s = new String[3];
//        switch (decision.length()) {
//            case 1:
//                c1 = String.valueOf(decision.charAt(0));
//                c2 = null;
//                c3 = null;
//                break;
//            case 2:
//                c1 = String.valueOf(decision.charAt(0));
//                c2 = String.valueOf(decision.charAt(1));
//                c3 = null;
//                break;
//            case 3:
//                c1 = String.valueOf(decision.charAt(0));
//                c2 = String.valueOf(decision.charAt(1));
//                c3 = String.valueOf(decision.charAt(2));
//                break;
//            default:
//                break;
//        }
//        s[0] = c1;
//        s[1] = c2;
//        s[2] = c3;
//        return s;
//    }
    private String tansforCards(String _handCards, String _tableCards) {
        String currentCards = "";
        if (_tableCards != null) {
            currentCards = _handCards + " " + _tableCards;
        } else {
            currentCards = _handCards;
        }
        return currentCards;
    }

    private int calculateCurretnValue(String currentCards) {
        int value = 0;
        HandEvaluator handEvaluator = new HandEvaluator(new Hand(currentCards));
        value = handEvaluator.getValue();
        return value;
    }

    private double calculatePotentialValue(String currentCards) {
        double value = 0;
        PotentialEvaluator potEvaluator = new PotentialEvaluator(new Hand(currentCards));
        value = potEvaluator.getPotentialValue();
        return value;
    }

    private double calculateAggIndex(String actions) {
        double aggIndex = 0;
        double total = 0;
        char[] actions_char = actions.toCharArray();
        for (char c : actions_char) {
            switch (c) {
                case 'f':
                    ++total;
                    break;
                case 'k':
                    ++total;
                    break;
                case 'c':
                    ++total;
                    aggIndex += 1;
                    break;
                case 'b':
                    ++total;
                    aggIndex += 1;
                    break;
                case 'B':
                    ++total;
                    aggIndex += 1;
                    break;
                case 'r':
                    ++total;
                    aggIndex += 2;
                    break;
                case 'A':
                    ++total;
                    aggIndex += 3;
                    break;
                default:
                    break;
            }
        }

        if (total == 0) {
            return 0;
        } else {
            return aggIndex / total;
        }
    }

    private int calculatePot0(String className) {
        switch (className) {
            case "holdem2":
            case "a_test":
                return 10 + 20;
            case "holdem3":
                return 25 + 50;
            default:
                return 5 + 10;
        }
    }

    private int calculatePotAndCost(String className, int input) {
        switch (className) {
            case "holdem2":
            case "a_test":
                return input / 2;
            case "holdem3":
                return input / 5;
            default:
                return input;
        }
    }

    //若包含fold则保留f
    //若未弃牌，且包含All-in，则保留A
    //若未弃牌、未All-in，且包含bet或raise，则保留r
    //若未弃牌、未All-in、未bet或raise，且包含call，则保留c
    //若未弃牌、未All-in、未bet或raise、未call，且包含check，则保留c
    //若未弃牌、未All-in、未bet或raise、未call、未check，则取最后一个决策
    private String extractDecision(String d) {
        if (d.contains("f")) {
            return "f";
        } else if (d.contains("A")) {
            return "A";
        } else if (d.contains("b") || d.contains("r")) {
            return "r";
        } else if (d.contains("c")) {
            return "c";
        } else if (d.contains("k")) {
            return "k";
        } else {
            return d.substring(d.length() - 1, d.length());
        }
    }

    private Features putIntoFeatures(String h1, String h2, String t1, String t2,
            String t3, String t4, String t5, int cv, double pv, int pot, int cost,
            String d) {
        Features features = new Features();
        features.setName(name);
        features.setMyHand1(h1);
        features.setMyHand2(h2);
        features.setTableCards1(t1);
        features.setTableCards2(t2);
        features.setTableCards3(t3);
        features.setTableCards4(t4);
        features.setTableCards5(t5);
        features.setCurrentValue(cv);
        features.setPotentialValue(pv);
        features.setShortAggIndex(shortAggIndex);
        features.setLongAggIndex(longAggIndex);
        features.setPot(pot);
        features.setCost(cost);
        features.setDecision(extractDecision(d));
        return features;
    }

}
