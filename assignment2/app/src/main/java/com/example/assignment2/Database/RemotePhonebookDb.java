package com.example.assignment2.Database;

import com.example.assignment2.Model.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RemotePhonebookDb
{
    @POST("Contacts")
    Call<Contact> CreateContact(@Body Contact contact);

    @GET("Contacts")
    Call<List<Contact>> GetAllContacts();

    @GET("Contacts/{id}")
    Call<Contact> GetContactByID(@Path("id") int id);

    @PUT("Contacts/{id}")
    Call<Void> UpdateContactByID(@Path("id") int id, @Body Contact contact);

    @DELETE("Contacts/{id}")
    Call<Contact> DeleteContactByID(@Path("id") int id);
}
