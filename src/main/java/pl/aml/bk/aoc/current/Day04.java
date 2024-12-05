package pl.aml.bk.aoc.current;

import lombok.extern.slf4j.Slf4j;
import pl.aml.bk.aoc.common.FailedToLoadResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Day04 {

    private final Pattern xmasPattern = Pattern.compile("XMAS");
    private final Pattern samxPattern = Pattern.compile("SAMX");

    public void part2() {
        char[][] input = loadInput("/inputs/day04.txt");
        int counter = 0;
        for (int i = 1; i < input.length - 1; i++) {
            for (int j = 1; j < input.length - 1; j++) {
                if (input[i][j] == 'A') {
                    // check X shape
                    char topLeftChar = input[i - 1][j - 1];
                    char botLeftChar = input[i + 1][j - 1];
                    char topRightChar = input[i - 1][j + 1];
                    char botRightChar = input[i + 1][j + 1];
                    boolean leftToRight = (topLeftChar == 'M' && botRightChar == 'S') || (topLeftChar == 'S' && botRightChar == 'M');
                    boolean rightToLeft = (topRightChar == 'M' && botLeftChar == 'S') || (topRightChar == 'S' && botLeftChar == 'M');
                    if (leftToRight && rightToLeft) {
                        counter++;
                    }

                }
            }
        }
        log.info("Part 2 total count: {}", counter);
    }

    public void part1() {
        char[][] input = loadInput("/inputs/day04.txt");
        int totalCount = 0;
        // find in rows (reverse too)
        int rowsCount = findInRows(input);
        log.info("In rows: {}", rowsCount);
        // find in cols (reverse too)
        int colsCount = findInCols(input);
        log.info("In cols: {}", colsCount);
        // find in diagonal     1 0
        //                      0 2
        int botLeft = findInDiag1002(input);
        log.info("Diag left to right: {}", botLeft);
        // find in diagonal     0 1
        //                      2 0
        int diagCount0120Bot = findInDiag0120(input);
        log.info("Diag right to left: {}", diagCount0120Bot);


        totalCount = rowsCount + colsCount + botLeft + diagCount0120Bot;
        log.info("Part 1 total count: {}", totalCount);


    }


    private int findInDiag0120(char[][] input) {
        char[][] rotated = rotate90Degrees(input);
        return findInDiag1002(rotated);
    }

    private int findInDiag1002(char[][] input) {
        int counter = 0;
        for (int i = input.length - 1; i >= 0; i--) {
            StringBuilder stringBuilder = new StringBuilder();
            int helperIndex = i;
            for (int j = 0; j < input.length; j++) {
                stringBuilder.append(input[helperIndex][j]);
                helperIndex++;
                if (helperIndex == input.length) {
                    break;
                }
            }
            String line = stringBuilder.toString();
            Matcher xmasMatcher = xmasPattern.matcher(line);
            Matcher samxMatcher = samxPattern.matcher(line);
            counter += xmasMatcher.results().count();
            counter += samxMatcher.results().count();
        }
        for (int i = 0; i < input.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            int helperIndex = 0;
            for (int j = i + 1; j < input.length; j++) {
                stringBuilder.append(input[helperIndex][j]);
                helperIndex++;
                if (helperIndex == input.length) {
                    break;
                }
            }
            String line = stringBuilder.toString();
            Matcher xmasMatcher = xmasPattern.matcher(line);
            Matcher samxMatcher = samxPattern.matcher(line);
            counter += xmasMatcher.results().count();
            counter += samxMatcher.results().count();
        }
        return counter;
    }

    private int findInRows(char[][] input) {
        int counter = 0;

        for (char[] chars : input) {
            StringBuilder lineBuffer = new StringBuilder();
            for (char aChar : chars) {
                lineBuffer.append(aChar);
            }
            String line = lineBuffer.toString();
            Matcher xmasMatcher = xmasPattern.matcher(line);
            Matcher samxMatcher = samxPattern.matcher(line);
            counter += xmasMatcher.results().count();
            counter += samxMatcher.results().count();
        }


        return counter;
    }

    private int findInCols(char[][] input) {
        int counter = 0;
        for (int i = 0; i < input[0].length; i++) {
            StringBuilder colBuffer = new StringBuilder();
            for (int j = 0; j < input.length; j++) {
                colBuffer.append(input[j][i]);
            }
            String line = colBuffer.toString();
            Matcher xmasMatcher = xmasPattern.matcher(line);
            Matcher samxMatcher = samxPattern.matcher(line);
            counter += xmasMatcher.results().count();
            counter += samxMatcher.results().count();
        }
        return counter;
    }


    public char[][] loadInput(String filePath) {

        try (InputStream inputStream = Day04.class.getResourceAsStream(filePath)) {
            assert inputStream != null;
            String data = new String(inputStream.readAllBytes());
            List<String> lines = data.lines().toList();
            int cols = lines.get(0).length();
            int rows = lines.size();
            char[][] result = new char[cols][rows];
            for (int i = 0; i < cols; i++) {
                String line = lines.get(i);
                for (int j = 0; j < rows; j++) {
                    char letter = line.charAt(j);
                    result[i][j] = letter;
                }
            }
            return result;
        } catch (IOException e) {
            throw new FailedToLoadResourceException(e);
        }
    }

    public char[][] rotate90Degrees(char[][] matrix) {
        int n = matrix.length;
        char[][] rotatedMatrix = new char[n][n]; // Create a new array
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[n - i - 1], 0, rotatedMatrix[i], 0, n);
        }
        return rotatedMatrix;
    }

}
