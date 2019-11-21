package controller.user;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.PhotoEntry;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller to view images in a single album as a slideshow
 * @author Mohammed Alnadi
 * @author Salman Hashmi
 */
public class ViewSlideshowController implements Initializable {
    @FXML
    private ImageView imageContainer;

    private Object[] entries;
    private int currentIndex = 0;


    /**
     * Runs as soon  as class is triggered
     * @param location default param
     * @param resources default param
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entries = OpenAlbumController.staticPhotoTable.getItems().toArray();
        setImage(0);
    }

    /**
     * Displays next image, or first image if end is reached
     * @param mouseEvent response to clicking button
     */
    @FXML
    public void nextImage(MouseEvent mouseEvent) {
        setImage(1);
    }

    /**
     * Displays previous image, or last image if at beginning
     * @param mouseEvent response to clicking button
     */
    @FXML
    public void previousImage(MouseEvent mouseEvent) {
        setImage(-1);
    }

    /**
     * Updates the image displayed
     * @param incrementValue Whether to increment or decrement
     */
    public void setImage(int incrementValue) {
        currentIndex += incrementValue;
        if (currentIndex == -1) currentIndex = entries.length - 1;
        if (currentIndex == entries.length) currentIndex = 0;
        imageContainer.setImage(((PhotoEntry)entries[currentIndex]).getImage().getImage());
    }
}
