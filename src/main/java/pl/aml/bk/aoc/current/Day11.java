package pl.aml.bk.aoc.current;

import pl.aml.bk.aoc.common.FailedToLoadResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;

public class Day11 {


    public static final BigInteger MULTIPLIER = new BigInteger("2024");

    public void part1() {
        List<BigInteger> stones = loadData("/inputs/day11.txt");
        int blinks = 25;

        for (int i = 0; i < blinks; i++) {
            List<BigInteger> copyStones = new ArrayList<>();
            int helperIdx = 0;
            for (int j = 0; j < stones.size(); j++) {
                String stringifiedStoneValue = stones.get(j).toString();
                int length = stringifiedStoneValue.length();
                // rule no.1
                if (BigInteger.ZERO.equals(stones.get(j))) {
                    copyStones.add(helperIdx, BigInteger.ONE);

                } else if (length % 2 == 0) { // rule no.2
                    BigInteger leftSide = new BigInteger(stringifiedStoneValue.substring(0, length / 2));
                    BigInteger rightSide = new BigInteger(stringifiedStoneValue.substring(length / 2, length));
                    copyStones.add(helperIdx, leftSide);
                    helperIdx++;
                    copyStones.add(helperIdx, rightSide);
                } else {
                    copyStones.add(helperIdx, stones.get(j).multiply(MULTIPLIER));
                }

                helperIdx++;

            }
            stones = copyStones;
        }
        System.out.println(stones.size());

    }

    public void helper(BigInteger init) {
        List<BigInteger> stones = new ArrayList<>();
        stones.add(init);
        int i = 0;
        do {
            i++;
            List<BigInteger> copyStones = new ArrayList<>();
            for (BigInteger stone : stones) {
                String stoneStringified = stone.toString();
                int length = stoneStringified.length();
                // rule no.1
                if (BigInteger.ZERO.equals(stone)) {
                    copyStones.add(BigInteger.ONE);
                } else if (length % 2 == 0) { // rule no.2

                    String leftStringified = stoneStringified.substring(0, length / 2);
                    BigInteger leftSide = new BigInteger(leftStringified);
                    String rightStringified = stoneStringified.substring(length / 2, length);
                    BigInteger rightSide = new BigInteger(rightStringified);
                    copyStones.add(leftSide);
                    copyStones.add(rightSide);

                } else {
                    BigInteger multiply = stone.multiply(MULTIPLIER);
                    copyStones.add(multiply);
                }
            }
            stones = copyStones;
            System.out.println("Number of stones after " + i + " iteration: " + stones.size());
            System.out.println(stones);
        } while (!stones.getFirst().equals(init));
        System.out.println("Iterations to do a cycle: " + i + " | size after: " + stones.size());
    }

    public void part2() {
//        List<BigInteger> stones = loadData("/inputs/day11.txt");
        List<BigInteger> stones = new ArrayList<>();
        stones.add(BigInteger.ZERO);
        Map<BigInteger, BigInteger> mem = new HashMap<>();
        Map<BigInteger, Pair> memForEven = new HashMap<>();
        int blinks = 75;

        for (int i = 0; i < blinks; i++) {
            List<BigInteger> copyStones = new ArrayList<>();
            for (BigInteger stone : stones) {
                if (mem.containsKey(stone)) {
                    BigInteger fromMem = mem.get(stone);
                    copyStones.add(fromMem);
                    continue;
                } else if (memForEven.containsKey(stone)) {
                    Pair pair = memForEven.get(stone);
                    copyStones.add(pair.left);
                    copyStones.add(memForEven.get(stone).right);
                    continue;
                }
                String stoneStringified = stone.toString();
                int length = stoneStringified.length();
                // rule no.1
                if (BigInteger.ZERO.equals(stone)) {
                    copyStones.add(BigInteger.ONE);
                    mem.put(BigInteger.ZERO, BigInteger.ONE);
                } else if (length % 2 == 0) { // rule no.2

                    String leftStringified = stoneStringified.substring(0, length / 2);
                    BigInteger leftSide = new BigInteger(leftStringified);
                    String rightStringified = stoneStringified.substring(length / 2, length);
                    BigInteger rightSide = new BigInteger(rightStringified);
                    copyStones.add(leftSide);
                    copyStones.add(rightSide);
                    memForEven.put(stone, new Pair(leftSide, rightSide));

                } else {
                    BigInteger multiply = stone.multiply(MULTIPLIER);
                    copyStones.add(multiply);
                    mem.put(stone, multiply);
                }


            }
            stones = copyStones;
            System.out.println("Number of stones after " + i + " iteration: " + stones.size());
//            System.out.println(stones);

        }
        System.out.println(stones.size());

    }

    private List<BigInteger> loadData(String path) {
        try (InputStream inputStream = Day11.class.getResourceAsStream(path)) {
            assert inputStream != null;
            return Arrays.stream(new String(inputStream.readAllBytes()).split(" "))
                    .map(BigInteger::new).toList();

        } catch (IOException e) {
            throw new FailedToLoadResourceException(e);
        }
    }

    record Pair(BigInteger left, BigInteger right) {
    }

}
