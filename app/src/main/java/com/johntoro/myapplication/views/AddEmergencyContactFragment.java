package com.johntoro.myapplication.views;
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
import com.johntoro.myapplication.remotes.ContactViewModel;
import com.johntoro.myapplication.R;
import com.johntoro.myapplication.models.EmergencyContact;

/**
 * Represents a fragment where the user can add an emergency contact.
 */
public class AddEmergencyContactFragment extends Fragment {

    private FragmentAddEmergencyContactBinding binding;
    private ProfileActivity profileActivity = new ProfileActivity();
    private ContactViewModel viewModel;
    String userEmail, fullName, email, mobile;

    /**
     * Overrides this fragment's onCreate method.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        userEmail = intent.getStringExtra("email");
        Log.d("AddEmergencyContact", "Email: " + userEmail);
    }

    /**
     * Overrides this fragment's onCreateView method.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return root of binding
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddEmergencyContactBinding.inflate(getActivity().getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        return binding.getRoot();
    }

    /**
     * Overrides this fragment's onViewCreated method.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
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
            }
        });

        //Add Contact button
        binding.addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName = binding.fullnameField.getText().toString().trim();
                mobile = binding.phoneNumberField.getText().toString().trim();
                email = binding.emailField.getText().toString().trim();

                //Error Checking
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
                contact.setUserEmail(userEmail);
                contact.setFullName(fullName);
                contact.setMobile(mobile);
                contact.setEmail(email);
                viewModel.addContact(contact);
            }
        });

        //Back button
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    /**
     * Returns to EmergencyContactsFragment.
     */
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