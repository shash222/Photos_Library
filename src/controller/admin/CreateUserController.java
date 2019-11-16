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
import java.util.HashSet;

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
        	HashSet<String> albums = new HashSet<String>(); 
            Photos.users.put(usernameInputText, albums);
            String filePath = "src/resources/USER" + usernameInputText + ".txt";
            File file = new File(filePath);
            try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
            Utilities.displayAlert(AlertType.CONFIRMATION, "User will be added after closing this box");
        }
    }

}
