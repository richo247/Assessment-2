package com.example.assignment2.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.Database.PhonebookDb;
import com.example.assignment2.Helper.MyButtonClickListener;
import com.example.assignment2.Helper.SwiperHelper;
import com.example.assignment2.Lib.MyHash;
import com.example.assignment2.Model.Contact;
import com.example.assignment2.R;
import com.example.assignment2.ViewModel.HashModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class list_page extends AppCompatActivity implements MainListRecyclerViewAdaptor.ContactRecordListener, SearchView.OnQueryTextListener
{
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        loadList();
    }

    private String TAG = this.getClass().getSimpleName();
    MainListRecyclerViewAdaptor adaptor;

    private Contact deletingContact;
    private int ContactIndex;
    AlertDialog.Builder confirm;
    private FloatingActionButton btnAZ;
    private FloatingActionButton btnZA;
    private FloatingActionButton btnAdd;
    private FloatingActionButton btnDel;

    private HashModel hash;
    private int Edit_pos;

    private RecyclerView recyclerViewMainList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);
        getSupportActionBar().setTitle("List Page");

        PhonebookDb.initData(this);

        hash = new ViewModelProvider(this).get(HashModel.class);

        if (hash.myHash == null)
        {
            Log.d(TAG, "ViewModel has not been created yet.");

            ArrayList<Contact> allContacts = (ArrayList<Contact>) PhonebookDb.getInstance(this).contactDao().getAllContacts();
            hash.myHash = new MyHash();
            hash.myHash.buildHashTable(allContacts);
        }
        else
        {
            Log.d(TAG, "ViewModel has been created.");
        }

        recyclerViewMainList = findViewById(R.id.recycler_view);
        confirm = new AlertDialog.Builder(this);
        adaptor = new MainListRecyclerViewAdaptor(hash.myHash.toList(false), this);
        recyclerViewMainList.setAdapter(adaptor);
        recyclerViewMainList.setLayoutManager(new LinearLayoutManager(this));

        //SWIPE CONTACT
        SwiperHelper swipeHelper = new SwiperHelper(this, recyclerViewMainList, 200)
        {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<SwiperHelper.MyButton> buffer)
            {
                buffer.add(new MyButton(list_page.this, "Update", 30, R.drawable.ic_mode_edit_black_24dp, Color.parseColor("#FF3C30"),

                        new MyButtonClickListener()
                        {
                            @Override
                            public void onClick(int pos)
                            {
                                Edit_pos = pos;
                                Intent intent = new Intent(list_page.this, edit_page.class);
                                Contact c = adaptor.getContactList().get(pos);
                                intent.putExtra("selected_ID", c.getId());
                                startActivityForResult(intent, 0);
                            }
                        }));
            }
        };

        //Set OnClick function for the 27 buttons
        setAllNavBtnClickListener();
        floatingAddButton();
        //Sort Ascending
        sortAZ();
        //Sort Descending
        sortZA();
        //Delete a Contact
        deleteContact(this);
    }

    private void loadList()
    {
        hash = new ViewModelProvider(this).get(HashModel.class);
        ArrayList<Contact> allContacts = (ArrayList<Contact>) PhonebookDb.getInstance(this).contactDao().getAllContacts();
        hash.myHash = new MyHash();
        hash.myHash.buildHashTable(allContacts);
        adaptor.reloadContactList(hash.myHash.toList(false));
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        String userInput = newText.toLowerCase();
        ArrayList<Contact> searchList = new ArrayList<>();
        for (Contact contact : (ArrayList<Contact>) PhonebookDb.getInstance(this).contactDao().getAllContacts())
        {
            if (contact.getName().toLowerCase().contains(userInput))
            {
                searchList.add(contact);
            }
        }
        adaptor.reloadContactList(searchList);
        return false;
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    private void floatingAddButton()
    {
        btnAdd = findViewById(R.id.float_add_button);
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(list_page.this, add_page.class);
                startActivityForResult(intent, 0);
            }
        });
    }
    @Override
    public void onClickOfAContact(int position)
    {
        Intent intent = new Intent(this, detail_page.class);
        intent.putExtra("selected_contact", adaptor.getContactList().get(position));
        startActivity(intent);
    }

    @Override
    public void onLongClickOfAContact(int position)
    {
        System.out.println("LONG CLICK Postion: " + position);
        ContactIndex = position;
        deletingContact = adaptor.getContactList().get(position);
    }

    private void deleteContact(Context context) {
        findViewById(R.id.float_delete_button).setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {
                int dragEvent = event.getAction();
                final View view = (View) event.getLocalState();

                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:

                        break;

                    case DragEvent.ACTION_DRAG_EXITED:

                        break;

                    case DragEvent.ACTION_DROP:
                        confirm.setMessage("Do you wish to Delete?");

                        confirm.setPositiveButton("YES", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                PhonebookDb.getInstance(context).contactDao().delete(deletingContact); //remove record
                                loadList();
                                Toast.makeText(list_page.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                        confirm.setNegativeButton("NO", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                Toast.makeText(list_page.this, "Contact couldn't be deleted", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        });
                        confirm.create();
                        confirm.show();
                        break;
                }
                return true;
            }
        });
    }

    private void sortAZ()
    {
        btnAZ = findViewById(R.id.float_AtoZ_button);
        btnAZ.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                adaptor.reloadContactList(hash.myHash.toList(false));
                reverseNavBtns(false);
            }
        });
    }

    private void sortZA()
    {
        btnZA = findViewById(R.id.float_ZtoA_button);
        btnZA.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                adaptor.reloadContactList(hash.myHash.toList(true));
                reverseNavBtns(true);
            }
        });
    }

    private void navBtnClick(int key)
    {
        if (key < 0 || key > 26)
        {
            return;
        }

        int offset = hash.myHash.calcOffsetByKey(key);
        Log.d(TAG, "offset( " + key + ") = " + offset);

        ((LinearLayoutManager) recyclerViewMainList.getLayoutManager()).scrollToPositionWithOffset(offset, 0);
    }

    private void setAllNavBtnClickListener()
    {
        findViewById(R.id.btn_main_nav_ee).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(0);
            }
        });

        findViewById(R.id.btn_main_nav_a).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(1);
            }
        });

        findViewById(R.id.btn_main_nav_b).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(2);
            }
        });

        findViewById(R.id.btn_main_nav_c).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(3);
            }
        });

        findViewById(R.id.btn_main_nav_d).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(4);
            }
        });

        findViewById(R.id.btn_main_nav_e).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(5);
            }
        });

        findViewById(R.id.btn_main_nav_f).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(6);
            }
        });

        findViewById(R.id.btn_main_nav_g).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(7);
            }
        });

        findViewById(R.id.btn_main_nav_h).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(8);
            }
        });

        findViewById(R.id.btn_main_nav_i).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(9);
            }
        });

        findViewById(R.id.btn_main_nav_j).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(10);
            }
        });

        findViewById(R.id.btn_main_nav_k).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(11);
            }
        });

        findViewById(R.id.btn_main_nav_l).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(12);
            }
        });

        findViewById(R.id.btn_main_nav_m).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(13);
            }
        });

        findViewById(R.id.btn_main_nav_n).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(14);
            }
        });

        findViewById(R.id.btn_main_nav_o).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(15);
            }
        });

        findViewById(R.id.btn_main_nav_p).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(16);
            }
        });

        findViewById(R.id.btn_main_nav_q).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(17);
            }
        });

        findViewById(R.id.btn_main_nav_r).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(18);
            }
        });

        findViewById(R.id.btn_main_nav_s).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(19);
            }
        });

        findViewById(R.id.btn_main_nav_t).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(20);
            }
        });

        findViewById(R.id.btn_main_nav_u).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(21);
            }
        });

        findViewById(R.id.btn_main_nav_v).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(22);
            }
        });

        findViewById(R.id.btn_main_nav_w).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(23);
            }
        });

        findViewById(R.id.btn_main_nav_x).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(24);
            }
        });

        findViewById(R.id.btn_main_nav_y).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(25);
            }
        });

        findViewById(R.id.btn_main_nav_z).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                list_page.this.navBtnClick(26);
            }
        });

    }

    private void reverseNavBtns(boolean reverse)
    {
        if (reverse == true)
        {
            TextView z = findViewById(R.id.btn_main_nav_ee);
            z.setText("Z");
            TextView y = findViewById(R.id.btn_main_nav_a);
            y.setText("Y");
            TextView x = findViewById(R.id.btn_main_nav_b);
            x.setText("X");
            TextView w = findViewById(R.id.btn_main_nav_c);
            w.setText("W");
            TextView v = findViewById(R.id.btn_main_nav_d);
            v.setText("V");
            TextView u = findViewById(R.id.btn_main_nav_e);
            u.setText("U");
            TextView t = findViewById(R.id.btn_main_nav_f);
            t.setText("T");
            TextView s = findViewById(R.id.btn_main_nav_g);
            s.setText("S");
            TextView r = findViewById(R.id.btn_main_nav_h);
            r.setText("R");
            TextView q = findViewById(R.id.btn_main_nav_i);
            q.setText("Q");
            TextView p = findViewById(R.id.btn_main_nav_j);
            p.setText("P");
            TextView o = findViewById(R.id.btn_main_nav_k);
            o.setText("O");
            TextView n = findViewById(R.id.btn_main_nav_l);
            n.setText("N");
            TextView m = findViewById(R.id.btn_main_nav_m);
            m.setText("M");
            TextView l = findViewById(R.id.btn_main_nav_n);
            l.setText("L");
            TextView k = findViewById(R.id.btn_main_nav_o);
            k.setText("K");
            TextView j = findViewById(R.id.btn_main_nav_p);
            j.setText("J");
            TextView i = findViewById(R.id.btn_main_nav_q);
            i.setText("I");
            TextView h = findViewById(R.id.btn_main_nav_r);
            h.setText("H");
            TextView g = findViewById(R.id.btn_main_nav_s);
            g.setText("G");
            TextView f = findViewById(R.id.btn_main_nav_t);
            f.setText("F");
            TextView e = findViewById(R.id.btn_main_nav_u);
            e.setText("E");
            TextView d = findViewById(R.id.btn_main_nav_v);
            d.setText("D");
            TextView c = findViewById(R.id.btn_main_nav_w);
            c.setText("C");
            TextView b = findViewById(R.id.btn_main_nav_x);
            b.setText("B");
            TextView a = findViewById(R.id.btn_main_nav_y);
            a.setText("A");
            TextView ha = findViewById(R.id.btn_main_nav_z);
            ha.setText("#");
        }
        else
        {
            TextView ha = findViewById(R.id.btn_main_nav_ee);
            ha.setText("#");
            TextView a = findViewById(R.id.btn_main_nav_a);
            a.setText("A");
            TextView b = findViewById(R.id.btn_main_nav_b);
            b.setText("B");
            TextView c = findViewById(R.id.btn_main_nav_c);
            c.setText("C");
            TextView d = findViewById(R.id.btn_main_nav_d);
            d.setText("D");
            TextView e = findViewById(R.id.btn_main_nav_e);
            e.setText("E");
            TextView f = findViewById(R.id.btn_main_nav_f);
            f.setText("F");
            TextView g = findViewById(R.id.btn_main_nav_g);
            g.setText("G");
            TextView h = findViewById(R.id.btn_main_nav_h);
            h.setText("H");
            TextView i = findViewById(R.id.btn_main_nav_i);
            i.setText("I");
            TextView j = findViewById(R.id.btn_main_nav_j);
            j.setText("J");
            TextView k = findViewById(R.id.btn_main_nav_k);
            k.setText("K");
            TextView l = findViewById(R.id.btn_main_nav_l);
            l.setText("L");
            TextView m = findViewById(R.id.btn_main_nav_m);
            m.setText("M");
            TextView n = findViewById(R.id.btn_main_nav_n);
            n.setText("N");
            TextView o = findViewById(R.id.btn_main_nav_o);
            o.setText("O");
            TextView p = findViewById(R.id.btn_main_nav_p);
            p.setText("P");
            TextView q = findViewById(R.id.btn_main_nav_q);
            q.setText("Q");
            TextView r = findViewById(R.id.btn_main_nav_r);
            r.setText("R");
            TextView s = findViewById(R.id.btn_main_nav_s);
            s.setText("S");
            TextView t = findViewById(R.id.btn_main_nav_t);
            t.setText("T");
            TextView u = findViewById(R.id.btn_main_nav_u);
            u.setText("U");
            TextView v = findViewById(R.id.btn_main_nav_v);
            v.setText("V");
            TextView w = findViewById(R.id.btn_main_nav_w);
            w.setText("W");
            TextView x = findViewById(R.id.btn_main_nav_x);
            x.setText("X");
            TextView y = findViewById(R.id.btn_main_nav_y);
            y.setText("Y");
            TextView z = findViewById(R.id.btn_main_nav_z);
            z.setText("Z");
        }
    }
}
