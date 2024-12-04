package pl.aml.bk.aoc.current;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Day03 {


    public void part1() {
        String data = loadData("/inputs/day03.txt");
        List<String> muls = extractMulSubstrings(data);
        int result = parseMuls(muls);
        log.info("Result={}", result);
    }

    public void part2() {
        String data = loadData("/inputs/day03.txt");
        List<String> muls = extractMulSubstringsWithConditions(data);
        int result = parseMulsWithConditions(muls);
        log.info("Result={}", result);
    }

    private String loadData(String path) {
        try (InputStream inputStream = Day03.class.getResourceAsStream(path)) {
            assert inputStream != null;
            return new String(inputStream.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private List<String> extractMulSubstrings(String inputString) {
        List<String> substrings = new ArrayList<>();
        Pattern pattern = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)");
        Matcher matcher = pattern.matcher(inputString);
        while (matcher.find()) {
            substrings.add(matcher.group());
        }
        return substrings;
    }

    private List<String> extractMulSubstringsWithConditions(String inputString) {
        List<String> substrings = new ArrayList<>();
        Pattern pattern = Pattern.compile("do\\(\\)|mul\\(\\d{1,3},\\d{1,3}\\)|don't\\(\\)");
        Matcher matcher = pattern.matcher(inputString);
        while (matcher.find()) {
            substrings.add(matcher.group());
        }
        return substrings;
    }

    private int parseMuls(List<String> muls) {
        int sum = 0;
        for (String mul :
                muls) {
            sum += parseSingleMul(mul);
        }
        return sum;
    }

    private int parseSingleMul(String mul) {
        Pattern pattern = Pattern.compile("\\d{1,3}");
        Matcher matcher = pattern.matcher(mul);
        int[] array = matcher.results().map(MatchResult::group).mapToInt(Integer::parseInt).toArray();

        return array[0] * array[1];
    }

    private int parseMulsWithConditions(List<String> muls) {
        int sum = 0;
        boolean stop = false;
        for (String input : muls) {
            if (input.equals("do()")) {
                stop = false;
                continue;
            }
            if (input.equals("don't()")) {
                stop = true;
                continue;
            }
            if (!stop) {
                sum += parseSingleMul(input);
            }
        }
        return sum;
    }

}
