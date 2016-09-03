package com.duffin22.mapsapps;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by matthewtduffin on 03/09/16.
 */
public class Line {
    LatLng startPoint, endPoint;

    public Line(LatLng startPoint, LatLng endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

}
