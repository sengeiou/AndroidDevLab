package com.yunjeapark.technote.tn_network.network_api_library;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yunjeapark.technote.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VolleyExample extends AppCompatActivity {

    private TextView txtShowTextResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_lib_volley_example);

        txtShowTextResult = findViewById(R.id.txtDisplay);

        RequestQueue requestQueue = Volley.newRequestQueue(this); //Queue Start

        final String url
                = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=AIzaSyBrJ3ec9wTuS6L-xHkaXLU8BJbFsx_LZ9o";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    StringBuilder formattedResult = new StringBuilder();
                    JSONArray responseJSONArray = response.getJSONArray("results");
                    for (int i = 0; i < responseJSONArray.length(); i++) {
                        formattedResult.append("\n" + responseJSONArray.getJSONObject(i).get("name") + " => \t" + responseJSONArray.getJSONObject(i).get("rating"));
                    }
                    txtShowTextResult.setText("List of Restaurants \n" + " Name" + "\t Rating \n" + formattedResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtShowTextResult.setText("An Error occured while making the request");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}

