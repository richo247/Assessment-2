package com.example.assignment2.Activities;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.Model.Contact;
import com.example.assignment2.R;

import java.util.ArrayList;
import java.util.Collections;

public class MainListRecyclerViewAdaptor extends RecyclerView.Adapter<MainListRecyclerViewAdaptor.MainListItemViewHolder>
{
    private ArrayList<Contact> contactList;
    private ContactRecordListener contactRecordListener;

    public MainListRecyclerViewAdaptor(ArrayList<Contact> contactList, ContactRecordListener contactRecordListener)
    {
        this.contactList = contactList;
        this.contactRecordListener = contactRecordListener;
    }

    @NonNull
    @Override
    public MainListRecyclerViewAdaptor.MainListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);

        MainListItemViewHolder vh = new MainListItemViewHolder(v, contactRecordListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MainListRecyclerViewAdaptor.MainListItemViewHolder holder, int position)
    {
        holder.txtViewPhone.setText(contactList.get(position).getPhone());
        holder.txtViewName.setText(contactList.get(position).getName());
        holder.txtViewDate.setText(contactList.get(position).getDate());
        holder.txtViewEmail.setText(contactList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return contactList == null? 0 : contactList.size();
    }

    public ArrayList<Contact> getContactList()
    {
        return contactList;
    }

    class MainListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        private TextView txtViewPhone;
        private TextView txtViewName;
        private TextView txtViewDate;
        private TextView txtViewEmail;

        ContactRecordListener contactRecordListener;

        public MainListItemViewHolder(View itemView, ContactRecordListener contactRecordListener)
        {
            super(itemView);
            txtViewName = itemView.findViewById(R.id.txt_name);
            txtViewPhone = itemView.findViewById(R.id.txt_phone);
            txtViewEmail = itemView.findViewById(R.id.txt_email);
            txtViewDate = itemView.findViewById(R.id.txt_date);

            this.contactRecordListener = contactRecordListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            contactRecordListener.onClickOfAContact(getAdapterPosition());
        }


        @Override
        public boolean onLongClick(View view)
        {
            contactRecordListener.onLongClickOfAContact(getAdapterPosition());
            ClipData data = ClipData.newPlainText("ClipData","Hello?");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, myShadowBuilder, view, 0);

            return false;
        }
    }


    public void reloadContactList(ArrayList<Contact> contactList)
    {
        this.contactList = contactList;
        this.notifyDataSetChanged();
    }

    public interface ContactRecordListener
    {
        void onClickOfAContact(int position);
        void onLongClickOfAContact(int position);
    }
}
