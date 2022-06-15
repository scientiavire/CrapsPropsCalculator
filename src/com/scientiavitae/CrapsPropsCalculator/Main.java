package com.scientiavitae.CrapsPropsCalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        primaryStage.setTitle("Craps Props Calculator");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    
    
        
    
        
        
    }
    
    
}

/*
HashMap<String, Integer[]> betsMap = new HashMap<>();
        //betsMap.put("Hop_1_1", new Integer[]{1,1}); - Not used since 1,1 is a Horn bet (2)
        //betsMap.put("Hop_1_2", new Integer[]{1,2}); - Not used since 1,2 is a Horn bet (3)
        betsMap.put("Hop_1_3", new Integer[]{1,3});
        betsMap.put("Hop_1_4", new Integer[]{1,4});
        betsMap.put("Hop_1_5", new Integer[]{1,5});
        betsMap.put("Hop_1_6", new Integer[]{1,6});
        //betsMap.put("Hop_2_1", new Integer[]{2,1}); - Not used since 2,1 is a Horn bet (3)
        betsMap.put("Hop_2_2", new Integer[]{2,2});
        betsMap.put("Hop_2_3", new Integer[]{2,3});
        betsMap.put("Hop_2_4", new Integer[]{2,4});
        betsMap.put("Hop_2_5", new Integer[]{2,4});
        betsMap.put("Hop_2_6", new Integer[]{2,4});
        betsMap.put("Hop_3_1", new Integer[]{2,4});
        betsMap.put("Hop_3_2", new Integer[]{2,4});
        betsMap.put("Hop_3_3", new Integer[]{2,4});
        betsMap.put("Hop_3_4", new Integer[]{2,4});
        betsMap.put("Hop_3_5", new Integer[]{2,4});
        betsMap.put("Hop_3_6", new Integer[]{2,4});
        betsMap.put("Hop_4_1", new Integer[]{2,4});
        betsMap.put("Hop_4_2", new Integer[]{2,4});
        betsMap.put("Hop_4_3", new Integer[]{2,4});
        betsMap.put("Hop_4_4", new Integer[]{2,4});
        betsMap.put("Hop_4_5", new Integer[]{2,4});
        betsMap.put("Hop_4_6", new Integer[]{2,4});
        betsMap.put("Hop_5_1", new Integer[]{2,4});
        betsMap.put("Hop_5_2", new Integer[]{2,4});
        betsMap.put("Hop_5_3", new Integer[]{2,4});
        betsMap.put("Hop_5_4", new Integer[]{2,4});
        betsMap.put("Hop_5_5", new Integer[]{2,4});
        //betsMap.put("Hop_5_6", new Integer[]{2,4}); - Not used since 5,6 is a Horn bet (11)
        betsMap.put("Hop_6_1", new Integer[]{2,4});
        betsMap.put("Hop_6_2", new Integer[]{2,4});
        betsMap.put("Hop_6_3", new Integer[]{2,4});
        betsMap.put("Hop_6_4", new Integer[]{2,4});
        //betsMap.put("Hop_6_5", new Integer[]{2,4}); - Not used since 6,5 is a Horn bet (11)
        //betsMap.put("Hop_6_6", new Integer[]{2,4}); - Not used since 6,6 is a Horn bet (12)
        
        // I think that the above is completely unnecessary. All of these Hop bets have the values in the name.
        // The Horn bets are necessary though, as they have different lengths and values.
        //TODOne: Need to edit the bet ID to remove the "textField_" portion of the String
        
        betsMap.put("Horn_2", new Integer[]{2});
        betsMap.put("Horn_3", new Integer[]{3});
        betsMap.put("Horn_11", new Integer[]{11});
        betsMap.put("Horn_12", new Integer[]{12});
        betsMap.put("Horn_2_3", new Integer[]{2,3});
        betsMap.put("Horn_2_11", new Integer[]{2,11});
        betsMap.put("Horn_2_12", new Integer[]{2,12});
        betsMap.put("Horn_3_11", new Integer[]{3,11});
        betsMap.put("Horn_3_12", new Integer[]{3,12});
        betsMap.put("Horn_11_12", new Integer[]{11,12});
        betsMap.put("Horn_2_3_11", new Integer[]{2,3,11});
        betsMap.put("Horn_2_3_12", new Integer[]{2,3,12});
        betsMap.put("Horn_2_11_12", new Integer[]{2,11,12});
        betsMap.put("Horn_3_11_12", new Integer[]{3,11,12});
        betsMap.put("Horn_Horn", new Integer[]{2,3,11,12});
        betsMap.put("Horn_HornHigh_2", new Integer[]{2,2,3,11,12});
        betsMap.put("Horn_HornHigh_3", new Integer[]{2,3,3,11,12});
        betsMap.put("Horn_HornHigh_11", new Integer[]{2,3,11,11,12});
        betsMap.put("Horn_HornHigh_12", new Integer[]{2,3,11,12,12});
        betsMap.put("Horn_World", new Integer[]{2,3,7,11,12});
        betsMap.put("Horn_AnyCraps", new Integer[]{2,3,12});
        betsMap.put("Horn_AnySeven", new Integer[]{7});
        betsMap.put("Horn_CandE", new Integer[]{2,3,11,12});

 */