package com.example.assignment2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assignment2.Database.PhonebookDb;
import com.example.assignment2.Database.RetrofitServices;
import com.example.assignment2.Model.Contact;
import com.example.assignment2.R;

import java.util.ArrayList;

public class add_page extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);
        getSupportActionBar().setTitle("Add Page");
        addButton();
    }

    private void addButton()
    {
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener()
        {

            EditText name_input = findViewById(R.id.txt_add_name);
            EditText email_input = findViewById(R.id.txt_add_email);
            EditText phone_input = findViewById(R.id.txt_add_phone);
            EditText date_input = findViewById(R.id.txt_add_date);

            @Override
            public void onClick(View view)
            {
                String name = name_input.getText().toString();
                String email = email_input.getText().toString();
                String phone = phone_input.getText().toString();
                String date = date_input.getText().toString();

                Contact contact = new Contact(name, email, phone, date);

                //adding to local db
                PhonebookDb.getInstance(getApplicationContext()).contactDao().insert(contact);

                try
                {
                    //using API to add to remote db
                    RetrofitServices.getInstance().AddAContact(contact);
                    Toast.makeText(add_page.this, "successfully added to database", Toast.LENGTH_SHORT).show();
                }
                catch(Exception ex)
                {
                    Toast.makeText(add_page.this, "Couldn't add to database", Toast.LENGTH_SHORT).show();
                }

                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
