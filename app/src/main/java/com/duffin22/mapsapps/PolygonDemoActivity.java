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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolygonDemoActivity extends AppCompatActivity
            implements OnMapReadyCallback {

    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    GoogleMap mMap;
    List<LatLng> shapePoints;
    int currentDragMarker;

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
        mMap = map;
        map.setContentDescription("Google Map with polygons.");

        LatLng start = SYDNEY;
        int height = 4;
        int width = 4;
        LatLng btmLeft = new LatLng(start.latitude, start.longitude);
        LatLng topLeft = new LatLng(start.latitude + height, start.longitude);
        LatLng topRight = new LatLng(start.latitude + height, start.longitude + width);
        LatLng btmRight = new LatLng(start.latitude, start.longitude + width);
        shapePoints = new ArrayList(Arrays.asList(btmLeft, topLeft, topRight, btmRight));

        final PolygonOptions poly = new PolygonOptions()
                .addAll(createShapeFromArray(shapePoints))
                .fillColor(Color.argb(90,112,123,43))
                .strokeColor(Color.BLACK);

        final Polygon polygon = mMap.addPolygon(poly);

        // Move the map so that it is centered on the mutable polygon.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                int tag = Integer.parseInt(marker.getTitle());
                currentDragMarker = tag;
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                shapePoints.set(currentDragMarker, marker.getPosition());
                polygon.setPoints(shapePoints);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            }
        });

    }

    private List<LatLng> createShapeFromArray(List<LatLng> points) {

        for (int j = 0; j < points.size(); j++) {
            mMap.addMarker(new MarkerOptions()
                    .position(points.get(j))
                    .title(""+j)
                    .draggable(true));
        }
        return points;
    }
}
