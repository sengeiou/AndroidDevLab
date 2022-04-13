package com.yunjeapark.technote.network.network_api_library;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yunjeapark.technote.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestAPIExample extends AppCompatActivity {
    private TextView textView;
    GetData getData;
    private String mURL =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=AIzaSyBrJ3ec9wTuS6L-xHkaXLU8BJbFsx_LZ9o";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_rest_api_example);
        getData = new GetData();
        getData.execute(mURL);
        textView = (TextView)findViewById(R.id.rest_api_text);
    }

    public class GetData extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected String doInBackground(String... params){
            String result = null;
            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream in = httpURLConnection.getInputStream();

                StringBuilder builder = new StringBuilder();
                StringBuilder formattedResult = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                result = builder.toString();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray responseJSONArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < responseJSONArray.length(); i++) {
                    formattedResult.append("\n" + responseJSONArray.getJSONObject(i).get("name") + " => \t" + responseJSONArray.getJSONObject(i).get("rating"));
                }
                result = formattedResult.toString();
            } catch (Exception e) {
                // Error calling the rest api
                Log.e("REST_API", "GET method failed: " + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            textView.setText(result);
        }
    }
}
