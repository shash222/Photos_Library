package utilities;

import constants.Constants;
import controller.Photos;
import controller.user.OpenAlbumController;
import controller.user.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AlbumEntry;
import model.Photo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static constants.Constants.DEFAULT_HEIGHT;
import static constants.Constants.USERS_FILE_PATH;


public class Utilities {
    private static final String DEFAULT_VIEW_LOCATION = "../../view";

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
    
    public static void updateSerilization(ListView listView, Set arr, ObservableList items, ListView table) {
    	listView.setItems(FXCollections.observableList(new ArrayList(arr)));
		List<Photo> photosInAlbum = new ArrayList();

		for (Object item : items) {
			photosInAlbum.add(((AlbumEntry) item).getAssociatedPhoto());
		}
		String albumPath = String.format(Constants.ALBUM_PATH_FORMAT, Photos.currentUser, UserController.selectedAlbum);
		Utilities.writeSerializedObjectToFile(photosInAlbum, albumPath);
		table.refresh();
	
    }
    
    public static void updateSerilization(ListView listView, Set arr, ObservableList items, TableView table) {
    	listView.setItems(FXCollections.observableList(new ArrayList(arr)));
		List<Photo> photosInAlbum = new ArrayList();

		for (Object item : items) {
			photosInAlbum.add(((AlbumEntry) item).getAssociatedPhoto());
		}
		String albumPath = String.format(Constants.ALBUM_PATH_FORMAT, Photos.currentUser, UserController.selectedAlbum);
		Utilities.writeSerializedObjectToFile(photosInAlbum, albumPath);
		table.refresh();
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

    public static void displayView(String filePath) {
//        try {
            Stage createUserStage = new Stage();
//            Parent root = FXMLLoader.load(Utilities.class.getClass().getResource(String.format("%s/%s", DEFAULT_VIEW_LOCATION, filePath)));
//            createUserStage.setScene(new Scene(root, Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT));
            createUserStage.setScene(createScene(filePath, Constants.DEFAULT_WIDTH, DEFAULT_HEIGHT));
            createUserStage.initModality(Modality.WINDOW_MODAL);
            createUserStage.showAndWait();
//        } catch (IOException e) {
//            String msg = "Could not find file";
//            throw new RuntimeException(msg, e);
//        }
    }

    public static Scene createScene(String filePath, int width, int height) {
        try {
            Parent root = FXMLLoader.load(Utilities.class.getClass().getResource(String.format("%s/%s", DEFAULT_VIEW_LOCATION, filePath)));
            return new Scene(root, width, height);
        } catch (IOException e) {
            String msg = "Could not find file";
            throw new RuntimeException(msg, e);
        }
    }

    public static List<Photo> readSerializedObjectFromFile(String filePath) {
        List<Photo> photosInAlbum = new ArrayList<>();
        try {
            BufferedReader b = new BufferedReader(new FileReader(filePath));
            String line;
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
            System.out.println(objectInputStream.available());
//            while (objectInputStream.available() != 0) {
//                Object p = objectInputStream.readObject();
//                System.out.println(p);
////                photosInAlbum.add(p);
//            }
//            Object o;
//            while ((o = objectInputStream.readObject()) != null) {
//                photosInAlbum.add((Photo) o);
//            }
            // Photos are stored as ArrayLists
            return (ArrayList<Photo>) objectInputStream.readObject();
        } catch (EOFException e) {
            System.out.println("End of file reached, prevented from throwing");
        } catch (IOException e) {
            String msg = "IOException";
            throw new RuntimeException(msg, e);
        } catch (ClassNotFoundException e) {
            String msg = "Could not find class";
            throw new RuntimeException(msg, e);
        }
        return photosInAlbum;
    }

    public static void writeSerializedObjectToFile(List<Photo> photos, String filePath) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
            objectOutputStream.writeObject(photos);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            String msg = "Cannot find file";
            throw new RuntimeException(msg, e);
        } catch (IOException e) {
            String msg = "IOException";
            throw new RuntimeException(msg, e);
        }
    }

    public static ImageView getImageView(String location) {
        return new ImageView(getImage(location));
    }

    public static Image getImage(String location) {
        return new Image(new File(location).toURI().toString());
    }
}
