package com.example.jugal.studentprofilebuilder;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
     public ImageView iv;
     public static final  int Req_Key=100;
     public static final String IMAGE_ID="jugal";
     public EditText first_name;
     public EditText last_name;
     public EditText student_id;
     public RadioGroup department;
     public RadioButton selected_department;
     public Button save;
     public static final int Req_Key_2 = 101;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAllViews();


        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent select_avatar = new Intent(MainActivity.this,SelectAvatar.class);
                startActivityForResult(select_avatar,Req_Key);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = first_name.getText().toString();
                String lastName = last_name.getText().toString();
                String studentId = student_id.getText().toString();
                int department_id = department.getCheckedRadioButtonId();
                selected_department = (RadioButton)findViewById(department_id);
                String department = selected_department.getText().toString();
                int image = iv.getId();
                sendDataOnSave(firstName,lastName,studentId,department,image);


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         if(requestCode==Req_Key)
         {
             
             if(resultCode==RESULT_OK && data.getExtras()!= null)
             {
                temp=data.getExtras().getString(IMAGE_ID);
                 Toast.makeText(getApplicationContext(), temp,Toast.LENGTH_LONG).show();
                 //Log.d("Kush",data.getExtras().getString("image_id"));
                 switch (temp)
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

             }
         }

    }

    void findAllViews(){
        first_name = (EditText)findViewById(R.id.EditText_FirstName);
        last_name = (EditText)findViewById(R.id.EditTex_LastName);
        department = (RadioGroup) findViewById(R.id.radioGroup2);
        student_id = (EditText)findViewById(R.id.EditText_SID);
        iv = (ImageView) findViewById(R.id.image_view_m);
        save = (Button)findViewById(R.id.Button_Save);

    }
    void sendDataOnSave(String firstName, String lastName, String studentId, String department, int image){
        Intent i = new Intent(MainActivity.this,DisplayProfile.class);
        i.putExtra("Name",firstName+" "+lastName);
        //i.putExtra("LastName",lastName);
        i.putExtra("StudentId",studentId);
        i.putExtra("Department",department);
        i.putExtra("Image",temp);
        startActivity(i);

    }
}
