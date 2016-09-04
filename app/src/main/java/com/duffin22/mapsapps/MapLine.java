package com.duffin22.mapsapps;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by matthewtduffin on 03/09/16.
 */
public class MapLine {
    LatLng startPoint, endPoint;
    double gradient;
    double constant;

    public MapLine(LatLng startPoint, LatLng endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;

        if (startPoint.latitude == endPoint.latitude) {
            this.gradient = 0;
        } else {
            this.gradient = (startPoint.longitude - endPoint.longitude) / (startPoint.latitude-endPoint.latitude);
        }

        this.constant = startPoint.longitude - (this.gradient * startPoint.latitude);
    }

    public double getLatFromLong(double longitude) {
        if (containsLong(longitude)) {
            if (gradient!=0) {
                return (longitude - constant) / gradient;
            } else {
                return constant;
            }
        } else {
            return 0;
        }
    }

    public double getLongFromLat(double latitude) {
        if (containsLat(latitude)) {
            return gradient*latitude+constant;
        } else {
            return 0;
        }
    }


    public boolean containsLat(double latitude) {
        double lowestLat, highestLat;
        if (this.startPoint.latitude < this.endPoint.latitude) {
            lowestLat = this.startPoint.latitude;
            highestLat = this.endPoint.latitude;
        } else {
            highestLat = this.startPoint.latitude;
            lowestLat = this.endPoint.latitude;
        }
        if (lowestLat <= latitude && latitude <= highestLat) {
            return true;
        } else {
            return false;
        }
    }

    public boolean containsLong(double longitude) {
        double lowestLong, highestLong;
        if (this.startPoint.longitude < this.endPoint.longitude) {
            lowestLong = this.startPoint.longitude;
            highestLong = this.endPoint.longitude;
        } else {
            lowestLong = this.startPoint.longitude;
            highestLong = this.endPoint.longitude;
        }
        if (lowestLong <= longitude && longitude <= highestLong) {
            return true;
        } else {
            return false;
        }
    }

    public LatLng intersectWith(MapLine line) {
        if (line.gradient - this.gradient < 0.0001) {
            return null;
        }
        double latIntersect = (line.constant - this.constant)/(this.gradient - line.gradient);

        if (line.containsLat(latIntersect) && this.containsLat(latIntersect)) {
            return new LatLng(latIntersect,this.getLongFromLat(latIntersect));
        } else {
            return null;
        }
    }

}
