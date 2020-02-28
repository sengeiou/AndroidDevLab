package com.example.technote.Network;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.technote.R;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

public class FANExample extends AppCompatActivity {
    TextView textView;
    private String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=AIzaSyBrJ3ec9wTuS6L-xHkaXLU8BJbFsx_LZ9o";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_fan_example);

        AndroidNetworking.initialize(getApplicationContext());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);

        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        textView = (TextView)findViewById(R.id.result_text);

        AndroidNetworking.get(url)
                //.setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse","FANExample Type : get, result: onResponse");
                        try {
                            StringBuilder formattedResult = new StringBuilder();
                            JSONArray responseJSONArray = response.getJSONArray("results");
                            for (int i = 0; i < responseJSONArray.length(); i++) {
                                formattedResult.append("\n" + responseJSONArray.getJSONObject(i).get("name") + " => \t" + responseJSONArray.getJSONObject(i).get("rating"));
                            }
                            textView.setText("List of Restaurants \n" + " Name" + "\tRating \n" + formattedResult);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("RequestResult","FANExample Type : get, result : onError" + error.toString());
                        // handle error
                    }
                });

        /* post code
        AndroidNetworking.post(url)
                //.addBodyParameter("firstname", "Amit")
                //.addBodyParameter("lastname", "Shekhar")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            StringBuilder formattedResult = new StringBuilder();
                            JSONArray responseJSONArray = response.getJSONArray("results");
                            for (int i = 0; i < responseJSONArray.length(); i++) {
                                formattedResult.append("\n" + responseJSONArray.getJSONObject(i).get("name") + " => \t" + responseJSONArray.getJSONObject(i).get("rating"));
                            }
                            textView.setText("List of Restaurants \n" + " Name" + "\tRating \n" + formattedResult);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RequestResult","FANExample Type : post, result : onResponse");
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("RequestResult","FANExample Type : post, result : onError" + error.toString());
                    }
                });

         */
    }
}
