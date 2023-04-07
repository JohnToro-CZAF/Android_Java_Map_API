/**

 A class representing a photo from a location.
 The class is Serializable.
 */
package com.johntoro.myapplication.models;
import androidx.annotation.NonNull;

import java.io.Serializable;

public class Photos implements Serializable {

    /**
     * The photo reference for this photo.
     */
    private String photo_reference;

    /**
     * The height of the photo in pixels.
     */
    private String height;

    /**
     * The HTML attributions associated with the photo.
     */
    private String[] html_attributions;

    /**
     * The width of the photo in pixels.
     */
    private String width;

    /**
     * Returns the photo reference for this photo.
     *
     * @return The photo reference for this photo.
     */
    public String getPhoto_reference() {
        return photo_reference;
    }

    /**
     * Sets the photo reference for this photo.
     *
     * @param photo_reference The photo reference for this photo.
     */
    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    /**
     * Returns the height of the photo in pixels.
     *
     * @return The height of the photo in pixels.
     */
    public String getHeight() {
        return height;
    }

    /**
     * Sets the height of the photo in pixels.
     *
     * @param height The height of the photo in pixels.
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * Returns the HTML attributions associated with the photo.
     *
     * @return The HTML attributions associated with the photo.
     */
    public String[] getHtml_attributions() {
        return html_attributions;
    }

    /**
     * Sets the HTML attributions associated with the photo.
     *
     * @param html_attributions The HTML attributions associated with the photo.
     */
    public void setHtml_attributions(String[] html_attributions) {
        this.html_attributions = html_attributions;
    }

    /**
     * Returns the width of the photo in pixels.
     *
     * @return The width of the photo in pixels.
     */
    public String getWidth() {
        return width;
    }

    /**
     * Sets the width of the photo in pixels.
     *
     * @param width The width of the photo in pixels.
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * Returns a string representation of the Photos object.
     *
     * @return A string representation of the Photos object.
     */
    @NonNull
    @Override
    public String toString() {
        return "ClassPojo [photo_reference = " + photo_reference + ", height = " + height + ", html_attributions = " + html_attributions + ", width = " + width + "]";
    }

}

