package com.example.assignment2.Lib;


import com.example.assignment2.Model.Contact;

import java.util.ArrayList;
import java.util.Collections;


public class MyHash
{
    private ArrayList<Contact> hashTable[];

    //creating hash table for all letters of alphabet + #.
    public MyHash()
    {
        hashTable = new ArrayList[27];
        for(int i = 0; i < hashTable.length; i++)
        {
            hashTable[i] = new ArrayList<Contact>();
        }
    }

    //hash table length will be based on how many contacts there are in the table.
    public ArrayList<Contact> toList(boolean reverse)
    {
        ArrayList<Contact> c = new ArrayList ();
        for(int i = 0; i < hashTable.length; i++)
        {

            for(int j = 0; j < hashTable[i].size(); j++)
            {
                c.add(hashTable[i].get(j));
            }
        }
        if(reverse == true)
        {
            Collections.reverse(c);
        }
        return c;
    }

    // for a specific key, this function calculate the offset of the element
    // who's first letter start with "key" in the arraylist.
    // for example. in the following list, if key == c/C, offset = 3 + 1 = 4.
    // meaning that the first name in C list has an index of 4.
    // A -> Alan -> Alex -> Amahli
    // B -> Bob
    // C -> Cali -> Cindy
    public int calcOffsetByKey(int k)
    {
        int offset = 0;
        if (k < 0 || k >= hashTable.length)
        {
            offset = 0;
        } else {
            for(int i = 0; i < k; i++){
                // offset is the sum of the size of all previous list.
                offset = offset + hashTable[i].size();
            }
        }
        return offset;
    }

    // build hash table. use arraylist to resolve collision.
    // sort all arraylists
    public void buildHashTable(ArrayList<Contact> list)
    {
        if(list == null)
        {
            return;
        }
        for(int i = 0; i < list.size(); i++)
        {
            Contact c = list.get(i);
            int hashTableIndex = hash(c.getName());
            hashTable[hashTableIndex].add(c);
        }

        for(int i = 0; i < hashTable.length; i++)
        {
            Collections.sort(hashTable[i]);
        }
        return;
    }

    private int hash(String s)
    {
        char c = s.toUpperCase().charAt(0);
        int asciiValue = (int)c;
        if(asciiValue >= 65 && asciiValue <= 90)
        {
            asciiValue = asciiValue - 64;
        }
        else
        {
            asciiValue = 0;
        }
        return asciiValue;
    }



    public void dump()
    {
        for(int i = 0; i < hashTable.length; i++)
        {
            System.out.print("[" + i + "] ");
            for(int j = 0; j < hashTable[i].size(); j++)
            {
                System.out.print("->(" + hashTable[i].get(j).getName() + " / " + hashTable[i].get(j).getPhone() + ")");
            }
            System.out.print("\n");
        }
    }
}