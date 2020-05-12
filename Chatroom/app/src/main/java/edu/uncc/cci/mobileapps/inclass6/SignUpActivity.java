package edu.uncc.cci.mobileapps.inclass6;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {
    private Button cancel;
    private Button signUp;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText rePassword;
    private final OkHttpClient client = new OkHttpClient();
    private final static String SIGN_UP_URL = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup";
    private Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findAllViews();
  //      handler = new Handler(Looper.getMainLooper());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_str = email.getText().toString();
                String fname = firstName.getText().toString();
                String lname = lastName.getText().toString();
                String password_str = password.getText().toString();
                String rePassword_str = rePassword.getText().toString();
                Log.d("kush",password_str);

                if(password_str.equals(rePassword_str)){
                    signUp(email_str,password_str,fname,lname);
                }
                else {
                    rePassword.setError("Passwords dosen't match");

                }


            }
        });

    }

    private void findAllViews() {
        cancel = (Button)findViewById(R.id.button_cancel);
        signUp = (Button)findViewById(R.id.button_ssignup);
        firstName = (EditText)findViewById(R.id.edittext_fname);
        lastName = (EditText)findViewById(R.id.editText_lname);
        email = (EditText)findViewById(R.id.editText_semail);
        password = (EditText)findViewById(R.id.editText_spassword);
        rePassword = (EditText)findViewById(R.id.editText_srpassword);


    }
    private void handleError(String resp){
        Log.d("kush","In handle error method");
        try {
            JSONObject obj = new JSONObject(resp);
            Log.d("kush","JSON Obj: "+obj);
            String messsage_error = obj.getString("message");
            Log.d("kush","Error message from Json: "+messsage_error);

            Toast.makeText(SignUpActivity.this,messsage_error,Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void handleSuccessfulSignUp(String res){
        Intent intent = new Intent();
        intent.putExtra(AppConstant.USER, res);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void signUp(String email, String password, String fname, String lname){


        RequestBody formBOdy = new FormBody.Builder()
                .add("email",email)
                .add("password",password)
                .add("fname",fname)
                .add("lname",lname)
                .build();

        final Request request = new Request.Builder()
                .url(SIGN_UP_URL)
                .post(formBOdy)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call,  Response response) throws IOException {
              final   String response_str = response.body().string();

                if(response.isSuccessful()){
                    Log.d("kush",response_str);


                    //Code if account was created

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleSuccessfulSignUp(response_str);
                        }
                    });





                }
                else{
//
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                                handleError(response_str);

                        }
                    });




                }


            }
        });
    }
}
