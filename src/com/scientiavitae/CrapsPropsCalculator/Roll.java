package com.scientiavitae.CrapsPropsCalculator;


/**
 * Class for storing Roll information, and which die selected contains what value.
 * <p>
 * Each Roll is a single instance, there should only be one Roll instance at any time.
 *
 * @author ScientiaVire
 * @version 0.2
 * @since 0.2
 */


public class Roll {
    private static Roll instance = new Roll();
    private int die1;
    private int die2;
    
    // Constructor
    private Roll() {
        this.die1 = 0;
        this.die2 = 0;
    }
    
    
    // ********************
    // Public Methods
    // ********************
    
    
    /**
     * Checks the Roll to make sure it is valid, i.e. two dice are selected.
     *
     * @return isValid  returns false if one or both die are set to 0 (unselected), True otherwise
     */
    public boolean isValid() {
        if (die1 == 0 || die2 == 0) { return false; }
        return true;
    }
    
    
    /**
     * Checks the Roll to see if it is a Pair, i.e., both dice are the same value.
     *
     * @return isPair  returns true if the dice selected are the same, otherwise false
     */
    public boolean isPair() {
        if (die1 == die2) { return true; }
        return false;
    }
    
    
    // ********************
    // Public Getters and Setters
    // ********************
    
    
    public static Roll getInstance() {
        return instance;
    }
    
    // Returns the total of the dice selected. Returns 0 if one or no dice are selected.
    public int getTotal() {
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
    
    
    
}
