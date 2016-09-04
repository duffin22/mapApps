package com.duffin22.mapsapps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolygonDemoActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
//        LocationListener
        {

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

    }

    private List<LatLng> createShapeFromArray(List<LatLng> points) {

        for (int j = 0; j < points.size(); j++) {
            if (j == 0) {
                mMap.addMarker(new MarkerOptions()
                        .position(points.get(j))
                        .title("" + j)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .draggable(true));
            } else {
                mMap.addMarker(new MarkerOptions()
                        .position(points.get(j))
                        .title("" + j)
                        .draggable(true));
            }

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
            onPermissionLocationSuccess();
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

            setUpMapForNewProject();

        }
    }

    public void setUpMapForNewProject() {

        LatLng start = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        double height = 0.003;
        double width = 0.005;
        LatLng btmLeft = new LatLng(start.latitude, start.longitude);
        LatLng topLeft = new LatLng(start.latitude + height, start.longitude);
        LatLng topRight = new LatLng(start.latitude + height, start.longitude + width);
        LatLng btmRight = new LatLng(start.latitude, start.longitude + width);
        shapePoints = new ArrayList(Arrays.asList(btmLeft, topLeft, topRight, btmRight));

        final PolygonOptions polyS = new PolygonOptions()
                .addAll(createShapeFromArray(shapePoints))
                .fillColor(Color.argb(90, 200, 200, 50))
                .strokeColor(Color.WHITE);

        final Polygon polygon = mMap.addPolygon(polyS);

        final PolylineOptions polyL = getCurrentPolyLine(8);
//
        final Polyline polyline = mMap.addPolyline(polyL);

        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.animateCamera(zoom);

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
                polyline.setPoints(getCurrentPolyLine(5).getPoints());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            }
        });


    }

    public PolylineOptions makeNewStylePolyline(int split) {

        PolylineOptions pl = new PolylineOptions()
                .color(Color.argb(255, 50, 50, 200));

        return pl;
    }

    public PolylineOptions getCurrentPolyLine(int split) {
        boolean toggle = true;
        PolylineOptions pl = new PolylineOptions()
                .color(Color.argb(255, 50, 50, 200));

        for (int i=0; i<=split; i++) {
            if (toggle) {
                pl.add(getFractionAlongLine(i, split, shapePoints.get(0), shapePoints.get(3)));
                pl.add(getFractionAlongLine(i, split, shapePoints.get(1), shapePoints.get(2)));
            } else {
                pl.add(getFractionAlongLine(i, split, shapePoints.get(1), shapePoints.get(2)));
                pl.add(getFractionAlongLine(i, split, shapePoints.get(0), shapePoints.get(3)));
            }
            toggle=!toggle;
        }


        return pl;
    }

            //TODO: CHange logic in this method
    public LatLng getDistanceAlongLine(double distance, MapLine line) {
        LatLng point1 = line.startPoint;
        LatLng point2 = line.endPoint;
        double lat1 = point1.latitude, lat2 = point2.latitude;
        double lon1 = point1.longitude, lon2 = point2.longitude;

        double newLat = lat1+distance;
        double newLong = lon1+distance;

        return new LatLng(newLat,newLong);
    }

    public LatLng getFractionAlongLine(int numerator, int denominator, LatLng point1, LatLng point2) {
        double lat1 = point1.latitude, lat2 = point2.latitude;
        double lon1 = point1.longitude, lon2 = point2.longitude;

        double fraction = (double) numerator/denominator;

        double newLat = (lat2 * fraction) + (lat1 * (1-fraction));
        double newLong = (lon2 * fraction) + (lon1 * (1-fraction));


        return new LatLng(newLat,newLong);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case ACCESS_FINE_LOCATION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onPermissionLocationSuccess();
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
