package com.example.jugal.sqlite;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
public class MainActivity extends AppCompatActivity implements main{
    ListView listView;
    Button add;
    EditText taskname;
    ArrayList<Note> todoItems = new ArrayList<>();
    ArrayList<Note> todoItems_completed = new ArrayList<>();
    ArrayList<Note> todoItems_pending = new ArrayList<>();
    String[] spinnerlist = {"High Priority","Medium Priority","Low Priority"};
    Spinner spinner;
    Note t;
    public listadapter listadapter;
    DatabaseDataManager dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TODO List");
        t = new Note();
        dm = new DatabaseDataManager(this);
        listView = (ListView) findViewById(R.id.listview);
        listviewmanager(dm);
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
                t.setPosition(position);
                t.setPriority(spinnerlist[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                t.setPosition(0);
                t.setPriority("High");
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setTask(taskname.getText().toString());
                t.setStatus(0);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                t.setDate(dateFormat.format(date).toString());
                dm.saveTask(t);
                listviewmanager(dm);
                Log.d("a",""+dm.getAllTask());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
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



    @Override
    public void calllistview(Note note) {
        Log.d("check","callview");
        Log.d("note",""+note);


    }

    @Override
    public void update(Note note)
    {
        dm.UpdateTask(note);

        Log.d("check","update");
    }
    public void listviewmanager(DatabaseDataManager dm){
        todoItems.clear();
        todoItems_pending.clear();
        todoItems_completed.clear();
        todoItems = dm.getAllTask();
        int lenght = todoItems.size();
        Collections.sort(todoItems, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                return Integer.valueOf(o1.getPosition()).compareTo(o2.getPosition());
            }
        });
        Collections.sort(todoItems, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                return Integer.valueOf(o1.getStatus()).compareTo(o2.getStatus());
            }
        });
        for(int i=0;i<lenght;i++){
            if(todoItems.get(i).getStatus()==0){
                Note n = todoItems.get(i);
                todoItems_pending.add(n);
            }
            else if(todoItems.get(i).getStatus()==1){
                Note n = todoItems.get(i);
                todoItems_completed.add(n);
            }


        }
        Log.d("list",""+dm.getAllTask());
        Log.d("sorted",""+todoItems);
        Log.d("sortedchech",""+todoItems_completed);
        Log.d("sortnotchech",""+todoItems_pending);
        listadapter = new listadapter(MainActivity.this,R.layout.listview,todoItems);
        listView.setAdapter(listadapter);

    }
    public class listadapter extends ArrayAdapter<Note> {
        Context context;
        DatabaseDataManager dm;
        main main = new MainActivity();
        List<Note> array;
        public listadapter(@NonNull Context context, int resource, @NonNull List<Note> objects) {
            super(context, resource, objects);
            this.context=context;
            this.array=objects;
            dm = new DatabaseDataManager(this.context);
        }
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Note todoItem = getItem(position);
            if(convertView == null)
            {
                Log.d("b","aa"+todoItem);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview, parent, false);

            }
            TextView task = convertView.findViewById(R.id.textview_task);
            TextView priority = convertView.findViewById(R.id.textView_priority);
            TextView date = convertView.findViewById(R.id.textView_time);
            PrettyTime p = new PrettyTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date dates = dateFormat.parse(todoItem.getDate());
                date.setText(p.format(dates));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            CheckBox checkBox;

                if (todoItem.getStatus() == 1) {
                    checkBox = convertView.findViewById(R.id.checkBox);
                    checkBox.setChecked(true);
                    if(array.size()!=dm.getAllTask().size())
                    {
                        checkBox.setVisibility(View.INVISIBLE);
                    }
                } else {
                    checkBox = convertView.findViewById(R.id.checkBox);
                    checkBox.setChecked(false);
                    if(array.size()!=dm.getAllTask().size())
                    {
                        checkBox.setVisibility(View.INVISIBLE);
                    }

                }



            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    if (checked) {
                        Toast.makeText(context, "Checked and saved", Toast.LENGTH_LONG).show();
                        todoItem.setStatus(1);
                        dm.UpdateTask(todoItem);
                        listviewmanager(dm);
                        Log.d("check", "" + dm.getAllTask());
                    } else {
                        Toast.makeText(context, "Un-Checked and saved", Toast.LENGTH_LONG).show();
                        todoItem.setStatus(0);
                        dm.UpdateTask(todoItem);
                        listviewmanager(dm);
                        Log.d("check", "" + dm.getAllTask());
                    }

                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Do you want to delete this task?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dm.deleteTask(todoItem);
                            listviewmanager(dm);
                            Toast.makeText(context, "Task Deleted", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                    Log.d("newarray",""+array);
                    return false;
                }
            });

            task.setText(todoItem.getTask());
            priority.setText(todoItem.getPriority());
            return convertView;

        }






    }


}
