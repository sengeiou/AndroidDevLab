package com.example.technote.Network.RetrofitExample;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.example.technote.R;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitExample extends AppCompatActivity {
    private Retrofit mRetrofit;
    private GitHubService mRetrofitAPI;
    private Call<Repo> mCallMovieList;
    private Gson mGson;
    private TextView textView;

    private static final String url =
            "https://api.github.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_retrofit_example);

        textView = (TextView)findViewById(R.id.retrofit_text);
        setRetrofitInit();
//        callMovieList();
    }

    private void setRetrofitInit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRetrofitAPI = mRetrofit.create(GitHubService.class);
        final Call<List<Repo>> repos = mRetrofitAPI.listRepos("sarang628");
        repos.enqueue(mRetrofitCallback);
    }


    private Callback<List<Repo>> mRetrofitCallback = new Callback<List<Repo>>() {

        @Override
        public void onResponse(Call<List<Repo>> call, retrofit2.Response<List<Repo>> response) {
            Log.d("onResponse","RetrofitExample, result: onResponse");
            textView.setText(response.toString());
            //MovieListVO mMovieListVO = (MovieListVO) mGson.fromJson(result, MovieListVO.class);
        }

        @Override
        public void onFailure(Call<List<Repo>> call, Throwable t) {
            t.printStackTrace();
            Log.d("onResponse","RetrofitExample, result: onFailure");
        }
    };
}