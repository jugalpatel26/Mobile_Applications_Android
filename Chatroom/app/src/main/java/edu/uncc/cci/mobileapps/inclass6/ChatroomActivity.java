package edu.uncc.cci.mobileapps.inclass6;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ChatroomActivity extends AppCompatActivity implements AsyncDeleteCallback {

    private final static String GET_MESG_LIST = "/api/messages/";
    public static final String ADD_MSG_LIST = "/api/message/add";
    private final static String DELETE_MESG = "/api/message/delete/";

    private String authorizationKey;
    private String currUserId;
    private Handler handler;
    private String threadId;
    ProgressDialog progressBarDialog;
    ArrayList<Message> mesgList;
    MessageListViewAdapter<Object> adapter;
    ListView listView;
    TextView chatroomName;
    EditText addMessageEt;
    Button addMessageBtn;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        setTitle("Chatroom");

        addMessageBtn = (Button)findViewById(R.id.addMsgBtn);
        addMessageEt = (EditText)findViewById(R.id.addMessageEt);

        handler = new Handler(Looper.getMainLooper());
        progressBarDialog = new ProgressDialog(this);
        progressBarDialog.setCancelable(true);
        progressBarDialog.setIndeterminate(false);
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBarDialog.setMessage("Loading...");

        listView = findViewById(R.id.messageListView);
        chatroomName = findViewById(R.id.chatroomName);
        findViewById(R.id.homeImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent() != null && getIntent().getExtras() != null) {
            chatroomName.setText(getIntent()
                    .getExtras()
                    .getString(AppConstant.THREAD_NAME));
            threadId = getIntent()
                    .getExtras()
                    .getString(AppConstant.THREAD_ID);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(AppConstant.SHARED_PREF,
                MODE_PRIVATE);

        if (sharedPreferences != null) {
            authorizationKey = sharedPreferences.getString(AppConstant.TOKEN_KEY, "");
            currUserId = sharedPreferences.getString(AppConstant.USER_ID, "");

            if (isConnected()) {
                String url = AppConstant.URL + GET_MESG_LIST + threadId;
                getMessageList(url);
            } else {
                displayMessage("No connectivity");
            }

        }

        addMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isConnected()) {
                    String url = AppConstant.URL + ADD_MSG_LIST;
                    addNewMessage(url);
                } else {
                    displayMessage("No connectivity");
                }

            }
        });
    }

    public void getMessageList(String url) {
        displayLoadingIndicator(true);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authorizationKey)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final Response resp = response;
                try {
                    ResponseBody responseBody = resp.body();
                    if (!resp.isSuccessful()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                displayMessage("Can't get message list");
                                displayLoadingIndicator(false);
                            }
                        });
                    } else {
                        ArrayList<Message> temp = JsonParser.parseMessageList(responseBody.string());
                        Collections.sort(temp, new MessageComperator());
                        mesgList = temp;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                adapter = new MessageListViewAdapter<>(
                                        ChatroomActivity.this,
                                        R.layout.message_item,
                                        mesgList);
                                adapter.setCallback(ChatroomActivity.this);
                                adapter.setCurrentUserId(currUserId);
                                listView.setAdapter(adapter);
                                displayLoadingIndicator(false);
                            }
                        });
                    }

                } catch (Exception e) {
                    displayLoadingIndicator(false);
                    displayMessage("Can't get message threads");
                }
            }
        });
    }

    private void displayMessage(String mesg) {
        Toast.makeText(this, mesg, Toast.LENGTH_LONG).show();
    }

    private void displayLoadingIndicator(boolean show) {
        if (show) {
            progressBarDialog.show();
        } else {
            progressBarDialog.dismiss();
        }
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
    public void onDelete(String id, Integer position) {
        if (isConnected()) {
            String url = AppConstant.URL + DELETE_MESG + id;
            delete(url, position);
        } else {
            displayMessage("No connectivity");
        }
    }

    public void addNewMessage(String url){
        String messageContent = addMessageEt.getText().toString();

        if(messageContent.isEmpty()){
            displayMessage("Message Cannot be empty");
            return;
        }
        displayLoadingIndicator(true);
        FormBody formBody = new FormBody.Builder()
                .add("message",messageContent)
                .add("thread_id",threadId)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization",authorizationKey)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseString = response.body().string();

                if(!response.isSuccessful()){
                    displayMessage("Can't add this message");
                    displayLoadingIndicator(false);
                }
                else{

                    try {
                        final Message obj = JsonParser.parseMessage(responseString);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.add(obj);
                                adapter.notifyDataSetChanged();
                                addMessageEt.setText("");
                                listView.setSelection(listView.getCount() - 1);
                                displayMessage("Message Sucessfully added");
                                displayLoadingIndicator(false);



                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        displayLoadingIndicator(false);
                        displayMessage("Can't add this message");
                    }


                }


            }
        });
    }

    private void delete(String url, final Integer position) {
        displayLoadingIndicator(true);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authorizationKey)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final Response resp = response;
                try {
                    ResponseBody responseBody = resp.body();
                    if (!resp.isSuccessful()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                displayMessage("Can't delete this message");
                                displayLoadingIndicator(false);
                            }
                        });

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                adapter.remove(adapter.getItem(position));
                                adapter.notifyDataSetChanged();
                                displayLoadingIndicator(false);
                                displayMessage("Successfully delete message");
                            }
                        });
                    }

                } catch (Exception e) {
                    displayLoadingIndicator(false);
                    displayMessage("Error deleting message");
                }
            }
        });
    }
}
class MessageComperator implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
        return o1.getCreated_at().compareTo(o2.getCreated_at());
    }
}
