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
//    List<LatLng> shapePoints;
    MapShape mappy;
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

    private void addMarkers(MapShape shape) {
        List<LatLng> points = shape.getVertices();

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
    }

    public Polygon addMapShapeToMap(MapShape shape) {
        List<LatLng> points = shape.getVertices();

        final PolygonOptions polyS = new PolygonOptions()
                .addAll(points)
                .fillColor(Color.argb(90, 200, 200, 50))
                .strokeColor(Color.WHITE);

        addMarkers(shape);

        return mMap.addPolygon(polyS);
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

    public MapShape getDefaultShape(LatLng start) {
        double height = 0.003;
        double width = 0.005;
        LatLng btmLeft = new LatLng(start.latitude, start.longitude);
            LatLng tl = new LatLng(start.latitude + height-0.001, start.longitude-0.002);
        LatLng topLeft = new LatLng(start.latitude + height+0.001, start.longitude-0.001);
            LatLng tr = new LatLng(start.latitude + height+0.002, start.longitude + width+0.001);
        LatLng topRight = new LatLng(start.latitude + height, start.longitude + width+0.002);
        LatLng btmRight = new LatLng(start.latitude-0.001, start.longitude + width-0.003);
        List<LatLng> shapePoints = new ArrayList(Arrays.asList(btmLeft,tl, topLeft,tr, topRight, btmRight));
        return new MapShape(shapePoints);
    }

    public void setUpMapForNewProject() {

        LatLng start = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mappy = getDefaultShape(start);
        final Polygon polygon = addMapShapeToMap(mappy);

        Line line1 = mappy.getMiddleParallelLine();
        PolylineOptions poly1 = new PolylineOptions()
                                .color(Color.argb(255, 50, 50, 200))
                                .add(line1.startPoint)
                                .add(line1.endPoint);
        final Polyline polyline1 = mMap.addPolyline(poly1);

        Line line2 = mappy.getParallelLineAtInterval(1,4);
        PolylineOptions poly2 = new PolylineOptions()
                .color(Color.argb(255, 50, 50, 200))
                .add(line2.startPoint)
                .add(line2.endPoint);
        final Polyline polyline2 = mMap.addPolyline(poly2);

        Line line3 = mappy.getParallelLineAtInterval(3,4);
        PolylineOptions poly3 = new PolylineOptions()
                .color(Color.argb(255, 50, 50, 200))
                .add(line3.startPoint)
                .add(line3.endPoint);
        final Polyline polyline3 = mMap.addPolyline(poly3);

        //perpendicular skeleton line
//        Line line2 = mappy.getPerpendicularBaseLine();
//        PolylineOptions poly2 = new PolylineOptions()
//                .color(Color.argb(255, 50, 200, 50))
//                .add(line2.startPoint)
//                .add(line2.endPoint);
//        final Polyline polyline2 = mMap.addPolyline(poly2);


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
                List<LatLng> verts = mappy.getVertices();
                verts.set(currentDragMarker, marker.getPosition());
                mappy.setVertices(verts);
                polygon.setPoints(mappy.getVertices());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                List<LatLng> listy = new ArrayList<>(), listy2 = new ArrayList<>(), listy3 = new ArrayList<>();
//                Line bl = mappy.getPerpendicularBaseLine();
//                Line bl = mappy.getPerpendicularBaseLine();
//                listy.add(bl.startPoint);
//                listy.add(bl.endPoint);
//                polyline2.setPoints(listy);
                Line pl = mappy.getMiddleParallelLine();
                listy.add(pl.startPoint);
                listy.add(pl.endPoint);
                polyline1.setPoints(listy);

                Line pl2 = mappy.getParallelLineAtInterval(1,4);
                listy2.add(pl2.startPoint);
                listy2.add(pl2.endPoint);
                polyline2.setPoints(listy2);

                Line pl3 = mappy.getParallelLineAtInterval(3,4);
                listy3.add(pl3.startPoint);
                listy3.add(pl3.endPoint);
                polyline3.setPoints(listy3);
            }
        });


    }

    public PolylineOptions getCurrentPolyLine(int split) {
        List<LatLng> shapePoints = mappy.getVertices();
        boolean toggle = true;
        PolylineOptions pl = new PolylineOptions()
                .color(Color.argb(255, 50, 50, 200));

        for (int i = 0; i <= split; i++) {
            if (toggle) {
                pl.add(getFractionAlongLine(i, split, shapePoints.get(0), shapePoints.get(3)));
                pl.add(getFractionAlongLine(i, split, shapePoints.get(1), shapePoints.get(2)));
            } else {
                pl.add(getFractionAlongLine(i, split, shapePoints.get(1), shapePoints.get(2)));
                pl.add(getFractionAlongLine(i, split, shapePoints.get(0), shapePoints.get(3)));
            }
            toggle = !toggle;
        }


        return pl;
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
