package edu.uncc.cci.mobileapps.inclass6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.ocpsoft.prettytime.Duration;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class MessageListViewAdapter<M> extends ArrayAdapter<Message> {
    private final static String DATE_TIME_FORMAT = "yyyy-MMM-dd HH:mm:ss";
    private AsyncDeleteCallback callback;
    private String currUserId;

    public MessageListViewAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Message message = getItem(position);
        final int pos = position;
        ViewHolder viewHolder;

        if(convertView == null){ //if no view to re-use then inflate a new one
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.message_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewMesg = convertView.findViewById(R.id.textViewMesg);
            viewHolder.textViewFullName = convertView.findViewById(R.id.userFullNameTextView);
            viewHolder.textViewCreatedAt = convertView.findViewById(R.id.createdAtTextView);
            viewHolder.deleteImageView = convertView.findViewById(R.id.trashCanImageView);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewMesg.setText(message.getMessage());
        viewHolder.textViewFullName.setText(getUserFullName(message));
        viewHolder.textViewCreatedAt.setText(getFormattedTime(message));
        viewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(callback != null) {
                    callback.onDelete(message.getId(), pos);
                }
            }
        });
        if (displayTrashCan(message)) {
            viewHolder.deleteImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.deleteImageView.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private String getUserFullName(Message message) {
        if (message.getUser_id().equals(currUserId)) {
            return "Me";
        } else {
            return message.getUser_fname() + " " + message.getUser_lname();
        }
    }

    private Boolean displayTrashCan(Message message) {
        return message.getUser_id().equals(currUserId);
    }

    private String getFormattedTime(Message message) {
        SimpleDateFormat dateFormatUtc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(dateFormatUtc.parse(message.getCreated_at()));
            cal1.add(Calendar.HOUR, -4);
            // String s1 = dateFormatUtc.format(cal1.getTime());
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(new Date());
            PrettyTime p = new PrettyTime(cal2.getTime());
            // String s = dateFormatUtc.format(cal2.getTime());
            return p.format(cal1.getTime());
        } catch (ParseException e) {
            return "";
        }
    }

    public void setCallback(AsyncDeleteCallback callback) {
        this.callback = callback;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currUserId = currentUserId;
    }

    //View Holder to cache the views
    private static class ViewHolder{
        TextView textViewMesg;
        ImageView deleteImageView;
        TextView textViewFullName;
        TextView textViewCreatedAt;

    }


}