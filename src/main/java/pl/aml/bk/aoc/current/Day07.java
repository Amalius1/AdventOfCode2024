package pl.aml.bk.aoc.current;

import pl.aml.bk.aoc.common.FailedToLoadResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day07 {

    public void part1() {

        List<PuzzleRow> data = loadData("/inputs/day07.txt");

        System.out.println(data);
        BigInteger sum = BigInteger.ZERO;
        for (var row : data) {
            sum = sum.add(calculate(row));
        }
        System.out.println(sum);
    }

    public void part2() {
        List<PuzzleRow> data = loadData("/inputs/test_day07.txt");
        BigInteger sum = BigInteger.ZERO;
        for (var row : data) {
            sum = sum.add(calculatePart2(row));
        }
        System.out.println(sum);

    }

    private BigInteger calculatePart2(PuzzleRow puzzle) {
        List<String> permutations = generatePermutations(puzzle.numbers.size() - 1, new char[]{'+', '*', '|'});
        BigInteger result = BigInteger.ZERO;
        for (var permutation : permutations) {
            List<Integer> splitIndicies = getCharIndices(permutation, '|');
            List<List<BigInteger>> subEquations = new ArrayList<>(splitIndicies.size() + 1);
            int lastIdx = 0;
            for (var idx : splitIndicies) {
                subEquations.add(puzzle.numbers.subList(lastIdx, idx + 1));
                lastIdx = idx + 1;
            }
            System.out.println(subEquations);
        }

        return result;
    }

    public List<Integer> getCharIndices(String text, char targetChar) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == targetChar) {
                indices.add(i);
            }
        }

        return indices;
    }

    private BigInteger joinNumbers(BigInteger left, BigInteger right) {
        StringBuilder sb = new StringBuilder();
        sb.append(left);
        sb.append(right);
        return new BigInteger(sb.toString());
    }


    private BigInteger calculate(PuzzleRow puzzle) {
        List<String> permutations = generatePermutations(puzzle.numbers.size() - 1, new char[]{'+', '*'});
        BigInteger result;

        for (var combination : permutations) {
            result = puzzle.numbers.get(0);
            // combinations:
            for (int i = 0; i < combination.length(); i++) {
                if (combination.charAt(i) == '+') {
                    result = result.add(puzzle.numbers.get(i + 1));
                } else if (combination.charAt(i) == '*') {
                    result = result.multiply(puzzle.numbers.get(i + 1));
                }
            }
            if (result.equals(puzzle.result)) {
                return result;
            }
        }
        return BigInteger.ZERO;
    }

    public List<String> generatePermutations(int n, char[] chars) {
        List<String> result = new ArrayList<>();
        generatePermutationsHelper(n, chars, new StringBuilder(), result);
        return result;
    }

    private void generatePermutationsHelper(int n, char[] chars, StringBuilder current, List<String> result) {
        if (current.length() == n) {
            result.add(current.toString());
            return;
        }

        for (char c : chars) {
            current.append(c);
            generatePermutationsHelper(n, chars, current, result);
            current.deleteCharAt(current.length() - 1); // Backtrack
        }
    }

    private List<PuzzleRow> loadData(String path) {
        try (InputStream inputStream = Day07.class.getResourceAsStream(path)) {
            return new String(inputStream.readAllBytes()).lines().map(x -> {
                String[] split = x.split(":");
                BigInteger result = new BigInteger(split[0]);
                List<BigInteger> numbers = Arrays.stream(split[1].trim().split(" ")).map(BigInteger::new).toList();
                return new PuzzleRow(result, numbers);
            }).toList();
        } catch (IOException e) {
            throw new FailedToLoadResourceException(e);
        }

    }


    record PuzzleRow(BigInteger result, List<BigInteger> numbers) {
    }

}
