package controller;

import constants.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Photo;
import utilities.Utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static constants.Constants.USERS_FILE_PATH;

public class Photos extends Application {
    public static HashMap<String, HashMap<String,HashSet<Photo>>> users = new HashMap<String, HashMap<String,HashSet<Photo>>>();
    
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
            String filePath = Utilities.getUserTxtPath(line);
            BufferedReader readerAlbums = new BufferedReader(new FileReader(filePath));
            HashMap<String, HashSet<Photo>> albums = new HashMap<String,HashSet<Photo>>(); 
        	String albumLine; 
            while ((albumLine = readerAlbums.readLine()) != null) {
            	if(!albums.containsKey(albumLine)) {
            		albums.put(albumLine, new HashSet<Photo>()); 
            	}
            }  
            users.put(line, albums);
            readerAlbums.close(); 
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