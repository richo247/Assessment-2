package com.example.assignment2.Dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import com.example.assignment2.Model.Contact;

import java.util.List;

@androidx.room.Dao
public interface ContactDao
{
    @Query("SELECT * FROM contact WHERE id = :contactID")
    public Contact getContactById(int contactID);

    @Update
    public void update(Contact...contacts);

    @Query("DELETE FROM contact")
    public void clearTable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Contact...contacts);

    @Delete
    public void delete(Contact...contacts);

    @Query("SELECT * FROM contact")
    public List<Contact> getAllContacts();

}
