package pl.aml.bk.aoc.current;

import java.io.InputStream;
import java.util.*;

public class Day08 {

    public static void main(String[] args) {
        part1();
    }

    private static void part1() {

        Map<Character, List<Point>> points = new HashMap<>();
        Set<Point> results = new HashSet<>();
        int xSize = 0;
        int ySize = 0;
        try (InputStream inputStream = Day08.class.getResourceAsStream("/inputs/day08.txt")) {
            String[] lines = new String(inputStream.readAllBytes()).replaceAll("\r", "").split("\n");
            ySize = lines.length;
            xSize = lines[0].length();
            for (int i = 0; i < lines.length; i++) {
                char[] line = lines[i].toCharArray();
                for (int j = 0; j < line.length; j++) {
                    if ('.' != line[j]) {
                        points.computeIfAbsent(line[j], k -> new ArrayList<>()).add(new Point(i, j));
                    }
                }
            }
            System.out.println(points);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<Character> keys = points.keySet();
        for (Character key : keys) {
            List<Point> pointList = points.get(key);
            for (Point point : pointList) {
                for (Point otherPoint : pointList) {
                    if (!point.equals(otherPoint)) {
                        Point antiNode = point.calculateAntinode(otherPoint);
                        if (antiNode.validate(xSize, ySize)) {
                            results.add(antiNode);
                        }
                    }
                }
            }
        }
        System.out.println("Results:");
        System.out.println(results);
        System.out.println(results.size());
    }


    record Point(int x, int y) {

        public Point calculateAntinode(Point other) {
            int xDistance = Math.abs(other.x - x);
            int yDistance = Math.abs(other.y - y);
            int xTarget, yTarget;
            xTarget = x > other.x ? other.x - xDistance : other.x + xDistance;
            yTarget = y > other.y ? other.y - yDistance : other.y + yDistance;
            return new Point(xTarget, yTarget);
        }

        public boolean validate(int maxX, int maxY) {
            return this.x >= 0 && this.y >= 0 && this.x < maxX && this.y < maxY;

        }
    }
}
