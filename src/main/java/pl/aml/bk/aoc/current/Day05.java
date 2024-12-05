package pl.aml.bk.aoc.current;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Day05 {

    public void part2() {
        Set<String> rules = loadRules("/inputs/day05_rules.txt");
        List<String> pages = loadPages("/inputs/day05_pages.txt");
        List<String> invalidPages = sortManualsByValidity(rules, pages).invalidPages;
        List<String[]> manuals = parseManuals(invalidPages);
        int sum = 0;
        // reorder

        for (var manual : manuals) {
            String[] rearranged = Arrays.copyOf(manual, manual.length);
            boolean isManualValid = false;

            do {
                for (int i = 0; i < rearranged.length; i++) {
                    String currentPage = rearranged[i];
                    boolean swapped = false;
                    if (rules.stream().filter(x -> x.startsWith(currentPage)).findAny().isPresent()) {
                        for (int j = 0; j < rearranged.length; j++) {
                            if (i == j) {
                                continue;
                            }
                            String checkedPage = rearranged[j];
                            if (rules.contains(currentPage + "|" + checkedPage)) {
                                boolean isPairValid = isPairValid(i, j);
                                if (!isPairValid) {
                                    // swap
                                    swapPlaces(rearranged, i, j);
                                    swapped = true;
                                    break;
                                }
                            }
                        }
                        if (swapped) {
                            break;
                        }
                    }
                }
                isManualValid = checkIfManualIsValid(rules, rearranged);
            } while (!isManualValid);
            if (isManualValid) {
                sum += Integer.parseInt(rearranged[rearranged.length / 2]);
            }

        }

        System.out.println(sum);
    }

    private void swapPlaces(String[] array, int i, int j) {
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public void part1() {
        Set<String> rules = loadRules("/inputs/day05_rules.txt");
        List<String> pages = loadPages("/inputs/day05_pages.txt");
        List<String> validPages = sortManualsByValidity(rules, pages).validPages;
        int sum = 0;
        for (var page :
                validPages) {
            String[] split = page.split(",");
            sum += Integer.parseInt(split[split.length / 2]);
        }

        System.out.println(sum);
    }

    private JoinedPages sortManualsByValidity(Set<String> rules, List<String> manuals) {
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

        return new JoinedPages(validPages, invalidPages);
    }

    private boolean isPairValid(int currentPageIdx, int checkedPageIdx) {
        boolean isForward = false;
        if (currentPageIdx < checkedPageIdx) {
            isForward = true;
        }
        return isForward;
    }

    private boolean checkIfManualIsValid(Set<String> rules, String[] pageNumbers) {
        for (int i = 0; i < pageNumbers.length; i++) {
            String currentPage = pageNumbers[i];
            for (int j = 0; j < pageNumbers.length; j++) {
                boolean contains = rules.contains(currentPage + "|" + pageNumbers[j]);
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
                    return false;
                }
            }
        }
        return true;
    }

    private List<String[]> parseManuals(List<String> pages) {
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
        try (InputStream inputStream = Day05.class.getResourceAsStream(path)) {
            assert inputStream != null;
            return new String(inputStream.readAllBytes()).lines().collect(Collectors.toSet());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    record JoinedPages(List<String> validPages, List<String> invalidPages) {
    }

}
