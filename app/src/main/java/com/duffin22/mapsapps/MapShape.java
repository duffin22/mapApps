package com.duffin22.mapsapps;
import com.google.android.gms.maps.model.LatLng;

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
        double D = A*x0 + B*y0;

        Line newLine = new Line(A,B,D);

        Line trimmedLine = intersectLineWithEndPoints(newLine);

        return trimmedLine;
    }

    public Line intersectLineWithEndPoints(Line line) {
        Line baseline = this.edges.get(0);
        LatLng startPoint = null, endPoint = null;
        for (Line edge : this.edges) {
            if (line.intersectionWith(edge) != null && startPoint == null) {
                startPoint = line.intersectionWith(edge);
            } else if (line.intersectionWith(edge) != null) {
                endPoint = line.intersectionWith(edge);
            }
        }
        return new Line(startPoint, endPoint);
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
