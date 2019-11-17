package controller.user;

import controller.Photos;
import javafx.fxml.Initializable;
import model.Photo;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OpenAlbumController implements Initializable {
    List<Photo> photos = new ArrayList<>();

    private void setPhotos() {
        try {
            String albumPath = String.format("src/resources/ALBUM/%s%s.txt", Photos.currentUser, UserController.selectedAlbum);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(albumPath));
            OutputStream outputStream ;
            BufferedReader reader = new BufferedReader(new FileReader(albumPath));
            String line;
            while ((line = reader.readLine()) != null) {
            }
        } catch (IOException e) {
            String msg = "Could not find file";
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPhotos();
    }


}
