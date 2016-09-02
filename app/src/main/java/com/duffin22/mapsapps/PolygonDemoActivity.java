package com.duffin22.mapsapps;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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
            implements OnMapReadyCallback,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener {

    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    public static final int ACCESS_FINE_LOCATION = 1992;
    GoogleMap mMap;
    List<LatLng> shapePoints;
    int currentDragMarker;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polygon_demo);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

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

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())));
        }

    }

    public void onPermissionLocationSuccess() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case ACCESS_FINE_LOCATION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {

                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
