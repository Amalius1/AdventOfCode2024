package pl.aml.bk.aoc.current;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Day15 {

    public static void main(String[] args) throws InterruptedException {
        boolean debug = false;
        try (InputStream inputStream = Day15.class.getResourceAsStream("/inputs/day15.txt")) {
            Board board = new Board();
            board.initializeBoard(new String(inputStream.readAllBytes()));
            while (board.hasNextMove()) {
                board.move();
                if (debug) {
                    board.printBoard();
                    Thread.sleep(2000L);
                }
            }

            System.out.println(board.calculateGPSSum());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static class Board {
        private static final String SPLIT_DELIMITER = ";";
        private static final BigInteger ONE_HUNDRED = new BigInteger("100");
        private int width;
        private int height;
        private Point robotLocation;
        private char[][] board;
        private Queue<Movement> moves;

        /**
         * @param input - raw input from file
         */
        public void initializeBoard(String input) {
            String[] split = input.split(SPLIT_DELIMITER);
            String boardString = split[0].trim();
            String movesString = split[1].trim();
            initializePlayArea(boardString);
            initializeMovementQueue(movesString);
            initializeRobot();

        }

        public boolean hasNextMove() {
            return !this.moves.isEmpty();
        }

        private void initializeMovementQueue(String movesString) {
            char[] moveArray = movesString.replaceAll("\r", "").replaceAll("\n", "").toCharArray();
            this.moves = new LinkedList<>();
            for (char move : moveArray) {
                this.moves.add(Movement.fromRepresentation(move));
            }
        }

        private void initializePlayArea(String boardString) {
            String[] boardRows = boardString.replaceAll("\r", "").split("\n");
            this.width = boardRows[0].length();
            this.height = boardRows.length;
            this.board = new char[height][width];
            for (int i = 0; i < height; i++) {
                this.board[i] = boardRows[i].toCharArray();
            }
        }

        private void initializeRobot() {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (this.board[i][j] == '@') {
                        this.robotLocation = new Point(j, i);
                        return;
                    }

                }
            }
        }

        private void printBoard() {
            for (var line : board) {
                System.out.println(Arrays.toString(line).replaceAll(",", ""));
            }
        }

        public void move() {
            Movement nextStep = moves.poll();
            System.out.println("Next move: " + nextStep.representation);
            Point nextPositionOnBoard = robotLocation.nextPoint(nextStep.movement);
            char nextObject = getObjectFromPoint(nextPositionOnBoard);
            if (nextObject == '#') {
                return;
            }
            if (nextObject == '.') {
                this.board[robotLocation.y][robotLocation.x] = '.';
                this.board[nextPositionOnBoard.y][nextPositionOnBoard.x] = '@';
                robotLocation = nextPositionOnBoard;
                return;
            }
            if (nextObject == 'O') {
                // check for next available location
                // if in the end it can be moved - place robot in the nextPositionOnBoard and 'O' on the next available position
                Point checkForNextAvailablePosition = nextPositionOnBoard.nextPoint(nextStep.movement);
                char nextAvailableObject = getObjectFromPoint(checkForNextAvailablePosition);
                while (nextAvailableObject != '.' && nextAvailableObject != '#') {
                    checkForNextAvailablePosition = checkForNextAvailablePosition.nextPoint(nextStep.movement);
                    nextAvailableObject = getObjectFromPoint(checkForNextAvailablePosition);
                }

                if (nextAvailableObject == '#') {
                    return;
                }
                if (nextAvailableObject == '.') {
                    this.board[robotLocation.y][robotLocation.x] = '.';
                    this.board[nextPositionOnBoard.y][nextPositionOnBoard.x] = '@';
                    robotLocation = nextPositionOnBoard;

                    this.board[checkForNextAvailablePosition.y][checkForNextAvailablePosition.x] = 'O';
                }
            }


        }

        public BigInteger calculateGPSSum() {
            BigInteger sum = BigInteger.ZERO;
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    if (this.board[i][j] == 'O') {
                        BigInteger calculatedWidthDistance = BigInteger.valueOf(i).multiply(ONE_HUNDRED);
                        BigInteger calculatedHeightDistance = BigInteger.valueOf(j);
                        sum = sum.add(calculatedWidthDistance).add(calculatedHeightDistance);
                    }
                }
            }
            return sum;
        }

        private char getObjectFromPoint(Point point) {
            return board[point.y][point.x];
        }

    }

    // x - place in width
    // y - place in height
    record Point(int x, int y) {
        public Point nextPoint(Point point) {
            return new Point(point.x + x, point.y + y);
        }

    }

    public enum Movement {
        LEFT('<', -1, 0),
        RIGHT('>', 1, 0),
        UP('^', 0, -1),
        DOWN('v', 0, 1);;

        private char representation;
        private Point movement;

        private Movement(char representation, int moveInWidth, int moveInHeight) {
            this.representation = representation;
            this.movement = new Point(moveInWidth, moveInHeight);
        }

        public static Movement fromRepresentation(char representation) {
            return switch (representation) {
                case '<' -> LEFT;
                case '>' -> RIGHT;
                case '^' -> UP;
                case 'v' -> DOWN;
                default ->
                        throw new IllegalArgumentException("Char " + representation + " is not a valid representation");
            };
        }


    }

}
