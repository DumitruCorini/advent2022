package com.example.day8.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ForestManagerServiceTest {

    @Autowired
    ForestManagerService forestManagerService;

    @Test
    void should_create_two_dimensional_list_with_3_rows_and_columns_from_simple_text_block_forest() {
        // GIVEN
        String forest = """
                101
                212
                323
                """;

        List<List<Integer>> expectedCreatedForest = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(1, 0, 1)),
                        new ArrayList<>(Arrays.asList(2, 1, 2)),
                        new ArrayList<>(Arrays.asList(3, 2, 3))
                )
        );

        // WHEN
        List<List<Integer>> actualCreatedForest = forestManagerService.createForestFromTextBlock(forest);

        // THEN
        assertEquals(expectedCreatedForest, actualCreatedForest);
    }

    @ParameterizedTest
    @MethodSource(value = "getForestWithExpectedVisibleTrees")
    void should_detect_visible_trees_in_text_block_forest(String forest, int expectedVisibleTreeCount) {
        // GIVEN
        List<List<Integer>> createdForest = forestManagerService.createForestFromTextBlock(forest);

        // WHEN
        int actualVisibleTreeCount = forestManagerService.getVisibleTreesCount(createdForest);

        // THEN
        assertEquals(expectedVisibleTreeCount, actualVisibleTreeCount);
    }

    @Test
    void should_get_highest_visibility_score_0_for_tree_in_forest_with_single_line_and_column() {
        // GIVEN
        List<List<Integer>> forest = forestManagerService.createForestFromTextBlock("""
                1
                """);
        Integer expectedHighestTreeVisibilityScore = 0;

        // WHEN
        Integer actualHighestTreeVisibilityScore = forestManagerService.getHighestTreeVisibilityScore(forest);

        // THEN
        assertEquals(expectedHighestTreeVisibilityScore, actualHighestTreeVisibilityScore);
    }

    @Test
    void should_get_highest_visibility_score_1_for_tree_in_forest_with_3_lines_and_columns() {
        // GIVEN
        List<List<Integer>> forest = forestManagerService.createForestFromTextBlock("""
                121
                212
                121
                """);
        Integer expectedHighestTreeVisibilityScore = 1;

        // WHEN
        Integer actualHighestTreeVisibilityScore = forestManagerService.getHighestTreeVisibilityScore(forest);

        // THEN
        assertEquals(expectedHighestTreeVisibilityScore, actualHighestTreeVisibilityScore);
    }

    @Test
    void should_get_highest_visibility_score_8_for_tree_in_forest_with_5_lines_and_columns() {
        // GIVEN
        /*
         * Visibility scores for this forest
         * 0 0 0 0 0
         * 0 1 4 1 0
         * 0 6 1 2 0
         * 0 1 8 3 0
         * 0 0 0 0 0
         */
        List<List<Integer>> forest = forestManagerService.createForestFromTextBlock("""
                30373
                25512
                65332
                33549
                35390
                """);
        Integer expectedHighestTreeVisibilityScore = 8;

        // WHEN
        Integer actualHighestTreeVisibilityScore = forestManagerService.getHighestTreeVisibilityScore(forest);

        // THEN
        assertEquals(expectedHighestTreeVisibilityScore, actualHighestTreeVisibilityScore);
    }

    @Test
    void should_get_highest_visibility_score_56_in_forest_with_10_lines_and_5_columns() {
        // GIVEN
        /* Visibility scores for this forest
         * 0 0 0  0 0  0 0  0  0  0
         * 0 1 1  9 8  1 18 1  2  0
         * 0 2 16 1 24 4 1  56 2  0
         * 0 3 1  1 12 1 4  7  24 0
         * 0 0 0  0 0  0 0  0  0  0
         */
        List<List<Integer>> forest = forestManagerService.createForestFromTextBlock("""
                0121123120
                2002323014
                0120320431
                0100201341
                2120213010
                """);
        Integer expectedHighestTreeVisibilityScore = 56;

        // WHEN
        Integer actualHighestTreeVisibilityScore = forestManagerService.getHighestTreeVisibilityScore(forest);

        // THEN
        assertEquals(expectedHighestTreeVisibilityScore, actualHighestTreeVisibilityScore);
    }

    private static Stream<Arguments> getForestWithExpectedVisibleTrees() {
        return Stream.of(
                Arguments.of("""
                        1
                        """, 1),
                Arguments.of("""
                        12
                        21
                        """, 4),
                Arguments.of("""
                        151
                        433
                        161
                        """, 8),
                Arguments.of("""
                        141
                        134
                        141
                        """, 9),
                Arguments.of("""
                        141
                        431
                        141
                        """, 9),
                Arguments.of("""
                        313
                        434
                        434
                        """, 9),
                Arguments.of("""
                        343
                        334
                        514
                        """, 9),
                Arguments.of("""
                        3231
                        2343
                        1243
                        2131
                        """, 16),
                Arguments.of("""
                        30373
                        25512
                        65332
                        33549
                        35390
                        """, 21)
        );
    }
}
