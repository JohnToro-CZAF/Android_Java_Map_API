package com.johntoro.myapplication.views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.johntoro.myapplication.BuildConfig;
import com.johntoro.myapplication.R;
import com.johntoro.myapplication.models.Constants;
import com.johntoro.myapplication.models.EmergencyContact;
import com.johntoro.myapplication.models.Location;


import java.util.ArrayList;

/**
 * An activity where the user can send messages to their emergency contacts.
 */
public class EmergencyMessageActivity extends AppCompatActivity {
    private String email;
    private ArrayList<EmergencyContact> contactsList;
    private String lat;
    private String longi;
    private Button hangUp;
    ValueEventListener listener;
    private static final int PERMISSION_RQST_SEND = 0;
    DatabaseReference dbcontacts;

    /**
     * Overrides onCreate() to connect to database and send a message to each contact.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_message);

        email = getIntent().getStringExtra("email");
        lat = getIntent().getStringExtra("lat");
        longi = getIntent().getStringExtra("long");

        dbcontacts = FirebaseDatabase.getInstance(BuildConfig.DATABASE_URL).getReference(Constants.NODE_CONTACTS);
        contactsList = new ArrayList<>();

        //When a new contact is sensed, send a message to the contact.
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    EmergencyContact contact = postSnapshot.getValue(EmergencyContact.class);
                    Log.d("ContactList ", "Adding Contact");
                    contactsList.add(contact);
                    if(contact != null)
                        sendEmergencyMessage(contact);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        dbcontacts.addValueEventListener(listener);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        PERMISSION_RQST_SEND);
            }
        }

        //Set listener for Hang Up button
        hangUp = findViewById(R.id.end_call_button);

        hangUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hangUpCall();
            }
        });


    }

    /**
     * Requests for permission and sends a message to each of the user's emergency contacts.
     * @param contact
     */
    public void sendEmergencyMessage(EmergencyContact contact){
        String message = (email + (" needs help! Here are their coordinates: Latitude: ") + lat + " | Longitude: " + longi);
        String phoneNo;

        Log.d("Send Emergency Message", "Running now!");

        phoneNo = contact.getMobile();
        Log.d("Current Phone Number", phoneNo);

        if(contact.getUserEmail().equals(this.email)){
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(getApplicationContext(), "Message Sent",
                        Toast.LENGTH_LONG).show();
                Log.d("Toast", "Message Sent");
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                Log.d("Toast", "Not Sent");
                ex.printStackTrace();
            }
        }
    }

    /**
     * Returns to previous activity.
     */
    private void hangUpCall() {
//        Intent intent = new Intent(EmergencyMessageActivity.this, MapsActivity.class);
//        intent.putExtra("email", email);
//        startActivity(intent);
        dbcontacts.removeEventListener(listener);
        finish();
    }
}