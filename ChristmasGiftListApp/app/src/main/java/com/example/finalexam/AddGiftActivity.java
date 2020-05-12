package com.example.finalexam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.finalexam.utils.Gift;
import com.example.finalexam.utils.GiftsAdapter;
import com.example.finalexam.utils.Person;
import com.example.finalexam.utils.PersonsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AddGiftActivity extends AppCompatActivity {
    final String TAG = "demo";
    ListView listView;
    GiftsAdapter giftsAdapter;
    ArrayList<Gift> gifts = new ArrayList<>();
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift);
        setTitle(R.string.add_gift);
        listView = findViewById(R.id.listview);
        database = FirebaseDatabase.getInstance().getReference().child("gifts");
        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gifts.clear();
                for(DataSnapshot tasksnapshot:dataSnapshot.getChildren())
                {
                    Gift g = tasksnapshot.getValue(Gift.class);
                    if(g.getPrice()<=getIntent().getExtras().getInt("diff")) {
                        gifts.add(g);
                    }

                }
                giftsAdapter = new GiftsAdapter(AddGiftActivity.this, R.layout.gift_item, gifts);
                listView.setAdapter(giftsAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        giftsAdapter = new GiftsAdapter(this, R.layout.gift_item, gifts);
        listView.setAdapter(giftsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent();
                i.putExtra("name",gifts.get(position).getName());
                Log.d("price",""+gifts.get(position).getPrice());
                i.putExtra("price",gifts.get(position).getPrice());
                setResult(100,i);
                finish();
            }
        });
    }
}
