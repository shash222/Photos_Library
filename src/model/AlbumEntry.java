package model;

import javafx.scene.image.ImageView;
import utilities.Utilities;

import java.util.Date;

public class AlbumEntry {
    private ImageView image;
    private String caption;
    private Date dateTaken;
    private Photo associatedPhoto;

    public AlbumEntry (String location, String caption,  Date dateTaken, Photo associatedPhoto) {
        this.image = Utilities.getImageView(location);
        this.caption = caption;
        this.dateTaken = dateTaken;
        this.associatedPhoto = associatedPhoto;
    }

    public ImageView getImage() {
        return this.image;
    }

    public String getCaption() {
        return this.caption;
    }

    public Date getDateTaken() {
        return this.dateTaken;
    }

    public Photo getAssociatedPhoto() {
        return associatedPhoto;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        this.associatedPhoto.setCaption(caption);
    }

}
