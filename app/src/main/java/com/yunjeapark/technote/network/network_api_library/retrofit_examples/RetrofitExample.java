package com.yunjeapark.technote.network.network_api_library.retrofit_examples;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yunjeapark.technote.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitExample extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_lib_retrofit_example);

        textView = (TextView)findViewById(R.id.retrofit_text);

        Call<JsonObject> res = NetRetrofit.getInstance().getService().getMyJsonObject();
        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("RequestResult","RetrofitExample, Type : get, Result : onResponse");
                try {
                    StringBuilder formattedResult = new StringBuilder();
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    JSONArray responseJSONArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < responseJSONArray.length(); i++) {
                        formattedResult.append("\n" + responseJSONArray.getJSONObject(i).get("name") + " => \t" + responseJSONArray.getJSONObject(i).get("rating"));
                    }
                    textView.setText(formattedResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("RequestResult","RetrofitExample, Type : get, Result : onFailure, Error Message : " + t.getMessage());
                Log.e("Err", t.getMessage());
            }
        });
    }
}