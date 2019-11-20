package controller.user;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import constants.Constants;
import controller.Photos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Photo;
import utilities.Utilities;

public class SearchPhotoByTagController implements Initializable {
	
		@FXML
		TextField tag1; 
		
		@FXML
		TextField value1; 
	
		@FXML
		TextField tag2; 
	
		@FXML
		TextField value2; 
		
		@FXML
		ComboBox operation; 
		
		Set<Photo> finalList = new HashSet<Photo>();
	
	
	 	@Override
	    public void initialize(URL location, ResourceBundle resources) {
	 		operation.getItems().add("And"); 
	 		operation.getItems().add("Or"); 
	 		tag1.setText("");
	 		tag2.setText("");
	 		value1.setText("");
	 		value2.setText("");
	 		operation.setValue("And");
	    }
	 	
	 	@FXML
	 	public void search() {
	 		finalList.clear();
	 		String choice = operation.getValue().toString(); 
	 		if(choice.equals("And") || choice.equals("Or")) {
	 			String t1 = tag1.getText().toLowerCase(); 
		 		String v1 = value1.getText().toLowerCase(); 
		 		
		 		String t2 = tag2.getText().toLowerCase(); 
		 		String v2 = value2.getText().toLowerCase(); 
		 		HashMap<String, HashSet<Photo>> Album = Photos.users.get(Photos.currentUser); 
		        Iterator albumIterator = Album.entrySet().iterator(); 
		         
		        while (albumIterator.hasNext()) { 
		            Map.Entry albumElement = (Map.Entry)albumIterator.next();
		    		

		            String key = (String)albumElement.getKey(); 
		            String albumPath = String.format(Constants.ALBUM_PATH_FORMAT, Photos.currentUser, key);
		    		List<Photo> photos = Utilities.readSerializedObjectFromFile(albumPath);


		            for(Photo photo:photos) {
		            	Set<String> tagNames = photo.getTags().keySet(); 

		            	if(choice.equals("And")) {
	    	        		if(tagNames.contains(t1) && tagNames.contains(t2) && photo.getTags().get(t1).contains(v1) && photo.getTags().get(t2).contains(v2)) {
	    	        			finalList.add(photo); 


	    	        		}
	    	        	} else {
	    	        		if((tagNames.contains(t1) && photo.getTags().get(t1).contains(v1)) || (tagNames.contains(t2) && photo.getTags().get(t2).contains(v2))) {
	    	        			finalList.add(photo); 

	    	        		}
	    	        	}
		            }
		        } 
	 		}
	 		
	 		
	 		System.out.println(finalList); 
	 	}
	 	
	 	

}
