package com.yunjeapark.technote.TN_OpenLibrary;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.androidquery.AQuery;
import com.androidquery.auth.GoogleHandle;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.yunjeapark.technote.R;

import org.json.JSONObject;

public class AQuery_Example extends AppCompatActivity {
    AQuery aq = new AQuery(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openlibrary_aquery);

        aq.id(R.id.image).image(R.drawable.profile);

        GoogleHandle handle = new GoogleHandle(this, AQuery.AUTH_PICASA, AQuery.ACTIVE_ACCOUNT);
        String url = "https://picasaweb.google.com/data/feed/api/user/default?alt=json";
        aq.auth(handle).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                Log.d("callbackCheck",object.toString());
                System.out.println(object);
            }
        });
    }
}
