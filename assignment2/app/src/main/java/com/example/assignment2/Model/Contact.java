package com.example.assignment2.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact")
public class Contact implements Comparable, Parcelable
{
    public static final Creator<Contact> CREATOR = new Creator<Contact>()
    {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "date")
    private String date;

    public Contact()
    {

    }

    @Ignore
    public Contact(String name, String email, String phone, String date)
    {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.date = date;
    }

    @Ignore
    public Contact(int id, String name, String phone, String email, String date)
    {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.date = date;
    }

    @Ignore
    protected Contact(Parcel in)
    {
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        date = in.readString();
    }

    public int getId() {return id;}

    public void setId(int id) { this.id = id; }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return this.name.compareToIgnoreCase(((Contact) o).name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(email);
        parcel.writeString(date);
    }
}