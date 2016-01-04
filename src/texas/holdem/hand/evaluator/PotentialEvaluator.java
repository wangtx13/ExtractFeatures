/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texas.holdem.hand.evaluator;

import java.util.HashMap;

/**
 *
 * @author wangtianxia1
 */
public class PotentialEvaluator {

    /**
     * The potential value of hand
     */
    private double potentialValue = 0;
    
    private double aboveRoyalFlushWeight = 0;
    private double aboveStraightFlushWeight = 0;
    private double aboveFourOfAKindWeight = 0;
    private double aboveFullHouseWeight = 0;
    private double aboveFlushWeight = 0;
    private double aboveStraightWeight = 0;
    private double aboveThreeOfAKindWeight = 0;
    private double aboveTwoPairWeight = 0;
    private double aboveOnePairWeight = 0;
    private double aboveHighCardWeight = 0;

    /**
     * Constructor.
     *
     * @param currentHand The current hand.
     */
    public PotentialEvaluator(Hand currentHand) {
        double weight = 0;

        double totalValue = 0;
        int[][] rankings = new int[Card.NO_OF_RANKS][Card.NO_OF_SUITS];
        int[] types = new int[10];
        for (int i = 0; i < 10; ++i) {
            types[i] = 0;
        }
        Card[] currentCards = currentHand.getCards();

        for (int i = 0; i < currentHand.size(); ++i) {
            ++rankings[currentCards[i].getRank()][currentCards[i].getSuit()];
        }

        //Calculate the number of cards for each rank
        //Calculate the potential value of current hand
        HashMap<String, Boolean> existing = new HashMap();
        if (currentHand.size() == 2) {
            weight = 1d / ((52 - 2) * (52 - 5) * (52 - 6));
            totalValue = calculatePotentialVAndT(0, 3, currentHand, rankings, existing, types);
        } else if (currentHand.size() == 5) {
            weight = 1d / ((52 - 5) * (52 - 6));
            totalValue = calculatePotentialVAndT(0, 2, currentHand, rankings, existing, types);
        } else if (currentHand.size() == 6) {
            weight = (1d / (52 - 6));
            totalValue = calculatePotentialVAndT(0, 1, currentHand, rankings, existing, types);
        }
        potentialValue = totalValue * weight;
        
//        System.out.println(types[8]);
        
        double totalTypeNumbers = 0;
        for(int i = 0; i < 10; ++i) {
            totalTypeNumbers += types[i];
        }
        totalTypeNumbers = 1;
//        System.out.println(totalTypeNumbers);
        aboveRoyalFlushWeight = getAimTypes(types, 9) / totalTypeNumbers;
        aboveStraightFlushWeight = getAimTypes(types, 8) / totalTypeNumbers;
        aboveFourOfAKindWeight = getAimTypes(types, 7) / totalTypeNumbers;
        aboveFullHouseWeight = getAimTypes(types, 6) / totalTypeNumbers;
        aboveFlushWeight = getAimTypes(types, 5) / totalTypeNumbers;
        aboveStraightWeight = getAimTypes(types, 4) / totalTypeNumbers;
        aboveThreeOfAKindWeight = getAimTypes(types, 3) / totalTypeNumbers;
        aboveTwoPairWeight = getAimTypes(types, 2) / totalTypeNumbers;
        aboveOnePairWeight = getAimTypes(types, 1) / totalTypeNumbers;
        aboveHighCardWeight = getAimTypes(types, 0) / totalTypeNumbers;
    }

    /**
     * Returns the potential value as a double in one situation
     *
     * This method should be used to calculate the potential value.
     *
     * @return the potential value in one situation
     */
    private double calculateHandValue(Hand newHand) {
        HandEvaluator evaluator = new HandEvaluator(newHand);
        int cardValue = evaluator.getValue();
        return cardValue;
    }

    private int calculateHandType(Hand newHand) {
        HandEvaluator evaluator = new HandEvaluator(newHand);
        int handType = evaluator.getType().getValue();
        return handType;
    }

    private double calculatePotentialVAndT(int roundedTime, int needRounds, Hand currentHand, 
            int[][] rankings, HashMap<String, Boolean> exsiting, int[] types) {
        if (roundedTime < needRounds) {
            double value = 0;
            for (int i = 0; i < Card.NO_OF_RANKS; ++i) {
                for (int j = 0; j < Card.NO_OF_SUITS; ++j) {
                    if (rankings[i][j] == 0) {
                        Card newCard = new Card(i, j);
                        Hand newHand = new Hand(currentHand.toString() + ' ' + newCard.toString());
                        rankings[i][j] = 1;
//                        System.out.println(newHand.toString());                    
                        value += calculatePotentialVAndT(roundedTime + 1, needRounds, newHand, 
                                rankings, exsiting, types);
                        rankings[i][j] = 0;
                    }
                }
            }
            return value;
        } else {
            if (exsiting.containsKey(currentHand.toString())) {
                return 0;
            }
            exsiting.put(currentHand.toString(), true);
            types[calculateHandType(currentHand)]++;
            return calculateHandValue(currentHand);
        }
    }

    private double getAimTypes(int[] types, int aimType) {
        int aimTotalNumber = 0;
        for(int i  = 9; i >= aimType; --i) {
            aimTotalNumber += types[i];
        }
        return aimTotalNumber;
    }

    public double getPotentialValue() {
        return potentialValue;
    }

    public double getAboveRoyalFlushWeight() {
        return aboveRoyalFlushWeight;
    }

    public double getAboveStraightFlushWeight() {
        return aboveStraightFlushWeight;
    }

    public double getAboveFourOfAKindWeight() {
        return aboveFourOfAKindWeight;
    }

    public double getAboveFullHouseWeight() {
        return aboveFullHouseWeight;
    }

    public double getAboveFlushWeight() {
        return aboveFlushWeight;
    }

    public double getAboveStraightWeight() {
        return aboveStraightWeight;
    }

    public double getAboveThreeOfAKindWeight() {
        return aboveThreeOfAKindWeight;
    }

    public double getAboveTwoPairWeight() {
        return aboveTwoPairWeight;
    }

    public double getAboveOnePairWeight() {
        return aboveOnePairWeight;
    }

    public double getAboveHighCardWeight() {
        return aboveHighCardWeight;
    }
    
    

}
