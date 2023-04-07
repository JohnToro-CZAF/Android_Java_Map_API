package com.johntoro.myapplication.views;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.johntoro.myapplication.databinding.RecyclerViewContactBinding;
import com.johntoro.myapplication.models.EmergencyContact;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Updates the UI for the list of emergency contacts.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public List<EmergencyContact> contactList = new ArrayList<>();
    private String email;
    //private Context context;

    /**
     * Default constructor for ContactAdapter.
     */
    public ContactAdapter(){
    }
//    public ContactAdapter(List<EmergencyContact> contactList, Context ctx){
//        this.contactList = contactList;
//        context = ctx;
//    }

    /**
     * Overrides onCreateViewHolder() to initialise view for list of emergency contacts.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return new view
     */
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewContactBinding binding= RecyclerViewContactBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    /**
     * Sets text of emergency contact details.
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.textViewName.setText(contactList.get(position).getFullName());
        holder.binding.textViewContact.setText(contactList.get(position).getMobile());
        holder.binding.textViewEmail.setText(contactList.get(position).getEmail());
    }

    /**
     * Retrieves size of emergency contacts list.
     * @return number of emergency contacts
     */
    @Override
    public int getItemCount() {
        return contactList.size();
    }


    /**
     * Adds contact to emergency contacts list view.
     * @param contact
     */
    public void addContact(EmergencyContact contact){

        //Checks if User Email is null
        if (email == NULL) {
            Log.d("ContactAdapter", "email is null");
        } else {
            Log.d("ContactAdapter", "email is " + email);
        }

        Log.d("ContactAdapter", "contact email is " + contact.getUserEmail());
        Log.d("ContactAdapter", "contact name is " + contact.getFullName());

        //Check if contact is already in the contacts list.
        if(!contactList.contains(contact) && !Objects.equals(contact.getUserEmail(), null) && (contact.getUserEmail()).equals(email)){
            contactList.add(contact);
        }else{
            try {
                int index = contactList.indexOf(contact);

                //Check if contact is deleted and remove from list if so
                if(contact.isDeleted()){
                    contactList.remove(index);
                }else{
                    contactList.set(index,contact);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Initialises view for emergency contacts RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewContactBinding binding;

        public ViewHolder(RecyclerViewContactBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * Sets email of current logged-in user.
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets email of current logged-in user.
     * @return current user email
     */
    public String getEmail() {
        return this.email;
    }

    //public List<EmergencyContact> getContactsList(){return this.contactList;}
}