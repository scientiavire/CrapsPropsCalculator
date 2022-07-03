package com.scientiavitae.CrapsPropsCalculator;

import java.util.ArrayList;

/**
 * Class for handling (creating, updating, deleting) Bets.
 * <p>
 * The primary method of this class is the handleBet() method, which takes the bet ID to be updated, the old and new
 * bet amounts, and handles everything (creating a new Bet object, updating a Bet, deleting a Bet) internally.
 * This class also stores a list of all the current active Bets for use in calculations, display, etc.
 *
 * @author ScientiaVire
 * @version 0.3
 * @since 0.2
 */

public class BetHandler {
    // Note that everything in here is static because I'm not actually using any instance of BetHandler, just using it
    // to run Bet handling methods, rather than having them in Main or in a fxController.
    
    private static Double betsTotal = 0d;       // the combined total of all betAmount attributes for all Bet instances contained in betList, used to calculate total payout.
    private static Double winsTotal = 0d;       // the combined total payout of all winning bets, used to calculate total payout.
    private static Double lossesTotal = 0d;     // the combined total losses of all losing bets, used to calculate total payout.
    private static ArrayList<String> winnersList = new ArrayList<>();   // list of bet ID's of all Bet instances that are winners, used to highlight respective text fields Green.
    private static ArrayList<String> losersList = new ArrayList<>();    // list of bet ID's of all Bet instances that are losers, used to highlight respective text fields Red.
    private static ArrayList<Bet> betList = new ArrayList<>();          // List of all the Bet objects created. This should be private and only allow access to Bets through getters for the Calculate function.
    
    
    // ********************
    // Public Methods
    // ********************
    
    
    /**
     * Handles all the Bet changes
     * <p>
     * Handles all Bet logic processing, including creating a new Bet instance, deleting a Bet instance, or changing
     * the Bet amount.
     * <p>
     * Possible cases:
     * 1. Bet doesn't exist yet - Create bet
     * 2. Bet exists, now we're setting it to 0 - Delete bet
     * 3. Bet exists, newValue is different from before (but not 0) - Update bet amount to newValue
     * 4. Bet exists, value is the same as before - Do nothing
     * 5. Bet doesn't exist yet, but the newValue is 0 - Do nothing
     *
     * @param tfSourceID  the ID of the textField this Bet is associated with
     * @param oldValue   the existing (previous) value of the betAmount
     * @param newValue   the new value of the betAmount
     * @return betsTotal  the added total of all betAmounts, for display purposes in the totalBets textField.
     */
    public static double handleBet(String tfSourceID, Double oldValue, Double newValue) {
        Bet currentBet = getCurrentBet(tfSourceID);
        
        // Case 1 - Bet doesn't exist yet - Create new Bet object
        if (currentBet == null && newValue != 0d) {     // The bet doesn't exist, we want to create one with newValue (unless the newValue is 0, in which case creating an empty Bet is a waste of time and resources).
            createBet(tfSourceID, newValue);
        }
        // Case 2 - Bet exists, now we're setting it to 0 - Delete bet
        else if (currentBet != null && newValue == 0d) {    // The bet was something, now it's 0
            deleteBet(tfSourceID);
        }
        // Case 3. Bet exists, newValue is different from before (but not 0) - Update bet amount to newValue
        else if (currentBet != null && !newValue.equals(oldValue)) {    // The bet exists and is betAmount was changed
            updateBetAmount(tfSourceID, newValue);
        }
        // Case 4 and 5 - Not handled, do Nothing.
        
        return betsTotal;
    }
    
    
    /**
     * Deletes all the Bet objects in the betList array.
     * <p>
     * Deletes all current Bets in order to clear the calculator.
     *
     * @return betIDList  a list of all Bet ID's so the controller knows which textFields to clear
     */
    // Deletes all the Bet objects in the betList array.
    // Creates a String list of all the bets and returns it so the mainController knows which textFields to clear.
    public static ArrayList<String> deleteAll() {
        ArrayList<String> betIDList = new ArrayList<>();
        for (Bet currentBet : betList) {
            betIDList.add(currentBet.getID());
        }
        betList.removeAll(betList);
        betsTotal = 0d;
        return betIDList;
    }
    
    
    /**
     * Calculates Bet Totals
     * <p>
     * Reads the current Roll, and goes through the bet list to determine which bets are winners, and which are losers.
     * These winning and losing bets are totalled, and the total payout is calculated.
     * Rather than return several data items, this class' fields are updated, and the information is requested by the
     * controller via getters after the calculate() method is called.
     */
    public static void calculate() {
        int die1 = Roll.getInstance().getDie1();
        int die2 = Roll.getInstance().getDie2();
        int rollTotal = Roll.getInstance().getTotal();
        String roll = "";
        double winAmount = 0d;
        int betMultiplier = 15;
        // The betMultiplier is the standard bet multiplier, most bets on the table pay 15:1, except for pairs and
        // special bets, which are handled separately and a separate bet multiplier is used in those special cases.
        
        
        // Reset the Totals fields for the new bet set:
        betsTotal = 0d;
        winsTotal = 0d;
        lossesTotal = 0d;
        winnersList = new ArrayList<>();
        losersList = new ArrayList<>();
        
        
        /*
            We need to go through every Bet in the betList and extract the betType, the numbersCovered, and amountBet.
            Then, based on the betType, we compare the Bet numbersCovered to see if one matches the Roll.
            If the bet doesn't match the roll, we add amountBet to the lossesTotal, and add the betID to the losersList.
            If the bet matches the roll, we compute the amountBet divided by the number of bets covered (4 for a Horn,
             2 for 5's hopping) and multiply that portion of the amountBet times either 15 or 30 (all bets pay either
             15 or 30, except for the special bets.
             We also need to add this winning bet to the winsTotal, and add the betID to the winnersList.
        */
        for (Bet currentBet : betList) {
            double betAmount = currentBet.getBetAmount();
            double numBets = currentBet.getNumBetsCovered();
            
            // If the Bet is a Hop bet, the number we're looking for is a specific dice combination, not dice total.
            if (currentBet.getBetType().equals("Hop")) {
                // Orient the dice so the lowest die is always first, makes checking a lot simpler.
                // E.g., 5-2 becomes 2-5, 1-3 stays 1-3.
                if (die1 < die2) {
                    roll = "" + die1 + "-" + die2;
                } else {
                    roll = "" + die2 + "-" + die1;
                }
            } else {
                roll = "" + rollTotal;
            }
            
            if (currentBet.contains(roll)) {    // Roll and bet match, so this bet won
                // Calculate the win
                if (Roll.getInstance().isPair()) {
                    betMultiplier = 30;     // Pairs pay 30:1 instead of the usual 15:1
                }
                
                /*
                    Handle the special cases of Any Craps, Any Seven, and C&E. These also have numbers they cover like
                    the other bets (so they win or lose like the other bets when a corresponding roll happens), but the
                    payouts are different because they cover multiple bets differently:
                    Any Seven pays 4:1 for any combination of 7.
                    Any Craps pays 7:1 for any 2, 3, or 12.
                    C&E pays 3:1 if 2, 3, or 12 rolls, 7:1 if 11 rolls.
                 */
                if (currentBet.getBetType().equals("Special")) {
                    String specialBet = currentBet.getID();
                    switch (specialBet) {
                        case "Special_AnyCraps=2,3,12":
                            winAmount = betAmount * 7;
                            break;
                        case "Special_CandE=2,3,11,12":
                            if (rollTotal == 11) {
                                winAmount = betAmount * 7;
                            } else { // 2, 3, or 12 rolled
                                winAmount = betAmount * 3;
                            }
                            break;
                        case "Special_AnySeven=7":
                            winAmount = betAmount * 4;
                            break;
                        case "Special_World=2,3,7,11,12":
                            if (rollTotal == 7) {
                                // This bet pushes (doesn't win or lose) on a 7
                                winAmount = 0;
                            } else if (rollTotal == 3 || rollTotal == 11) {
                                // Multiplier is equivalent to the 1/5 bet that won times 15, minus 4/5 that lost.
                                winAmount = betAmount * 2.2;
                            } else {
                                // Multiplier is equivalent to the 1/5 bet that won times 30, minus 4/5 that lost.
                                winAmount = betAmount * 5.2;
                            }
                            break;
                    }
                    
                } else {
                    // Calculate the amount that won (part of bet that won * amount won, minus losing part of the bet)
                    winAmount = ((betAmount / numBets) * betMultiplier) - (betAmount * ((numBets - 1) / numBets));
                }
                
                // Add the winner to the winsTotal and add the ID to the winnersList
                winsTotal += winAmount;
                winnersList.add(currentBet.getID());
                
            } else { // This bet didn't win
                // Add the loser to the lossesTotal and add the ID to the losersList
                lossesTotal += currentBet.getBetAmount();
                losersList.add(currentBet.getID());
            }
            // Whether a win or a loss, add the bet to the betsTotal for a running total of all bets.
            betsTotal += currentBet.getBetAmount();
        }
    }
    
    
    // ********************
    // Public Getters
    // ********************
    
    
    public static Double getBetsTotal() {
        return betsTotal;
    }
    
    public static Double getWinsTotal() {
        return winsTotal;
    }
    
    public static Double getLossesTotal() {
        return lossesTotal;
    }
    
    public static ArrayList<String> getWinnersList() {
        return winnersList;
    }
    
    public static ArrayList<String> getLosersList() {
        return losersList;
    }
    
    
    // ********************
    // Private Methods
    // ********************
    
    
    // Searches through the betList and returns the requested Bet instance identified by tfSourceID. If no Bets in the betList match, returns null
    private static Bet getCurrentBet(String tfSourceID) {
        String currentBetID;
        
        // Cycle through the list of existing bets to find the selected one
        for (Bet currentBet : betList) {
            currentBetID = currentBet.getID();      // Get the ID of the Bet in the list
            if (currentBetID.equals(tfSourceID)) {  // Compare the Bet ID in the list to the target Bet ID
                return currentBet;                  // If found, return the current Bet in the list
            }
        }
        return null;                                // Target Bet was not found in the list, return null
    }
    
    
    // Creates a new Bet instance using the new betAmount
    private static void createBet(String tfSourceID, Double newValue) {
        System.out.println("createBet method called: tfSourceID = " + tfSourceID + ", newValue = " + newValue);
        Bet newBet = new Bet(tfSourceID, newValue);
        betList.add(newBet);
        betsTotal += newBet.getBetAmount();
    }
    
    
    // Updates the given Bet instance with the new betAmount.
    private static void updateBetAmount(String tfSourceID, Double newValue) {
        System.out.println("updateBetAmount method called: tfSourceID = " + tfSourceID + ", newValue = " + newValue);
        // Next line gets the old amount for troubleshooting
        Double oldAmount = getCurrentBet(tfSourceID).getBetAmount();
        betsTotal = 0d;
        for (Bet currentBet : betList) {
            if (currentBet.getID().equals(tfSourceID)) {
                currentBet.updateAmount(newValue);
            }
            betsTotal += currentBet.getBetAmount();
        }
        System.out.println("The current bet of " + getCurrentBet(tfSourceID).getID() + " had an old value of: " + oldAmount + " and a new value of: " + getCurrentBet(tfSourceID).getBetAmount());
    }
    
    
    // This should erase the Bet object associated with a certain Text field.
    // This should run whenever the text is empty, or updated to 0.
    private static void deleteBet(String tfSourceID) {
        System.out.println("deleteBet method called: tfSourceID = " + tfSourceID);
        betsTotal -= getCurrentBet(tfSourceID).getBetAmount();
        betList.removeIf(currentBet -> currentBet.getID().equals(tfSourceID));
        
    }
    
    
}
