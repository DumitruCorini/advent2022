package org.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalorieFinder {
    public List<Integer> getElfRucksackCaloriesFromFile(String fileName) throws IOException {
        List<Integer> elfRucksackCalories = new ArrayList<>();
        List<String> allLines = Files.readAllLines(Path.of(fileName));
        int currentElfRucksackCalories = 0;

        for (String line : allLines) {
            if (line.isBlank()) {
                elfRucksackCalories.add(currentElfRucksackCalories);
                currentElfRucksackCalories = 0;
            } else {
                currentElfRucksackCalories += Integer.parseInt(line);
            }
        }
        return elfRucksackCalories;
    }

    public Integer findMostCalories(List<Integer> elfRucksacks) {
        return Collections.max(elfRucksacks);
    }

    public Integer getSumOfThreeBiggestRucksacksByCalorieContent(List<Integer> elfRucksacks) {
        Collections.sort(elfRucksacks);
        Collections.reverse(elfRucksacks);
        System.out.println(elfRucksacks);

        return elfRucksacks.get(0) + elfRucksacks.get(1) + elfRucksacks.get(2);
    }
}