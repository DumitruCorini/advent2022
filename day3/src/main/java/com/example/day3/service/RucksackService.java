package com.example.day3.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RucksackService {

    // Split in half and fill compartments
    public List<String> getCompartmentsFromRucksack(String rucksackInput) {
        int compartmentSize = rucksackInput.length() / 2;
        List<String> compartments = new ArrayList<>();
        compartments.add(rucksackInput.substring(0, compartmentSize));
        compartments.add(rucksackInput.substring(compartmentSize));

        return compartments;
    }

    public Character getDuplicatedItem(String rucksack) {
        List<String> compartments = getCompartmentsFromRucksack(rucksack);
        String firstCompartment = compartments.get(0);
        String secondCompartment = compartments.get(1);

        for (char item : firstCompartment.toCharArray()) {
            if (secondCompartment.indexOf(item) > -1) {
                return item;
            }
        }

        return null;
    }

    public int getPriorityForItem(Character item) {
        // Lowercases start at 1
        if (!Character.isUpperCase(item)) {
            return item - 'a' + 1;
        }
        // Uppercases start at 27
        return item - 'A' + 27;
    }

    public List<String> getRucksacksFromFile(String fileName) {
        List<String> rucksacks;
        Resource file = new ClassPathResource(fileName);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getFile()));
            rucksacks = reader.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rucksacks;
    }

    public int getTotalDuplicatedItemPriority(List<String> rucksacks) {
        int totalItemPriorities = 0;
        for (String rucksack : rucksacks) {
            Character duplicatedItem = getDuplicatedItem(rucksack);
            int priorityForItem = getPriorityForItem(duplicatedItem);
            totalItemPriorities += priorityForItem;
        }

        return totalItemPriorities;
    }
}