package com.example.jugal.inclass9;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Chatroom extends AppCompatActivity {
    TextView name;
    ImageButton logout;
    ImageButton selectimage;
    ImageButton send;
    private Uri filePath;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    EditText message;
    Chat c;
    Chat list;
    FirebaseStorage storage;
    StorageReference storageReference;
    String fullname;
    DatabaseReference database;
    ArrayList<Chat> chat = new ArrayList<>();
    int flag=0;

    @Override
    protected void onStart() {
        super.onStart();
        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chat.clear();
                for(DataSnapshot tasksnapshot:dataSnapshot.getChildren())
                {
                    list = tasksnapshot.getValue(Chat.class);
                    chat.add(list);

                }
                listView = (ListView) findViewById(R.id.listview);
                chatadapter listadapter = new chatadapter(Chatroom.this,R.layout.chat_listview,chat);
                listView.setAdapter(listadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        setTitle("Chat Room");
        c = new Chat();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        message = (EditText) findViewById(R.id.editText_message);
        Log.d("user",""+user.getDisplayName());
        name = (TextView) findViewById(R.id.textview_name);
        logout = (ImageButton) findViewById(R.id.imageButton_logout);
        selectimage = (ImageButton) findViewById(R.id.imageButton_select);
        send = (ImageButton) findViewById(R.id.imageButton_send);
        name.setText(user.getDisplayName());
        fullname = user.getDisplayName();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Chatroom.this, MainActivity.class);
                startActivity(intent);

            }
        });
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  chooseImage();
                  flag=1;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==1) {
                    uploadImage();
                    flag=0;

                }
                else {
                    if(c.imageurl!=null)
                    {
                        c.imageurl=null;
                    }
                    String[] name= fullname.split("\\s+");
                    c.fname= name[0];
                    c.lname= name[1];
                    c.message = message.getText().toString();
                    c.id =database.push().getKey();
                    database.child(c.id).setValue(c);
                    selectimage.setImageResource(R.drawable.addimage);
                    message.setText(null);
                    }

            }
        });
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                selectimage.setImageBitmap(bitmap);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(Chatroom.this,new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    c.imageurl=uri.toString();
                                    String[] name= fullname.split("\\s+");
                                    c.fname= name[0];
                                    c.lname= name[1];
                                    c.message = message.getText().toString();
                                    c.time= new Date();
                                    c.id =database.push().getKey();
                                    database.child(c.id).setValue(c);
                                    selectimage.setImageResource(R.drawable.addimage);
                                    message.setText(null);
                                    Toast.makeText(Chatroom.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
        }
    }
}
