package com.example.technote.GoogleMap;

import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.technote.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import java.util.Arrays;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,PlaceSelectionListener {

    private GoogleMap mMap;
    AutocompleteSupportFragment place1;
    TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        txtInfo = (TextView) findViewById(R.id.txtInfo);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_map_api_key), Locale.KOREAN);
        }

        // Specify the types of place data to return.

        place1 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place1);
        place1.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        place1.setOnPlaceSelectedListener(this);

    }
    @Override
    public void onPlaceSelected(Place place) {
        AddPlace(place, 1);

    }

    @Override
    public void onError(Status status) {
    }

    public void AddPlace(Place place, int pno) {
        try {
            if (mMap == null) {
                Log.d("MasActivityError","Please check your API key for Google Places SDK and your internet conneciton");
            } else {

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 4));

                mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                        .title("Name:" + place.getName() + ". Address:" + place.getAddress()));

                txtInfo.setText("Name:" + place.getName() + ". Address:" + place.getAddress());

            }
        } catch (Exception ex) {

            if (ex != null) {
                Log.d("MasActivityError",ex.getMessage());
                Toast.makeText(MapsActivity.this, "Error:" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(mMap!=null)
        {
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    txtInfo.setText(marker.getTitle().toString() + " Lat:" + marker.getPosition().latitude + " Long:" + marker.getPosition().longitude);
                    return false;
                }
            });
        }
    }
}