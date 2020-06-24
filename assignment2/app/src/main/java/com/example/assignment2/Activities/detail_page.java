package com.example.assignment2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.assignment2.Model.Contact;
import com.example.assignment2.R;

public class detail_page extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        getSupportActionBar().setTitle("Detail Page");

        TextView name = findViewById(R.id.detail_name);
        TextView email = findViewById(R.id.detail_email);
        TextView phone= findViewById(R.id.detail_phone);
        TextView date = findViewById(R.id.detail_date);

        //Fill TextViews with Selected Contact
        if(getIntent().hasExtra("selected_contact"))
        {
            Contact record = getIntent().getParcelableExtra("selected_contact");
            name.setText(record.getName());
            email.setText(record.getEmail());
            phone.setText(record.getPhone());
            date.setText(record.getDate());
        }
        else
        {
            System.out.println("NO CONTACT RECEIVED");
        }
        backButton();
    }

    private void backButton()
    {
        findViewById(R.id.btn_back_detail).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
