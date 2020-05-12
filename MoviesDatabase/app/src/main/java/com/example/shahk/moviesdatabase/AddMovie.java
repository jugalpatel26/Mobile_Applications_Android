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
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class AddMovie extends AppCompatActivity {

    EditText movieName;
    EditText movieDesc;
    EditText year;
    Spinner genre;
    SeekBar rating;
    EditText imdb;
    Button addMovie;
    static String[] genereOptions = {"Action","Animation","Comedy","Documentry","Family","Horror","Crime","Others"};
    TextView percentage;
    boolean flag=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        findAllViews();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,genereOptions);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genre.setAdapter(adapter);


        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    String movie_name = movieName.getText().toString();
                    String movie_dec = movieDesc.getText().toString();
                    int movie_year = Integer.parseInt(year.getText().toString());
                    String imdb_str = imdb.getText().toString();
                    String genere_str = genre.getSelectedItem().toString();
                    int rating_p = Integer.valueOf(rating.getProgress());
                    if(movie_year<=2018&&movie_year>1800) {
                        Movie m1 = new Movie(movie_name, rating_p, genere_str, imdb_str, movie_dec, movie_year);
                        Intent i = new Intent();
                        i.putExtra("serialize_data", m1);
                        setResult(Activity.RESULT_OK, i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(AddMovie.this,"Please enter valid year",Toast.LENGTH_SHORT).show();
                    }


                }catch (Exception e){
                    Toast.makeText(AddMovie.this,"Please fill in all the fields",Toast.LENGTH_SHORT).show();
                }






            }
        });
        rating.setMax(5);
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//

                percentage.setText((String.valueOf(progress)));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }
    void findAllViews(){

        movieName = (EditText)findViewById(R.id.et_moviename);
        movieDesc = (EditText)findViewById(R.id.et_moviedesc);
        year = (EditText)findViewById(R.id.et_year);
        imdb = (EditText)findViewById(R.id.et_imdb);
        genre = (Spinner) findViewById(R.id.spinner_genere);
        rating = (SeekBar)findViewById(R.id.seekBar_rating);
        addMovie = (Button)findViewById(R.id.btn_addmovie);
        percentage = (TextView)findViewById(R.id.seekbar_value);

    }
}
class Movie implements Serializable{
    String movieName;
    int year;
    int rating;
    String genere;
    String imdb;
    String desc;

    Movie(String movieName,int rating,String genere,String imdb, String desc, int year){
        this.movieName = movieName;
        this.desc = desc;
        this.year = year;
        this.rating = rating;
        this.imdb = imdb;
        this.genere = genere;

    }


}
