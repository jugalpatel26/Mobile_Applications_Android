package com.example.shahk.moviesdatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button addMovie;
    Button edit;
    Button deleteMovie;
    Button listYear;
    Button listRating;
    int t;

    ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllViews();
        addMovie.setOnClickListener(new View.OnClickListener() {

            //This is fully implemented.
            @Override
            public void onClick(View v) {
                //This call the Add Movie activity
                Intent i = new Intent(MainActivity.this,AddMovie.class);
                startActivityForResult(i,100);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog is Build which displays the list of movies.
                //I have a plan for this activity in my mind in-case you need my help.

                final String[] movie_names = getMovieNames();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pick a Movie").setItems(movie_names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(MainActivity.this,EditActivity.class);
                        i.putExtra("edit_movie",movies.get(which));
                        startActivityForResult(i,101);
                        t=which;

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        deleteMovie.setOnClickListener(new View.OnClickListener() {

            //This is fully implemented.
            @Override
            public void onClick(View v) {

                final String[] movie_names = getMovieNames();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pick a Movie").setItems(movie_names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String delete_name = movie_names[which];
                        movies.remove(which);
                        Toast.makeText(MainActivity.this,delete_name+" movie is deleted",Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        listYear.setOnClickListener(new View.OnClickListener() {
            //I have not worked in this so far I suggest you to start with this as this may take a long time.
            @Override
            public void onClick(View v) {
                int n=movies.size();
                if(n>0) {
                    Intent i = new Intent("year");
                    i.putExtra("a", movies);
                    startActivity(i);

                }
                else
                {
                    Toast.makeText(MainActivity.this,"No Movies to Display",Toast.LENGTH_LONG).show();
                }
            }
        });
        listRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n=movies.size();
                if(n>0) {
                    Intent i = new Intent("rating");
                    i.putExtra("ab", movies);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"No Movies to Display",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    void findAllViews(){

        addMovie = (Button)findViewById(R.id.btn_add_movie);
        edit = (Button)findViewById(R.id.btn_edit);
        deleteMovie = (Button)findViewById(R.id.btn_delete);
        listYear = (Button)findViewById(R.id.btn_year_list);
        listRating = (Button)findViewById(R.id.btn_rating_list);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==100){
            //Request code 100 is for add Movie activity in case you need to add another activity for result, use some other code.
            //This is part of Add movie activity.
            if(resultCode== Activity.RESULT_OK && data.getExtras()!= null){
                Movie m1 = (Movie) data.getSerializableExtra("serialize_data");
                movies.add(m1);
                }
        }
        else if(requestCode==101)
        {
            if(resultCode==105&& data.getExtras()!= null)
            {
                Movie m2 = (Movie) data.getSerializableExtra("serialize_data");
                movies.add(t,m2);
                movies.remove(t+1);
            }
        }
    }

    String[] getMovieNames(){
        String[] movie_names = new String[movies.size()];

        for(int i=0; i<movies.size();i++){
            movie_names[i] = movies.get(i).movieName;

        }

        return movie_names;

    }
}

