package com.example.assignment2.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.assignment2.Dao.ContactDao;
import com.example.assignment2.Model.Contact;
@Database(
        entities = {Contact.class},
        version = 1,
        exportSchema = false
)
public abstract class PhonebookDb extends RoomDatabase
{

    private final String TAG = this.getClass().getSimpleName();

    public abstract ContactDao contactDao();

    private static PhonebookDb phonebookDb;

    public static PhonebookDb getInstance(final Context context)
    {
        if(phonebookDb == null)
        {
            phonebookDb = Room.databaseBuilder(
                    context.getApplicationContext(),
                    PhonebookDb.class,
                    "phonebook_room.db"
            ).allowMainThreadQueries().build();
        }
        return  phonebookDb;
    }

    public static int initData(final Context context)
    {
        PhonebookDb db = getInstance(context);
        if(db.contactDao().getAllContacts().size() == 0)
        {
            db.contactDao().insert(
            new Contact("D Luffy", "DLuffy@gmail.com", "123 123 123","11/03/2005"),
            new Contact("A Juniper", "Juniper@gmail.com", "123 123 123","01/09/2009"),
            new Contact("A Sycamore", "Sycamore@gmail.com", "123 123 123","01/01/1999"),
            new Contact("Y Berlitz", "Berlitz@gmail.com", "123 123 123","01/02/2004"),
            new Contact("B Belcher", "Belcher@gmail.com", "123 123 123","01/08/2003"),
            new Contact("J Seinfeld", "Seinfeld@gmail.com", "123 123 123","01/07/2006"),
            new Contact("L David", "David@gmail.com", "123 123 123","01/03/1994"),
            new Contact("L Black", "Black@gmail.com", "123 123 123","01/04/1985"),
            new Contact("S Oak", "Oak@gmail.com", "123 123 123","01/06/1976"),
            new Contact("V Ives", "Ives@gmail.com", "123 123 123","01/05/1965")
            );
        }
        return db.contactDao().getAllContacts().size();
    }
}


