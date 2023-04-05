package com.johntoro.myapplication.views;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
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

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johntoro.myapplication.databinding.FragmentEmergencyContactsBinding;
import com.johntoro.myapplication.models.EmergencyContact;
import com.johntoro.myapplication.remotes.ContactViewModel;
import com.johntoro.myapplication.views.AddEmergencyContactFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity where the user can view their emergency contacts.
 */
public class EmergencyContactsFragment extends Fragment {
    private ContactAdapter adapter = new ContactAdapter();
    private FragmentEmergencyContactsBinding binding;
    private ContactViewModel viewModel;
    private String email;
    private ArrayList<EmergencyContact> contactsList;

    /**
     * Overrides activity's onCreate method to retrieve the user email.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = getActivity().getIntent().getStringExtra("email");

    }

    /**
     * Overrides onCreateView() to show list of emergency contacts.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmergencyContactsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        return binding.getRoot();
    }

    /**
     * Overrides onViewCreated() to update list view of emergency contacts.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
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

    /**
     * Sets an emergency contact to be deleted when swiped.
     */
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

    /**
     * Return to previous activity.
     */
    public void goBack(){
        requireActivity().finish();
    }
}