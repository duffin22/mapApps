package com.duffin22.mapsapps;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.Arrays;
import java.util.List;

public class PolygonDemoActivity extends AppCompatActivity
            implements OnMapReadyCallback {

    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polygon_demo);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        map.setContentDescription("Google Map with polygons.");

        map.addPolygon(new PolygonOptions()
                .addAll(createRectangle(map,SYDNEY, 4, 4))
                .fillColor(Color.argb(90,112,123,43))
                .strokeColor(Color.BLACK));

        // Move the map so that it is centered on the mutable polygon.
        map.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));

        // Add a listener for polygon clicks that changes the clicked polygon's stroke color.
        map.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                // Flip the r, g and b components of the polygon's stroke color.
                int strokeColor = polygon.getStrokeColor() ^ 0x00ffffff;
                polygon.setStrokeColor(strokeColor);
            }
        });
    }

    private List<LatLng> createRectangle(GoogleMap mMap, LatLng start, double width, double height) {
        LatLng btmLeft = new LatLng(start.latitude, start.longitude);
        LatLng topLeft = new LatLng(start.latitude + height, start.longitude);
        LatLng topRight = new LatLng(start.latitude + height, start.longitude + width);
        LatLng btmRight = new LatLng(start.latitude, start.longitude + width);

        mMap.addMarker(new MarkerOptions()
                .position(topRight));

        return Arrays.asList(
                btmLeft,
                topLeft,
                topRight,
                btmRight);
    }
}
