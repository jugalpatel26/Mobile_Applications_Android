package com.example.jugal.firbasesetup;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String id;
    String task;
    String priority;
    int tposition;
    ListView listView;
    Button add;
    EditText taskname;
    DatabaseReference database;
    ArrayList<TodoItem> todoItems = new ArrayList<>();
    ArrayList<TodoItem> todoItems_completed = new ArrayList<>();
    ArrayList<TodoItem> todoItems_pending = new ArrayList<>();
    String[] spinnerlist = {"High Priority","Medium Priority","Low Priority"};
    Spinner spinner;
    TodoItem t;
    int temp;

    @Override
    protected void onStart() {
        super.onStart();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todoItems.clear();
                todoItems_completed.clear();
                todoItems_pending.clear();

                for(DataSnapshot tasksnapshot:dataSnapshot.getChildren())
                 {
                     t = tasksnapshot.getValue(TodoItem.class);
                     todoItems.add(t);

                 }
                Collections.sort(todoItems, new Comparator<TodoItem>() {
                    @Override
                    public int compare(TodoItem o1, TodoItem o2) {
                        return Integer.valueOf(o1.position).compareTo(o2.position);
                    }
                });
                Collections.sort(todoItems, new Comparator<TodoItem>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public int compare(TodoItem o1, TodoItem o2) {
                        return Integer.valueOf(o1.completed).compareTo(o2.completed);
                    }
                });
                int number = todoItems.size();
                for(int i=0;i<number;i++)
                {
                    if(todoItems.get(i).completed==1)
                    {
                        todoItems_completed.add(todoItems.get(i));
                    }
                    else if(todoItems.get(i).completed==0)
                    {
                        todoItems_pending.add(todoItems.get(i));
                    }
                }
                Log.d("array","aa"+todoItems);
                 listView = (ListView) findViewById(R.id.listview);
                 listadapter listadapter = new listadapter(MainActivity.this,R.layout.listview,todoItems);
                 listView.setAdapter(listadapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TODO List");
        temp=5;
        database = FirebaseDatabase.getInstance().getReference();
        taskname = (EditText) findViewById(R.id.editText_task);
        spinner = (Spinner) findViewById(R.id.spinner);
        add = (Button) findViewById(R.id.button_add);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.spinnerlist,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setPrompt("Priority");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tposition=position;
                priority= spinnerlist[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tposition=0;
                priority="High";
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task= taskname.getText().toString();
                id =database.push().getKey();
                t = new TodoItem(task,priority,tposition,id,0);
                database.child(id).setValue(t);

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuitem_sp)
        {
            listadapter listadapter = new listadapter(MainActivity.this,R.layout.listview,todoItems_pending);
            listView.setAdapter(listadapter);
        }
        else if(item.getItemId()== R.id.menuitem_sc)
        {
            listadapter listadapter = new listadapter(MainActivity.this,R.layout.listview,todoItems_completed);
            listView.setAdapter(listadapter);
        }
        else if(item.getItemId()==R.id.menuitem_sa)
        {
            listadapter listadapter = new listadapter(MainActivity.this,R.layout.listview,todoItems);
            listView.setAdapter(listadapter);
        }
        else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
