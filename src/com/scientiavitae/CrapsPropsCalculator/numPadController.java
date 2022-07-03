package com.scientiavitae.CrapsPropsCalculator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Controller for the number pad that opens up when the user clicks on a text field.
 * <p>
 * Allows the user to enter a bet amount. When the user hits Enter or Cancel, the number pad closes.
 * The number pad is contained in an application-modal window.
 *
 * @version 0.3
 * @since 0.2
 */
public class numPadController {
    
    @FXML
    private TextField textField_numPad;     // textField to display the user's entry as they click on number buttons
    static Stage numPadWindow;              // Stage for this window
    
    private static Double newValue = 0.0d;  // the new value of Bet the user has entered
    private static Double oldValue;         // the old value of Bet the user is updating (or 0 if this Bet did not exist yet)
    
    
    /**
     * Gets the new Bet value that the user specifies.
     * <p>
     * Creates a new window for a number pad that the user can use to enter the bet amount.
     *
     * @param passedValue  the old value that the bet contained before the user clicked on it; used to determine
     *                     if the Bet object needs to be created, updated, or deleted
     * @return newValue  the new bet value that the user entered
     */
    /* Note that getNewValue() must be static since I'm accessing it from the mainController, and
        the mainController can't access this method if it doesn't know which instance of numPadController to access.
       Making it static forces all instances to be the same, bypassing this issue.
       Making this static also means I can't use the normal FXMLLoader.load(getClass().getResource("num_pad.fxml"))
        since now that can't be accessed from a static context, so I have to specify numPadController.class()
    */
    public static Double getNewValue(Double passedValue) {
        oldValue = passedValue;
        
        // Create a new application-modal window for the NumPad to reside in.
        numPadWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(numPadController.class.getResource("num_pad.fxml"));
        numPadWindow.setTitle("Bet amount");
        numPadWindow.initStyle(StageStyle.UNDECORATED);
        numPadWindow.initModality(Modality.APPLICATION_MODAL);
        
        try {
            numPadWindow.setScene(new Scene(loader.load()));
            numPadWindow.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("NumPad Closed. Returning newValue: " + newValue);
        
        return newValue;
    }
    
    
    // Handler for when user clicks any number key. Gets the text of the button pressed to determine its value, and
    // adds that to the String in the NumPad's text field.
    @FXML
    private void onNumKeyPressed(MouseEvent event) {
        String newText = "";
        System.out.println(event.getEventType().toString());
        String buttonText = ((Button) event.getSource()).getText();
        System.out.println(buttonText);
        String tfText = textField_numPad.getText();
        newText = tfText + buttonText;
        updateNumPadTextField(newText);
    }
    
    
    // Handler for when user clicks the Cancel key. The new value is set to the old value (nothing changed),
    // and the NumPad closes.
    @FXML
    private void onNumKeyCancel(MouseEvent event) {
        System.out.println(event.getSource().toString());
        newValue = oldValue;
        closeNumPad(event);
    }
    
    
    // Handler for when user clicks the Enter key. The newValue is set to whatever is stored in the NumPad's
    // text field, and the NumPad is closed.
    @FXML
    private void onNumKeyEnter(MouseEvent event) {
        try {
            newValue = Double.parseDouble(textField_numPad.getText());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing Double. Maybe there's something other than a number there?");
        }
        closeNumPad(event);
    }
    
    
    // Handler for when user clicks the Delete key. Whatever is in the NumPad's text field is read, and the last
    // character is deleted, and the text field updated. If there is nothing in the text field, nothing happens.
    @FXML
    private void onNumKeyDelete(MouseEvent event) {
        String oldText = textField_numPad.getText();
        String newText = "";
        System.out.println("Backspace key pressed.");
        System.out.println("Former text: " + oldText);
        try {
            newText = oldText.substring(0, oldText.length() - 1);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error removing last character: String is empty.");
        }
        
        System.out.println("New text: " + newText);
        updateNumPadTextField(newText);
        
    }
    
    
    // Handler for when user clicks the Clear key. Whatever is in the NumPad's text field is deleted.
    @FXML
    private void onNumKeyClear(MouseEvent event) {
        updateNumPadTextField("");
    }
    
    
    // Updates the NumPad's text field. This is a separate method in case I wanted to do something else in addition
    // to strictly updating the text field. Deleting this method and replacing with the line setting the text of the
    // NumPad text field may improve performance slightly, but I'm leaving it for now.
    private void updateNumPadTextField(String numPadText) {
        textField_numPad.setText(numPadText);
        System.out.println("NumPad Text set to: " + numPadText);
    }
    
    
    // Closes the NumPad window.
    private void closeNumPad(MouseEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }
    
    
}
