package com.duffin22.mapsapps;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewtduffin on 04/09/16.
 */
public class MapShape {
    private List<LatLng> vertices;
    private List<Line> edges;

    public MapShape(List<LatLng> vertices) {
        this.setVertices(vertices);
    }

    public void setVertices(List<LatLng> vertices) {
        this.vertices = vertices;
        List<Line> edgeList = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Line newLine;
            if (i == (vertices.size()-1)) {
                newLine = new Line(vertices.get(i),vertices.get(0));
            } else {
                newLine = new Line(vertices.get(i), vertices.get(i + 1));
            }
            edgeList.add(newLine);
        }
        this.edges = edgeList;
    }

    public List<LatLng> getVertices() {
        return vertices;
    }

    public List<Line> getEdges() {
        return edges;
    }

    public Line getParallelLineAtInterval(int numerator, int denominator) {
        Line baseline = this.edges.get(0);
        Line perpLine = this.getPerpendicularBaseLine();
        LatLng midPoint = getFractionAlongLine(numerator,denominator,perpLine);
        Line parLine = getParallelLine(baseline, midPoint);
        return parLine;
    }

    public List<LatLng> getCoveringPath(double distBetweenLines) {
        Line perpLine = this.getPerpendicularBaseLine();
        LatLng startPoint = perpLine.startPoint;
        LatLng endPoint = perpLine.endPoint;
        double distanceInMetres = SphericalUtil.computeDistanceBetween(startPoint,endPoint);
        int numOfLines = (int) (distanceInMetres/distBetweenLines);

        return getCoveringPathWithLines(numOfLines);
    }

    private List<LatLng> getCoveringPathWithLines(int numOfLines) {

        //This should be changed to below 1 if logic holds
        if (numOfLines < 2) {
            return null;
        }

        Line startLine = this.edges.get(0);
        Line perpLine = this.getPerpendicularBaseLine();
        LatLng startPoint = perpLine.startPoint;
        LatLng endPoint = perpLine.endPoint;

        List<Line> newLines = new ArrayList<>();
        for (int i = 1; i < numOfLines; i++) {
            double fraction = i * 1.0 / numOfLines;
            LatLng point = SphericalUtil.interpolate(startPoint, endPoint, fraction);
            Line newLine = getParallelLine(startLine,point);
            newLines.add(newLine);
        }

        List<LatLng> newPolyline = new ArrayList<>();
        newPolyline.add(startLine.startPoint);
        newPolyline.add(startLine.endPoint);
        boolean toggle = false;

        for (Line l : newLines) {
            if (toggle) {
                newPolyline.add(l.endPoint);
                newPolyline.add(l.startPoint);
            } else {
                newPolyline.add(l.startPoint);
                newPolyline.add(l.endPoint);
            }
            toggle=!toggle;
        }

        newPolyline.add(getFurthestNode());

        return newPolyline;
    }

    public double getPerpendicularLineDistance() {
        Line perpLine = this.getPerpendicularBaseLine();
        LatLng startPoint = perpLine.startPoint;
        LatLng endPoint = perpLine.endPoint;

        double distanceInMetres = SphericalUtil.computeDistanceBetween(startPoint,endPoint);
        return distanceInMetres;

    }

    public Line getMiddleParallelLine() {

        Line baseline = this.edges.get(0);
        Line perpLine = this.getPerpendicularBaseLine();
        LatLng midPoint = getFractionAlongLine(1,2,perpLine);
        Line parLine = getParallelLine(baseline, midPoint);
        return parLine;
    }

    public Line getPerpendicularBaseLine() {
        //Get coordinates for starting node (x0,y0) and furthest away node (x1,y1)
        LatLng startPoint = this.vertices.get(0);
        LatLng furthestPoint = this.getFurthestNode();
        double x0 = startPoint.latitude,
                y0 = startPoint.longitude,
                x1 = furthestPoint.latitude,
                y1 = furthestPoint.longitude;

        //Get equation of initial line in form Ax +By + C = 0
        Line startLine = this.edges.get(0);
        double A = startLine.xCoefficient,
                B = startLine.yCoefficient,
                C = startLine.constant;

        double D = A*x1 + B*y1;
        double E = A*y0 - B*x0;

        double x2y2 = Math.pow(A,2)+Math.pow(B,2);

        double x2 = (A*D - B*E)/x2y2;
        double y2 = (A*E + B*D)/x2y2;

        return new Line(startPoint, new LatLng(x2,y2));

    }

    private LatLng getFurthestNode() {
        Line startLine = edges.get(0);
        double furthestNodeDistance = startLine.perpendicularDistanceTo(this.vertices.get(0));
        int furthestNodeIndex = 0;
        for (int i = 1; i < this.vertices.size(); i++) {
            if (startLine.perpendicularDistanceTo(this.vertices.get(i)) > furthestNodeDistance) {
                furthestNodeDistance = startLine.perpendicularDistanceTo(this.vertices.get(i));
                furthestNodeIndex = i;
            }
        }
        LatLng furthestPoint = this.vertices.get(furthestNodeIndex);
        return furthestPoint;
    }

    public Line getParallelLine(Line baseline, LatLng newPoint) {
        double A = baseline.xCoefficient, B = baseline.yCoefficient, C = baseline.constant;
        double x0 = newPoint.latitude, y0 = newPoint.longitude;
        double D = -1*(A*x0 + B*y0);

        Line newLine = new Line(A,B,D);

        Line trimmedLine = intersectLineWithEndPoints(newLine);

        return trimmedLine;
    }

    public Line intersectLineWithEndPoints(Line line) {
        //The line in here has a gradient etc just no end points
        List<LatLng> intersects = new ArrayList<>();
        LatLng startPoint = null,endPoint = null;
        for (int i=1; i < this.edges.size(); i++) {
            //get access to each edge that is not the baseline
            Line edge = this.edges.get(i);
            LatLng intersect = lineIntersection(edge,line);
            intersects.add(intersect);
        }
        System.out.println();
        for (LatLng ll : intersects) {
            if (ll != null && startPoint == null) {
                startPoint = ll;
            } else if (ll != null) {
                endPoint = ll;
            }
        }
        return new Line(startPoint, endPoint);
    }

    public LatLng lineIntersection(Line l1, Line l2) {
        Line mainLine, secondLine;
        if (l1.startPoint != null) {
            mainLine = l1;
            secondLine = l2;
        } else {
            mainLine = l2;
            secondLine = l1;
        }

        double loLat = Math.min(mainLine.startPoint.latitude, mainLine.endPoint.latitude),
                hiLat = Math.max(mainLine.startPoint.latitude, mainLine.endPoint.latitude),
                loLong = Math.min(mainLine.startPoint.longitude, mainLine.endPoint.longitude),
                hiLong = Math.max(mainLine.startPoint.longitude, mainLine.endPoint.longitude);

        double a = mainLine.xCoefficient, b = mainLine.yCoefficient, c = mainLine.constant,
                d = secondLine.xCoefficient, e = secondLine.yCoefficient, f = secondLine.constant;

        double det = b*d - a*e;
        if (equalLL(0,det)) {
            return null;
        }

        double newLong = (a*f - c*d)/det;
        double newLat = (c*e - b*f)/det;

        if (loLat <= newLat && newLat <= hiLat && loLong <= newLong && newLong <= hiLong) {
            return new LatLng(newLat,newLong);
        }

        return null;


    }

    public boolean equalLL(double first, double second) {
        if (Math.abs(first-second) < 0.000001) {
            return true;
        }
        return false;
    }

    public LatLng getFractionAlongLine(int numerator, int denominator, Line line) {
        LatLng point1 = line.startPoint;
        LatLng point2 = line.endPoint;
        double lat1 = point1.latitude, lat2 = point2.latitude;
        double lon1 = point1.longitude, lon2 = point2.longitude;

        double fraction = (double) numerator/denominator;

        double newLat = (lat2 * fraction) + (lat1 * (1-fraction));
        double newLong = (lon2 * fraction) + (lon1 * (1-fraction));

        return new LatLng(newLat,newLong);
    }



}
