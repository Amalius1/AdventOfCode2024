package pl.aml.bk.aoc.current;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Day05 {

    public void part1() {
        Set<String> rules = loadRules("/inputs/day05_rules.txt");
        List<String> pages = loadManuals("/inputs/day05_manuals.txt");
        List<String> validManuals = sortManualsByValidity(rules, pages).validManuals;
        int sum = 0;
        for (var manual : validManuals) {
            String[] split = manual.split(",");
            sum += Integer.parseInt(split[split.length / 2]);
        }

        log.info("Sum of part 1: {}", sum);
    }

    public void part2() {
        Set<String> rules = loadRules("/inputs/day05_rules.txt");
        List<String> allManuals = loadManuals("/inputs/day05_manuals.txt");
        List<String> invalidManuals = sortManualsByValidity(rules, allManuals).invalidManuals;
        List<String[]> manuals = parseManualsToArrays(invalidManuals);
        int sum = 0;
        for (var manual : manuals) {
            String[] rearranged = Arrays.copyOf(manual, manual.length);
            do {
                checkAndSwap(rearranged, rules);
            } while (!checkIfManualIsValid(rules, rearranged));
            sum += Integer.parseInt(rearranged[rearranged.length / 2]);
        }

        log.info("Sum of part 2: {}", sum);

    }

    private void checkAndSwap(String[] rearranged, Set<String> rules) {
        for (int i = 0; i < rearranged.length; i++) {
            String currentPage = rearranged[i];
            boolean swapped = false;
            if (rules.stream().anyMatch(x -> x.startsWith(currentPage))) {
                for (int j = 0; j < rearranged.length; j++) {
                    String checkedPage = rearranged[j];
                    if (rules.contains(currentPage + "|" + checkedPage) && i > j) {
                        // swap
                        swapPlaces(rearranged, i, j);
                        swapped = true;
                        break;
                    }
                }
                if (swapped) {
                    break;
                }
            }
        }
    }

    private void swapPlaces(String[] array, int i, int j) {
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private SortedManuals sortManualsByValidity(Set<String> rules, List<String> manuals) {
        List<String> validPages = new ArrayList<>();
        List<String> invalidPages = new ArrayList<>();
        for (var manual : manuals) {
            String[] pageNumbers = manual.split(",");
            if (checkIfManualIsValid(rules, pageNumbers)) {
                validPages.add(manual);
            } else {
                invalidPages.add(manual);
            }
        }

        return new SortedManuals(validPages, invalidPages);
    }

    private boolean checkIfManualIsValid(Set<String> rules, String[] manual) {
        for (int i = 0; i < manual.length; i++) {
            String currentPage = manual[i];
            if (rules.stream().anyMatch(x -> x.startsWith(currentPage))) {
                for (int j = 0; j < manual.length; j++) {
                    if (rules.contains(currentPage + "|" + manual[j]) && i > j) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private List<String[]> parseManualsToArrays(List<String> pages) {
        return pages.stream()
                .map(s -> s.split(","))
                .toList();
    }

    private List<String> loadManuals(String path) {
        try (InputStream inputStream = Day05.class.getResourceAsStream(path)) {
            assert inputStream != null;
            return new String(inputStream.readAllBytes()).lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<String> loadRules(String path) {
        try (InputStream inputStream = Day05.class.getResourceAsStream(path)) {
            assert inputStream != null;
            return new String(inputStream.readAllBytes()).lines().collect(Collectors.toSet());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    record SortedManuals(List<String> validManuals, List<String> invalidManuals) {
    }

}
