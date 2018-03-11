package com.javarank.syncpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anik on 3/12/2018.
 */

public class NetworkMonitor extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if( Util.checkNetworkConnection(context) ) {
            DBHelper dbHelper = new DBHelper(context);
            List<Person> personList = dbHelper.getPersonList();
            for( Person person : personList ) {
                saveToServer(person, dbHelper, context);
            }
            //dbHelper.close();
            //context.sendBroadcast(new Intent(DBContract.UI_UPDATE_BROADCAST));
            Toast.makeText(context, "Broadcast received", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToServer(final Person person, final DBHelper dbHelper, final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBContract.CREATE_PERSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("response");
                    if( "ok".equals(status) ) {
                        dbHelper.saveToLocalDb(person, DBContract.SYNC_STATUS_OK);
                        context.sendBroadcast(new Intent(DBContract.UI_UPDATE_BROADCAST));
                    } else {
                        dbHelper.saveToLocalDb(person, DBContract.SYNC_STATUS_FAILED);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dbHelper.saveToLocalDb(person, DBContract.SYNC_STATUS_FAILED);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", person.getFirstName());
                params.put("last_name", person.getLastName());
                return params;
            }
        };
        //MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
