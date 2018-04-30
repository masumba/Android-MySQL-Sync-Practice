package com.javarank.syncpractice;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.first_name_edit_text)
    EditText firstNameEditText;
    @BindView(R.id.last_name_edit_text)
    EditText lastNameEditText;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    private MyAdapter adapter;
    private List<Person> personList;
    private DBHelper dbHelper;
    BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dbHelper = new DBHelper(this);
        personList = dbHelper.getPersonList();
        adapter = new MyAdapter();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                notifyDatasetChangedForPerson();
            }
        };

        ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Hello");
        progressDialog.show();

        //dbHelper.updatePersonById(personList.get(0), 0);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        int size = dbHelper.getUnSyncedPersonList().size();
        notifyDatasetChangedForPerson();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(DBContract.UI_UPDATE_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void notifyDatasetChangedForPerson() {
        personList = dbHelper.getPersonList();
        //adapter.clear();
        adapter.setPersonList(personList);
    }

    @OnClick(R.id.save_button)
    public void saveUserInfo() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        boolean isAvailable = Util.checkNetworkConnection(this);
        if( isAvailable ) {
            saveToCloudStorage(firstName, lastName);
            //Toast.makeText(MainActivity.this, "Connection", Toast.LENGTH_SHORT).show();
        } else {
            saveToLocalStorage(firstName, lastName, DBContract.SYNC_STATUS_FAILED);
        }

    }


    private void saveToLocalStorage(String firstName, String lastName, int status) {
        Person person = new Person(firstName, lastName, status);
        dbHelper.saveToLocalDb(person, status);
        notifyDatasetChangedForPerson();
    }

    private void saveToCloudStorage(final String firstName, final String lastName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBContract.CREATE_PERSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("response");
                    if( "ok".equals(status) ) {
                        saveToLocalStorage(firstName, lastName, DBContract.SYNC_STATUS_OK);
                    } else {
                        saveToLocalStorage(firstName, lastName, DBContract.SYNC_STATUS_FAILED);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                saveToLocalStorage(firstName, lastName, DBContract.SYNC_STATUS_FAILED);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                return params;
            }
        };

        //MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }



}
