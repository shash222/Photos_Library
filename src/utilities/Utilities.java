package utilities;

import constants.Constants;
import controller.Photos;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static constants.Constants.USERS_FILE_PATH;


public class Utilities {
    public static void logout() throws IOException {
        Photos.primaryStage.setScene(new Scene(FXMLLoader.load(Utilities.class.getResource("../view/Login.fxml")), Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT));
    }


    public static void quitApp() {
        System.exit(0);
    }

    public static void displayAlert(AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType.toString());
        alert.setContentText(message);
        alert.showAndWait();
    }
    
  
    public static String getUserPath(String user) {
    	return String.format("src/resources/USERS/%s", user);
    }
    
    public static String getUserTxtPath(String user) {
    	return String.format("src/resources/USERS/%s/USER%s.txt", user, user);
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
    
    public static void deleteFile(String path) {
		File file = new File(path);
		System.out.println(path); 
		if(file.delete()) {
			System.out.println("File Deleted"); 
		} else {
			System.out.println("Failed to Deleted"); 

		}
	}

	public static void deleteFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] filesInFolder = folder.list();
        for (String file : filesInFolder) {
            File currentFile = new File(folder.getPath(), file);
            currentFile.delete();
            // deleteFile(file);
        }
        folder.delete(); 
    }

    public static void createFile(String path) {
    	File file = new File(path);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
