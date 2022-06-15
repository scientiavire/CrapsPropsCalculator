package com.scientiavitae.CrapsPropsCalculator;

import java.util.ArrayList;

public class BetHandler {
    // Note that everything in here is static because I'm not actually using any instance of BetHandler, just using it
    // to run Bet handling methods, rather than having them in Main or in a fxController.
    
    private static Double betsTotal = 0d;
    private static Double winsTotal = 0d;
    private static Double lossesTotal = 0d;
    private static ArrayList<String> winnersList = new ArrayList<>();
    private static ArrayList<String> losersList = new ArrayList<>();
    
    
    private static ArrayList<Bet> betList = new ArrayList<>();   // List of all the Bet objects created. This should be private and only allow access to Bets through getters for the Calculate function.
    
    
    public static void handleBet(String tfSourceID, Double oldValue, Double newValue) {
        
        Bet currentBet = getCurrentBet(tfSourceID);
        // Possible cases:
        // 1. Bet doesn't exist yet - Create bet
        // 2. Bet exists, now we're setting it to 0 - Delete bet
        // 3. Bet exists, newValue is different from before (but not 0) - Update bet amount to newValue
        // 4. Bet exists, value is the same as before - Do nothing
        // 5. Bet doesn't exist yet, but the newValue is 0 - Do nothing
        
        
        // Case 1 - Bet doesn't exist yet - Create new Bet object
        if (currentBet == null && newValue != 0d) {     // The bet doesn't exist, we want to create one with newValue (unless the newValue is 0, in which case creating an empty Bet is a waste of time and resources).
            createBet(tfSourceID, newValue);
        }
        // Case 2 - Bet exists, now we're setting it to 0 - Delete bet
        else if (currentBet != null && newValue == 0d) {    // The bet was something, now it's 0
            deleteBet(tfSourceID);
        }
        // Case 3. Bet exists, newValue is different from before (but not 0) - Update bet amount to newValue
        else if (currentBet != null && !newValue.equals(oldValue)) {
            updateBetAmount(tfSourceID, newValue);
        }
        // Case 4 and 5 - Not handled, do Nothing.
        
        
    }
    
    private static void createBet(String tfSourceID, Double newValue) {
        System.out.println("createBet method called: tfSourceID = " + tfSourceID + ", newValue = " + newValue);
        Bet newBet = new Bet(tfSourceID, newValue);
        betList.add(newBet);
    }
    
    private static Bet getCurrentBet(String tfSourceID) {
        //int listSize = betList.size();
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
    
    
    private static void updateBetAmount(String tfSourceID, Double newValue) {
        System.out.println("updateBetAmount method called: tfSourceID = " + tfSourceID + ", newValue = " + newValue);
        // Next line gets the old amount for troubleshooting
        Double oldAmount = getCurrentBet(tfSourceID).getBetAmount();
        for (Bet currentBet : betList) {
            if (currentBet.getID().equals(tfSourceID)) {
                currentBet.updateAmount(newValue);
            }
        }
        System.out.println("The current bet of " + getCurrentBet(tfSourceID).getID() + " had an old value of: " + oldAmount + " and a new value of: " + getCurrentBet(tfSourceID).getBetAmount());
    }
    
    
    // Deletes all the Bet objects in the betList array. In doing so, it creates a String list of all the bets and
    // returns it so the mainController knows which textFields to clear.
    public static ArrayList<String> deleteAll() {
        ArrayList<String> betIDList = new ArrayList<>();
        for (Bet currentBet : betList) {
            betIDList.add(currentBet.getID());
            //deleteBet(currentBet.getID());
            
        }
        betList.removeAll(betList);
        return betIDList;
    }
    
    // This should erase the Bet object associated with a certain Text field.
    // This should run whenever the text is empty, or updated to 0.
    private static void deleteBet(String tfSourceID) {
        System.out.println("deleteBet method called: tfSourceID = " + tfSourceID);
        betList.removeIf(currentBet -> currentBet.getID().equals(tfSourceID));
    }
    
    // Extract the betType from the textField ID.
    private static String extractBetType(String tfSourceID) {
        String extractedBetType = "";
        
        return extractedBetType;
    }
    
/*

    private static int[] extractNumsCovered() {
        int[] numsCovered;
        
        return numsCovered;
    }
*/
    
    
    public static void calculate() {
        int die1 = Roll.getInstance().getDie1();
        int die2 = Roll.getInstance().getDie2();
        int rollTotal = die1 + die2;
        String roll = "";
        double winAmount = 0d;
        int betMultiplier = 15;
        
        // Reset the fields for the new bet set:
        betsTotal = 0d;
        winsTotal = 0d;
        lossesTotal = 0d;
        winnersList = new ArrayList<>();
        losersList = new ArrayList<>();
        
        // We need to go through every Bet in the betList and extract the betType, the numbersCovered, and the amountBet.
        // Then, based on the betType, we compare the Bet numbersCovered to see if they match the Roll.
        // If the bet doesn't match the roll, we add the amountBet to the lossesTotal, and add the betID to the losersList.
        // If the bet matches the roll, we compute the amountBet divided by the number of bets covered (4 for a Horn, 2 for 5's hopping)
        //  and multiply that portion of the amountBet times either 15 or 30 (all bets pay either 15 or 30, except for the special bets.
        //  We also need to add this winning bet to the winsTotal, and add the betID to the winnersList.
        
        for (Bet currentBet : betList) {
            // If the Bet is a Hop bet, then the number we're looking for is a specific dice combination, not dice total.
            
            double betAmount = currentBet.getBetAmount();
            double numBets = currentBet.getNumBetsCovered();
            
            if (currentBet.getBetType().equals("Hop")) {
                if (die1 < die2) {
                    roll = "" + die1 + "-" + die2;
                } else {
                    roll = "" + die2 + "-"  + die1;
                }
            } else {
                roll = "" + rollTotal;
            }
            
            if (currentBet.contains(roll)) {
                // Calculate the win
                if (Roll.getInstance().isPair()) {
                    betMultiplier = 30;
                }
                
                // Handle the special cases of Any Craps, Any Seven, and C&E. These also have numbers they cover like
                //  the other bets (so they win or lose like the other bets when a corresponding roll happens), but the
                //  payouts are different because they cover multiple bets at once:
                //  Any Seven pays 4:1 for any combination of 7.
                //  Any Craps pays 7:1 for any 2, 3, or 12.
                //  C&E pays 3:1 if 2, 3, or 12 rolls, 7:1 if 11 rolls.
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
                                winAmount = betAmount; // This bet pushes (doesn't win or lose) on a 7
                            } else if (rollTotal == 3 || rollTotal == 11) {
                                winAmount = betAmount * 2.2;
                            } else {
                                winAmount = betAmount * 5.2;
                            }
                            break;
                    }
                    
                } else {
                    // Calculate the amount that won
                    winAmount = (betAmount / numBets) * betMultiplier;
                    // Subtract the losing portion of the bet. This won't be added to the losersList, this is just part of this bet.
                    winAmount -= betAmount * ((numBets - 1) / numBets);
                }
                
                // Add the winner to the winsTotal
                winsTotal += winAmount;
                // Add the winner ID to the winnersList
                winnersList.add(currentBet.getID());
                
            } else { // This bet didn't win
                lossesTotal += currentBet.getBetAmount();
                losersList.add(currentBet.getID());
            }
            betsTotal += currentBet.getBetAmount();
        }
        
        
    }
    
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
}
