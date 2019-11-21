package controller.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import constants.Constants;
import controller.Photos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Photo;
import model.PhotoEntry;
import utilities.Utilities;

/**
 * Controller that handles logic that displays search results
 * @author Mohammed Alnadi
 * @author Salman Hashmi
 */
public class SearchResultsController implements Initializable {
	
	@FXML
	TableView resultsTable;
	@FXML
	private TableColumn imageColumn;
	@FXML
	private TableColumn captionColumn;
	@FXML
	private TableColumn dateTakenColumn;


	static TableView staticResultsTable;

	List<Photo> photos = new ArrayList<>();

	/**
	 * Sets the photos in the tableview from the search results
	 */
	private void setPhotos() {

		resultsTable.getItems().remove(0, resultsTable.getItems().size());
		List<Photo> photosInAlbum = new ArrayList<>(); 
		if(SearchPhotoByTagController.tags) {
			photosInAlbum = (List<Photo>)SearchPhotoByTagController.finalList; 
		} else {
			photosInAlbum = SearchPhotoByDateController.searchResult;
			// SET photosInAlbum TO EQUAL THE LIST FROM SearchPhotoByDateController
		}
		imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
		captionColumn.setCellValueFactory(new PropertyValueFactory<>("caption"));
		dateTakenColumn.setCellValueFactory(new PropertyValueFactory<>("dateTaken"));
		for (Photo p : photosInAlbum) {
			PhotoEntry photoEntry = new PhotoEntry(p.getLocation(), p.getCaption(), p.getDateTaken(), p);
			resultsTable.getItems().add(photoEntry);
		}
		staticResultsTable = resultsTable;
	}

	/**
	 * Runs when class is triggered
	 * @param location default param
	 * @param resources default param
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("There");
		setPhotos();
		System.out.println("There");
	}

}
