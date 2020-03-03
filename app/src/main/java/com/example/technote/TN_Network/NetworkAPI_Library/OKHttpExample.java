package com.example.technote.TN_Network.NetworkAPI_Library;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKHttpExample extends AppCompatActivity implements View.OnClickListener {
    TextView txtString;
    Button asynchronousGet, synchronousGet, asynchronousPOST;

    private String url =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=AIzaSyBrJ3ec9wTuS6L-xHkaXLU8BJbFsx_LZ9o";

    public String postBody = "{\n" +
            "    \"name\": \"morpheus\",\n" +
            "    \"job\": \"leader\"\n" +
            "}";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_ok_http_example);

        asynchronousGet = (Button) findViewById(R.id.asynchronousGet);
        synchronousGet = (Button) findViewById(R.id.synchronousGet);

        asynchronousGet.setOnClickListener(this);
        synchronousGet.setOnClickListener(this);

        txtString = (TextView) findViewById(R.id.txtString);
        txtString.setMovementMethod(new ScrollingMovementMethod());
    }


    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("RequestResult","onFailure");
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                OKHttpExample.this.runOnUiThread(new Runnable() { // 버튼을 누르면 핸들러가 없기 때문에 MainThread에서 UI 작업을 실행해야한다.
                    @Override
                    public void run() {
                        Log.d("RequestResult","OkHttpExample run() onResponse");
                        try {
                            StringBuilder formattedResult = new StringBuilder();
                            JSONObject jsonObject = new JSONObject(myResponse);
                            JSONArray responseJSONArray = jsonObject.getJSONArray("results");
                            for (int i = 0; i < responseJSONArray.length(); i++) {
                                formattedResult.append("\n" + responseJSONArray.getJSONObject(i).get("name") + " => \t" + responseJSONArray.getJSONObject(i).get("rating"));
                            }
                            txtString.setText("List of Restaurants \n" + " Name" + "\tRating \n" + formattedResult);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.asynchronousGet: //비동기 : 비동기적으로 Task를 실행하면 먼저 실행된 Task가 종료되지 않아도 다른 Task를 실행할 수 있다.
                try {
                    run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.synchronousGet: // 동기 : 동기적으로 Task를 싱행한 후, 다른 Task를 실행하려면 먼저 실행된 Task가 종료되기를 기다려야 한다.
                OkHttpHandler okHttpHandler = new OkHttpHandler();
                okHttpHandler.execute(url);
                break;
        }
    }

    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            Request.Builder builder = new Request.Builder().get(); //getType으로 Data를 가져온다.
            builder.url(params[0]);
            Request request = builder.build();

            try {
                //Post to a Server
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtString.setText(s);
        }
    }
}
