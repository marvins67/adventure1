package com.marvins.adventure1;

import java.util.*;

public class Pathfinder {

    static class Node {
        int x, y;
        int g, h;
        Node parent;

        Node(int x, int y, int g, int h, Node parent) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
            this.parent = parent;
        }

        int getF() {
            return g + h;
        }
    }

    public static List<Node> findPath(boolean[][] walkable, int startX, int startY, int endX, int endY) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        Set<Node> closedList = new HashSet<>();

        Node startNode = new Node(startX, startY, 0, heuristic(startX, startY, endX, endY), null);
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            closedList.add(current);

            if (current.x == endX && current.y == endY) {
                return constructPath(current);
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;

                    int newX = current.x + dx;
                    int newY = current.y + dy;

                    if (isValid(walkable, newX, newY) && !isInClosedList(closedList, newX, newY)) {
                        int g = current.g + 1;
                        int h = heuristic(newX, newY, endX, endY);
                        Node neighbor = new Node(newX, newY, g, h, current);

                        if (!isInOpenList(openList, newX, newY) || g < neighbor.g) {
                            openList.add(neighbor);
                        }
                    }
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private static int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan distance
    }

    private static boolean isValid(boolean[][] walkable, int x, int y) {
        return x >= 0 && x < walkable.length && y >= 0 && y < walkable[0].length && walkable[x][y];
    }

    private static boolean isInClosedList(Set<Node> closedList, int x, int y) {
        return closedList.stream().anyMatch(node -> node.x == x && node.y == y);
    }

    private static boolean isInOpenList(PriorityQueue<Node> openList, int x, int y) {
        return openList.stream().anyMatch(node -> node.x == x && node.y == y);
    }

    private static List<Node> constructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }
}