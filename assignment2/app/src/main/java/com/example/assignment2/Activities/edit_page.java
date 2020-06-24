package com.example.assignment2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.example.assignment2.Database.PhonebookDb;
import com.example.assignment2.Database.RetrofitServices;
import com.example.assignment2.Model.Contact;
import com.example.assignment2.R;

import java.io.Console;
import java.util.List;

import retrofit2.Retrofit;

public class edit_page extends AppCompatActivity implements RetrofitServices.ResultsHandler
{
    private String TAG = this.getClass().getSimpleName();
    private Contact record;
    private EditText edit_name;
    private EditText edit_email;
    private EditText edit_phone;
    private EditText edit_date;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);
        getSupportActionBar().setTitle("Edit Page");

        edit_name = findViewById(R.id.txt_edit_name);
        edit_email = findViewById(R.id.txt_edit_email);
        edit_phone = findViewById(R.id.txt_edit_phone);
        edit_date = findViewById(R.id.txt_edit_date);

        if(getIntent().hasExtra("selected_ID"))
        {
            id = getIntent().getIntExtra("selected_ID", 0);

            //retrieve contacts from database
            Contact contact = PhonebookDb.getInstance(this).contactDao().getContactById(id);

            edit_name.setText(contact.getName());
            edit_email.setText(contact.getEmail());
            edit_phone.setText(contact.getPhone());
            edit_date.setText(contact.getDate());
        }
        else
        {
            System.out.println("NO CONTACT RECEIVED");
        }

        updateButton(this);
    }

    public void updateButton(Context context)
    {
        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener()
        {
            //on click, update contact
            @Override
            public void onClick(View view)
            {
                String name = edit_name.getText().toString();
                String email = edit_email.getText().toString();
                String phone = edit_phone.getText().toString();
                String date = edit_date.getText().toString();

                //create new contact
                Contact contact = new Contact(id, name, email, phone, date);

                //update
                PhonebookDb.getInstance(context).contactDao().update(contact);

                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }


    @Override
    public void CreateOnResponseHandler(Contact contact)
    {

    }

    @Override
    public void ReadOneOnResponseHandler(Contact contact)
    {
        Log.d(TAG, contact + "received by Edit_Page ReadOneOnResponseHandler");
        edit_name.setText(contact.getName());
        edit_email.setText(contact.getEmail());
        edit_phone.setText(contact.getPhone());
        edit_date.setText(contact.getDate());
    }

    @Override
    public void ReadAllOnResponseHandler(List<Contact> contactList)
    {

    }

    @Override
    public void UpdateOnResponseHandler()
    {

    }

    @Override
    public void DeleteOnResponseHandler(Contact contact)
    {

    }

    @Override
    public void OnFailureHandler()
    {

    }
}

