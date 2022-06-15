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

public class numPadController {
    
    @FXML
    private TextField textField_numPad;
    static Stage numPadWindow;
    
    private static Double newValue = 0.0d;
    private static Double oldValue;
    
    /* getNewValue() must be static since I'm accessing it from the mainController, and
        the mainController can't access this method if it doesn't know which instance of numPadController to access.
       Making it static forces all instances to be the same, bypassing this issue.
       Making this static also means I can't use the normal FXMLLoader.load(getClass().getResource("num_pad.fxml"))
        since now that can't be accessed from a static context, so I have to specify numPadController.class()
    */
    static Double getNewValue(Double passedValue) {
        oldValue = passedValue;
        
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
    
    @FXML
    private void onNumKeyCancel(MouseEvent event) {
        System.out.println(event.getSource().toString());
        newValue = oldValue;
        closeNumPad(event);
    }
    
    @FXML
    private void onNumKeyEnter(MouseEvent event) {
        newValue = Double.parseDouble(textField_numPad.getText());
        closeNumPad(event);
    }
    
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
    
    @FXML
    private void onNumKeyClear(MouseEvent event) {
        updateNumPadTextField("");
    }
    
    private void updateNumPadTextField(String numPadText) {
        textField_numPad.setText(numPadText);
        System.out.println("NumPad Text set to: " + numPadText);
    }
    
    
    private void closeNumPad(MouseEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
    
    
    
}
