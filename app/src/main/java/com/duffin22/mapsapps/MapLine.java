//package com.duffin22.mapsapps;
//
//import com.google.android.gms.maps.model.LatLng;
//
///**
// * Created by matthewtduffin on 03/09/16.
// */
//public class MapLine {
//    LatLng startPoint, endPoint;
//    double gradient;
//    double constant;
//    int type;
//    public static final int TYPE_HORIZONTAL = 1,
//                            TYPE_VERTICAL = 2,
//                            TYPE_NORMAL = 3;
//
//
//    public MapLine(LatLng startPoint, LatLng endPoint) {
//
//        this.startPoint = startPoint;
//        this.endPoint = endPoint;
//
//        if (startPoint.latitude == endPoint.latitude) {
//            this.type = TYPE_VERTICAL;
//            this.gradient = 0;
//            this.constant = 0;
//        } else if (startPoint.longitude == endPoint.longitude) {
//            this.type = TYPE_HORIZONTAL;
//            this.gradient = 0;
//            this.constant = startPoint.longitude;
//        } else {
//            this.type = TYPE_NORMAL;
//            double top = startPoint.longitude - endPoint.longitude;
//            double bottom = startPoint.latitude-endPoint.latitude;
//            this.gradient = top/bottom;
//            this.constant = startPoint.longitude - (this.gradient * startPoint.latitude);
//        }
//    }
//
//    public double perpendicularDistanceFromPoint(LatLng point) {
//
//        double x = point.latitude, y = point.longitude;
//
//        double distance = (Math.abs(this.gradient*x-y+this.constant))/(Math.sqrt(Math.pow(this.gradient,2)+1));
//
//        return distance;
//    }
//
//    public LineInfinite getPerpendicularLine(LatLng crossPoint) {
//        if (this.type == TYPE_HORIZONTAL) {
//            //TODO: Add logic here
//            return null;
//        } else if (this.type == TYPE_VERTICAL) {
//            double newGradient = 0;
//            double newConstant = this.startPoint.longitude;
//            return new LineInfinite(newGradient, newConstant);
//        } else {
//            double newGradient = (-1)/this.gradient;
//            double newConstant = crossPoint.longitude + (crossPoint.latitude/this.gradient);
//            return new LineInfinite(newGradient, newConstant);
//        }
//
//    }
//
//    public double getLatFromLong(double longitude) {
//        if (containsLong(longitude)) {
//            if (gradient!=0) {
//                return (longitude - constant) / gradient;
//            } else {
//                return constant;
//            }
//        } else {
//            return 0;
//        }
//    }
//
//    public double getLongFromLat(double latitude) {
//        if (containsLat(latitude)) {
//            return gradient*latitude+constant;
//        } else {
//            return 0;
//        }
//    }
//
//    public boolean containsLat(double latitude) {
//
//        if (this.type == TYPE_VERTICAL) {
//            if (this.startPoint.latitude == latitude) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//
//        double lowestLat, highestLat;
//        if (this.startPoint.latitude < this.endPoint.latitude) {
//            lowestLat = this.startPoint.latitude;
//            highestLat = this.endPoint.latitude;
//        } else {
//            highestLat = this.startPoint.latitude;
//            lowestLat = this.endPoint.latitude;
//        }
//        if (lowestLat <= latitude && latitude <= highestLat) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean containsLong(double longitude) {
//
//        if (this.type == TYPE_HORIZONTAL) {
//            if (this.startPoint.longitude == longitude) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//
//        double lowestLong, highestLong;
//        if (this.startPoint.longitude < this.endPoint.longitude) {
//            lowestLong = this.startPoint.longitude;
//            highestLong = this.endPoint.longitude;
//        } else {
//            lowestLong = this.startPoint.longitude;
//            highestLong = this.endPoint.longitude;
//        }
//        if (lowestLong <= longitude && longitude <= highestLong) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public LatLng intersectWith(MapLine line) {
//        if (line.gradient - this.gradient < 0.000001) {
//            return null;
//        }
//        double latIntersect = (line.constant - this.constant)/(this.gradient - line.gradient);
//
//        if (line.containsLat(latIntersect) && this.containsLat(latIntersect)) {
//            return new LatLng(latIntersect,this.getLongFromLat(latIntersect));
//        } else {
//            return null;
//        }
//    }
//
//}
