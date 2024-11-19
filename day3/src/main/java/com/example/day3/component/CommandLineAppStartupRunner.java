package com.example.day3.component;

import com.example.day3.service.RucksackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    RucksackService rucksackService;

    @Override
    public void run(String... args) {
        int totalDuplicatedItemPriority = rucksackService.getTotalDuplicatedItemPriority(rucksackService.getRucksacksFromFile("input.txt"));
        System.out.println("Total dup item prio: " + totalDuplicatedItemPriority);

        List<String> rucksacks = rucksackService.getRucksacksFromFile("input.txt");
        List<List<String>> groupedRucksacks = rucksackService.getGroupedRucksacks(rucksacks);
        Integer badgePrioritySum = rucksackService.getBadgePrioritySum(groupedRucksacks);

        System.out.println("Total badge prio: " + badgePrioritySum);
    }
}
