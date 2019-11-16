package utilities;

import controller.Photos;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static constants.Constants.USERS_FILE_PATH;


public class Utilities {
    public static void quitApp() {
        System.exit(0);
    }

    public static void displayAlert(AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType.toString());
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void updateListView(ListView<String> listView, List<String> updatedList, String path) {
        listView.setItems(FXCollections.observableList(updatedList));
        listView.refresh();
        writeToFile(path, updatedList);
    }
    
    public static void transferAlbumContent(String pathFrom, String pathTo) throws IOException {
		BufferedReader photos = new BufferedReader(new FileReader(pathFrom));
        String line;
        List<String> paths = new ArrayList<String>();
        while ((line = photos.readLine()) != null) {
        	paths.add(line); 
        }
        writeToFile(pathTo, paths); 
        photos.close();
	}

    public static void writeToFile(String filePath, List<String> content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            for (String user : content) {
                writer.write(user);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException( e);
        }

    }
}
