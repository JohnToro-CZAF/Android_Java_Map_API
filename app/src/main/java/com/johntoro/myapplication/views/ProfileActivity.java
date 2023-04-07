package com.johntoro.myapplication.views;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.johntoro.myapplication.R;
import com.johntoro.myapplication.models.User;

/**
 * An activity where the user can view their profile.
 */
public class ProfileActivity extends AppCompatActivity{

    //create user
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button backButton;
    private Button logoutButton;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    String fullName;
    String email;
    static String userEmail;


    /**
     * Overrides onCreate() to initialise UI showing current user profile information based on
     * Google login or Email and Password login.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final TextView fullNameTextView = (TextView) findViewById(R.id.fullNameText);
        final TextView emailTextView = (TextView) findViewById(R.id.emailText);
        backButton = (Button) findViewById(R.id.back_Button);
        logoutButton = (Button) findViewById(R.id.logout_button);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Retrieve information from user logged in with Google
        gsc=GoogleSignIn.getClient(this,gso);
        //check if user signed in with google
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this);

        //Retrieve information from user logged in with email and password
        if(googleAccount == null){
            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance("https://sc2006app-e510e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
            userID = user.getUid();
            userEmail = user.getEmail();

            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //if user didn't log in with google
                    User userProfile = snapshot.getValue(User.class);
                    fullName = userProfile.getFullName();
                    email = userProfile.getEmail();

                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            fullName = googleAccount.getDisplayName();
            email = googleAccount.getEmail();
            userEmail = googleAccount.getEmail();
            fullNameTextView.setText(fullName);
            emailTextView.setText(email);
        }

        //Return to previous activity
        backButton.setOnClickListener((View view) -> {
            goBack();
        });

        //Log out of user account
        logoutButton.setOnClickListener((View view) -> {
            if(googleAccount != null)
                googleLogOut();
            else
                logOut();
        });
    }

    /**
     * Logs out of user account that logged in with email and password.
     */
    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    /**
     * Logs out of user account that logged in with Google.
     */
    private void googleLogOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    /**
     * Returns to previous activity.
     */
    private void goBack() {
        finish();
    }

    /**
     * Retrieves current user email.
     * @return user email
     */
    public static String getUserEmail(){
        return userEmail;
    }
}