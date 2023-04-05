package com.johntoro.myapplication.views;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.johntoro.myapplication.R;

/**
 * An activity where the user can reset their password.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;

    FirebaseAuth auth;

    /**
     * Overrides onCreate() to connect to authentication database.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //reset password
        emailEditText = findViewById(R.id.email_field);
        resetPasswordButton = findViewById(R.id.resetPassword_button);

        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    /**
     * Resets user password.
     */
    private void resetPassword(){
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide a valid email.");
            emailEditText.requestFocus();
            return;
        }

        //send password reset email
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                }
                else{
                    Toast.makeText(ForgotPasswordActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}