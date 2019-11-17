package model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Photo implements Serializable {
    String location;
    String caption;
    Set<String> tags;
    Date dateTaken;

    public Photo(String location, String caption, Date dateTaken) {
        this.location =  location;
        this.caption = caption;
        this.dateTaken = dateTaken;
        this.tags = new HashSet<>();
    }
}
