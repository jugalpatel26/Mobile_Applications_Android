package com.example.jugal.firbasesetup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class listadapter extends ArrayAdapter<TodoItem> {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public listadapter(@NonNull Context context, int resource, @NonNull List<TodoItem> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final TodoItem todoItem = getItem(position);
        Holder holder;
        if(convertView == null)
        {
            Log.d("b","aa"+todoItem);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview, parent, false);
            holder = new Holder();
            holder.task = convertView.findViewById(R.id.textview_task);
            holder.priority = convertView.findViewById(R.id.textView_priority);
            holder.date = convertView.findViewById(R.id.textView_time);
            convertView.setTag(holder);

        }
        else {
                Log.d("a","else");
                holder = (Holder) convertView.getTag();

        }
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        if(todoItem.completed==1) {
            checkBox.setChecked(true);
        }
        holder.task.setText(todoItem.task);
        holder.priority.setText(todoItem.priority);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    Log.d("a","bb"+todoItem.id);
                    databaseReference.child(todoItem.id).child("completed").setValue(1);
                }
                else if(isChecked==false)
                {
                    databaseReference.child(todoItem.id).child("completed").setValue(0);
                }
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                databaseReference.child(todoItem.id).removeValue();
                return false;
            }
        });

        return convertView;

    }
    private static class Holder{
        TextView task;
        TextView priority;
        TextView date;


    }

}
