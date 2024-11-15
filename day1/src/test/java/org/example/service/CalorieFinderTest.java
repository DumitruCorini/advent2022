package org.example.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalorieFinderTest {
    
    @Test
    void should_get_10000_as_max_rucksack_calories_count() {
        // GIVEN
        CalorieFinder calorieFinder = new CalorieFinder();
        List<Integer> rucksacksCalories = List.of(10000, 2000);

        // WHEN
        Integer expectedMostCalories = 10000;
        Integer actualMostCalories = calorieFinder.findMostCalories(rucksacksCalories);
        
        // THEN
        assertEquals(expectedMostCalories, actualMostCalories);
    }

    @Test
    void should_get_68171_as_most_calories() throws IOException {
        // GIVEN
        CalorieFinder calorieFinder = new CalorieFinder();
        List<Integer> elfRucksackCalories = calorieFinder.getElfRucksackCaloriesFromFile("src/test/resources/inputTest.txt");

        // WHEN
        Integer expectedMostCalories = 68171;
        Integer actualMostCalories = calorieFinder.findMostCalories(elfRucksackCalories);

        // THEN
        assertEquals(expectedMostCalories, actualMostCalories);
    }

    @Test
    void should_get_calories_count_from_three_biggest_rucksacks() throws IOException {
        // GIVEN
        CalorieFinder calorieFinder = new CalorieFinder();
        List<Integer> elfRucksackCalories = calorieFinder.getElfRucksackCaloriesFromFile("src/test/resources/inputTest.txt");

        // WHEN
        Integer expectedMostCalories = 122801;
        Integer actualMostCalories = calorieFinder.getSumOfThreeBiggestRucksacksByCalorieContent(elfRucksackCalories);

        // THEN
        assertEquals(expectedMostCalories, actualMostCalories);
    }
}

