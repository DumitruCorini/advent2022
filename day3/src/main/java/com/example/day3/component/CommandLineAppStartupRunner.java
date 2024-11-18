package com.example.day3.component;

import com.example.day3.service.RucksackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    RucksackService rucksackService;

    @Override
    public void run(String... args) {
        int totalDuplicatedItemPriority = rucksackService.getTotalDuplicatedItemPriority(rucksackService.getRucksacksFromFile("inputPart1.txt"));

        System.out.println("Total dup item prio: " + totalDuplicatedItemPriority);
    }
}
