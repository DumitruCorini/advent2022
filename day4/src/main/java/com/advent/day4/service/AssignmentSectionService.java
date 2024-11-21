package com.advent.day4.service;

import com.advent.day4.model.Section;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssignmentSectionService {

    public List<Section> buildSectionsPair(String sections) {
        List<Section> builtSections = new ArrayList<>();
        String[] sectionsList = sections.split(",");

        for (String section : sectionsList) {
            String[] splitSection = section.split("-");

            builtSections.add(Section.builder().start(Integer.valueOf(splitSection[0])).end(Integer.valueOf(splitSection[1])).build());
        }

        return builtSections;
    }

    public boolean isSectionInclusion(Section firstSection, Section secondSection) {
        return (secondSection.getStart() <= firstSection.getStart() && firstSection.getEnd() <= secondSection.getEnd())
                || (firstSection.getStart() <= secondSection.getStart() && secondSection.getEnd() <= firstSection.getEnd());
    }

    public List<String> getAssignmentPairsFromFile(String fileName) {
        List<String> assignments;
        Resource file = new ClassPathResource(fileName);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getFile()));
            assignments = reader.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return assignments;
    }

    public Integer getTotalNumberOfAssignmentsWithSectionInclusions(List<String> assignmentPairs) {
        int assignmentsWithSectionInclusions = 0;

        for (String assignmentPair : assignmentPairs) {
            List<Section> assignmentSections = buildSectionsPair(assignmentPair);
            if (isSectionInclusion(assignmentSections.get(0), assignmentSections.get(1))) {
                assignmentsWithSectionInclusions++;
            }
        }

        return assignmentsWithSectionInclusions;
    }
}
