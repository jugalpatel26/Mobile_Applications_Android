package com.example.jugal.inclass9;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText em;
    EditText pass;
    String email=null;
    String password=null;
    Button login;
    Button signup;
    DatabaseReference reference;
    String message;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");
        mAuth = FirebaseAuth.getInstance();
        em= (EditText) findViewById(R.id.email);
        pass =(EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.button_login);
        signup=(Button) findViewById(R.id.button_signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = em.getText().toString();
                password = pass.getText().toString();
                if(email.length()>0&&password.length()>0) {
                    Log.d("a", "inif" + password + email);
                    auth(email, password);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,signin.class);
                startActivity(i);

            }
        });

    }
    public void auth(String email,String password){
        Log.d("a","auth");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent i = new Intent(MainActivity.this,Chatroom.class);
                            startActivity(i);

                        } else {

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            Intent i = new Intent(MainActivity.this,Chatroom.class);
            startActivity(i);
        }

    }

}
