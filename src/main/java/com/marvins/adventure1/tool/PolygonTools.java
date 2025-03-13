package com.marvins.adventure1.tool;

import java.awt.Point;
import java.util.List;

public class PolygonTools {

    public static boolean isPointInPolygons(Point point, List<Polygon> polygons) {
        for (Polygon p : polygons){
            if(isPointInPolygon(point, p.getPositions())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPointInPolygon(Point point, List<Point> polygon) {
        int count = 0;
        int n = polygon.size();

        for (int i = 0; i < n; i++) {
            Point vertex1 = polygon.get(i);
            Point vertex2 = polygon.get((i + 1) % n); // next vertex (wrap around)

            // Check if the ray from point intersects with the edge (vertex1, vertex2)
            if (doIntersect(point, vertex1, vertex2)) {
                count++;
            }
        }

        // Point is inside if the count of intersections is odd
        return count % 2 == 1;
    }

    private static boolean doIntersect(Point p, Point vertex1, Point vertex2) {
        // Check if the point is between the y-coordinates of vertex1 and vertex2
        if ((vertex1.y > p.y) != (vertex2.y > p.y)) {
            // Calculate the x-coordinate of the intersection of the line
            double xIntersection = ((double) (vertex2.x - vertex1.x) * (p.y - vertex1.y) / (vertex2.y - vertex1.y)) + vertex1.x;
            return p.x < xIntersection; // Check if the point is to the left of the intersection
        }
        return false;
    }
}