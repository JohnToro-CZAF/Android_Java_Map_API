package com.johntoro.myapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.johntoro.myapplication.BuildConfig;
import com.johntoro.myapplication.R;
import com.johntoro.myapplication.models.User;

/**
 * An activity where a user can register for a new account.
 */
public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView login, registerUser;
    private EditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private FirebaseAuth mAuth;

    private String fullName, email, password, confirmPassword;

    /**
     * Overrides onCreate() to define view elements.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        mAuth = FirebaseAuth.getInstance();
        registerUser = (Button) findViewById(R.id.register_button);
        registerUser.setOnClickListener(this);
        login = (TextView) findViewById(R.id.prompt_login_2);
        login.setOnClickListener(this);
        editTextFullName = (EditText) findViewById(R.id.fullname_field);
        editTextEmail = (EditText) findViewById(R.id.email_field);
        editTextPassword = (EditText) findViewById(R.id.password_field);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirmpassword_field);
    }

    /**
     * Sets onClickListener() for view elements.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.prompt_login_2:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.register_button:
                registerUser();
                break;
        }
    }

    /**
     * Registers a new user in the database.
     */
    private void registerUser() {
        fullName = editTextFullName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        confirmPassword = editTextConfirmPassword.getText().toString().trim();

        //ERROR CHECKING

        //Check that fields are not empty
        if(fullName.isEmpty()){
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if(confirmPassword.isEmpty()){
            editTextConfirmPassword.setError("Please re-enter your password!");
            editTextConfirmPassword.requestFocus();
            return;
        }

        //Check email format
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide a valid email.");
            editTextEmail.requestFocus();
            return;
        }

        //Check that passwords match
        if(!password.equals(confirmPassword)){
            editTextConfirmPassword.setError("Passwords don't match!");
            editTextConfirmPassword.requestFocus();
            return;
        }

        //Check that passwords are a correct length
        if(password.length() < 8){
            editTextPassword.setError("Password must be at least 8 characters!");
            editTextPassword.requestFocus();
            return;
        }

        //Create user in Firebase with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                //Check that task was successful
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        //If success, create new user
                        if(task.isSuccessful()) {
                            User user = new User(fullName, email);

                            //Add User to Firebase Realtime Database
                            FirebaseDatabase.getInstance(BuildConfig.DATABASE_URL).getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //error messages
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterUserActivity.this, "User has been registered!", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(RegisterUserActivity.this, LoginActivity.class)); //take to user
                                            } else {
                                                Toast.makeText(RegisterUserActivity.this, "Failed to register. Try again!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(RegisterUserActivity.this, "Failed to register. Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
