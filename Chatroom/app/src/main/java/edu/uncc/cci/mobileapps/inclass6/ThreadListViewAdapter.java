package edu.uncc.cci.mobileapps.inclass6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ThreadListViewAdapter<M> extends ArrayAdapter<MessageThread> {

    private AsyncDeleteCallback callback;
    private String currUserId;

    public ThreadListViewAdapter(Context context, int resource, List<MessageThread> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MessageThread thread = getItem(position);
        final int pos = position;
        ViewHolder viewHolder;

        if(convertView == null){ //if no view to re-use then inflate a new one
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.thread_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            viewHolder.deleteBtn = (Button) convertView.findViewById(R.id.deleteBtn);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //set the data from the email object
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(callback != null) {
                    callback.onDelete(thread.getId(), pos);
                }
            }
        });
        viewHolder.textViewTitle.setText(thread.getTitle());
        if (displayDeleteBtn(thread)) {
            viewHolder.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            viewHolder.deleteBtn.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setCallback(AsyncDeleteCallback callback) {
        this.callback = callback;
    }

    //View Holder to cache the views
    private static class ViewHolder{
        TextView textViewTitle;
        Button deleteBtn;

    }

    private Boolean displayDeleteBtn(MessageThread thread) {
        return thread.getUser_id().equals(currUserId);
    }

    public void setCurrentUserId(String currentUserId) {
        this.currUserId = currentUserId;
    }

}
