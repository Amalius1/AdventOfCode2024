package pl.aml.bk.aoc.current;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Day14 {

    public static void main(String[] args) {
        part2();


    }

    private static void part2() {
        int width = 101;
        int height = 103;
        List<Robot> robots = loadRobots();
        robots.forEach(robot -> robot.initBoundaries(width, height));
        int iterations = 0;
        do {
            iterations++;
            robots.parallelStream().forEach(Robot::move);
            if (iterations % 100000 == 0) {
                System.out.println("Iterations: " + iterations);
            }
        }
        while (robots.stream().filter(robot -> robot.positionX == 0).count() != width);
        printMap(new int[height][width], robots);

        System.out.println("=".repeat(10));
        System.out.println("Number of iterations: " + iterations);
    }

    private static void printMap(int[][] height, List<Robot> robots) {
        int[][] arr = height;
        for (Robot r :
                robots) {
            arr[r.positionY][r.positionX] += 1;
        }
        for (int[] line : arr) {
            System.out.println(Arrays.toString(line));
        }
    }


    private static void part1() {
        int width = 101;
        int height = 103;
        int iterations = 100;
        List<Robot> robots = loadRobots();
        robots.forEach(robot -> robot.initBoundaries(width, height));
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            for (Robot robot : robots) {
                executorService.submit(() -> {
                    for (int i = 0; i < iterations; i++) {
                        robot.move();
                    }
                });
            }
        }
        printMap(new int[height][width], robots);
        Map<Integer, Integer> map = new HashMap<>();
        for (Robot robot : robots) {
            int i = robot.assignQuadrant();
            map.put(i, map.getOrDefault(i, 0) + 1);
        }
        map.remove(0);
        int result = map.values().stream()
                .reduce(1, (a, b) -> a * b);
        System.out.println("Multiplication result: " + result);


    }


    private static List<Robot> loadRobots() {
        List<Robot> robots = new ArrayList<>();
        try (InputStream inputStream = Day14.class.getResourceAsStream("/inputs/day14.txt")) {
            List<String> lines = new String(inputStream.readAllBytes()).lines().toList();
            for (String line : lines) {
                String[] split = line.trim().split(" ");
                int[] pValues = Arrays.stream(split[0].replaceAll("p=", "").split(",")).mapToInt(Integer::parseInt).toArray();
                int[] vValues = Arrays.stream(split[1].replaceAll("v=", "").split(",")).mapToInt(Integer::parseInt).toArray();
                robots.add(new Robot(pValues[0], pValues[1], vValues[0], vValues[1]));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return robots;
    }


    static class Robot {
        private int positionX;
        private int positionY;
        private int velocityX;
        private int velocityY;
        private int widthBoundaries;
        private int heightBoundaries;


        public Robot(int positionX, int positionY, int velocityX, int velocityY) {
            this.positionX = positionX;
            this.positionY = positionY;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }

        public void initBoundaries(int widthBoundaries, int heightBoundaries) {
            this.widthBoundaries = widthBoundaries;
            this.heightBoundaries = heightBoundaries;
        }


        public void move() {
            if (velocityX > 0) {
                positionX = (positionX + velocityX) % widthBoundaries;
            } else {
                if (positionX + velocityX < 0) {
                    positionX = (positionX + velocityX + widthBoundaries);
                } else {
                    positionX = positionX + velocityX;
                }
            }
            if (velocityY > 0) {
                positionY = (positionY + velocityY) % heightBoundaries;
            } else {
                if (positionY + velocityY < 0) {
                    positionY = (positionY + velocityY + heightBoundaries);
                } else {
                    positionY = positionY + velocityY;
                }
            }
        }

        /**
         * 1 - top left
         * 2 - top right
         * 3 - bottom left
         * 4 - bottom right
         * 0 - no quadrant
         *
         * @return
         */
        public int assignQuadrant() {
            // top left q: x<width/2 y<height/2
            if (positionX < widthBoundaries / 2 && positionY < heightBoundaries / 2) {
                return 1;
            }
            // top right q: x>width/2 y<height/2
            if (positionX > widthBoundaries / 2 && positionY < heightBoundaries / 2) {
                return 2;
            }
            // bottom left q: x<width/2 y>height/2
            if (positionX < widthBoundaries / 2 && positionY > heightBoundaries / 2) {
                return 3;
            }
            // bottom right q: x>width/2 y>height/2
            if (positionX > widthBoundaries / 2 && positionY > heightBoundaries / 2) {
                return 4;
            }
            return 0;
        }

        @Override
        public String toString() {
            return "Robot{" +
                    "positionX=" + positionX +
                    ", positionY=" + positionY +
                    '}';
        }
    }

}

