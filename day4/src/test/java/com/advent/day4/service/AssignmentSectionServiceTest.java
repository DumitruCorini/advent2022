package com.advent.day4.service;

import com.advent.day4.model.Section;
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
public class AssignmentSectionServiceTest {

    @Autowired
    AssignmentSectionService assignmentSectionService;

    @Test
    void should_create_two_section_with_start_3_end_7_and_start_2_end_6_from_section_3_7_2_6() {
        // GIVEN
        String sections = "3-7,2-6";

        // WHEN
        Section expectedBuiltFirstSection = Section.builder().start(3).end(7).build();
        Section expectedBuiltSecondSection = Section.builder().start(2).end(6).build();
        List<Section> actualBuiltSection = assignmentSectionService.buildSectionsPair(sections);

        // THEN
        assertEquals(expectedBuiltFirstSection, actualBuiltSection.get(0));
        assertEquals(expectedBuiltSecondSection, actualBuiltSection.get(1));
    }

    @ParameterizedTest
    @MethodSource(value = "getSectionInclusionData")
    void should_detect_two_sections_inclusion_from_single_string(String sectionsString, boolean expectedSectionInclusion) {
        // GIVEN
        List<Section> buildSections = assignmentSectionService.buildSectionsPair(sectionsString);

        // WHEN
        boolean actualSectionInclusion = assignmentSectionService.isSectionInclusion(buildSections.get(0), buildSections.get(1));

        // THEN
        assertEquals(expectedSectionInclusion, actualSectionInclusion);
    }

    @Test
    void should_detect_total_number_of_assignments_with_section_inclusions_5_from_file() {
        // GIVEN
        List<String> assignmentPairs = assignmentSectionService.getAssignmentPairsFromFile("inputTest.txt");

        // WHEN
        Integer expectedNumberOfInclusions = 5;
        Integer actualNumberOfInclusions = assignmentSectionService.getTotalNumberOfAssignmentsWithSectionInclusions(assignmentPairs);

        // THEN
        assertEquals(expectedNumberOfInclusions, actualNumberOfInclusions);
    }

    private static Stream<Arguments> getSectionInclusionData() {
        return Stream.of(
                Arguments.of("3-5,2-6", true),
                Arguments.of("2-5,3-4", true),
                Arguments.of("2-3,2-3", true),
                Arguments.of("2-8,3-7", true),
                Arguments.of("6-6,4-6", true),
                Arguments.of("2-3,4-5", false),
                Arguments.of("2-4,6-8", false),
                Arguments.of("5-7,7-9", false),
                Arguments.of("2-6,4-8", false),
                Arguments.of("3-5,4-6", false)
        );
    }
}
