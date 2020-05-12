package com.example.jugal.studentprofilebuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SelectAvatar extends AppCompatActivity {


    ImageView female_1;
    ImageView female_2;
    ImageView female_3;
    ImageView male_1;
    ImageView male_2;
    ImageView male_3;
    String image_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);
        findAllViews();
        female_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_id = "female_1";
                sendSelectedImage();

            }
        });
        female_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_id = "female_2";
                sendSelectedImage();

            }
        });
        female_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_id = "female_3";
                sendSelectedImage();

            }
        });
        male_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_id = "male_1";
                sendSelectedImage();

            }
        });
        male_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_id = "male_2";
                sendSelectedImage();

            }
        });




    }

    void findAllViews(){
        female_1 = (ImageView)findViewById(R.id.female_1);
        female_2 = (ImageView)findViewById(R.id.female_2);
        female_3 = (ImageView)findViewById(R.id.female_3);
        male_1 = (ImageView)findViewById(R.id.male_1);
        male_2 = (ImageView)findViewById(R.id.male_2);
        male_3 = (ImageView)findViewById(R.id.male_3);
    }
    void sendSelectedImage(){

        Intent i = new Intent();
        i.putExtra(MainActivity.IMAGE_ID,image_id);
        setResult(RESULT_OK,i);
        finish();

    }
}
