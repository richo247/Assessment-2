package com.example.assignment2.Database;

import android.util.Log;

import com.example.assignment2.Model.Contact;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServices
{
    private static RetrofitServices single_instance = null;
    private static RemotePhonebookDb remotePhonebookDb;

    private String TAG = this.getClass().getSimpleName();

    private RetrofitServices()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.15.6:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        remotePhonebookDb = retrofit.create(RemotePhonebookDb.class);

    }

    public static RetrofitServices getInstance()
    {
        if (single_instance == null)
        {
            single_instance = new RetrofitServices();
        }
        return single_instance;
    }

    public void GetContactById(int id, final ResultsHandler handler)
    {
        Call<Contact> contactByID = remotePhonebookDb.GetContactByID(id);
        contactByID.enqueue(new Callback<Contact>()
        {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response)
            {
                Contact contact = response.body();
                Log.d(TAG, contact.toString());

                handler.ReadOneOnResponseHandler(contact);

                return;
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t)
            {
                Log.d(TAG, "onFailure");
                handler.OnFailureHandler();
                return;
            }
        });
        return;
    }

    public void AddAContact(Contact newContact)
    {
        Call<Contact> contactCreate = remotePhonebookDb.CreateContact(newContact);
        contactCreate.enqueue(new Callback<Contact>()
        {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response)
            {
                Contact contact = response.body();
                Log.d(TAG, contact.toString() + " ADDED TO SERVER DB.");
                return;
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t)
            {
                Log.d(TAG, "onFailure");
                return;
            }
        });
    }

    public void GetAllContacts()
    {
        Call<List<Contact>> allContacts = remotePhonebookDb.GetAllContacts();
        allContacts.enqueue(new Callback<List<Contact>>()
        {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response)
            {
                List<Contact> list = response.body();
                Log.d(TAG, list.toString());
                return;
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t)
            {
                Log.d(TAG, "onFailure");
                return;
            }
        });
    }

    public void UpdateContactById(Contact updatedContact)
    {
        Call<Void> contactUpdate = remotePhonebookDb.UpdateContactByID(updatedContact.getId(), updatedContact);
        contactUpdate.enqueue(new Callback<Void>()
        {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                Log.d(TAG, "onResponse");
                return;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t)
            {
                Log.d(TAG, "onFailure");
                return;
            }
        });
    }

    public void deleteContactById(int id)
    {
        Call<Contact> contactDelete = remotePhonebookDb.DeleteContactByID(id);
        contactDelete.enqueue(new Callback<Contact>()
        {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response)
            {
                Contact contact = response.body();
                Log.d(TAG, "Response code: " + Integer.toString(response.code()));
                Log.d(TAG, contact.toString());
                return;
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t)
            {
                Log.d(TAG, "onFailure");
                return;
            }
        });
    }

    public interface ResultsHandler
    {
        void CreateOnResponseHandler(Contact contact);

        void ReadOneOnResponseHandler(Contact contact);

        void ReadAllOnResponseHandler(List<Contact> contactList);

        void UpdateOnResponseHandler();

        void DeleteOnResponseHandler(Contact contact);

        void OnFailureHandler();
    }
}
