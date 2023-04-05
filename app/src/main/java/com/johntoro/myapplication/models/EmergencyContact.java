package com.johntoro.myapplication.models;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.Objects;

/**
 * Represents an emergency contact.
 */
public class EmergencyContact {

    //change to private
    @Exclude
    private String id = null;
    private String userEmail = null;
    private String fullName = null;
    private String email = null;
    private String mobile = null;
    @Exclude
    private boolean isDeleted = false;

    /**
     * Overrides equals method to verify if an object is an emergency contact.
     * @param obj
     * @return if object is an emergency contact.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj.getClass() == EmergencyContact.class){
            return (((EmergencyContact) obj).id == id);
        }else
            return false;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, userEmail, fullName, email, mobile, isDeleted);
    }

    /**
     * Overrides toString() method to return contact as a string.
     * @return contact as a string
     */
    @NonNull
    @Override
    public String toString() {
        return "id: " + id + ", userEmail: " + userEmail + ", fullName: " + fullName + ", email: " + email + ", mobile: " + mobile + ", isDeleted: " + isDeleted;
    }

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the email of the user who added this contact.
     * @return user email
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Sets the email of the user who added this contact.
     * @param userEmail
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return full name of the contact
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return email of the contact
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return mobile number of contact
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets whether contact is deleted.
     * @return isDeleted.
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Sets whether contact is deleted.
     * @param deleted
     */
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

}
