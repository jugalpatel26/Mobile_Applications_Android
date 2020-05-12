package com.example.jugal.studentprofilebuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayProfile extends AppCompatActivity {
     public Button edit;
     public TextView user_name;
     public TextView sid;
     public TextView dept;
     public ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);
        Intent i  = getIntent();
        if(i.getExtras() != null) {
            String username = i.getStringExtra("Name");
            String Student_Id = i.getStringExtra("StudentId");
            String department = i.getStringExtra("Department");
            String image = i.getStringExtra("Image");
            user_name = (TextView) findViewById(R.id.textView4);
            sid = (TextView) findViewById(R.id.textView5);
            dept = (TextView) findViewById(R.id.textView6);
            iv = (ImageView)findViewById(R.id.imageView) ;

            //Log.d("kush",i.getStringExtra("Image"));
            user_name.setText(username);
            sid.setText(Student_Id);
            dept.setText(department);
            //iv.setImageResource(image);
            switch (image)
            {
                case "female_1":
                    iv.setImageResource(R.drawable.avatar_f_1);
                    break;
                case "female_2":
                    iv.setImageResource(R.drawable.avatar_f_2);
                    break;
                case "female_3":
                    iv.setImageResource(R.drawable.avatar_f_3);
                    break;
                case "male_1":
                    iv.setImageResource(R.drawable.avatar_m_1);
                    break;
                case "male_2":
                    iv.setImageResource(R.drawable.avatar_m_2);
                    break;
                case "male_3":
                    iv.setImageResource(R.drawable.avatar_m_3);
                    break;
            }


            edit = (Button) findViewById(R.id.Button_Edit);

           edit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    finish();
               }
           });

        }
    }
}
