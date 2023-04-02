package com.johntoro.myapplication.remotes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.johntoro.myapplication.BuildConfig;
import com.johntoro.myapplication.models.Constants;
import com.johntoro.myapplication.models.EmergencyContact;
import java.util.ArrayList;

public class ContactViewModel extends ViewModel {
    public DatabaseReference dbcontacts;
    public ArrayList<EmergencyContact> contactsList;
    public final @Nullable MutableLiveData<Exception> result;
    public final @Nullable MutableLiveData<EmergencyContact> contact;
    
    public ContactViewModel(){
        this.dbcontacts = FirebaseDatabase.getInstance(BuildConfig.DATABASE_URL).getReference(Constants.NODE_CONTACTS);

        this.result = new MutableLiveData<>();
        this.contact = new MutableLiveData<>();
        contactsList = new ArrayList<>();

    }
    public @Nullable LiveData<Exception> getResult() {
        if (this.result != null) {
            LiveData<Exception> result = this.result;
            return result;
        }else{
            return null;
        }
    }
    public @Nullable LiveData<EmergencyContact> getContact(){
        if (this.contact != null) {
            LiveData<EmergencyContact> contact = this.contact;
            return contact;
        }else{
            return null;
        }
    }
    //get emergency contacts list
    public ArrayList<EmergencyContact> getContactsList(){
        return this.contactsList;
    }
    public void addContact(EmergencyContact contact){
        //create reference for object and generate key
        contact.id = dbcontacts.push().getKey();
        //set value for child in database
        dbcontacts.child(contact.id).setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    result.setValue(null);
                }else{
                    result.setValue(task.getException());
                }
            }
        });

    }

    //listen for when a new contact added
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            EmergencyContact contact = snapshot.getValue(EmergencyContact.class);
            contact.id = snapshot.getKey();
            ContactViewModel.this.contact.setValue(contact);
            contactsList.add(contact);
        }
        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
        //notify UI that contact has been deleted
        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            EmergencyContact contact = snapshot.getValue(EmergencyContact.class);
            contact.id = snapshot.getKey();
            contact.isDeleted = true;
            ContactViewModel.this.contact.setValue(contact);
        }
        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };

    //get updates to database
    public void getRealtimeUpdate(){
        dbcontacts.addChildEventListener(childEventListener);
    }

    //Delete Contact
    public void deleteContact(EmergencyContact contact){
        dbcontacts.child(contact.id).setValue(null)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            result.setValue(null);
                        }
                        else
                            result.setValue(task.getException());
                    }
                });
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        dbcontacts.removeEventListener(childEventListener);
    }


}
