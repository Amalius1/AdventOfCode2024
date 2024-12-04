package pl.aml.bk.aoc.old.day19;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 {


    private static final List<String> FINAL_VALUES = List.of("A", "R");

    public static void part1() {

        Map<String, Function<Map<String, Integer>, String>> workflow = new HashMap<>();
        String lines;
        try (InputStream resourceAsStream = Day19.class.getResourceAsStream("/workflow.txt")) {
            lines = new String(resourceAsStream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        lines.lines().forEach(line -> {
            String[] split = line.split("\\{");
            String key = split[0];
            String functionLine = split[1].replace("}", "");
            Function<Map<String, Integer>, String> calcuationFunction = parseFunction(functionLine);
            workflow.put(key, calcuationFunction);
        });


        // input
        // part 1
        String inputLines;
        try (InputStream inputStream = Day19.class.getResourceAsStream("/inputs.txt")) {
            inputLines = new String(inputStream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<Map<String, Integer>> inputs = inputLines.lines().map(Day19::getInput).toList();

        // result
        List<Map<String, Integer>> results = new ArrayList<>();

        for (var input : inputs) {
            String destination = "in";
            do {
                destination = workflow.get(destination).apply(input);
            } while (!FINAL_VALUES.contains(destination));
            if (destination.equals("A")) {
                results.add(input);
            }
        }
        int sum = results.stream().flatMapToInt(m -> m.values().stream().mapToInt(Integer::intValue)).sum();
        System.out.println(sum);

    }


    private static Function<Map<String, Integer>, String> parseFunction(String functionLine) {
        return (Map<String, Integer> map) -> {
            for (String part : functionLine.split(",")) {
                String[] rule = part.split(":");
                if (rule.length == 2) {
                    String condition = rule[0];
                    String destination = rule[1];

                    Matcher matcher = Pattern.compile("(x|m|a|s)([<>])(\\d+)").matcher(condition);
                    if (matcher.matches()) {
                        String variable = matcher.group(1);
                        String operator = matcher.group(2);
                        int value = Integer.parseInt(matcher.group(3));

                        if (operator.equals("<") && map.get(variable) < value ||
                                operator.equals(">") && map.get(variable) > value) {
                            return destination;
                        }
                    }
                } else {
                    return rule[0]; // Destination (A or R)
                }
            }
            throw new RuntimeException("Empty functionLine!");
        };
    }


    private static Map<String, Integer> getInput(String inputLine) {
        String sanitized = inputLine.replace("{", "").replace("}", "");
        String[] variables = sanitized.split(",");

        return Arrays.stream(variables).map(s -> s.split("="))
                .collect(Collectors.toMap(a -> a[0], a -> Integer.parseInt(a[1])));
    }

    private static List<Map<String, Integer>> getDistinctPermutations() {
        List<Map<String, Integer>> permutations = new ArrayList<>();
        for (int x = 1; x <= 4000; x++) {
            for (int m = 1; m <= 4000; m++) {
                for (int a = 1; a <= 4000; a++) {
                    for (int s = 1; s <= 4000; s++) {
                        permutations.add(createMapEntryForList(x, m, a, s));
                    }
                }
            }
        }

        return permutations;
    }

    private static Map<String, Integer> createMapEntryForList(Integer x, Integer m, Integer a, Integer s) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("x", x);
        map.put("m", m);
        map.put("a", a);
        map.put("s", s);
        return map;
    }

    public static void part2() {

    }


}
