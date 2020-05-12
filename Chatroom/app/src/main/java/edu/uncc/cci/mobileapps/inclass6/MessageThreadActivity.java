package edu.uncc.cci.mobileapps.inclass6;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MessageThreadActivity extends AppCompatActivity implements AsyncDeleteCallback {

    private final static String GET_MESG_THREADS = "/api/thread";
    private final static String ADD_MESG_THREAD = "/api/thread/add";
    private final static String DELETE_MESG_THREAD = "/api/thread/delete/";
    ProgressDialog progressBarDialog;
    ArrayList<MessageThread> threadList;
    ListView listView;
    String authorizationKey;
    EditText title;
    TextView fullName;
    String currUserId;
    ThreadListViewAdapter<MessageThread> adapter;
    private Handler handler;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesg_thread);
        setTitle("Message Threads");

        progressBarDialog = new ProgressDialog(this);
        progressBarDialog.setCancelable(true);
        progressBarDialog.setIndeterminate(false);
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBarDialog.setMessage("Loading...");

        listView = findViewById(R.id.threadListView);
        title = findViewById(R.id.title);
        fullName = findViewById(R.id.userFullName);

        handler = new Handler(Looper.getMainLooper());

        findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    String url = AppConstant.URL + ADD_MESG_THREAD;;
                    addNew(url);
                } else {
                    displayMessage("No connectivity");
                }
            }
        });

        findViewById(R.id.logoutImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor =
                        getSharedPreferences(AppConstant.SHARED_PREF, MODE_PRIVATE).edit().clear();
                editor.commit();
               finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(AppConstant.SHARED_PREF, MODE_PRIVATE);

        if (sharedPreferences != null) {
            authorizationKey = sharedPreferences.getString(AppConstant.TOKEN_KEY, "");
            fullName.setText(sharedPreferences.getString(AppConstant.FULL_NAME, ""));
            currUserId = sharedPreferences.getString(AppConstant.USER_ID, "");

            if (isConnected()) {
                String url = AppConstant.URL + GET_MESG_THREADS;
                getList(url);
            } else {
                displayMessage("No connectivity");
            }

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MessageThreadActivity.this,
                        ChatroomActivity.class);
                intent.putExtra(AppConstant.THREAD_NAME, threadList.get(position).getTitle());
                intent.putExtra(AppConstant.THREAD_ID, threadList.get(position).getId());
                startActivity(intent);
            }
        });

    }

    public void getList(String url) {
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

                                displayMessage("Can't get message threads");
                                displayLoadingIndicator(false);
                            }
                        });
                    } else {

                        threadList = JsonParser.parseThreadList(responseBody.string());



                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                adapter = new ThreadListViewAdapter<>(
                                        MessageThreadActivity.this,
                                        R.layout.thread_item,
                                        threadList);
                                adapter.setCallback(MessageThreadActivity.this);
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

    public void addNew(String url) {
        String mesgThreadTitle = title.getText().toString();
        if (mesgThreadTitle.isEmpty()) {
            displayMessage("Message thread cannot be empty");
            return;
        }
        displayLoadingIndicator(true);
        FormBody formBody = new FormBody.Builder()
                .add("title", mesgThreadTitle)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authorizationKey)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody responseBody = response.body();
                    if (!response.isSuccessful()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                displayMessage("Can't add this thread");
                                displayLoadingIndicator(false);
                            }
                        });
                    } else {

                        MessageThread thread = JsonParser.parseThread(responseBody.string());
                        final MessageThread t = thread;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.add(t);
                                adapter.notifyDataSetChanged();
                                listView.setSelection(listView.getCount() - 1);
                                title.setText("");
                                displayMessage("Successfully add message thread");
                                displayLoadingIndicator(false);
                            }
                        });
                    }
                } catch (Exception e) {
                    displayLoadingIndicator(false);
                    displayMessage("Can't add this thread");
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
            String url = AppConstant.URL + DELETE_MESG_THREAD + id;
            delete(url, position);
        } else {
            displayMessage("No connectivity");
        }
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

                                displayMessage("Can't delete this thread");
                                displayLoadingIndicator(false);
                            }
                        });

                    } else {

                        // threadList = JsonParser.parseThreadList(responseBody.string());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                adapter.remove(adapter.getItem(position));
                                adapter.notifyDataSetChanged();
                                displayLoadingIndicator(false);
                                displayMessage("Successfully delete thread");
                            }
                        });
                    }

                } catch (Exception e) {
                    displayLoadingIndicator(false);
                    displayMessage("Error deleting thread");
                }
            }
        });
    }
}

