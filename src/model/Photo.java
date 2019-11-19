package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Photo implements Serializable {
    private String location;
    private String caption;
    private Map<String, List<String>> tags;
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

    public Map<String, List<String>> getTags() {
        return this.tags;
    }

    public Date getDateTaken() {
        return this.dateTaken;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void addTag(String key, String value) {
        List<String> values = tags.getOrDefault(key, new ArrayList<>());
        values.add(value);
        tags.put(key, values);
    }

}
