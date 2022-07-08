package com.scientiavitae.CrapsPropsCalculator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
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


/**
 * Main window Controller
 * <p>
 * This Controller handles the interaction between the JavaFX Main Window and the Bet Handler logic.
 * The main window is created using the main_window.fxml file, and text fields are used to handle bet amounts.
 * The window includes a section on the right where the user can select two dice to represent the dice Roll.
 * The window includes a Calculate button to calculate the total payout based on all the bets in the text fields.
 * The window includes a Clear Roll button to clear the current dice selection, and a button to Clear All bets.
 *
 * @author ScientiaVire
 * @version 0.3
 * @since 0.2
 */

public class mainController {
    
    @FXML
    private Scene scene;                // The scene where this show takes place
    @FXML
    private TextField tfTotalBets;      // The text field used to display the total of all current bets
    @FXML
    private TextField tfWinnings;       // The text field used to display the total payout of all winning bets
    @FXML
    private TextField tfLosses;         // The text field used to display the total of all losing bets
    @FXML
    private TextField tfTotalPayout;    // The text field used to display the total payout after all calculations
    
    // Styles used for the winning and losing bets. To change colors/style, alter these here.
    String winTFStyle = "-fx-background-color: Lime; -fx-text-fill: Black";
    String lossTFStyle = "-fx-background-color: Firebrick; -fx-text-fill: White";
    
    
    // ********************
    // Public Methods
    // ********************
    
    
    /**
     * Handles user clicking any bet-associated text field
     * <p>
     * When the user clicks on a bet text field, the method gets the source of the caller to update that text field.
     * A new number pad window is opened where the user can enter a new bet value.
     * The number pad window is closed, the bet text field is updated with the new bet value.
     * The Total Bets text field is also updated to keep a running total of all bets made.
     *
     * @param event the source of the MouseEvent, used to identify which text field was clicked, and access the scene
     */
    public void onSelectTextField(MouseEvent event) {
        double oldValue = 0.0d;     // The previous bet value stored in the text field (if any)
        double newValue = 0.0d;     // The new bet value in the text field
        Double betTotal = 0d;
        DecimalFormat df = new DecimalFormat("#.####");
        
        
        // Get the current scene, so we can find desired text fields other than the event caller.
        Node eventSource = (Node) event.getSource();
        scene = eventSource.getScene();
        tfTotalBets = (TextField) scene.lookup("#textField_TotalBets");
        
        // Get the source of the caller (a textfield), and grab the text and ID from it.
        TextField tfSource = (TextField) event.getSource();
        String tfSourceIDFull = tfSource.getId();
        String tfSourceText = tfSource.getText();
        String tfSourceID = tfSourceIDFull.substring(10);
        
        
        if (tfSourceText.isEmpty()) {
            // If the text field is empty, there's no bet so it should be handled as 0.
            oldValue = 0.0d;
        } else {
            // If the text isn't empty, get the value and convert from String text to Double.
            oldValue = Double.parseDouble(tfSourceText);
        }
        System.out.println("TextField " + tfSourceID + " accessed. The previous value is: " + oldValue);
        
        // Open the NumPad window to get a new bet value from the user
        newValue = numPadController.getNewValue(oldValue);
        
        // Update the selected bet text field with the new value
        if (newValue == 0d) {
            tfSource.setText("");
            tfSource.setStyle(null);
        } else {
            tfSource.setText(df.format(newValue));
        }
        
        // Get the total of all Bets for running display in the Total Bets text field
        betTotal = BetHandler.handleBet(tfSourceID, oldValue, newValue);
        
        // Set the text of the Total Bets text field to the updated value
        if (betTotal == 0d) {
            tfTotalBets.setText("");
        } else {
            tfTotalBets.setText(df.format(betTotal));
        }
        
        // Remove focus from the updated text field by setting focus on the parent instead
        tfSource.getParent().requestFocus();
    }
    
    
    /**
     * Handles the user clicking on the Calculate button.
     * <p>
     * Verifies that there is a valid Roll (two dice are selected), calls the BetHandler for winning bets and losing
     * bet totals based on the selected Roll.
     * Gets lists of winning and losing bets from the BetHandler to highlight the respective bet text fields either red
     * or green.
     * Updates the bet totals fields and displays the total payout.
     *
     * @param event the source of the MouseEvent, used to identify which text field was clicked, and access the scene
     */
    public void onCalculate(MouseEvent event) {
        /* The MouseEvent is no longer used in this method, left in for possible future use:
          Node eventSource = (Node) event.getSource();
          scene = eventSource.getScene();
        */
        
        // The Roll dice should be selected already. If the Roll is not valid (one or both die are not selected),
        // just return and don't do anything.
        // It might be helpful to highlight the dice set not selected, maybe later I can add that...
        if (!Roll.getInstance().isValid()) {
            System.out.println("Error processing calculation: Dice Not Selected.");
            System.out.println("Die 1 is: " + Roll.getInstance().getDie1());
            System.out.println("Die 2 is: " + Roll.getInstance().getDie2());
            return;
        }
        
        // Have the BetHandler calculate the winning and losing bet amounts. This doesn't return anything, but
        // sets the values internally in the BetHandler, so we can then retrieve them.
        BetHandler.calculate();
        
        // Get the win/loss totals and the lists of winners and losers from the BetHandler.
        Double betsTotal = BetHandler.getBetsTotal();
        Double winsTotal = BetHandler.getWinsTotal();
        Double lossesTotal = BetHandler.getLossesTotal();
        ArrayList<String> winnersList = BetHandler.getWinnersList();
        ArrayList<String> losersList = BetHandler.getLosersList();
        
        // Update the text fields to color the winning and losing text fields, and update the Totals fields.
        colorTextFields(winnersList, losersList);
        updateTotals(betsTotal, winsTotal, lossesTotal);
    }
    
    
    /**
     * Handles the user clicking on the 'Clear All' button. Clears all bet fields, total fields, and dice selections.
     * <p>
     * Sets all the Roll selection dice to their unselected state, with their original red color.
     * Clears all the text from all text fields, deletes any associated Bet objects, resets text field colors.
     * Clears all totals text fields and resets text field colors.
     *
     * @param event the source of the MouseEvent, used to identify which text field was clicked, and access the scene
     */
    public void onClearAll(MouseEvent event) {
        /* The MouseEvent is no longer used in this method, left in for possible future use:
          Node eventSource = (Node) event.getSource();
          scene = eventSource.getScene();
        */
        
        clearRoll();
        clearAllBets();
    }
    
    
    /**
     * Handles the user clicking on any of the "Roll dice" images used to signify what number was rolled.
     * <p>
     * There are two columns of dice images numbered 1-6. The user selects one die from column 1 and one from column 2.
     * These dice signify what number was rolled. One die from each column must be selected for the roll to be valid.
     * When the user selects a die, that die's image is adjusted using a color hue change, to change from red to blue.
     * All other dice in that column are deselected and turned back to their original red.
     * If a die in one column is already selected and the user selects a different die in that column, then all dice
     * in that column are deselected and the new die is then selected.
     *
     * @param event the source of the MouseEvent, used to identify which text field was clicked, and access the scene
     */
    public void onClickRollDie(MouseEvent event) {
        // Get the current scene, so we can find desired dice images other than the event caller.
        Node eventSource = (Node) event.getSource();
        scene = eventSource.getScene();
        String dieSourceString = ((Node) event.getSource()).getId();
        String selectedDieValue = dieSourceString.substring(dieSourceString.length() - 1);
        int dieColumn;
        
        // Determine which dice column was selected
        if (dieSourceString.contains("RollDie1")) {
            dieColumn = 1;
        } else {
            dieColumn = 2;
        }
        
        // Create a ColorAdjust object with hue 0 to reset all dice images to their original color
        ColorAdjust resetColorAdjust = new ColorAdjust();
        resetColorAdjust.setHue(0.0);
        
        // Set all the dice in the column to their original Red
        for (int i = 1; i <= 6; i++) {
            Node die = scene.lookup("#die_RollDie" + dieColumn + "_" + i);
            die.setEffect(resetColorAdjust);
        }
        
        // Now change only the selected die to Blue by using a -67% Hue change
        ColorAdjust selectColorAdjust = new ColorAdjust();
        selectColorAdjust.setHue(-0.67);
        eventSource.setEffect(selectColorAdjust);
        
        // Update the Roll tracker with the new die selection
        if (dieColumn == 1) {
            // They selected a die from the left column, so set die1 to the selected die value
            Roll.getInstance().setDie1(Integer.parseInt(selectedDieValue));
        } else {
            // They selected a die from the right column, so set die2 to the selected die value
            Roll.getInstance().setDie2(Integer.parseInt(selectedDieValue));
        }
    }
    
    
    /**
     * Handles the user clicking on the "Clear Roll" button. Resets all Roll Dice.
     * <p>
     * The Clear Roll button resets all "Roll dice" to their original red color.
     * In addition, all of the Totals fields are cleared, since there is no longer a valid Roll to calculate bets.
     * Bet field texts are not cleared, but their colors are all reset to their original neutral color.
     *
     * @param event the source of the MouseEvent, used to identify which text field was clicked, and access the scene
     */
    public void onClearRoll(MouseEvent event) {
        /* The MouseEvent is no longer used in this method, left in for possible future use:
          Node eventSource = (Node) event.getSource();
          scene = eventSource.getScene();
        */
        clearRoll();
    }
    
    
    // ********************
    // Private Methods
    // ********************
    
    
    // Updates the Totals text fields with the appropriate data from the Bet Handler, and colors accordingly.
    // Totals TextFields are updated with data passed from the onCalculate method, and TextField coloring is
    // handled in this method.
    private void updateTotals(Double betsTotal, Double winsTotal, Double lossesTotal) {
        Double payout = winsTotal - lossesTotal;
        DecimalFormat df = new DecimalFormat("#.####");
        
        // Create TextField objects and set them equal to the ones already on the page so they are accessible.
        tfTotalBets = (TextField) scene.lookup("#textField_TotalBets");
        tfWinnings = (TextField) scene.lookup("#textField_Winnings");
        tfLosses = (TextField) scene.lookup("#textField_Losses");
        tfTotalPayout = (TextField) scene.lookup("#textField_TotalPayout");
        
        // Set the Totals fields data and colors
        tfTotalBets.setText(df.format(betsTotal));
        
        tfWinnings.setText(df.format(winsTotal));
        if (winsTotal != 0d) {
            tfWinnings.setStyle(winTFStyle);
        } else {
            tfWinnings.setStyle(null);
        }
        
        tfLosses.setText(df.format(lossesTotal));
        if (lossesTotal != 0d) {
            tfLosses.setStyle(lossTFStyle);
        } else {
            tfLosses.setStyle(null);
        }
        
        tfTotalPayout.setText(df.format(payout));
        if (payout > 0d) {
            tfTotalPayout.setStyle(winTFStyle);
        } else if (payout < 0d) {
            tfTotalPayout.setStyle(lossTFStyle);
        } else {
            tfTotalPayout.setStyle(null);
        }
        
    }
    
    
    // Takes lists of all the winning and losing Bets from the BetHandler, and colors them Green and Red, respectively.
    // Handles the coloring of the individual Bet fields, but not the Totals fields, which are handled in updateTotals.
    private void colorTextFields(ArrayList<String> winnersList, ArrayList<String> losersList) {
        TextField tfToColor;
        for (String currentID : winnersList) {
            tfToColor = (TextField) scene.lookup("#textField_" + currentID);
            tfToColor.setStyle(winTFStyle);
        }
        for (String currentID : losersList) {
            tfToColor = (TextField) scene.lookup("#textField_" + currentID);
            tfToColor.setStyle(lossTFStyle);
        }
    }
    
    
    // Clears the dice selections and resets the Totals text fields.
    private void clearRoll() {
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
        
        // Reset all the coloring on any active bets, since they are no longer winners/losers
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
    
    
    // Clears all bets by having the BetHandler delete the array of Bet objects. Gets back a list of deleted bet ID's
    // Goes through the list of those Bet TextField ID's and clears the contents and coloring.
    private void clearAllBets() {
        ArrayList<String> clearedBets = BetHandler.deleteAll();
        TextField textFieldToClear;
        for (String textFieldID : clearedBets) {
            textFieldToClear = (TextField) scene.lookup("#textField_" + textFieldID);
            textFieldToClear.setText("");
            textFieldToClear.setStyle(null);
        }
    }
}
