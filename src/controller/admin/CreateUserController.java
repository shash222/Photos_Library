package controller.admin;

import controller.Photos;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utilities.Utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import constants.Constants;

import static constants.Constants.USERS_FILE_PATH;

public class CreateUserController {
    @FXML
    private TextField usernameInput;

    @FXML
    private void createUser(MouseEvent mouseEvent)  {
        String usernameInputText = usernameInput.getText();
        if (Photos.users.containsKey(usernameInputText) || usernameInputText.equalsIgnoreCase("admin")) {
            Utilities.displayAlert(AlertType.ERROR, "Username already exists");
        } else {
        	HashMap<String, HashMap<String, HashSet<String>>> albums = new HashMap<String, HashMap<String, HashSet<String>>>(); 
            Photos.users.put(usernameInputText, albums);
            String filePath = String.format("%s\\%s", Utilities.getUserPath(), usernameInputText);
            File folder = new File(filePath);
            System.out.println(folder.getPath());
            if (folder.mkdirs()) System.out.println("Made");
            else System.out.println("Not made");
    		Utilities.createFile(filePath);
            Utilities.displayAlert(AlertType.CONFIRMATION, "User will be added after closing this box");
        }
    }

}
