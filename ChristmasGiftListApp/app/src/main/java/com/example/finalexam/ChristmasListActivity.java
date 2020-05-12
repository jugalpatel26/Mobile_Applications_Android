package com.example.finalexam;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.finalexam.utils.Person;
import com.example.finalexam.utils.PersonsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChristmasListActivity extends AppCompatActivity {
    final String TAG = "demo";
    ListView listView;
    PersonsAdapter personsAdapter;
    ArrayList<Person> persons = new ArrayList<>();
    DatabaseReference database;
    Person p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_christmas_list);
        setTitle(R.string.main_name);
        database = FirebaseDatabase.getInstance().getReference().child("person");
        listView = findViewById(R.id.listview);
        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                persons.clear();
                for(DataSnapshot tasksnapshot:dataSnapshot.getChildren())
                {
                    p = tasksnapshot.getValue(Person.class);
                    if(p.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        persons.add(p);
                    }

                }
                Collections.sort(persons, new Comparator<Person>() {
                    @Override
                    public int compare(Person o1, Person o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                personsAdapter = new PersonsAdapter(ChristmasListActivity.this, R.layout.person_item, persons);
                listView.setAdapter(personsAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(ChristmasListActivity.this,PersonGiftsActivity.class);
                i.putExtra("name",persons.get(position).getName());
                startActivity(i);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.christmas_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_person_menu_item:
                Log.d(TAG, "onOptionsItemSelected: add person");
                Intent intent = new Intent(this, AddPersonActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout_menu_item:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ChristmasListActivity.this, LoginActivity.class);
                startActivity(i);
                Log.d(TAG, "onOptionsItemSelected: logout");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
