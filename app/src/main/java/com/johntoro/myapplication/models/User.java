package com.johntoro.myapplication.models;
public class User {
    public String fullName, email;
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
}
