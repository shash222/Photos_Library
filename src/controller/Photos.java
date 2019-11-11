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
import java.util.HashSet;
import java.util.Set;

import static constants.Constants.USERS_FILE_PATH;

public class Photos extends Application {
    public static Set<String> users = new HashSet<>();
    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public void readUsers() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE_PATH));
        String line;
        users = new HashSet<>();
        while ((line = reader.readLine()) != null) {
            users.add(line);
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Photos Library");
        primaryStage.setScene(new Scene(root, Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT));
        primaryStage.show();
        readUsers();
    }
}