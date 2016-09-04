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
        return true;
    }

    public boolean containsLong(double longitude) {
        return true;
    }

    public boolean intersectsWith(MapLine line) {
        return true;
    }

}
