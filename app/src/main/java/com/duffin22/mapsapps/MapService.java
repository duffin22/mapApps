//package com.duffin22.mapsapps;
//
//import android.*;
//import android.app.Service;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Binder;
//import android.os.IBinder;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//
//import com.google.android.gms.location.LocationServices;
//
//public class MapService extends Service {
//    private final IBinder binder = new MapBinder();
//    private static Location lastLocation = null;
//
//    public MapService() {
//    }
//
//    public class MapBinder extends Binder {
//        MapService getMap() {
//            return MapService.this;
//        }
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//
//
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return binder;
//    }
//}
