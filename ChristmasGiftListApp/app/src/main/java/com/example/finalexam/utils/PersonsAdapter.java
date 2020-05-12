package com.example.finalexam.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.finalexam.R;

import java.util.List;

/**
 * Created by mshehab on 5/6/18.
 */

public class PersonsAdapter extends ArrayAdapter<Person>{

    public PersonsAdapter(Context context, int resource, List<Person> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Person person = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){ //if no view to re-use then inflate a new one
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.person_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            viewHolder.textViewPriceBudget = (TextView) convertView.findViewById(R.id.textViewPriceBudget);
            viewHolder.textViewGifts = (TextView) convertView.findViewById(R.id.textViewGifts);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("check"," "+person.getEmail());
        viewHolder.textViewName.setText(person.getName());
        viewHolder.textViewPriceBudget.setText("$"+person.totalBought+"/"+"$"+person.totalBudget);
        if(person.totalBudget==person.totalBought){
            viewHolder.textViewPriceBudget.setTextColor(Color.GREEN);
        }
        else if(person.totalBought==0)
        {
            viewHolder.textViewPriceBudget.setTextColor(Color.GRAY);
            Log.d("check","grey");

        }
        else {
            viewHolder.textViewPriceBudget.setTextColor(Color.RED);
        }
        viewHolder.textViewGifts.setText(person.giftCount+" gifts bought");



        return convertView;
    }

    //View Holder to cache the views
    private static class ViewHolder{
        TextView textViewName;
        TextView textViewPriceBudget;
        TextView textViewGifts;

    }
}
