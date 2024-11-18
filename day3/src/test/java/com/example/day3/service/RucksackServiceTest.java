package com.example.day3.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RucksackServiceTest {

    @Autowired
    private RucksackService rucksackService;

    @ParameterizedTest
    @MethodSource(value = "getRucksacks")
    void should_get_two_compartments_in_rucksack(String rucksack, String firstCompartment, String secondCompartment) {
        // GIVEN

        // WHEN
        List<String> compartments = rucksackService.getCompartmentsFromRucksack(rucksack);

        // THEN
        assertEquals(firstCompartment, compartments.get(0));
        assertEquals(secondCompartment, compartments.get(1));
    }

    @ParameterizedTest
    @MethodSource(value = "getRucksacksWithDuplicatedItems")
    void should_detect_duplicated_item_P_for_rucksack_aPbP(String rucksack, Character expectedDuplicatedItem) {
        // GIVEN

        // WHEN
        Character actualDuplicatedItem = rucksackService.getDuplicatedItem(rucksack);

        // THEN
        assertEquals(expectedDuplicatedItem, actualDuplicatedItem);
    }

    @ParameterizedTest
    @MethodSource(value = "getItemsWithPriorities")
    void should_get_priority_from_item(int expectedPriority, Character item) {
        // GIVEN

        // WHEN
        int actualPriority = rucksackService.getPriorityForItem(item);

        // THEN
        assertEquals(expectedPriority, actualPriority);
    }

    @Test
    void should_get_total_duplicated_item_priority_157_from_file() {
        // GIVEN
        List<String> rucksacks = rucksackService.getRucksacksFromFile("inputTest.txt");

        // WHEN
        int expectedDuplicatedItemPriority = 157;
        int actualDuplicatedItemPriority = rucksackService.getTotalDuplicatedItemPriority(rucksacks);

        // THEN
        assertEquals(expectedDuplicatedItemPriority, actualDuplicatedItemPriority);
    }

    private static Stream<Arguments> getRucksacks() {
        return Stream.of(
                Arguments.of("vJrwpWtwJgWrhcsFMMfFFhFp", "vJrwpWtwJgWr", "hcsFMMfFFhFp"),
                Arguments.of("jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL", "jqHRNqRjqzjGDLGL", "rsFMfFZSrLrFZsSL"),
                Arguments.of("PmmdzqPrVvPwwTWBwg", "PmmdzqPrV", "vPwwTWBwg"),
                Arguments.of("wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", "wMqvLMZHhHMvwLH", "jbvcjnnSBnvTQFn"),
                Arguments.of("ttgJtRGJQctTZtZT", "ttgJtRGJ", "QctTZtZT"),
                Arguments.of("CrZsJsPPZsGzwwsLwLmpwMDw", "CrZsJsPPZsGz", "wwsLwLmpwMDw")
        );
    }

    private static Stream<Arguments> getRucksacksWithDuplicatedItems() {
        return Stream.of(
                Arguments.of("vJrwpWtwJgWrhcsFMMfFFhFp", 'p'),
                Arguments.of("jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL", 'L'),
                Arguments.of("PmmdzqPrVvPwwTWBwg", 'P'),
                Arguments.of("wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", 'v'),
                Arguments.of("ttgJtRGJQctTZtZT", 't'),
                Arguments.of("CrZsJsPPZsGzwwsLwLmpwMDw", 's')
        );
    }

    private static Stream<Arguments> getItemsWithPriorities() {
        return Stream.of(
                Arguments.of(16, 'p'),
                Arguments.of(38, 'L'),
                Arguments.of(42, 'P'),
                Arguments.of(22, 'v'),
                Arguments.of(20, 't'),
                Arguments.of(19, 's')
        );
    }
}
