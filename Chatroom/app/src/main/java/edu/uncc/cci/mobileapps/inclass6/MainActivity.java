package edu.uncc.cci.mobileapps.inclass6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements ClientHelper {

    private final static String LOGIN_URL = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login";

    private final OkHttpClient client = new OkHttpClient();


    private String email = "null";
    private String passowrd = "null";
    private Handler handler;

    EditText email_et;
    EditText password_et;
    Button login_btn;
    Button signup_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllViews();
        handler = new Handler(Looper.getMainLooper());
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_et.getText().toString();
                passowrd = password_et.getText().toString();

                try {
                    if (isConnected()) {
                        run(MainActivity.this);
                    } else {
                        Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivityForResult(i, AppConstant.VIEW_REQ_CODE);
            }
        });


    }

    @Override
    public void invalidCredentials() {
        password_et.setError("Check Credentials");
        email_et.setError("Check Credentials");
        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();

    }

    public void startNewActivity(String token) {
        Log.d("kush", "In Activity");
        User user = JsonParser.parseUser(token);
        Intent i = new Intent(MainActivity.this, MessageThreadActivity.class);

//            while(token_josn == null){};
        SharedPreferences.Editor editor = getSharedPreferences(AppConstant.SHARED_PREF, MODE_PRIVATE).edit();
        editor.putString(AppConstant.TOKEN_KEY, "BEARER " + user.getToken());
        editor.putString(AppConstant.FULL_NAME, user.getUser_fname() + " " + user.getUser_lname());
        editor.putString(AppConstant.USER_ID, user.getUser_id());
        editor.apply();
        startActivity(i);
    }


    public void run(ClientHelper activity) throws Exception {
        final ClientHelper activity_context = activity;
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", passowrd)
                .build();
        final Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(formBody)
                .build();

        Log.d("kush", "Request_Start");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String resp = response.body().string();


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            activity_context.startNewActivity(resp);

                        }
                    });


//                    Log.d("Kush",response.body().string());


                } else {
                    Log.d("Kush", "Bad Request");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            activity_context.invalidCredentials();
                        }
                    });

                }

            }
        });

    }

    public void findAllViews() {
        email_et = (EditText) findViewById(R.id.et_email_login);
        password_et = (EditText) findViewById(R.id.et_email_password);
        login_btn = (Button) findViewById(R.id.btn_login_login);
        signup_btn = (Button) findViewById(R.id.btn_signup_login);
    }


    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.VIEW_REQ_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                startNewActivity(data.getExtras().getString(AppConstant.USER));
            }
        }
    }
}

interface ClientHelper{
    void invalidCredentials();
    void startNewActivity(String token);
}
