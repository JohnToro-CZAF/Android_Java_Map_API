/**

 This class represents a ViewModel for managing emergency contacts.
 It extends the ViewModel class provided by Android Architecture Components.
 The ViewModel holds a reference to a database and manages a list of EmergencyContact objects.
 It also contains MutableLiveData objects for storing the result of an operation and a single contact.
 */
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

    /**
     * Constructor for ContactViewModel.
     */
    public ContactViewModel(){
        this.dbcontacts = FirebaseDatabase.getInstance(BuildConfig.DATABASE_URL).getReference(Constants.NODE_CONTACTS);
        this.result = new MutableLiveData<>();
        this.contact = new MutableLiveData<>();
        contactsList = new ArrayList<>();

    }

    /**
     * Gets exception if not null.
     * @return exception
     */
    public @Nullable LiveData<Exception> getResult() {
        if (this.result != null) {
            LiveData<Exception> result = this.result;
            return result;
        }else{
            return null;
        }
    }

    /**
     * Gets emergency contact if not null.
     * @return emergency contact
     */
    public @Nullable LiveData<EmergencyContact> getContact(){
        if (this.contact != null) {
            LiveData<EmergencyContact> contact = this.contact;
            return contact;
        }else{
            return null;
        }
    }

    /**
     * Gets emergency contacts list.
     * @return emergency contacts list
     */
    public ArrayList<EmergencyContact> getContactsList(){
        return this.contactsList;
    }

    /**
     * Adds contact to Firebase database.
     * @param contact
     */
    public void addContact(EmergencyContact contact){
        //Create reference for Emergency Contact object and generate key
        contact.setId(dbcontacts.push().getKey());

        //Set value for child in database
        dbcontacts.child(contact.getId()).setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
            /**
             * @param task A task that contains the result of the operation.
             */
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

    /**
     * ChildEventListener to sense when a new contact is added to database.
     */
    private ChildEventListener childEventListener = new ChildEventListener() {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            EmergencyContact contact = snapshot.getValue(EmergencyContact.class);
            contact.setId(snapshot.getKey());
            ContactViewModel.this.contact.setValue(contact);
            contactsList.add(contact);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            EmergencyContact contact = snapshot.getValue(EmergencyContact.class);
            contact.setId(snapshot.getKey());
            contact.setDeleted(true);
            ContactViewModel.this.contact.setValue(contact);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
        /**
         * {@inheritDoc}
         */
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };

    /**
     * Get updates from Firebase database.
     */
    public void getRealtimeUpdate(){
        dbcontacts.addChildEventListener(childEventListener);
    }

    /**
     * Delete contact from Firebase.
     * @param contact emergency contact
     */
    public void deleteContact(EmergencyContact contact){
        dbcontacts.child(contact.getId()).setValue(null)
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

    /**
     * {@inheritDoc}
     * Remove event listener from contact list to prevent resource leak.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        dbcontacts.removeEventListener(childEventListener);
    }
}
