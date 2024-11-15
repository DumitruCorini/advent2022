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
        String actualOpponentStrategy = strategyCalculator.getOpponentStrategy(roundPredefinedStrategy);

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

    @ParameterizedTest
    @MethodSource(value = "getWinningStrategies")
    void should_get_6_points_for_winning_when_using_winning_strategy_against_opponent(String ourPlayedStrategy, String opponentPlayedStrategy) {
        // GIVEN

        // WHEN
        int expectedPointsForWinning = 6;
        int actualPointsForWinning = strategyCalculator.calculateWinningPointsForRound(ourPlayedStrategy, opponentPlayedStrategy);

        // THEN
        assertEquals(expectedPointsForWinning, actualPointsForWinning);
    }

    @ParameterizedTest
    @MethodSource(value = "getDrawStrategies")
    void should_get_3_points_for_draw_when_using_draw_strategy_against_opponent(String ourPlayedStrategy, String opponentPlayedStrategy) {
        // GIVEN

        // WHEN
        int expectedPointsForDraw = 3;
        int actualPointsForDraw = strategyCalculator.calculateWinningPointsForRound(ourPlayedStrategy, opponentPlayedStrategy);

        // THEN
        assertEquals(expectedPointsForDraw, actualPointsForDraw);
    }

    @ParameterizedTest
    @MethodSource(value = "getLosingStrategies")
    void should_get_0_points_for_loss_when_using_losing_strategy_against_opponent(String ourPlayedStrategy, String opponentPlayedStrategy) {
        // GIVEN

        // WHEN
        int expectedPointsForLoss = 0;
        int actualPointsForLoss = strategyCalculator.calculateWinningPointsForRound(ourPlayedStrategy, opponentPlayedStrategy);

        // THEN
        assertEquals(expectedPointsForLoss, actualPointsForLoss);
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
    void should_get_15_score_for_entire_tournament_read_from_file() {
        // GIVEN
        List<String> linesFromFile = FileUtils.getLinesFromFile("src/test/resources/inputTest.txt");

        // WHEN
        int expectedTournamentTotalScore = 15;
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

    private static Stream<Arguments> provideOpponentStrategyStringsForRoundPredefinedStrategy() {
        return Stream.of(
                Arguments.of("A Y", "A"),
                Arguments.of("B X", "B"),
                Arguments.of("C Z", "C")
        );
    }

    private static Stream<Arguments> getWinningStrategies() {
        return Stream.of(
                Arguments.of("Y", "A"),
                Arguments.of("Z", "B"),
                Arguments.of("X", "C")
        );
    }

    private static Stream<Arguments> getDrawStrategies() {
        return Stream.of(
                Arguments.of("X", "A"),
                Arguments.of("Y", "B"),
                Arguments.of("Z", "C")
        );
    }

    private static Stream<Arguments> getLosingStrategies() {
        return Stream.of(
                Arguments.of("X", "B"),
                Arguments.of("Y", "C"),
                Arguments.of("Z", "A")
        );
    }

    private static Stream<Arguments> getPossiblePointsForShape() {
        return Stream.of(
                Arguments.of("X", 1),
                Arguments.of("Y", 2),
                Arguments.of("Z", 3)
        );
    }

    private static Stream<Arguments> getRoundPoints() {
        return Stream.of(
                Arguments.of("A Y", 8),
                Arguments.of("B X", 1),
                Arguments.of("C Z", 6)
        );
    }
}