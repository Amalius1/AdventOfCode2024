package pl.aml.bk;

import lombok.extern.slf4j.Slf4j;
import pl.aml.bk.aoc.current.*;

import java.math.BigInteger;
import java.util.stream.Stream;

@Slf4j
public class Main {


    public static void main(String[] args) {
        Stream<Integer> numStream = Stream.of(10, 20, 30);
        numStream.map(n -> n + 10).peek(s -> System.out.print(s));
        numStream.forEach(s -> System.out.println(s));
    }


    private static void day05() {
        Day05 day05 = new Day05();
        log.info("-".repeat(10) + " DAY 05 " + "-".repeat(10));
        log.info("-".repeat(10) + " PART 1 " + "-".repeat(10));
        day05.part1();
        log.info("-".repeat(10) + " PART 2 " + "-".repeat(10));
        day05.part2();
        log.info("-".repeat(28));


    }

    private static void day04() {
        Day04 day04 = new Day04();
        log.info("-".repeat(10) + " DAY 04 " + "-".repeat(10));
        log.info("-".repeat(10) + " PART 1 " + "-".repeat(10));
        day04.part1();
        log.info("-".repeat(10) + " PART 2 " + "-".repeat(10));
        day04.part2();
        log.info("-".repeat(28));

    }

    private static void day03() {
        Day03 day03 = new Day03();
        log.info("-".repeat(10) + " DAY 03 " + "-".repeat(10));
        log.info("-".repeat(10) + " PART 1 " + "-".repeat(10));
        day03.part1();
        log.info("-".repeat(10) + " PART 2 " + "-".repeat(10));
        day03.part2();
        log.info("-".repeat(28));
    }


    private static void day02() {
        Day02 day02 = new Day02();
        log.info("-".repeat(10) + " DAY 02 " + "-".repeat(10));
        log.info("-".repeat(10) + " PART 1 " + "-".repeat(10));
        day02.part1();
        log.info("-".repeat(10) + " PART 2 " + "-".repeat(10));
        day02.part2();
        log.info("-".repeat(28));
    }


}


