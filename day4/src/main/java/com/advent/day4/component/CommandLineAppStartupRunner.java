package com.advent.day4.component;

import com.advent.day4.service.AssignmentSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    AssignmentSectionService assignmentSectionService;

    @Override
    public void run(String... args) {
        List<String> assignmentPairs = assignmentSectionService.getAssignmentPairsFromFile("input.txt");

        int assignmentPairsWithInclusion = assignmentSectionService.getTotalNumberOfAssignmentsWithSectionInclusions(assignmentPairs);

        System.out.println("Total number of assignments with inclusions: " + assignmentPairsWithInclusion);

        int assignmentsOverlap = assignmentSectionService.getTotalNumberOfAssignmentsOverlap(assignmentPairs);

        System.out.println("Total number of assignments with overlap: " + assignmentsOverlap);
    }
}
