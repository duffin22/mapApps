package com.duffin22.mapsapps;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by matthewtduffin on 04/09/16.
 */
public class Line {
    double xCoefficient, yCoefficient, constant;
    LatLng startPoint, endPoint;

    public Line() {
        //required empty constructor for an extended class
    }

    public Line(LatLng startPoint, LatLng endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        double x0 = startPoint.latitude, y0 = startPoint.longitude,
                x1 = endPoint.latitude, y1 = endPoint.longitude;
        this.xCoefficient = y0-y1;
        this.yCoefficient = x1-x0;
        this.constant = x0*y1 - x1*y0;
    }

    public Line(double x, double y, double c) {
        this.xCoefficient = x;
        this.yCoefficient = y;
        this.constant = c;
    }

    public double perpendicularDistanceTo(LatLng point) {
        double x0 = point.latitude, y0 = point.longitude;

        double topLine = Math.abs(this.xCoefficient*x0 + this.yCoefficient*y0 + constant);
        double botLine = Math.sqrt(Math.pow(this.xCoefficient,2)+Math.pow(this.yCoefficient,2));

        return topLine/botLine;
    }

    public Line createLineParallelAt(LatLng point) {
        double newConstant = -1*(this.xCoefficient*point.latitude + this.yCoefficient*point.longitude);
        return new Line(xCoefficient, yCoefficient, newConstant);
    }

    public LatLng intersectionWith(Line line) {
        double a0 = this.xCoefficient, b0 = this.yCoefficient, c0 = this.constant,
                a1 = line.xCoefficient, b1 = line.yCoefficient, c1 = line.constant;

        double det = b0*a1 - a0*b1;
        if (equalLL(0,det)) {
            return null;
        }

        double newLat = (a0*c1 - c0*a1)/det;
        double newLong = (c0*b1 - b0-c1)/det;

        return new LatLng(newLat,newLong);
    }

    public boolean equalLL(double first, double second) {
        if (Math.abs(first-second) < 0.0000001) {
            return true;
        }
        return false;
    }


}
