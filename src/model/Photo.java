package model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.scene.control.Alert.AlertType;
import utilities.Utilities;

/**
 * Photo object which is serialized into files that can be retrieved later
 * @author Mohammed Alnadi
 * @author Salman Hashmi
 */
public class Photo implements Serializable {
    private String location;
    private String caption;
    private Map<String, Set<String>> tags;
    private Date dateTaken;

    /**
     * Constructor
     * @param location photo filepath
     * @param caption photo caption
     * @param dateTaken photo date
     */
    public Photo(String location, String caption, Date dateTaken) {
        this.location = location;
        this.caption = caption;
        this.dateTaken = dateTaken;
        this.tags = new HashMap<>();
    }

    /**
     * Retrieves location
     * @return photo filepath
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Returns caption
     * @return photo caption
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * Returns photo tags
     * @return photo tags
     */
    public Map<String, Set<String>> getTags() {
        return this.tags;
    }

    /**
     * Returns date taken
     * @return data of photo
     */
    public Date getDateTaken() {
        return this.dateTaken;
    }

    /**
     * Sets caption
     * @param caption new caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Deletes tag
     * @param key key of tag to delete
     */
    public void deleteTag(String key) {
    	if(tags.containsKey(key)) {
    		tags.remove(key); 
    	} else {
    		Utilities.displayAlert(AlertType.ERROR, "This tag does not exist.");
    	}
    }

    /**
     * Deletes tag value
     * @param key key associated with value
     * @param value value to delete
     */
    public void deleteTagValue(String key, String value) {
    	if(tags.containsKey(key) && tags.get(key).contains(value)) {
    		tags.get(key).remove(value); 
    	} else {
    		Utilities.displayAlert(AlertType.ERROR, "This value does not exist.");
    	}
    }

    /**
     * Adds new tag key
     * @param key new tag key
     */
    public void addTag(String key) {
    	if(tags.containsKey(key)) {
    		Utilities.displayAlert(AlertType.ERROR, "This photo already contains this tag.");
    	} else {
            tags.put(key, new HashSet<>());
    	}
    }

    /**
     * adds new tag
     * @param key key to add value to
     * @param value value to add
     */
    public void addTag(String key, String value) {
        Set<String> values = tags.getOrDefault(key, new HashSet<>());
        values.add(value);
        tags.put(key, values);
    }

}
