package com.duffin22.mapsapps;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by matthewtduffin on 04/09/16.
 */
public class DefinedLine extends InfiniteLine {
    LatLng startPoint, endPoint;

    public DefinedLine(LatLng startPoint, LatLng endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        double x0 = startPoint.latitude, y0 = startPoint.longitude,
                x1 = endPoint.latitude, y1 = endPoint.longitude;
        this.xCoefficient = y0-y1;
        this.yCoefficient = x1-x0;
        this.constant = x0*y1 - x1*y0;
    }

}
