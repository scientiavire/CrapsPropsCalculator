package com.scientiavitae.CrapsPropsCalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main actually launches the CrapsCalculator JavaFX program.
 * @author ScientiaVire
 * @version 0.1
 * @since 0.1
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        primaryStage.setTitle("Craps Props Calculator");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}