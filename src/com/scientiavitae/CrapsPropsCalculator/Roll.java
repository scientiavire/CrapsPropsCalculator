package com.scientiavitae.CrapsPropsCalculator;

public class Roll {
    private static Roll instance = new Roll();
    private int die1;
    private int die2;
    
    private Roll() {
        this.die1 = 0;
        this.die2 = 0;
    }
    
    public static Roll getInstance() {
        return instance;
    }
    
    // Returns the total of the dice selected. Returns 0 if one or no dice are selected.
    public int total() {
        if (isValid()) { return this.die1 + this.die2; }
        return 0;
    }
    
    public int getDie1() {
        return die1;
    }
    
    public int getDie2() {
        return die2;
    }
    
    public void setDie1(int die1) {
        this.die1 = die1;
    }
    
    public void setDie2(int die2) {
        this.die2 = die2;
    }
    
    // Returns false if one or both die are set to 0 (unselected). True otherwise.
    public boolean isValid() {
        if (die1 == 0 || die2 == 0) { return false; }
        return true;
    }
    
    public boolean isPair() {
        if (die1 == die2) { return true; }
        return false;
    }
    
}
