package controller;

import constants.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static constants.Constants.USERS_FILE_PATH;

public class Photos extends Application {
    public static HashMap<String, HashSet<String>> users = new HashMap<String, HashSet<String>>();
    
    public static String currentUser; 

    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public static void readUsers() throws IOException{
        BufferedReader readerUsers = new BufferedReader(new FileReader(USERS_FILE_PATH));
        String line;
        // users = new HashSet<>();
        while ((line = readerUsers.readLine()) != null) {
            String filePath = "src/resources/USER" + line + ".txt";
            BufferedReader readerAlbums = new BufferedReader(new FileReader(filePath));
        	HashSet<String> albums = new HashSet<String>(); 
        	String albumLine; 
            while ((albumLine = readerAlbums.readLine()) != null) {
            	if(!albums.contains(albumLine)) albums.add(albumLine); 
            }  
            users.put(line, albums);
        }
        readerUsers.close();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
    	readUsers();
        Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Photos Library");
        primaryStage.setScene(new Scene(root, Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT));
        primaryStage.show();
        
    }
}