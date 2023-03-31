package com.johntoro.myapplication;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johntoro.myapplication.databinding.FragmentEmergencyContactsBinding;
import com.johntoro.myapplication.models.EmergencyContact;

import java.util.Objects;

public class EmergencyContactsFragment extends Fragment {
    private ContactAdapter adapter = new ContactAdapter();
    private FragmentEmergencyContactsBinding binding;
    private ContactViewModel viewModel;
    private String email;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = getActivity().getIntent().getStringExtra("email");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmergencyContactsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        if (email == NULL) {
            Log.d("Emergency Contacts", "Email is null");
        }
        adapter.setEmail(email);
        binding.recyclerViewContacts.setAdapter(adapter);
        binding.addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = ((ViewGroup) getView().getParent()).getId();
                Fragment fragment = new AddEmergencyContactFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(id, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        viewModel.getContact().observe(getViewLifecycleOwner(), new Observer<EmergencyContact>() {
            @Override
            public void onChanged(@Nullable EmergencyContact contact) {
                Log.d("EmergencyContactsFragment", "Contact is " + contact.toString());
                adapter.addContact(contact);
            }
        });
        viewModel.getRealtimeUpdate();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallBack);
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewContacts);
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }
    private ItemTouchHelper.SimpleCallback simpleCallBack =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return true;
                }
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getBindingAdapterPosition();
                    EmergencyContact currentContact = adapter.contactList.get(position);
                    AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();

                    alertDialog.setTitle("Are you sure you want to delete this contact?");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            viewModel.deleteContact(currentContact);
                            (binding.recyclerViewContacts.getAdapter()).notifyItemRemoved(position);

                        }
                    });
                    alertDialog.show();
                    (binding.recyclerViewContacts.getAdapter()).notifyDataSetChanged();
                }

            };

    //return to previous
    public void goBack(){
        requireActivity().finish();
    }
}