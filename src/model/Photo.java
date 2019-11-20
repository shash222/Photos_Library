package model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.scene.control.Alert.AlertType;
import utilities.Utilities;

public class Photo implements Serializable {
    private String location;
    private String caption;
    private Map<String, Set<String>> tags;
    private Date dateTaken;

    public Photo(String location, String caption, Date dateTaken) {
        this.location = location;
        this.caption = caption;
        this.dateTaken = dateTaken;
        this.tags = new HashMap<>();
    }

    public String getLocation() {
        return this.location;
    }

    public String getCaption() {
        return this.caption;
    }

    public Map<String, Set<String>> getTags() {
        return this.tags;
    }

    public Date getDateTaken() {
        return this.dateTaken;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public void deleteTag(String key) {
    	if(tags.containsKey(key)) {
    		tags.remove(key); 
    	} else {
    		Utilities.displayAlert(AlertType.ERROR, "This tag does not exist.");
    	}
    }
    
    public void deleteTagValue(String key, String value) {
    	if(tags.containsKey(key) && tags.get(key).contains(value)) {
    		tags.get(key).remove(value); 
    	} else {
    		Utilities.displayAlert(AlertType.ERROR, "This value does not exist.");
    	}
    }
    
    public void addTag(String key) {
    	if(tags.containsKey(key)) {
    		Utilities.displayAlert(AlertType.ERROR, "This photo already contains this tag.");
    	} else {
            tags.put(key, new HashSet<>());
    	}
    }

    public void addTag(String key, String value) {
        Set<String> values = tags.getOrDefault(key, new HashSet<>());
        values.add(value);
        tags.put(key, values);
    }

}
