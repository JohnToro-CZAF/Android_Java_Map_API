package com.johntoro.myapplication.models;

/**
 * Represents a user.
 */
public class User {
    private String fullName;
    private String email;

    /**
     * Default constructor for User.
     */
    public User(){
    }

    /**
     * Constructor for User
     * @param fullName the user's full name
     * @param email the user's email address
     */
    public User(String fullName, String email){
        this.fullName = fullName;
        this.email = email;
    }

    /**
     * @return user's full name
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
     * @return user's email
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
}
