package pl.aml.bk.aoc.current;

import lombok.extern.slf4j.Slf4j;
import pl.aml.bk.aoc.common.FailedToLoadResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Day02 {

    public void part1() {
        List<Integer[]> list = loadData("/inputs/day02.txt");

        int validCount = 0;
        for (var line : list) {
            if (isEntryValid(line)) {
                validCount++;
            }
        }

        log.info("Valid count= {}", validCount);
    }

    public void part2() {
        List<Integer[]> list = loadData("/inputs/day02.txt");
        int validCount = 0;
        for (var line : list) {
            boolean isValid = isEntryValid(line);
            if (!isValid) {
                for (int i = 0; i < line.length; i++) {
                    Integer[] adjusted = removeElement(line, i);
                    if (isEntryValid(adjusted)) {
                        isValid = true;
                        break;
                    }
                }
            }
            if (isValid) {
                validCount++;
            }
        }
        log.info("Valid count= {}", validCount);
    }

    private List<Integer[]> loadData(String input) {
        try (InputStream inputStream = Day02.class.getResourceAsStream(input)) {

            assert inputStream != null;
            return new String(inputStream.readAllBytes()).lines().map(x -> x.split(" "))
                    .map(strings -> Arrays.stream(strings)
                            .map(Integer::parseInt)
                            .toArray(Integer[]::new))
                    .toList();
        } catch (IOException e) {
            throw new FailedToLoadResourceException(e);
        }
    }

    private boolean isEntryValid(Integer[] line) {
        boolean isAscending = line[0] < line[1];
        for (int i = 0; i < line.length - 1; i++) {
            int difference = line[i] - line[i + 1];
            if (Math.abs(difference) > 3 || difference == 0
                    || (isAscending && difference > 0)
                    || !isAscending && difference < 0) {
                return false;
            }
        }
        return true;
    }


    public Integer[] removeElement(Integer[] array, int index) {
        if (index < 0 || index >= array.length) {
            throw new IllegalArgumentException("Invalid index");
        }

        Integer[] newArray = new Integer[array.length - 1];
        int j = 0;
        for (int i = 0; i < array.length; i++) {
            if (i != index) {
                newArray[j++] = array[i];
            }
        }
        return newArray;
    }

}
