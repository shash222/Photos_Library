package controller.user;

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
import java.util.HashSet;

import static constants.Constants.USERS_FILE_PATH;

public class CreateAlbumController {
    @FXML
    private TextField usernameInput;

    @FXML
    private void createAlbum(MouseEvent mouseEvent)  {
        String albumName = usernameInput.getText();
    	
        if (Photos.users.get(Photos.currentUser).contains(albumName)) {
            Utilities.displayAlert(AlertType.ERROR, "Album already exists");
        } else {
        	Photos.users.get(Photos.currentUser).add(albumName); 
            String filePath = "src/resources/ALBUM" + Photos.currentUser + albumName + ".txt";
            File file = new File(filePath);
            try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
            Utilities.displayAlert(AlertType.CONFIRMATION, "Album will be added after closing this box");
        }
    }

}
