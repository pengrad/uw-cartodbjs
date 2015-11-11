package com.github.pengrad.uw_cartodbjs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                setUpMap(googleMap);
                addData();
            }
        });
    }

    private void setUpMap(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.0840889, -79.7401672), 10));
    }

    private void addData() {
        new AsyncTask<Object, Model, Model>() {
            @Override
            protected Model doInBackground(Object... params) {
                String url = "https://buildingupdreams.cartodb.com/api/v2/sql?q=select%20*%20from%20public.guilford_county_area_parks";
                try {
                    return new Gson().fromJson(new InputStreamReader(new URL(url).openStream()), Model.class);
                } catch (IOException e) {
                    return new Model();
                }
            }

            @Override
            protected void onPostExecute(Model model) {
                if (model.rows != null) {
                    for (Model.Place place : model.rows) {
                        if (!place.isCorrect()) continue;
                        MarkerOptions markerOptions = new MarkerOptions()
                                .title(place.name)
                                .snippet(place.address)
                                .position(new LatLng(place.latitude, place.longitude));
                        mMap.addMarker(markerOptions);
                    }
                }
            }
        }.execute(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
