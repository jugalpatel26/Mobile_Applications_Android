//Assignment 2
// Jugal Patel,Kush Shah
//InClass 02
package com.example.jugal.bmicalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button calculateButton = (Button)findViewById(R.id.CalculateButton);
        final EditText weightInput = (EditText) findViewById(R.id.WeightInput);
        final EditText heightFeet = (EditText) findViewById(R.id.HeightFeet);
        final EditText heightInches = (EditText) findViewById(R.id.HeightInch);
        final TextView rslt = (TextView) findViewById(R.id.Result);
        final TextView rsltview = (TextView) findViewById(R.id.FinalResult);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              try {
                  Double weight = Double.parseDouble(weightInput.getText().toString());
                  Double heightFeetvalue = Double.parseDouble(heightFeet.getText().toString());
                  Double heightInchesvalue = Double.parseDouble(heightInches.getText().toString());

                  if(heightInchesvalue>= 12)
                  {
                      Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                  }

                  else {

                      Double bmi = (weight / (((heightInchesvalue) + (heightFeetvalue * 12)) * ((heightInchesvalue) + (heightFeetvalue * 12)))) * 703;
                      if (bmi <= 18.5) {
                          String result = "Your Bmi:" + bmi;
                          String conclusion = "You are underweight";
                          rslt.setText(result);
                          rsltview.setText(conclusion);
                      } else if (bmi > 18.5 && bmi <= 24.9) {
                          String result = "Your Bmi:" + bmi;
                          String conclusion = "You are normal weight";
                          rslt.setText(result);
                          rsltview.setText(conclusion);
                      } else if (bmi > 25 && bmi <= 29.9) {
                          String result = "Your Bmi:" + bmi;
                          String conclusion = "You are over weight";
                          rslt.setText(result);
                          rsltview.setText(conclusion);
                      } else if (bmi >= 30) {
                          String result = "Your Bmi:" + bmi;
                          String conclusion = "You are obese";
                          rslt.setText(result);
                          rsltview.setText(conclusion);
                      }
                      Toast.makeText(getApplicationContext(), "BMI Calculated", Toast.LENGTH_LONG).show();
                  }
              }

              catch(Exception e)

                {
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG).show();

                }





            }
        });
    }
}
