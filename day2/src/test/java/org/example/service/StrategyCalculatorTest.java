package org.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StrategyCalculatorTest {

    private static final StrategyCalculator strategyCalculator = new StrategyCalculator();

    @ParameterizedTest
    @MethodSource(value = "provideOpponentStrategyStringsForRoundPredefinedStrategy")
    void should_get_opponent_strategy_A_from_round_predefined_strategy_A_Y(String roundPredefinedStrategy, String expectedOpponentStrategy) {
        // GIVEN

        // WHEN
        String actualOpponentStrategy = strategyCalculator.getOpponentShape(roundPredefinedStrategy);

        // THEN
        assertEquals(expectedOpponentStrategy, actualOpponentStrategy);
    }

    @ParameterizedTest
    @MethodSource(value = "provideExpectedStrategiesStringsForRoundPredefinedStrategy")
    void should_get_expected_strategy_to_play_with_round_predefined_strategies(String roundPredefinedStrategy, String expectedStrategyToPlay) {
        // GIVEN

        // WHEN
        String actualStrategyToPlay = strategyCalculator.getStrategyToPlayFromRoundStrategy(roundPredefinedStrategy);

        // THEN
        assertEquals(expectedStrategyToPlay, actualStrategyToPlay);
    }

    @Test
    void should_get_6_points_for_winning_when_using_Z_strategy_against_opponent() {
        // GIVEN
        String ourPlayedStrategy = "Z";

        // WHEN
        int expectedPointsForWinning = 6;
        int actualPointsForWinning = strategyCalculator.calculateWinningPointsForRound(ourPlayedStrategy);

        // THEN
        assertEquals(expectedPointsForWinning, actualPointsForWinning);
    }

    @Test
    void should_get_3_points_for_draw_when_using_Y_strategy_against_opponent() {
        // GIVEN
        String ourPlayedStrategy = "Y";

        // WHEN
        int expectedPointsForDraw = 3;
        int actualPointsForDraw = strategyCalculator.calculateWinningPointsForRound(ourPlayedStrategy);

        // THEN
        assertEquals(expectedPointsForDraw, actualPointsForDraw);
    }

    @Test
    void should_get_0_points_for_loss_when_using_X_strategy_against_opponent() {
        // GIVEN
        String ourPlayedStrategy = "X";

        // WHEN
        int expectedPointsForLoss = 0;
        int actualPointsForLoss = strategyCalculator.calculateWinningPointsForRound(ourPlayedStrategy);

        // THEN
        assertEquals(expectedPointsForLoss, actualPointsForLoss);
    }

    @ParameterizedTest
    @MethodSource(value = "getWinningShapesToPlayDependingOnOpponentShape")
    void should_get_shape_to_play_when_we_have_to_win_following_Z_round_strategy_depending_on_opponent_shape(String expectedShapeToPlay, String opponentShape) {
        // GIVEN
        String roundStrategy = "Z";

        // WHEN
        String actualShapeToPlay = strategyCalculator.getShapeToPlayFromRoundStrategy(roundStrategy, opponentShape);

        // THEN
        assertEquals(expectedShapeToPlay, actualShapeToPlay);
    }

    @ParameterizedTest
    @MethodSource(value = "getOpponentShapes")
    void should_mirror_opponent_shape_when_following_draw_round_strategy_Y(String opponentShape) {
        // GIVEN
        String roundStrategy = "Y";

        // WHEN
        String actualShapeToPlay = strategyCalculator.getShapeToPlayFromRoundStrategy(roundStrategy, opponentShape);

        // THEN
        assertEquals(opponentShape, actualShapeToPlay);
    }

    @ParameterizedTest
    @MethodSource(value = "getLosingShapesToPlayDependingOnOpponentShape")
    void should_get_shape_to_play_when_following_lose_round_strategy_X_depending_on_opponent_shape(String expectedShapeToPlay, String opponentShape) {
        // GIVEN
        String roundStrategy = "X";

        // WHEN
        String actualShapeToPlay = strategyCalculator.getShapeToPlayFromRoundStrategy(roundStrategy, opponentShape);

        // THEN
        assertEquals(expectedShapeToPlay, actualShapeToPlay);
    }

    @ParameterizedTest
    @MethodSource(value = "getPossiblePointsForShape")
    void should_get_expected_points_for_shape_depending_on_shape(String ourPickedStrategy, int expectedPointsForShape) {
        // GIVEN

        // WHEN
        int actualPointsForShape = strategyCalculator.getPointsForShape(ourPickedStrategy);

        // THEN
        assertEquals(expectedPointsForShape, actualPointsForShape);
    }

    @ParameterizedTest
    @MethodSource(value = "getRoundPoints")
    void should_get_total_points_for_round_when_using_round_strategies(String round, int expectedRoundTotalPoints) {
        // GIVEN

        // WHEN
        int actualRoundTotalPoints = strategyCalculator.getRoundScore(round);

        // THEN
        assertEquals(expectedRoundTotalPoints, actualRoundTotalPoints);
    }

    @Test
    void should_get_12_score_for_entire_tournament_read_from_file() {
        // GIVEN
        List<String> linesFromFile = FileUtils.getLinesFromFile("src/test/resources/inputTest.txt");

        // WHEN
        int expectedTournamentTotalScore = 12;
        int actualTournamentTotalScore = strategyCalculator.getTournamentScore(linesFromFile);

        // THEN
        assertEquals(expectedTournamentTotalScore, actualTournamentTotalScore);
    }

    private static Stream<Arguments> provideExpectedStrategiesStringsForRoundPredefinedStrategy() {
        return Stream.of(
                Arguments.of("A Y", "Y"),
                Arguments.of("B X", "X"),
                Arguments.of("C Z", "Z")
        );
    }

    private static Stream<Arguments> getWinningShapesToPlayDependingOnOpponentShape() {
        return Stream.of(
                Arguments.of("A", "C"),
                Arguments.of("B", "A"),
                Arguments.of("C", "B")
        );
    }

    private static Stream<Arguments> getOpponentShapes() {
        return Stream.of(
                Arguments.of("A"),
                Arguments.of("B"),
                Arguments.of("C")
        );
    }

    private static Stream<Arguments> getLosingShapesToPlayDependingOnOpponentShape() {
        return Stream.of(
                Arguments.of("C", "A"),
                Arguments.of("A", "B"),
                Arguments.of("B", "C")
        );
    }

    private static Stream<Arguments> provideOpponentStrategyStringsForRoundPredefinedStrategy() {
        return Stream.of(
                Arguments.of("A Y", "A"),
                Arguments.of("B X", "B"),
                Arguments.of("C Z", "C")
        );
    }

    private static Stream<Arguments> getPossiblePointsForShape() {
        return Stream.of(
                Arguments.of("A", 1),
                Arguments.of("B", 2),
                Arguments.of("C", 3)
        );
    }

    private static Stream<Arguments> getRoundPoints() {
        return Stream.of(
                Arguments.of("A Y", 4),
                Arguments.of("B X", 1),
                Arguments.of("C Z", 7)
        );
    }
}