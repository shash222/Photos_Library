package model;

import javafx.scene.image.ImageView;
import utilities.Utilities;

import java.util.Date;

/**
 * PhotoEntry object that is used to display photo details in album
 * @author Mohammed Alnadi
 * @author Salman Hashmi
 */
public class PhotoEntry {
    private ImageView image;
    private String caption;
    private Date dateTaken;
    private Photo associatedPhoto;

    /**
     * Constructor for object
     * @param location photo filepath
     * @param caption photo caption
     * @param dateTaken photo date
     * @param associatedPhoto associated photo with details
     */
    public PhotoEntry(String location, String caption, Date dateTaken, Photo associatedPhoto) {
        this.image = Utilities.getImageView(location);
        this.caption = caption;
        this.dateTaken = dateTaken;
        this.associatedPhoto = associatedPhoto;
    }

    /**
     * returns imageview of image
     * @return imageview
     */
    public ImageView getImage() {
        return this.image;
    }

    /**
     * returns photo caption
     * @return photo caption
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * returns date photo was taken
     * @return data of photo
     */
    public Date getDateTaken() {
        return this.dateTaken;
    }

    /**
     * Returns associated photo object
     * @return associated photo
     */
    public Photo getAssociatedPhoto() {
        return associatedPhoto;
    }

    /**
     * Sets caption of instance
     * @param caption new caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
        this.associatedPhoto.setCaption(caption);
    }

}
