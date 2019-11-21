package controller.user;

import constants.Constants;
import controller.Photos;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import model.Photo;
import utilities.Utilities;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Searches for photo between date range
 * @author Mohammed Alnadi
 * @author Salman Hashmi
 */
public class SearchPhotoByDateController {
    @FXML
    DatePicker rangeStartDate;
    @FXML
    DatePicker rangeEndDate;

    static List<Photo> searchResult = new ArrayList<>();

    /**
     * Returns list of photos between designated dates
     * @param mouseEvent response to clicking button
     */
    @FXML
    public void getResults(MouseEvent mouseEvent) {
        LocalDate startDate = rangeStartDate.getValue();
        LocalDate endDate = rangeEndDate.getValue();
        Date startDateAsDate = Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endDateAsDate = Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        if (startDate == null || endDate == null) {
            Utilities.displayAlert(Alert.AlertType.ERROR, "One or both of the fields are empty");
        } else {
            String user = Photos.currentUser;
            List<String> userAlbumsList = Utilities.getUserAlbums(String.format(Constants.USER_ALBUM_LIST_PATH_FORMAT, user, user));
            for (String s : userAlbumsList) {
                List<Photo> photosInAlbum = Utilities.readSerializedObjectFromFile(String.format(Constants.ALBUM_PATH_FORMAT, user, s));
                for (Photo p : photosInAlbum) {
                    Date dateTaken = p.getDateTaken();
                    if (dateTaken.compareTo(startDateAsDate) >= 0 && dateTaken.compareTo(endDateAsDate) <= 0) {
                        searchResult.add(p);
                    }
                }
            }
            Utilities.displayView("user/SearchResultsView.fxml");
        }
    }
}
