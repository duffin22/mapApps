//package com.duffin22.mapsapps;
//
//import com.google.android.gms.maps.model.LatLng;
//
///**
// * Created by matthewtduffin on 03/09/16.
// */
//public class LineInfinite {
//
//    double gradient;
//    double constant;
//
//    public LineInfinite(double gradient, double constant) {
//        this.gradient = gradient;
//        this.constant = constant;
//    }
//
//    public double getLatFromLong(double longitude) {
//        if (Math.abs(this.gradient)>0.0001) {
//            return (longitude - constant) / gradient;
//        } else {
//            return constant;
//        }
//
//    }
//
//    public double getLongFromLat(double latitude) {
//        return gradient*latitude+constant;
//    }
//
//    public LatLng intersectWith(MapLine line) {
//        if (line.gradient - this.gradient < 0.0001) {
//            return null;
//        }
//        double latIntersect = (line.constant - this.constant)/(this.gradient - line.gradient);
//
//        if (line.containsLat(latIntersect)) {
//            return new LatLng(latIntersect,this.getLongFromLat(latIntersect));
//        } else {
//            return null;
//        }
//    }
//
//}
