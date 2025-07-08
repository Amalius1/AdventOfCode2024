package pl.aml.bk.aoc.current;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 {


    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final BigDecimal PRIZE_ADDITION = new BigDecimal("10000000000000");

    public static void main(String[] args) {
        System.out.print("Part 1: ");
        part1();
        System.out.println("-".repeat(5));
        System.out.print("Part 2: ");
        part2();
    }

    private static void part1() {
        List<InputData> inputData = loadData();
        List<Solution> solutions = inputData.stream().map(InputData::getSolution).toList();
        BigDecimal reduce = solutions.stream().map(Solution::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(reduce);
    }

    private static void part2() {
        List<InputData> inputData = loadData2();
        List<Solution> solutions = inputData.stream().map(InputData::getSolution).toList();
        BigDecimal reduce = solutions.stream().map(Solution::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(reduce);
    }


    private static List<InputData> loadData() {
        ArrayList<InputData> inputData = new ArrayList<>();
        try (InputStream inputStream = Day13.class.getResourceAsStream("/inputs/day13.txt")) {
            List<String> lines = new String(inputStream.readAllBytes()).lines().toList();
            for (int i = 0; i < lines.size(); i += 4) {
                String firstLine = lines.get(i);
                String secondLine = lines.get(i + 1);
                String thirdLine = lines.get(i + 2);
                Pair buttonA = findPairInLine(firstLine);
                Pair buttonB = findPairInLine(secondLine);
                Pair prize = findPairInLine(thirdLine);
                inputData.add(new InputData(
                        new BigDecimal(buttonA.x), new BigDecimal(buttonB.x), new BigDecimal(prize.x),
                        new BigDecimal(buttonA.y), new BigDecimal(buttonB.y), new BigDecimal(prize.y)
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    private static List<InputData> loadData2() {
        ArrayList<InputData> inputData = new ArrayList<>();
        try (InputStream inputStream = Day13.class.getResourceAsStream("/inputs/day13.txt")) {
            List<String> lines = new String(inputStream.readAllBytes()).lines().toList();
            for (int i = 0; i < lines.size(); i += 4) {
                String firstLine = lines.get(i);
                String secondLine = lines.get(i + 1);
                String thirdLine = lines.get(i + 2);
                Pair buttonA = findPairInLine(firstLine);
                Pair buttonB = findPairInLine(secondLine);
                Pair prize = findPairInLine(thirdLine);
                inputData.add(new InputData(
                        new BigDecimal(buttonA.x), new BigDecimal(buttonB.x), new BigDecimal(prize.x).add(PRIZE_ADDITION),
                        new BigDecimal(buttonA.y), new BigDecimal(buttonB.y), new BigDecimal(prize.y).add(PRIZE_ADDITION)
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    private static Pair findPairInLine(String line) {
        Matcher matcher = NUMBER_PATTERN.matcher(line);
        matcher.find();
        String a = matcher.group();
        matcher.find();
        String b = matcher.group();
        return new Pair(a, b);
    }

    record Solution(BigDecimal aButtonTimes, BigDecimal bButtonTimes) {

        private static final BigDecimal COST_A_BUTTON = new BigDecimal("3");
        private static final BigDecimal COST_B_BUTTON = new BigDecimal("1");

        public BigDecimal getTotalCost() {
            return COST_A_BUTTON.multiply(aButtonTimes).add(COST_B_BUTTON.multiply(bButtonTimes));
        }

    }

    record InputData(BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal d, BigDecimal e, BigDecimal f) {
        public Solution getSolution() {
            try {
                BigDecimal rightX = e.multiply(c).subtract(f.multiply(b));
                BigDecimal x = e.multiply(a).subtract(b.multiply(d));
                x = rightX.divide(x);
                BigDecimal y = c.subtract(a.multiply(x)).divide(b);
                return new Solution(x, y);
            } catch (ArithmeticException exc) {
                return new Solution(BigDecimal.ZERO, BigDecimal.ZERO);
            }
        }
    }

    record Pair(String x, String y) {
    }
}
