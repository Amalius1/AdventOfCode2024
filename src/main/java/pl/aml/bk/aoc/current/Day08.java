package pl.aml.bk.aoc.current;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day08 {

    record PuzzleInput(Map<Character, List<Point>> points, int xSize, int ySize, int pointsCount) {
    }

    record Point(int x, int y) {

        public Point calculateAntinode(Point other) {
            int xTarget = 2 * other.x - this.x;
            int yTarget = 2 * other.y - this.y;
            return new Point(xTarget, yTarget);
        }

        public boolean validate(int maxX, int maxY) {
            return this.x >= 0 && this.y >= 0 && this.x < maxX && this.y < maxY;
        }
    }

    public static void main(String[] args) {
        System.out.print("Part 1: ");
        part1();
        System.out.println("-".repeat(10));
        System.out.print("Part 2: ");
        part2();
    }

    private static void part1() {
        try (InputStream inputStream = Day08.class.getResourceAsStream("/inputs/day08.txt")) {
            if (inputStream == null) {
                System.err.println("Resource not found: /inputs/day08.txt");
                return;
            }
            PuzzleInput input = parseInput(inputStream);
            Set<Point> results = calculateResultsPart1(input);

            System.out.println(results.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void part2() {
        try (InputStream inputStream = Day08.class.getResourceAsStream("/inputs/day08.txt")) {
            if (inputStream == null) {
                System.err.println("Resource not found: /inputs/day08.txt");
                return;
            }
            PuzzleInput input = parseInput(inputStream);
            Set<Point> reults = calculateResultsPart2(input);
            System.out.println(reults.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<Point> calculateResultsPart2(PuzzleInput input) {
        Set<Point> results = new HashSet<>();
        for (List<Point> pointList : input.points().values()) {
            if (pointList.size() < 2) {
                continue;
            }
            for (Point point : pointList) {
                results.add(point);
                for (Point otherPoint : pointList) {
                    if (!point.equals(otherPoint)) {
                        Point antiNode;
                        Point previousNode = point;
                        Point nextNode = otherPoint;
                        boolean isValid;
                        do {
                            antiNode = previousNode.calculateAntinode(nextNode);
                            isValid = antiNode.validate(input.xSize, input.ySize);
                            if (isValid) {
                                results.add(antiNode);
                            }
                            previousNode = nextNode;
                            nextNode = antiNode;

                        }
                        while (isValid);
                    }
                }
            }
        }
        return results;
    }

    private static PuzzleInput parseInput(InputStream inputStream) throws IOException {
        Map<Character, List<Point>> points = new HashMap<>();
        String[] lines = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).lines().toArray(String[]::new);

        if (lines.length == 0) {
            return new PuzzleInput(Collections.emptyMap(), 0, 0, 0);
        }

        int ySize = lines.length;
        int xSize = lines[0].length();
        for (int i = 0; i < ySize; i++) {
            String line = lines[i];
            for (int j = 0; j < xSize; j++) {
                char c = line.charAt(j);
                if (c != '.') {
                    points.computeIfAbsent(c, k -> new ArrayList<>()).add(new Point(i, j));
                }
            }
        }
        int pointsCount = points.values().stream().mapToInt(List::size).sum();
        return new PuzzleInput(points, xSize, ySize, pointsCount);
    }

    private static Set<Point> calculateResultsPart1(PuzzleInput input) {
        Set<Point> results = new HashSet<>();
        for (List<Point> pointList : input.points().values()) {
            if (pointList.size() < 2) {
                continue;
            }
            for (Point point : pointList) {
                for (Point otherPoint : pointList) {
                    if (!point.equals(otherPoint)) {
                        Point antiNode = point.calculateAntinode(otherPoint);
                        if (antiNode.validate(input.xSize(), input.ySize())) {
                            results.add(antiNode);
                        }
                    }
                }
            }
        }
        return results;
    }


    /**
     * For visual debugging
     */
    private static void visualize(Set<Point> results, int xSize, int ySize) {
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                if (results.contains(new Point(i, j))) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
    }

}
