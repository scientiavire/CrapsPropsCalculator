package com.scientiavitae.CrapsPropsCalculator;

import java.util.ArrayList;

/**
 * Object class for storing Bet information.
 * <p>
 * Any time the user creates a new bet, a new Bet object is created containing the bet amount, the numbers covered by
 * the bet, the bet ID and type.
 *
 * @author ScientiaVire
 * @version 0.3
 * @since 0.2
 */

public class Bet {
    private String id = "";                     // this Bet instance's ID, taken from the selected TextField, used as a unique identifier to edit this Bet's attributes.
    private double betAmount = 0d;              // dollar amount on this bet, which is stored in the selected TextField.
    private int numBetsCovered;                 // number of bets covered by this bet combination (e.g., the Horn covers 4 numbers).
    private ArrayList<String> numbersCovered;   // list of all the numbers covered by this bet (e.g., the Horn covers 2, 3, 11, 12).
    private String betType;                     // the type of bet, which determines how the bet calculation is handled. The type must be listed in the betTypesList below.
    private String[] betTypesList = {"Special", "Hop", "Horn", "PassLine", "ComeLine", "Odds", "Place", "Buy", "DontPass", "DontCome", "LayOdds", "BuyBehind", "Field", "Big6", "Big8", "Fire", "Bonus", "Progressive"};
    
    
    /**
     * Constructor
     * <p>
     * Creates a new Bet instance with the appropriate ID, betAmount, betType, and numbers covered by the Bet.
     *
     * @param textFieldID the ID of the textField this Bet is associated with, so we know what bet it is (Horn, Any Seven, etc.)
     * @param betAmount   the amount the user is placing on this bet
     */
    public Bet(String textFieldID, double betAmount) {
        this.id = textFieldID;
        this.betAmount = betAmount;
        this.betType = extractBetType(textFieldID);
        this.numbersCovered = extractNumsCovered(textFieldID);
        this.numBetsCovered = this.numbersCovered.size();
    }
    
    
    /**
     * Extract the betType String from the textFieldID
     *
     * @param textFieldID the ID of the textField this Bet is associated with
     * @return checkString  a String containing the Bet type
     */
    private String extractBetType(String textFieldID) {
        // Set the betType based on what is contained in the ID string
        boolean typeFound = false;
        for (String checkString : betTypesList) {
            if (textFieldID.contains(checkString)) {
                return checkString;
            }
        }
        // If the betType was not found, return null
        System.out.println("Error: betType not found in betTypeList");
        return null;
    }
    
    /**
     * Extract the numbers covered from the textFieldID and return in an INT array
     *
     * @param textFieldID the ID of the textField this Bet is associated with
     * @return numsFromTextField  the specific numbers or dice combinations that this Bet covers
     */
    private ArrayList<String> extractNumsCovered(String textFieldID) {

        /* Go through the textField string. Look for the "=" delimiter. From there, every number covered is separated by
           a comma. E.g., Horn_2_3_11=2,3,11 or Hop_Easy6s=1-5,2-4
           Each individual number (like 11) or dice combination (like 1-5) will be stored in an array.
           The length of that array will represent how many pieces that bet covers. For the Horn 2,3,11 it would be 3
           and for the Hop Easy 6's it would be 2. For the Horn High 12 it would be 5.
           There needs to be a special case for the AnyCraps, AnySeven, and C&E bets, as they don't conform to these conventions.
        */
        
        ArrayList<String> numsFromTextField = new ArrayList<>();
        char delimiter = '=';
        int numbersStart = 0;
        
        // Scan through the textFieldID to get to the Delimiter
        for (int i = 0; i < textFieldID.length(); i++) {
            //System.out.println("textFieldID.charAt(" + i + ") = " + textFieldID.charAt(i));
            if (textFieldID.charAt(i) == delimiter) {
                numbersStart = i;
                break;
            }
        }
        
        // Once we got to the '=' delimiter, from here the numbers are separated by ','.
        int placeHolder = numbersStart;
        delimiter = ',';
        
        // Scan through the rest of the textFieldID to extract each of the numsCovered.
        for (int i = numbersStart; i < (textFieldID.length()); i++) {
            if (textFieldID.charAt(i) == delimiter) {
                numsFromTextField.add(textFieldID.substring(placeHolder + 1, i));
                placeHolder = i;
            }
        }
        
        // Add in the last value
        numsFromTextField.add(textFieldID.substring(placeHolder + 1, textFieldID.length()));
        return numsFromTextField;
    }
    
    
    /**
     * Updates this Bet's betAmount with the given new value.
     *
     * @param newValue the new amount the user entered for this Bet
     */
    public void updateAmount(double newValue) {
        this.betAmount = newValue;
    }
    
    
    /**
     * Checks if this Bet contains the Roll value (either the Roll total or the Roll dice combination)
     *
     * @param roll the roll to check
     * @return true if this Bet contains the requested roll, otherwise false
     */
    public boolean contains(String roll) {
        for (String checkRoll : numbersCovered) {
            if (checkRoll.equals(roll)) {
                return true;
            }
        }
        return false;
    }
    
    
    // ********************
    // Public Getters
    // ********************
    
    
    public String getID() {
        return this.id;
    }
    
    public double getBetAmount() {
        return this.betAmount;
    }
    
    public String getBetType() {
        return this.betType;
    }
    
    public int getNumBetsCovered() {
        return this.numBetsCovered;
    }
    
}
