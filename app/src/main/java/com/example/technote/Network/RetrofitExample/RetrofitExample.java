package com.example.technote.Network.RetrofitExample;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitExample extends AppCompatActivity {
    private TextView textView;

    private static final String url =
            "https://api.github.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_retrofit_example);

        textView = (TextView)findViewById(R.id.retrofit_text);

        Call<ArrayList<JsonObject>> res = NetRetrofit.getInstance().getService().getListRepos("YunJaePark3908");
        res.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                Log.d("RequestResult","RetrofitExample, Type : get, Result : onResponse");
                if (response.body() != null)
                    textView.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                Log.d("RequestResult","RetrofitExample, Type : get, Result : onFailure, Error Message : " + t.getMessage());
                Log.e("Err", t.getMessage());
            }
        });
    }
}