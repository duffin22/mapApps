package com.duffin22.mapsapps;

import android.graphics.drawable.shapes.Shape;

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

    public LatLng getFurthestNode() {
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


}
