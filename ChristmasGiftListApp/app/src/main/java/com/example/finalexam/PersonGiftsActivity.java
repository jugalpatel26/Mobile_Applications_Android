package com.example.finalexam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.finalexam.utils.Gift;
import com.example.finalexam.utils.GiftsAdapter;
import com.example.finalexam.utils.Person;
import com.example.finalexam.utils.SelectGift;
import com.example.finalexam.utils.SelectedGift;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PersonGiftsActivity extends AppCompatActivity {
    final String TAG = "demo";
    ListView listView;
    GiftsAdapter giftsAdapter;
    ArrayList<SelectedGift> gifts = new ArrayList<>();
    ArrayList<Person> persons = new ArrayList<>();
    DatabaseReference database;
    DatabaseReference selectedgifts;
    DatabaseReference person;
    Person p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_gifts);
        database = FirebaseDatabase.getInstance().getReference();
        person = database.child("person");
        selectedgifts = database.child("selectedgifts");
        listView = findViewById(R.id.listview);
        p = new Person();
        person.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                persons.clear();
                for(DataSnapshot tasksnapshot:dataSnapshot.getChildren())
                {
                    Person g = tasksnapshot.getValue(Person.class);
                    persons.add(g);

                }
                int lenght = persons.size();
                for(int i=0;i<lenght;i++) {
                    if (getIntent().getExtras().getString("name").equals(persons.get(i).getName())) {
                        p.setTotalBought(persons.get(i).getTotalBought());
                        p.setGiftCount(persons.get(i).getGiftCount());
                        p.setName(persons.get(i).getName());
                        p.setEmail(persons.get(i).getEmail());
                        p.setId(persons.get(i).getId());
                        p.setTotalBudget(persons.get(i).getTotalBudget());

                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        selectedgifts.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gifts.clear();
                for(DataSnapshot tasksnapshot:dataSnapshot.getChildren())
                {
                    SelectedGift g = tasksnapshot.getValue(SelectedGift.class);
                    if(getIntent().getExtras().getString("name").equals(g.getPersonName())) {
                        gifts.add(g);
                    }

                }
                SelectGift selectGift = new SelectGift(PersonGiftsActivity.this, R.layout.gift_item,gifts);
                listView.setAdapter(selectGift);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.person_gifts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_gift_menu_item:
                Log.d(TAG, "onOptionsItemSelected: ");
                if(p.getTotalBought()==p.getTotalBudget()) {
                    Toast.makeText(PersonGiftsActivity.this, "remaining budget is $0", Toast.LENGTH_SHORT).show();
                }
                else {
                    int diff = p.getTotalBudget()- p.getTotalBought();
                    Intent intent = new Intent(this, AddGiftActivity.class);
                    intent.putExtra("diff",diff);
                    startActivityForResult(intent, 101);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==101&&resultCode==100)
        {
            Log.d("check","result");
            SelectedGift selectedGift = new SelectedGift();
            selectedGift.setPersonName(getIntent().getExtras().getString("name"));
            selectedGift.setId(database.child("selectedgifts").push().getKey());
            selectedGift.setName(data.getExtras().getString("name"));
            int price= data.getExtras().getInt("price");
            selectedGift.setPrice(price);
            int diff = p.getTotalBudget()- p.getTotalBought();
            database.child("selectedgifts").child(selectedGift.getId()).setValue(selectedGift);
            p.setGiftCount(p.getGiftCount() + 1);
            p.setTotalBought(p.getTotalBought() + price);
            person.child(p.getId()).setValue(p);

        }

    }
}
