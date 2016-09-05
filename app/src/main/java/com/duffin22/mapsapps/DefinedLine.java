//package com.duffin22.mapsapps;
//
//import com.google.android.gms.maps.model.LatLng;
//
///**
// * Created by matthewtduffin on 04/09/16.
// */
//public class DefinedLine extends Line {
//    LatLng startPoint, endPoint;
//
//    public DefinedLine(LatLng startPoint, LatLng endPoint) {
//        this.startPoint = startPoint;
//        this.endPoint = endPoint;
//        double x0 = startPoint.latitude, y0 = startPoint.longitude,
//                x1 = endPoint.latitude, y1 = endPoint.longitude;
//        this.xCoefficient = y0-y1;
//        this.yCoefficient = x1-x0;
//        this.constant = x0*y1 - x1*y0;
//    }
//
//    public DefinedLine perpendicularLineTo(Line line) {
//        double newX = yCoefficient;
//        double newY = -1*xCoefficient;
//        double newConstant = -1*(newX*this.startPoint.latitude+newY*this.startPoint.longitude);
//        Line infinitePerp = new Line(newX, newY, newConstant);
//        LatLng newEndPoint = line.intersectionOf(infinitePerp);
//        return new DefinedLine(this.startPoint,newEndPoint);
//    }
//
//}
