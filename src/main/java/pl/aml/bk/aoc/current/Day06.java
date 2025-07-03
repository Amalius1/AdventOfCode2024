package pl.aml.bk.aoc.current;

import pl.aml.bk.aoc.common.FailedToLoadResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Day06 {

    public static final String FINISH = "FINISH";
    public static final List<String> X = List.of("X", ".");
    public static final String AN_OBJECT = "#";
    private final List<String> GUARD_MARKS = List.of("<", ">", "v", "^");

    public void part2() {
        String[][] map = loadMap("/inputs/test_day06.txt");
        GuardCoordinates guardPosition = findGuardPosition(map);
        GuardCoordinates startingCoordinates = guardPosition;
        String[][] mapWithNewObstacle = Arrays.copyOf(map, map.length);
        int counter = 0;
        for (int i = 0; i < mapWithNewObstacle.length; i++) {
            for (int j = 0; j < mapWithNewObstacle[i].length; j++) {
                if ((startingCoordinates.direction.equals("^") && i == startingCoordinates.y - 1 && j == startingCoordinates.x)
                        || (startingCoordinates.direction.equals("<") && i == startingCoordinates.y && j == startingCoordinates.x - 1)
                        || (startingCoordinates.direction.equals(">") && i == startingCoordinates.y && j == startingCoordinates.x + 1)
                        || (startingCoordinates.direction.equals("v") && i == startingCoordinates.y + 1 && j == startingCoordinates.x)
                ) {
                    continue;
                }
                if (X.contains(mapWithNewObstacle[i][j]) && i != startingCoordinates.y && j != startingCoordinates.x) {
                    // put obstacle
                    mapWithNewObstacle[i][j] = AN_OBJECT;

                    // check if in loop
                    // increase if in loop
                    if (isGuardInLoop(mapWithNewObstacle, startingCoordinates)) {
                        counter++;
                        print2DArray(mapWithNewObstacle);
                        System.out.println("_".repeat(10));
                    }
                    mapWithNewObstacle[i][j] = ".";
                }
            }
        }
        System.out.println(counter);
    }

    private void print2DArray(String[][] map) {
        for (var line : map) {
            for (var cHar : line) {
                if (cHar.equals("X")) {
                    System.out.print(".");
                } else {
                    System.out.print(cHar);
                }
            }
            System.out.println("");
        }
    }

    private boolean isGuardInLoop(String[][] map, GuardCoordinates guardPosition) {
        AfterMovement obstacle;
        GuardCoordinates newGuardPosition = guardPosition;
        int maxIterations = 200_000;
        int currentIteration = 1;
        do {
            obstacle = findObstacle(map, newGuardPosition);
            newGuardPosition = obstacle.nextPosition;
            currentIteration++;
        } while (!obstacle.nextPosition.direction.equals(FINISH) && currentIteration < maxIterations);
        return currentIteration == maxIterations;
    }

    public void part1() {
        String[][] map = loadMap("/inputs/day06.txt");
        // find guard coords:
        GuardCoordinates guardPosition = findGuardPosition(map);
        AfterMovement obstacle;
        do {
            obstacle = findObstacle(map, guardPosition);
            guardPosition = obstacle.nextPosition;
        } while (!obstacle.nextPosition.direction.equals(FINISH));
        System.out.println(Arrays.deepToString(map));

        long count = Arrays.stream(map)
                .flatMap(Arrays::stream)
                .filter(s -> s.equals("X"))
                .count();
        System.out.println(count);

    }

    private GuardCoordinates findGuardPosition(String[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (GUARD_MARKS.contains(map[i][j])) {
                    return new GuardCoordinates(i, j, map[i][j]);
                }

            }
        }
        throw new RuntimeException("Did not found guard!");
    }

    private AfterMovement findObstacle(String[][] map, GuardCoordinates guardCoordinates) {
        int distance = -1;
        switch (guardCoordinates.direction) {
            case "^":
                for (int i = guardCoordinates.x; i >= 0; i--) {
                    if (map[i][guardCoordinates.y].equals(AN_OBJECT)) {
                        return new AfterMovement(new GuardCoordinates(i + 1, guardCoordinates.y, ">"), distance);
                    }
                    if (!map[i][guardCoordinates.y].equals("X")) {

                        map[i][guardCoordinates.y] = "X";
                    }
                }
                return new AfterMovement(new GuardCoordinates(0, guardCoordinates.y, FINISH), distance);
            case "<":
                for (int i = guardCoordinates.y; i >= 0; i--) {
                    if (map[guardCoordinates.x][i].equals(AN_OBJECT)) {
                        return new AfterMovement(new GuardCoordinates(guardCoordinates.x, i + 1, "^"), distance);
                    }
                    if (!map[guardCoordinates.x][i].equals("X")) {
                        distance++;
                        map[guardCoordinates.x][i] = "X";
                    }

                }
                return new AfterMovement(new GuardCoordinates(guardCoordinates.x, 0, FINISH), distance);
            case ">":
                for (int i = guardCoordinates.y; i < map.length; i++) {
                    if (map[guardCoordinates.x][i].equals(AN_OBJECT)) {
                        return new AfterMovement(new GuardCoordinates(guardCoordinates.x, i - 1, "v"), distance);
                    }
                    if (!map[guardCoordinates.x][i].equals("X")) {
                        distance++;
                        map[guardCoordinates.x][i] = "X";
                    }

                }
                return new AfterMovement(new GuardCoordinates(guardCoordinates.x, map[guardCoordinates.x].length - 1, FINISH), distance);

            case "v":
                for (int i = guardCoordinates.x; i < map.length; i++) {
                    if (map[i][guardCoordinates.y].equals(AN_OBJECT)) {
                        return new AfterMovement(new GuardCoordinates(i - 1, guardCoordinates.y, "<"), distance);
                    }
                    if (!map[i][guardCoordinates.y].equals("X")) {
                        distance++;
                        map[i][guardCoordinates.y] = "X";
                    }

                }
                return new AfterMovement(new GuardCoordinates(map.length - 1, guardCoordinates.y, FINISH), distance);
            default:
                throw new IllegalArgumentException("Invalid position character!");
        }
    }

    private String[][] loadMap(String path) {
        try (InputStream inputStream = Day06.class.getResourceAsStream(path)) {
            assert inputStream != null;
            List<String[]> list = new String(inputStream.readAllBytes()).lines()
                    .map(x -> x.split("")).toList();
            return list.toArray(new String[0][]);


        } catch (IOException e) {
            throw new FailedToLoadResourceException(e);
        }

    }

    record GuardCoordinates(int x, int y, String direction) {

    }

    record AfterMovement(GuardCoordinates nextPosition, int distance) {
    }

}
