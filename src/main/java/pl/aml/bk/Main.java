package pl.aml.bk;

import lombok.extern.slf4j.Slf4j;
import pl.aml.bk.aoc.current.Day02;
import pl.aml.bk.aoc.current.Day03;
import pl.aml.bk.aoc.current.Day04;
import pl.aml.bk.aoc.current.Day05;

@Slf4j
public class Main {


    public static void main(String[] args) {
//        day02();
//        day03();
//        day04();
        day05();
    }

    private static void day05() {
        Day05 day05 = new Day05();
        day05.part1();
        day05.part2();

    }

    private static void day04() {
        Day04 day04 = new Day04();
        day04.part2();
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


