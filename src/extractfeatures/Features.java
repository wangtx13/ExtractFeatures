/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractfeatures;

/**
 *
 * @author wangtianxia1
 */
public class Features {
    private String myHand;
    private String tableCards;
    private int currentValue;
    private double potentialValue;
    private double shortAggIndex;
    private double longAggIndex;
    private int[] pot = new int[4];
    private String decision;

    public Features() {
    }

    public String getMyHand() {
        return myHand;
    }

    public void setMyHand(String myHand) {
        this.myHand = myHand;
    }

    public String getTableCards() {
        return tableCards;
    }

    public void setTableCards(String tableCards) {
        this.tableCards = tableCards;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public double getPotentialValue() {
        return potentialValue;
    }

    public void setPotentialValue(double potentialValue) {
        this.potentialValue = potentialValue;
    }

    public double getShortAggIndex() {
        return shortAggIndex;
    }

    public void setShortAggIndex(double shortAggIndex) {
        this.shortAggIndex = shortAggIndex;
    }

    public double getLongAggIndex() {
        return longAggIndex;
    }

    public void setLongAggIndex(double longAggIndex) {
        this.longAggIndex = longAggIndex;
    }

    public int[] getPot() {
        return pot;
    }

    public void setPot(int[] pot) {
        this.pot = pot;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
    
    
    
}
