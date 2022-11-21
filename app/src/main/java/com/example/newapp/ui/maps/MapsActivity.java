package com.example.newapp.ui.maps;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
    }

        @Override
        protected void onStart () {
            super.onStart();
            mapView.onStart();
        }
        @Override
        protected void onStop () {
            super.onStop();
            mapView.onStop();
        }
        @Override
        public void onLowMemory () {
            super.onLowMemory();
            mapView.onLowMemory();
        }
        @Override
        protected void onDestroy () {
            super.onDestroy();
            mapView.onDestroy();
        }


        @Override
        public void onMapReady (@NonNull GoogleMap googleMap){

        }

}
