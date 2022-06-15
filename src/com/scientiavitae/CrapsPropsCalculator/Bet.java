package com.scientiavitae.CrapsPropsCalculator;


import java.util.ArrayList;

public class Bet {
    private String id = "";
    private double betAmount = 0d;
    private int numBetsCovered;
    private ArrayList<String> numbersCovered;
    private Double payout;
    private String betType;
    private String[] betTypesList = {"Special", "Hop", "Horn", "PassLine", "ComeLine", "Odds", "Place", "Buy", "DontPass", "DontCome", "LayOdds", "BuyBehind", "Field", "Big6", "Big8", "Fire", "Bonus", "Progressive"};
    
    
    
    public Bet(String textFieldID, double betAmount) {
        this.id = textFieldID;                  // id is the Bet instance's ID, taken from the selected TextField, used as a unique identifier to edit bet amounts.
        this.betAmount = betAmount;             // betAmount is the dollar amount on that bet, which is stored in the selected TextField.
        //this.numbersCovered = numbersCovered;   // numbersCovered is an array of integers that is a list of the numbers covered by the bet. E.g., for a Horn bet, it would be [2,3,11,12].
        //this.numBetsCovered = numbersCovered.length;    // numBetsCovered is just how many numbers this bet covers. E.g., for a Horn bet, it would be 4.
        this.payout = 0d;                       // payout is the amount this bet should pay. This will depend on the number rolled, the bet amount, and the number of bet numbers covered.
                                                // payout may not need to be declared here, I may not need this field at all. I'm not sure yet how to calculate and store the payout values.
    
        this.betType = extractBetType(textFieldID);
        this.numbersCovered = extractNumsCovered(textFieldID);
        this.numBetsCovered = this.numbersCovered.size();
        
        
        
       
    }
    
    // Extract the betType String from the textFieldID
    private String extractBetType(String textFieldID) {
        // Set the betType based on what is contained in the ID string
        boolean typeFound = false;
        for (String checkString : betTypesList) {
            if (textFieldID.contains(checkString)) {
                return checkString;
            }
        }
        // The only way to get here is if the betType was not found
        System.out.println("Error: betType not found in betTypeList");
        return "";
    }
    
    // Extract the numbers covered from the textFieldID and return in an INT array
    private ArrayList<String> extractNumsCovered(String textFieldID) {

        // Go through the textField string. Look for the "=" delimiter. From there, every number covered is separated by
        // a comma. E.g., Horn_2_3_11=2,3,11 or Hop_Easy6s=1-5,2-4
        // Each individual number (like 11) or dice combination (like 1-5) will be stored in an array.
        // The length of that array will represent how many pieces that bet covers. For the Horn 2,3,11 it would be 3
        // and for the Hop Easy 6's it would be 2. For the Horn High 12 it would be 5.
        // There needs to be a special case for the AnyCraps, AnySeven, and C&E bets, as they don't conform to the conventions.
        
        
        // Get to the Delimiter
        ArrayList <String> numsFromTextField = new ArrayList<>();
        
        char delimiter = '=';
        int numbersStart = 0;
        
        for (int i = 0; i < textFieldID.length(); i++) {
            //System.out.println("textFieldID.charAt(" + i + ") = " + textFieldID.charAt(i));
            if (textFieldID.charAt(i) == delimiter) {
                numbersStart = i;
                break;
            }
        }
        int placeHolder = numbersStart;
        delimiter = ',';
        for (int i = numbersStart; i < (textFieldID.length()); i++) {
            if (textFieldID.charAt(i) == delimiter) {
                numsFromTextField.add(textFieldID.substring(placeHolder+1,i));
                placeHolder = i;
            }
        }
        // Add in the last value
        numsFromTextField.add(textFieldID.substring(placeHolder+1,textFieldID.length()));
        return numsFromTextField;
    }
    
    
    public void updateAmount(double newValue) {
        this.betAmount = newValue;
    }
    
    
    
    public String getID() {
        return id;
    }
    
    public double getBetAmount() {
        return betAmount;
    }
    
    public String getBetType() {
        return betType;
    }
    
    public boolean contains(String roll) {
        for (String checkRoll : numbersCovered) {
            if (checkRoll.equals(roll)) {
                return true;
            }
        }
        return false;
    }
    
    public int getNumBetsCovered() {
        return this.numBetsCovered;
    }
    
    // Maybe use Bet objects for each bet actually made. Have a method contains(numberRolled) which returns T/F if it
    // has the rolled number.
    // Store all the Bet objects in a list and run through them to find which ones contain the number rolled
    // If it contains that number then the bet won, and if not it loses.
    // Have an attribute of how much is on each number (e.g. 1/4 of a Horn is on each number), then add that number to
    // figure the payout.
    // How do I update the appropriate Bet object when the text field is updated?
    // Once the text field dialog is closed, then cycle through all the Bets and update the counters.
    // Once text is entered in the textField, create a new Bet object.
    // Controller or Main should have a method for createNewBet(bet?) and one updateBet(bet?)
    
    // I think that the above constructors should work, when a Horn bet is created, it should createNewBet(), which will
    // need to use something like the TextField text or something? How do I create new Bets like that without hard-coding
    // the name of the variable? I don't really need to name them I suppose, but then how do I make sure I can access
    // the right one?
    // How do I cycle through the Bets in the list, if I don't know how many parameters each bet has? Oh, I can overload
    //  the methods as well, and have one method for 2 parameters, one for 3, one for 4, etc. The appropriate method
    //  will run based on the number of parameters it has.
    
}
