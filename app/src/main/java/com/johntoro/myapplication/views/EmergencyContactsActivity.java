package com.johntoro.myapplication.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.johntoro.myapplication.R;

/**
 * An activity where the user can view their emergency contacts.
 */
public class EmergencyContactsActivity extends AppCompatActivity {

    /**
     * Overrides this activity's onCreate method.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
    }
}