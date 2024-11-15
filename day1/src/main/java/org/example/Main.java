package org.example;

import org.example.service.CalorieFinder;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CalorieFinder calorieFinder = new CalorieFinder();

        try {
            List<Integer> elfRucksackCaloriesFromFile = calorieFinder.getElfRucksackCaloriesFromFile("src/main/resources/input.txt");
            System.out.println(calorieFinder.getSumOfThreeBiggestRucksacksByCalorieContent(elfRucksackCaloriesFromFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}