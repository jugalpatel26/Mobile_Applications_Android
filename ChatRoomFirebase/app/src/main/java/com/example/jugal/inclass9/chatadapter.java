package com.example.jugal.inclass9;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

public class chatadapter extends ArrayAdapter<Chat> {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String fullname = user.getDisplayName();
    String[] name= fullname.split("\\s+");
    String namef= name[0];
    String namel= name[1];

    public chatadapter(@NonNull Context context, int resource,@NonNull List<Chat> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView, @NonNull ViewGroup parent) {
        final Chat chat = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_listview,parent,false);
        }
        TextView message = (TextView) convertView.findViewById(R.id.textView_message);
        TextView fname = (TextView) convertView.findViewById(R.id.textView_fname);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView_d);
        TextView time = (TextView) convertView.findViewById(R.id.textView_time);
        PrettyTime p = new PrettyTime();
        time.setText(p.format(chat.time));
        final ImageButton delete = (ImageButton) convertView.findViewById(R.id.imageButton_delete);
        delete.setVisibility(View.INVISIBLE);
        message.setText(chat.message);
        fname.setText(chat.fname);
        if(chat.imageurl!=null){
            Picasso.get().load(chat.imageurl).into(imageView);

        }
        else {
            imageView.setVisibility(View.GONE);
        }
        if(namef.equals(chat.fname)&&namel.equals(chat.lname))
        {
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.child(chat.id).removeValue();
                }
            });
        }

        return convertView;
    }
}
