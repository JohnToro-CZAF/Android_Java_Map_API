package com.johntoro.myapplication;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.johntoro.myapplication.databinding.FragmentAddEmergencyContactBinding;
import com.johntoro.myapplication.models.EmergencyContact;


public class AddEmergencyContactFragment extends Fragment {

    private FragmentAddEmergencyContactBinding binding;
    private ProfileActivity profileActivity = new ProfileActivity();
    private ContactViewModel viewModel;
    String userEmail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        userEmail = intent.getStringExtra("email");
        Log.d("AddEmergencyContact", "Email: " + userEmail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddEmergencyContactBinding.inflate(getActivity().getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getResult().observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                String message;
                if(o == null){
                    message = getString(R.string.added_contact);
                    goBack();
                }else{
                    message = getString(R.string.added_contact);
                }

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                //dismiss?
            }
        });
        binding.addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validation
                String fullName = binding.fullnameField.getText().toString().trim();
                String mobile = binding.phoneNumberField.getText().toString().trim();
                String email = binding.emailField.getText().toString().trim();

                if(fullName.isEmpty()){
                    binding.fullnameField.setError("Full Name is required!");
                    binding.fullnameField.requestFocus();
                    return;
                }

                if(mobile.isEmpty()){
                    binding.phoneNumberField.setError("Phone Number is required!");
                    binding.phoneNumberField.requestFocus();
                    return;
                }

                if(email.isEmpty()){
                    binding.emailField.setError("Email is required!");
                    binding.emailField.requestFocus();
                    return;
                }

                EmergencyContact contact = new EmergencyContact();
                contact.userEmail = userEmail;
                contact.fullName = fullName;
                contact.mobile = mobile;
                contact.email = email;
                viewModel.addContact(contact);
            }
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }
    //go back
    public void goBack(){
        int id = ((ViewGroup) getView().getParent()).getId();
        Fragment fragment = new EmergencyContactsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}