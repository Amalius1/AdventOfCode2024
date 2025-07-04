package pl.aml.bk.aoc.current;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Queue;

public class Day09 {

    public static void main(String[] args) {
        part1();
    }

    private static String loadData() {
        try (InputStream in = Day09.class.getResourceAsStream("/inputs/day09.txt")) {
            return new String(in.readAllBytes()).replaceAll("\n", "").replaceAll("\r", "");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void part1() {
        String[] input = loadData().split("");
        int maxValue = (input.length - 1) / 2;

        Queue<Integer> queue = fillQueueWithDecrementingMax(input, maxValue);
        BigInteger lastUsedIndex = BigInteger.ZERO;
        BigInteger sum = BigInteger.ZERO;
        // iterate over whole input
        BigInteger baseIndex = BigInteger.ZERO; // index to multiply with, incrementing by one each time it is used
        boolean breakAll = false;
        for (int i = 0; i < input.length - 1; i += 2) { // i = iterator index over input
            // exit condition = when left id is the same as current id from queue?
            int timesFromLeft = Integer.parseInt(input[i]);
            int timesFromRight = Integer.parseInt(input[i + 1]);
            // left multiplication and sum
            for (int left = 0; left < timesFromLeft; left++) {
                sum = sum.add(baseIndex.multiply(lastUsedIndex));
                baseIndex = baseIndex.add(BigInteger.ONE);
            }
            if (queue.peek() != null && queue.peek().equals(lastUsedIndex.intValue())) {
                System.out.println("Finished calculating");
                break;
            }
            // right multiplication and sum
            for (int right = 0; right < timesFromRight; right++) {
                sum = sum.add(BigInteger.valueOf(queue.poll()).multiply(baseIndex));
                baseIndex = baseIndex.add(BigInteger.ONE);
                if (lastUsedIndex.equals(BigInteger.valueOf(queue.peek()))) {
                    breakAll = true;
                    break;
                }
            }
            lastUsedIndex = lastUsedIndex.add(BigInteger.ONE);
            if (breakAll) {
                System.out.println("Finished calculating");
                break;
            }
        }
        System.out.println(sum);
    }


    private static Queue<Integer> fillQueueWithDecrementingMax(String[] input, int maxValue) {
        Queue<Integer> queue = new LinkedList<>();
        // fill queue
        // iterate over every second number from the end to determine how many times put max value in queue and decrease max value by 1
        for (int i = input.length - 1; i >= 0; i -= 2) {
            for (int j = 0; j < Integer.parseInt(input[i]); j++) {
                queue.add(maxValue);
            }
            maxValue -= 1;
        }
        return queue;
    }


}
