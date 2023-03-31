package com.johntoro.myapplication;

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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public List<EmergencyContact> contactList = new ArrayList<>();
    private String email;
    private Context context;
    public ContactAdapter(){
    }
    public ContactAdapter(List<EmergencyContact> contactList, Context ctx){
        this.contactList = contactList;
        context = ctx;
    }
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //RecyclerViewContactBinding binding;
        RecyclerViewContactBinding binding= RecyclerViewContactBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    //show contacts
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.textViewName.setText(contactList.get(position).fullName);
        holder.binding.textViewContact.setText(contactList.get(position).mobile);
        holder.binding.textViewEmail.setText(contactList.get(position).email);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
    //add contact to list
    public void addContact(EmergencyContact contact){
        //if contact doesn't already exist in list
        if (email == NULL) {
            Log.d("ContactAdapter", "email is null");
        } else {
            Log.d("ContactAdapter", "email is " + email);
        }
        Log.d("ContactAdapter", "contact email is " + contact.userEmail);
        Log.d("ContactAdapter", "contact name is " + contact.fullName);
        if(!contactList.contains(contact) && !Objects.equals(contact.userEmail, null) && (contact.userEmail).equals(email)){
            contactList.add(contact);
        }else{
            try {
                int index = contactList.indexOf(contact);
                //if contact deleted, remove from list
                if(contact.isDeleted){
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
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewContactBinding binding;

        public ViewHolder(RecyclerViewContactBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }
}