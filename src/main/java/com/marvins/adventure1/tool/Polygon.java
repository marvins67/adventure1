package com.marvins.adventure1.tool;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Polygon {
    List<Point> positions = new ArrayList<>();

    public List<Point> getPositions() {
        return positions;
    }

    public void setPositions(List<Point> positions) {
        this.positions = positions;
    }

    public void addPosition(Point position) {
        this.positions.add(position);
    }

    @Override
    public String toString() {
        return "Polygon{" + positions.stream()
                .map(p -> String.format("(%d,%d)", (int)p.getX(), (int)p.getY()))
                .collect(Collectors.joining(" ; ")) + "}";
    }

    public void removeLastPosition() {
        positions.remove(positions.size()-1);
    }
}