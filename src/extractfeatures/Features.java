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

    private String name;
    private String myHand1;
    private String myHand2;
    private String tableCards1;
    private String tableCards2;
    private String tableCards3;
    private String tableCards4;
    private String tableCards5;
    private int currentValue;
    private double potentialValue;
    private double shortAggIndex;
    private double longAggIndex;
    private int pot;
    private int cost;
    private String decision;

    public Features() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getMyHand1() {
        return myHand1;
    }

    public void setMyHand1(String myHand1) {
        this.myHand1 = myHand1;
    }

    public String getMyHand2() {
        return myHand2;
    }

    public void setMyHand2(String myHand2) {
        this.myHand2 = myHand2;
    }

    public String getTableCards1() {
        return tableCards1;
    }

    public void setTableCards1(String tableCards1) {
        this.tableCards1 = tableCards1;
    }

    public String getTableCards2() {
        return tableCards2;
    }

    public void setTableCards2(String tableCards2) {
        this.tableCards2 = tableCards2;
    }

    public String getTableCards3() {
        return tableCards3;
    }

    public void setTableCards3(String tableCards3) {
        this.tableCards3 = tableCards3;
    }

    public String getTableCards4() {
        return tableCards4;
    }

    public void setTableCards4(String tableCards4) {
        this.tableCards4 = tableCards4;
    }

    public String getTableCards5() {
        return tableCards5;
    }

    public void setTableCards5(String tableCards5) {
        this.tableCards5 = tableCards5;
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

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }
    
    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    
}
