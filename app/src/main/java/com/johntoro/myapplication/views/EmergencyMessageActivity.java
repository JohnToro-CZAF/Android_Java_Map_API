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

public class EmergencyMessageActivity extends AppCompatActivity {

    private String email;
    private ArrayList<EmergencyContact> contactsList;

    private String lat;
    private String longi;
    private Button hangUp;

    private static final int PERMISSION_RQST_SEND = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_message);

        email = getIntent().getStringExtra("email");
        lat = getIntent().getStringExtra("lat");
        longi = getIntent().getStringExtra("long");

        //DatabaseReference dbcontacts = FirebaseDatabase.getInstance("https://sc2006app-e510e-default-rtdb.asia-southeast1.firebasedatabase.app").getReference(Constants.NODE_CONTACTS);
        DatabaseReference dbcontacts = FirebaseDatabase.getInstance(BuildConfig.DATABASE_URL).getReference(Constants.NODE_CONTACTS);
        contactsList = new ArrayList<>();

        dbcontacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    EmergencyContact contact = postSnapshot.getValue(EmergencyContact.class);
                    Log.d("ContactList ", "Adding Contact");
                    contactsList.add(contact);
                    sendEmergencyMessage(contact);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

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

        hangUp = findViewById(R.id.end_call_button);

        hangUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hangUpCall();
            }
        });


    }

    public void sendEmergencyMessage(EmergencyContact contact){
        String message = (email + (" needs help! Here are their coordinates: Latitude: ") + lat + " | Longitude: " + longi);
        String phoneNo;

        Log.d("Send Emergency Message", "Running now!");

        phoneNo = contact.mobile;
        Log.d("Current Phone Number", phoneNo);

        if(contact.userEmail.equals(this.email)){
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

    private void hangUpCall() {
        Intent intent = new Intent(EmergencyMessageActivity.this, MapsActivity.class);
        startActivity(intent);
    }
}