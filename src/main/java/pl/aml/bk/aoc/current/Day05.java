package pl.aml.bk.aoc.current;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Day05 {

    public void part2() {
        Set<String> rules = loadRules("/inputs/test_day05_rules.txt");
        List<String> pages = loadPages("/inputs/test_day05_pages.txt");
        List<String> invalidPages = getValidPages(rules, pages).invalidPages;
        List<String[]> lines = parsePages(invalidPages);
        // reorder
        for (var line : lines) {
            for (int i = 0; i < line.length; i++) {
                String currentPage = line[i];
                for (int j = 0; j < line.length; j++) {
                    if (j == i) {
                        continue;
                    }
                    boolean contains = rules.contains(currentPage + "_" + line[j]);
                    if (!contains) {
                        continue;
                    }
                    boolean isForward = false;
                    if (i < j) {
                        isForward = true;
                    } else if (i == j) {
                        continue;
                    }
                }
            }
        }

    }

    // so ugly >.<
    public void part1() {
        Set<String> rules = loadRules("/inputs/day05_rules.txt");
        List<String> pages = loadPages("/inputs/day05_pages.txt");
        List<String> validPages = getValidPages(rules, pages).validPages;
        int sum = 0;
        for (var page :
                validPages) {
            String[] split = page.split(",");
            sum += Integer.parseInt(split[split.length / 2]);
        }

        System.out.println(sum);
    }

    private JoinedPages getValidPages(Set<String> rules, List<String> pages) {
        List<String> validPages = new ArrayList<>();
        List<String> invalidPages = new ArrayList<>();
        for (var page : pages) {
            String[] pageNumbers = page.split(",");
            boolean valid = true;
            for (int i = 0; i < pageNumbers.length; i++) {
                String currentPage = pageNumbers[i];
                for (int j = 0; j < pageNumbers.length; j++) {
                    boolean contains = rules.contains(currentPage + "_" + pageNumbers[j]);
                    if (!contains) {
                        continue;
                    }
                    boolean isForward = false;
                    if (i < j) {
                        isForward = true;
                    } else if (i == j) {
                        continue;
                    }

                    if (!isForward) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) {
                    break;
                }
            }
            if (valid) {
                validPages.add(page);
            } else {
                invalidPages.add(page);
            }
        }

        return new JoinedPages(validPages, invalidPages);
    }

    private List<String[]> parsePages(List<String> pages) {
        return pages.stream()
                .map(s -> s.split(","))
                .toList();
    }

    private List<String> loadPages(String path) {
        try (InputStream inputStream = Day05.class.getResourceAsStream(path)) {
            assert inputStream != null;
            return new String(inputStream.readAllBytes()).lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<String> loadRules(String path) {
        Set<String> rules = new HashSet<>();
        try (InputStream inputStream = Day05.class.getResourceAsStream(path)) {
            assert inputStream != null;
            List<String> lines = new String(inputStream.readAllBytes()).lines().toList();
            for (var line : lines) {
                String[] split = line.split("\\|");
                rules.add(split[0] + "_" + split[1]);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rules;
    }

    record JoinedPages(List<String> validPages, List<String> invalidPages) {
    }

}
