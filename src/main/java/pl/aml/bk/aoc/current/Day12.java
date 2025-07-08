package pl.aml.bk.aoc.current;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Day12 {

    public static void main(String[] args) {
        part1();
    }

    private static void part1() {
        char[][] input = loadInput(false);
        int[][] areaInput = areaWithIdsInput(input);
        System.out.println(Arrays.deepToString(areaInput));
        Map<Integer, Calculations> map = new HashMap<>();
        for (int i = 0; i < areaInput.length; i++) {
            for (int j = 0; j < areaInput[i].length; j++) {
                int currentId = areaInput[i][j];
                map.putIfAbsent(currentId, new Calculations());
                // check for neighbors:
                int neighbourCount = checkNeighbourCount(areaInput, currentId, i, j);
                map.get(currentId).nextPlot(neighbourCount);
            }
        }
        System.out.println(map);
        int sum = map.values().stream().map(Calculations::getPrice).mapToInt(Integer::intValue).sum();
        System.out.println(sum);
    }

    private static char[][] loadInput(boolean test) {
        if (test) {
            return new char[][]{
                    {'A', 'A', 'A', 'A'},
                    {'B', 'B', 'C', 'D'},
                    {'B', 'B', 'C', 'C'},
                    {'E', 'E', 'E', 'C'}
            };
        }
        try (InputStream inputStream = Day12.class.getResourceAsStream("/inputs/day12.txt")) {
            byte[] bytes = inputStream.readAllBytes();
            String input = new String(bytes).trim();
            String[] rows = input.split("\n");
            char[][] result = new char[rows.length][];
            for (int i = 0; i < rows.length; i++) {
                result[i] = rows[i].trim().toCharArray();
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int[][] areaWithIdsInput(char[][] input) {
        int currentId = 1;
        int[][] areaIds = new int[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                if (areaIds[i][j] == 0) {
                    // start flood from here
                    Queue<Cell> queue = new LinkedList<>();
                    areaIds[i][j] = currentId;
                    Cell e = new Cell(i, j);
                    queue.add(e);
                    while (!queue.isEmpty()) {
                        floodFill(e, input, areaIds, currentId, queue);
                    }

                    // in the end, increment currentId
                    currentId++;
                }
            }
        }
        return areaIds;
    }

    private static void floodFill(Cell cell, char[][] input, int[][] areaIds, int currentId, Queue<Cell> queue) {
        areaIds[cell.row][cell.col] = currentId;
        while (!queue.isEmpty()) {
            Cell poll = queue.poll();
            for (Cell neighbor : getNeighbors(input, poll.row, poll.col)) {
                if (areaIds[neighbor.row][neighbor.col] == 0) {
                    areaIds[neighbor.row][neighbor.col] = currentId;
                    queue.add(neighbor);
                }
            }
        }
    }

    private static List<Cell> getNeighbors(char[][] input, int row, int col) {
        char currentChar = input[row][col];
        List<Cell> neighbors = new ArrayList<>(4);
        // check left
        if (col > 0 && input[row][col - 1] == currentChar) {
            neighbors.add(new Cell(row, col - 1));
        }
        // check right
        if (col < input[row].length - 1 && input[row][col + 1] == currentChar) {
            neighbors.add(new Cell(row, col + 1));
        }
        // check top
        if (row > 0 && input[row - 1][col] == currentChar) {
            neighbors.add(new Cell(row - 1, col));
        }
        // check bot
        if (row < input.length - 1 && input[row + 1][col] == currentChar) {
            neighbors.add(new Cell(row + 1, col));
        }

        return neighbors;
    }

    private static int checkNeighbourCount(int[][] input, int currentId, int i, int j) {
        int neighbourCount = 0;
        if (i > 0) {
            int top = input[i - 1][j];
            if (top == currentId) {
                neighbourCount++;
            }
        }
        if (j > 0) {
            int left = input[i][j - 1];
            if (left == currentId) {
                neighbourCount++;
            }
        }
        if (j < input[i].length - 1) {
            int right = input[i][j + 1];
            if (right == currentId) {
                neighbourCount++;
            }
        }
        if (i < input.length - 1) {
            int bottom = input[i + 1][j];
            if (bottom == currentId) {
                neighbourCount++;
            }
        }
        return neighbourCount;
    }


    private static class Calculations {
        private int area = 0;
        private int perimeter = 0;

        public void nextPlot(int countOfNeighbours) {
            area++;
            switch (countOfNeighbours) {
                case 0 -> perimeter += 4;
                case 1 -> perimeter += 3;
                case 2 -> perimeter += 2;
                case 3 -> perimeter += 1;
                case 4 -> perimeter += 0;
                default -> throw new IndexOutOfBoundsException("Too much neighbours!");
            }
        }

        public int getPrice() {
            return area * perimeter;
        }

        @Override
        public String toString() {
            return "Calculations{" +
                    "area=" + area +
                    ", perimeter=" + perimeter +
                    '}';
        }
    }

    private record Cell(int row, int col) {
    }
}
