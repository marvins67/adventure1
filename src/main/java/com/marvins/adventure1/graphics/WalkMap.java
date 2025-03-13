package com.marvins.adventure1.graphics;

import com.marvins.adventure1.tool.Polygon;

import java.util.ArrayList;
import java.util.List;

public class WalkMap {

    private List<Polygon> polygons = new ArrayList<>();

    public Polygon getPolygon(int i) {
        return polygons.get(i);
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public void addPolygon(Polygon polygon) {
        this.polygons.add(polygon);
    }

    public void removePolygon() {
        if (!polygons.isEmpty()) {
            this.polygons.remove(polygons.size() - 1);
        }
    }
}
