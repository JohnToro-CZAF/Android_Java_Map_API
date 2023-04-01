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
import com.johntoro.myapplication.R;
import com.johntoro.myapplication.models.User;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView registerGoogle, login, registerUser;
    private EditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private FirebaseAuth mAuth; //initialise firebase
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user); //set layouts
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
    @Override
    public void onClick(View v) {
        switch(v.getId()){ //get ID of target
            case R.id.prompt_login_2: //if login ->  go to login activity
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.register_button:
                registerUser();
                break;
        }
    }
    private void registerUser() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        //ERROR CHECKING//
        //check if fields empty
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
        //check email format
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide a valid email.");
            editTextEmail.requestFocus();
            return;
        }
        //check that passwords match
        if(!password.equals(confirmPassword)){
            editTextConfirmPassword.setError("Passwords don't match!");
            editTextConfirmPassword.requestFocus();
            return;
        }
        //check that passwords are a correct length
        if(password.length() < 8){
            editTextPassword.setError("Password must be at least 8 characters!");
            editTextPassword.requestFocus();
            return;
        }
        //CREATE USER IN FIREBASE with email, password
        mAuth.createUserWithEmailAndPassword(email, password)
                //if task complete, check if it was successful
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        //if success, create new user
                        if(task.isSuccessful()) {
                            User user = new User(fullName, email);

                            //realtime database; create User in collection "Users"
                            //need to pass database url in getInstance
                            FirebaseDatabase.getInstance("https://sc2006app-e510e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    //set User object to User registered in database (with ID)
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
