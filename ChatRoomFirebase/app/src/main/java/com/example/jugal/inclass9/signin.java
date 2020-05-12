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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class signin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText firstname;
    EditText lastname;
    EditText email;
    EditText password;
    EditText repassword;
    Button signin;
    Button cancel;
    String name;
    String passw;
    String repassw;
    String eml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setTitle("Sign In");
        mAuth = FirebaseAuth.getInstance();
        firstname = (EditText) findViewById(R.id.editText_firstname);
        lastname = (EditText) findViewById(R.id.editText_lastname);
        email = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);
        repassword = (EditText) findViewById(R.id.editText_repassword);
        signin = (Button) findViewById(R.id.button_signin);
        cancel = (Button) findViewById(R.id.button_cancel);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = firstname.getText().toString()+" "+lastname.getText().toString();
                eml = email.getText().toString();
                passw = password.getText().toString();
                repassw= repassword.getText().toString();
                if(name.length()>0&&eml.length()>0&&passw.length()>0&&repassw.length()>0){
                    if(passw.equals(repassw)) {
                        mAuth.createUserWithEmailAndPassword(eml,passw)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                        user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent i = new Intent(signin.this,Chatroom.class);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(signin.this, "Already Registered with same email",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(signin.this, "Password Not Match",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(signin.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });


    }

}
