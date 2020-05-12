package com.example.shahk.moviesdatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DisplayListYear extends AppCompatActivity {



    TextView n;
    TextView d;
    TextView y;
    TextView im;
    TextView r;
    TextView g;
    ImageButton p;
    ImageButton f;
    ImageButton nx;
    ImageButton l;
    Button fi;
    int ln;

    int t=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list_rating);
        Intent i = getIntent();
        final ArrayList<Movie> a = (ArrayList<Movie>) i.getSerializableExtra("a");
        ln=a.size()-1;
        n = (TextView) findViewById(R.id.r_name);
        d=  (TextView) findViewById(R.id.r_d);
        y= (TextView) findViewById(R.id.r_y);
        im= (TextView) findViewById(R.id.r_im);
        r= (TextView) findViewById(R.id.r_r);
        g= (TextView) findViewById(R.id.r_g);
        p= (ImageButton) findViewById(R.id.Button_previous);
        l= (ImageButton) findViewById(R.id.Button_last);
        fi=(Button) findViewById(R.id.Button_finish);
        nx=(ImageButton) findViewById(R.id.Button_next);
        f= (ImageButton) findViewById(R.id.Button_first);

        Collections.sort(a, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return Integer.valueOf(o1.year).compareTo(o2.year);
            }
        });
        n.setText(a.get(t).movieName);
        d.setText(a.get(t).desc);
        y.setText(Integer.toString(a.get(t).year));
        im.setText(a.get(t).imdb);
        r.setText(Integer.toString(a.get(t).rating));
        g.setText(a.get(t).genere);

        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t--;
                if(t>=0)
                {
                    n.setText(a.get(t).movieName);
                    d.setText(a.get(t).desc);
                    y.setText(Integer.toString(a.get(t).year));
                    im.setText(a.get(t).imdb);
                    r.setText(Integer.toString(a.get(t).rating));
                    g.setText(a.get(t).genere);
                }
                else
                {
                    t++;
                    Toast.makeText(DisplayListYear.this,"Showing first Movie",Toast.LENGTH_LONG).show();
                }
            }
        });
        nx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t++;
                if(t<=ln)
                {
                    n.setText(a.get(t).movieName);
                    d.setText(a.get(t).desc);
                    y.setText(Integer.toString(a.get(t).year));
                    im.setText(a.get(t).imdb);
                    r.setText(Integer.toString(a.get(t).rating));
                    g.setText(a.get(t).genere);
                }
                else
                {
                    t--;
                    Toast.makeText(DisplayListYear.this,"Showing Last Movie",Toast.LENGTH_LONG).show();

                }
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t==0)
                {
                    Toast.makeText(DisplayListYear.this,"Showing first Movie",Toast.LENGTH_LONG).show();
                }
                else
                {
                    t=0;
                    n.setText(a.get(t).movieName);
                    d.setText(a.get(t).desc);
                    y.setText(Integer.toString(a.get(t).year));
                    im.setText(a.get(t).imdb);
                    r.setText(Integer.toString(a.get(t).rating));
                    g.setText(a.get(t).genere);
                }
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t==ln)
                {
                    Toast.makeText(DisplayListYear.this,"Showing last Movie",Toast.LENGTH_LONG).show();
                }
                else
                {
                    t=ln;
                    n.setText(a.get(t).movieName);
                    d.setText(a.get(t).desc);
                    y.setText(Integer.toString(a.get(t).year));
                    im.setText(a.get(t).imdb);
                    r.setText(Integer.toString(a.get(t).rating));
                    g.setText(a.get(t).genere);

                }
            }
        });
        fi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
}
