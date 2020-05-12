package com.example.kush.areacalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText length1;
    EditText length2;
    ImageView triangle;
    ImageView square;
    ImageView circle;
    Button calculate;
    Button clear;
    TextView selected_shape;
    TextView result;
    TextView length2_textview;

    String currentShape = "None";
    double area;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findAllViews();

        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Square Selected",Toast.LENGTH_SHORT).show();
                removeLength2();
                currentShape = "Square";
                selected_shape.setText(currentShape);


            }
        });
        triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Triangle Selected",Toast.LENGTH_SHORT).show();
                viewLength2();
                currentShape = "Triangle";
                selected_shape.setText(currentShape);

            }
        });
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Circle Selected",Toast.LENGTH_SHORT).show();
                removeLength2();
                currentShape = "Circle";
                selected_shape.setText(currentShape);
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            double area;
            @Override
            public void onClick(View v) {

                switch (currentShape){
                    case "Triangle":
                        if(verifyInputs()){
                            double side = Double.parseDouble(length1.getText().toString());
                            double height = Double.parseDouble(length2.getText().toString());
                            area = 0.5*side*height;
                            double roundOff = Math.round(area * 100.0) / 100.0;
                            result.setText(""+roundOff);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Invalid Input",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case "Square":
                        if(verifyInput1()){
                            double length = Double.parseDouble(length1.getText().toString());
                            area = length*length;
                            double roundOff = Math.round(area * 100.0) / 100.0;
                            result.setText(""+roundOff);

                        }
                        else{
                            Toast.makeText(MainActivity.this,"Invalid Input",Toast.LENGTH_SHORT).show();
                        }


                        break;
                    case "Circle":
                        if(verifyInput1()){
                            double radius = Double.parseDouble(length1.getText().toString());
                            area = 3.1416*radius*radius;
                            double roundOff = Math.round(area * 100.0) / 100.0;
                            result.setText(""+roundOff);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Invalid Input",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        Toast.makeText(MainActivity.this,"No Shape Selected",Toast.LENGTH_SHORT).show();

                }



            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                length1.getText().clear();
                length2.getText().clear();
                viewLength2();
                result.setText("");
                selected_shape.setText("Select a shape");
                

            }
        });




    }

    void findAllViews(){
        length1 = (EditText)findViewById(R.id.length1);
        length2 = (EditText)findViewById(R.id.length2);
        triangle = (ImageView)findViewById(R.id.triangle);
        square = (ImageView)findViewById(R.id.square);
        circle = (ImageView)findViewById(R.id.circle);
        calculate = (Button) findViewById(R.id.calculate);
        clear = (Button) findViewById(R.id.clear);
        selected_shape = (TextView)findViewById(R.id.selected_shape);
        result = (TextView)findViewById(R.id.result);
        length2_textview = (TextView)findViewById(R.id.length2_textview);




    }
    void removeLength2(){
        length2_textview.setVisibility(View.GONE);
        length2.setVisibility(View.GONE);
    }
    void viewLength2(){
        length2_textview.setVisibility(View.VISIBLE);
        length2.setVisibility(View.VISIBLE);

    }
    boolean verifyInputs(){

        String user_input = length1.getText().toString();
        String user_input2 = length2.getText().toString();
        if(user_input.length()>0 && user_input2.length()>0){
            return true;
        }
        else{
            return false;
        }

    }
    boolean verifyInput1(){
        String user_input = length1.getText().toString();
        if(user_input.length()>0){
            return true;
        }
        else{
            return false;
        }

    }
}
