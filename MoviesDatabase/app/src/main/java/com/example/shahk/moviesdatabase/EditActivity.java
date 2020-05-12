package com.example.shahk.moviesdatabase;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {
    int selectedItemIndex;
    Spinner genere;
    EditText e_name;
    EditText e_d;
    EditText e_y;
    EditText e_im;
    SeekBar e_r;
    EditText e_p;
    Button e_b;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        final Movie m1 = (Movie)intent.getSerializableExtra("edit_movie");
        e_name= (EditText) findViewById(R.id.Et_ne);
        e_d=(EditText) findViewById(R.id.Et_de);
        e_y=(EditText) findViewById(R.id.Et_ye);
        e_im=(EditText) findViewById(R.id.Et_ime);
        e_r=(SeekBar) findViewById(R.id.Sb_re);
        e_p=(EditText) findViewById(R.id.Et_p);
        e_b=(Button) findViewById(R.id.Button_save);



        genere = (Spinner)findViewById(R.id.spinner_genre_options);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,AddMovie.genereOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genere.setAdapter(adapter);
        for(int i=0;i<AddMovie.genereOptions.length;i++){
            if(AddMovie.genereOptions[i].equals(m1.genere)){

                selectedItemIndex = i;


            }
        }
        genere.setSelection(selectedItemIndex);

        e_name.setText(m1.movieName);
        e_im.setText(m1.imdb);
        e_d.setText(m1.desc);
        e_r.setProgress(m1.rating);
        e_p.setText(Integer.toString(m1.rating));
        e_y.setText(Integer.toString(m1.year));

        e_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String movie_name = e_name.getText().toString();
                    String movie_dec = e_d.getText().toString();
                    int movie_year = Integer.parseInt(e_y.getText().toString());
                    String imdb_str = e_im.getText().toString();
                    String genere_str = genere.getSelectedItem().toString();
                    int rating_p = Integer.valueOf(e_r.getProgress());
                    if(movie_year<=2018&&movie_year>1800) {
                        Movie m1 = new Movie(movie_name, rating_p, genere_str, imdb_str, movie_dec, movie_year);
                        Intent i = new Intent();
                        i.putExtra("serialize_data", m1);
                        setResult(Activity.RESULT_OK, i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(EditActivity.this,"Please enter valid year",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(EditActivity.this,"Please fill in all the fields",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
