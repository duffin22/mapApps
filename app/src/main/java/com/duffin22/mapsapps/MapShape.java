package com.duffin22.mapsapps;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewtduffin on 04/09/16.
 */
public class MapShape {
    List<LatLng> vertices;
    List<DefinedLine> edges;

    public MapShape(List<LatLng> vertices) {
        this.vertices = vertices;
        List<DefinedLine> edgeList = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            DefinedLine newLine;
            if (i == (vertices.size()-1)) {
                newLine = new DefinedLine(vertices.get(i),vertices.get(0));
            } else {
                newLine = new DefinedLine(vertices.get(i), vertices.get(i + 1));
            }
            edgeList.add(newLine);
        }
        this.edges = edgeList;
    }


}
