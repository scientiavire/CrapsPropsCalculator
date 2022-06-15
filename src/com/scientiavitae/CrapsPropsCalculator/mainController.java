package com.scientiavitae.CrapsPropsCalculator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class mainController {
    
    @FXML
    private Scene scene;
    @FXML
    private TextField tfTotalBets;
    @FXML
    private TextField tfWinnings;
    @FXML
    private TextField tfLosses;
    @FXML
    private TextField tfTotalPayout;

    @FXML
    private void initialize() {
        // Here I'm trying to initialize the textfields so multiple methods can access them without having to look them
        // up in each method. So far it isn't working because I'm referencing a scene that isn't initialized, and I'm
        // not sure how to do that outside of some ActionEvent method...
        
    }
    
    
    
    /* onSelectTextField() method is called whenever user clicks on a bet's text field. This is where the user enters
       the amount they want on the specified bet. When they click on the text field, a number pad will open so they
       can enter a new number. When the numPad is closed, the Bet object and text fields are updated with the new bet amount.
       
    Okay, so here's what I need this method to do:
    - Get the ID or just the source of the call (i.e., which text field was clicked).
      - getSourceID
    - Read the value currently in the Text field.
      - oldValue = tfText
    - I think I'll need to find the Bet object associated with this field.
      - Bet bet = findBet(sourceID)
    - Call a method which will Open the NumPad to get a new number, and return the newValue. This means this method will wait until a new value is obtained.
      - if (oldValue == "" || oldValue == null) { oldValue = 0.0d; }
      - getNewValue(oldValue);
      
    The NumPad getNewValue() method should only get the new number, not compare new vs old; that comparison should be done in the onSelectTextField() method.
    - If there is a number previously present, that should display in the NumPad text field, so it should be passed to the NumPad method.
    - When the user clicks the Enter button on the NumPad, the number in the text field is returned as the new value.
    - If the Clear button is pressed, the newValue is set to 0.
    - If the Cancel button is clicked (Should not have a top bar, i.e., undecorated I think), the NumPad is closed and the calling TF is not updated. The oldValue is returned.
    
    Handling the associated Bet instance
    - If the new value = old value, nothing is updated and the method ends.
    - If the bet previously was "" and now it's something, we need to create a new Bet object with the new value, and update the TF.
    - If the bet previously was something and now it's "", we need to delete the associated Bet object, and update the TF.
    - If the bet previously was something and now it's something else, we need to update the associated Bet object, and updated the TF.
    - Once the Bet and TF have been updated with the correct numbers, the method ends; doesn't return anything.
    
    * */
    public void onSelectTextField(MouseEvent event) {
        double oldValue = 0.0d;
        double newValue = 0.0d;
        
        // Get the source of the caller (a textfield), and grab the text and ID from it.
        TextField tfSource = (TextField) event.getSource();
        String tfSourceIDFull = tfSource.getId();
        String tfSourceText = tfSource.getText();
        String tfSourceID = tfSourceIDFull.substring(10);
        
        /*
        This section was trying to extract the bets covered and other stuff from the tfSourceID, but I moved that to
        the Bet object itself.
        
        int delimiterPosition = 0;
        for (int i = 0; i < tfSourceID.length(); i++) {
            if (tfSourceID.charAt(i) == '=') {
                delimiterPosition = i;
                break;
            }
        }
        String numsCovered = tfSourceID.substring(delimiterPosition + 1);
        tfSourceID = tfSourceID.substring(0, delimiterPosition);
        */
        
        if (tfSourceText.isEmpty()) {
            oldValue = 0.0d;    // If the text field is empty, there's no bet so it should be handled as 0.
        } else {
            oldValue = Double.parseDouble(tfSourceText);    // If the text isn't empty, get the value and convert from Text to Double.
        }
        System.out.println("TextField " + tfSourceID + " accessed. The previous value is: " + oldValue);
        newValue = numPadController.getNewValue(oldValue);
        
        // Now handle the logic of the new value compared to the old
        BetHandler.handleBet(tfSourceID, oldValue, newValue);
        
        // Update the selected textField with the newValue
        if (newValue == 0d) {
            tfSource.setText("");
        } else {
            DecimalFormat df = new DecimalFormat("#.####");
            tfSource.setText(df.format(newValue));
        }
        // Remove focus from the updated text field by cheating (setting focus on the parent instead)
        tfSource.getParent().requestFocus();
    }
    
    
    
    
    /*
    The onCalculate() method is called when the Calculate button is pressed.
    It needs to do the following:
    1. Get the current Roll values
    2. Go through the list of Bets that were made and see if any of the Hop bets match the [die1, die2] combination
        - Add the total of all Hop bets to the "Total Bets" field.
        - Calculate how much the winning bet should be paid (15x the winning bet), add to the "winnings"
    3. Go through the list of Bets and see if any Horn bets match the total of the roll (2, or 3, or 7, etc.).
        - Add the total of all Horn bets to the "Total Bets" field.
        - Calculate how much the winning bets should be paid (depends on the bets made).
    4. Calculate and update the Total Winnings, losses, and total payout.
    5. Highlight the textFields with winning and losing bets in green and red, respectively
    
    
     */
    
    public void onCalculate(MouseEvent event) {
        // First, verify that there is a selected Die1 and Die2
        // Create a new roll object using Die1 and Die2
        // Go through the list of bets made, and total up the winners and losers (can probably go through the list
        //  and if the bet contains the roll total then add to the winners, if not then add to the losses).
        // Should also probably update a flag, for Win, Loss, or None. Likely have to be String "status" or something
        // Display the Total Bets, Winnings, Losses, and Total Payout
        // Color code the appropriate bet text fields
/*
        int die1 = getDieSelection(1);
        int die2 = getDieSelection(2);
        Roll currentRoll = new Roll(die1, die2);
        BetHandler.getWinsAndLosses(currentRoll);
*/
        Node eventSource = (Node) event.getSource();
        scene = eventSource.getScene();
        
        // The Roll dice should be selected already. If the Roll is not valid (one or both die are not selected), just
        // return null and don't do anything. It might be helpful to highlight the dice set not selected, maybe later I can add that...
        if (!Roll.getInstance().isValid()) {
            System.out.println("Error processing calculation: Dice Not Selected.");
            System.out.println("Die 1 is: " + Roll.getInstance().getDie1());
            System.out.println("Die 2 is: " + Roll.getInstance().getDie2());
            return;
        }
        
        
        BetHandler.calculate();
        Double betsTotal = BetHandler.getBetsTotal();
        Double winsTotal = BetHandler.getWinsTotal();
        Double lossesTotal = BetHandler.getLossesTotal();
        ArrayList<String> winnersList = BetHandler.getWinnersList();
        ArrayList<String> losersList = BetHandler.getLosersList();
        colorTextFields(winnersList, losersList);
        updateTotals(betsTotal, winsTotal, lossesTotal);
        
        
    }
    
    // Updates the Totals column with the appropriate data from the BetHandler, and colors accordingly.
    // (I have it color them here, so that when there is no calculation done yet they stay empty and white)
    private void updateTotals(Double betsTotal, Double winsTotal, Double lossesTotal) {
        Double payout = winsTotal - lossesTotal;
        DecimalFormat df = new DecimalFormat("#.####");
        tfTotalBets = (TextField) scene.lookup("#textField_TotalBets");
        tfWinnings = (TextField) scene.lookup("#textField_Winnings");
        tfLosses = (TextField) scene.lookup("#textField_Losses");
        tfTotalPayout = (TextField) scene.lookup("#textField_TotalPayout");
        
        tfTotalBets.setText(df.format(betsTotal));
        tfWinnings.setText(df.format(winsTotal));
        if (winsTotal != 0d) {
            tfWinnings.setStyle("-fx-background-color: Lime; -fx-text-fill: Black");
        } else {
            tfWinnings.setStyle(null);
        }
        tfLosses.setText(df.format(lossesTotal));
        if (lossesTotal != 0d) {
            tfLosses.setStyle("-fx-background-color: Firebrick; -fx-text-fill: White");
        } else {
            tfLosses.setStyle(null);
        }
        tfTotalPayout.setText(df.format(payout));
        if (payout > 0d) {
            tfTotalPayout.setStyle("-fx-background-color: Lime; -fx-text-fill: Black");
        } else if (payout < 0d){
            tfTotalPayout.setStyle("-fx-background-color: Firebrick; -fx-text-fill: White");
        } else {
            tfTotalPayout.setStyle(null);
        }
    
    }
    
    // Takes lists of all the winners and losers from the BetHandler, and colors the winners Green and the losers Red.
    private void colorTextFields(ArrayList<String> winnersList, ArrayList<String> losersList) {
        TextField tfToColor;
        for (String currentID : winnersList) {
            tfToColor = (TextField) scene.lookup("#textField_" + currentID);
            tfToColor.setStyle("-fx-background-color: Lime; -fx-text-fill: Black");
        }
        for (String currentID : losersList) {
            tfToColor = (TextField) scene.lookup("#textField_" + currentID);
            tfToColor.setStyle("-fx-background-color: Firebrick; -fx-text-fill: White");
        }
    }
    
    
    public void onClearAll(MouseEvent event) {
        // Go through the list of Bet objects, and delete all.
        // The delete Bet method should automatically update the Text fields.
        // Might need to go through and reset the colors on all of the text fields?
        // Clear the total fields and reset their colors
        // Deselect all the Dice
        // Could add a feature where once the ClearAll button is pressed, the current state is saved first, then
        //  everything is deleted, and an UNDO button is displayed. This would be helpful for new users who
        //  accidentally hit the ClearAll button instead of the Calculate button.
        
        //BetHandler.deleteAll();
    
        // Get the event's source information and extract the scene info, so I can later scan that scene for nodes
        Node eventSource = (Node) event.getSource();
        scene = eventSource.getScene();
        clearRoll();
        clearAllBets();
    }
    
    
    public void onClickRollDie(MouseEvent event) {
        // When a die is clicked, I want that die to change color from Red (unselected) to Blue (selected).
        // If the die is already Blue (selected), then change it back to Red (unselected).
        // When a die is selected, all other dice should be set to unselected.
        Node eventSource = (Node) event.getSource();
        scene = eventSource.getScene();
    
    
        String dieSourceString = ((Node) event.getSource()).getId();
        String selectedDieValue = dieSourceString.substring(dieSourceString.length() - 1);
        int dieColumn;
        if (dieSourceString.contains("RollDie1")) {
            dieColumn = 1;
        } else {
            dieColumn = 2;
        }
    
        ColorAdjust resetColorAdjust = new ColorAdjust();
        resetColorAdjust.setHue(0.0);
        
        // Set all the dice to Red
        for (int i = 1; i <= 6; i++) {
            Node die = scene.lookup("#die_RollDie" + dieColumn + "_" + i);
            die.setEffect(resetColorAdjust);
        }
        
        
        // Now change the selected die to Blue by using a -67% Hue change
        ColorAdjust selectColorAdjust = new ColorAdjust();
        selectColorAdjust.setHue(-0.67);
        eventSource.setEffect(selectColorAdjust);
    
        // Update the Roll tracker with the new die selection
        if (dieColumn == 1) {   // They selected a die from the left column, die1
            Roll.getInstance().setDie1(Integer.parseInt(selectedDieValue));
        } else {                // They selected a die from the right column, die2
            Roll.getInstance().setDie2(Integer.parseInt(selectedDieValue));
        }
    }
    
    
    // onClearRoll runs when the "Clear Roll" button is pressed. This will reset the dice selections and clear the Total text fields.
    public void onClearRoll(MouseEvent event) {
        // Get the event's source information and extract the parent's scene info, so I can later scan that scene for nodes
        Node eventSource = (Node) event.getSource();
        scene = eventSource.getScene();
        clearRoll();
    }
    
    
    // clearRoll() actually does the clearing of the dice selections and resetting of the Totals text fields.
    public void clearRoll() {
        // Deselect all the dice
        // Clear the Totals fields
        // Clear any coloring of text fields. (and set all Bet status to "None").
        
        // Create a new ColorAdjust to reset the color of the dice images back to their original red.
        ColorAdjust resetColorAdjust = new ColorAdjust();
        resetColorAdjust.setHue(0.0);
        
        // Deselect all the dice: Set all the dice to Red
        for (int i = 1; i <= 6; i++) {
            Node die = scene.lookup("#die_RollDie1_" + i);
            die.setEffect(resetColorAdjust);
        }
        for (int i = 1; i <= 6; i++) {
            Node die = scene.lookup("#die_RollDie2_" + i);
            die.setEffect(resetColorAdjust);
        }
        
        // Deselect all the dice: Reset the Roll tracker, set both die1 and die2 to 0 (unselected)
        Roll.getInstance().setDie1(0);
        Roll.getInstance().setDie2(0);
        
        // Clear the Totals fields and reset the colors to default white
    
    
        tfTotalBets = (TextField) scene.lookup("#textField_TotalBets");
        tfTotalBets.setText("");
        tfTotalBets.setStyle(null);
    
        tfWinnings = (TextField) scene.lookup("#textField_Winnings");
        tfWinnings.setText("");
        tfWinnings.setStyle(null);
    
        tfLosses = (TextField) scene.lookup("#textField_Losses");
        tfLosses.setText("");
        tfLosses.setStyle(null);
    
        tfTotalPayout = (TextField) scene.lookup("#textField_TotalPayout");
        tfTotalPayout.setText("");
        tfTotalPayout.setStyle(null);
    
        // Next we reset all the coloring on any active bets, since they are no longer winners/losers
        ArrayList<String> winnersList = BetHandler.getWinnersList();
        ArrayList<String> losersList = BetHandler.getLosersList();
        TextField tfToColor;
        for (String currentID : winnersList) {
            tfToColor = (TextField) scene.lookup("#textField_" + currentID);
            tfToColor.setStyle(null);
        }
        for (String currentID : losersList) {
            tfToColor = (TextField) scene.lookup("#textField_" + currentID);
            tfToColor.setStyle(null);
        }
    
        
        
    }
    
    public void clearAllBets() {
        ArrayList<String> clearedBets = BetHandler.deleteAll();
        TextField textFieldToClear;
        for (String textFieldID : clearedBets) {
            textFieldToClear = (TextField) scene.lookup("#textField_" + textFieldID);
            textFieldToClear.setText("");
            textFieldToClear.setStyle(null);
        }
    }
    
    
   
    
}
