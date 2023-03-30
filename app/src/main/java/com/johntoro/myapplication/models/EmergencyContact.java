package com.johntoro.myapplication.models;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.Objects;

public class EmergencyContact {
    @Exclude
    public String id = null;
    public String userEmail = null;
    public String fullName = null;
    public String email = null;
    public String mobile = null;
    @Exclude
    public boolean isDeleted = false;
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
    @NonNull
    @Override
    public String toString() {
        return "id: " + id + ", userEmail: " + userEmail + ", fullName: " + fullName + ", email: " + email + ", mobile: " + mobile + ", isDeleted: " + isDeleted;
    }
}
