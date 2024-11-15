package org.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StrategySelectorTest {

    private static final StrategySelector strategySelector = new StrategySelector();

    @ParameterizedTest
    @MethodSource(value = "provideStringsForRoundPredefinedStrategy")
    void should_get_expected_strategy_to_play_with_round_predefined_strategies(String roundPredefinedStrategy, String expectedStrategyToPlay) {
        // GIVEN

        // WHEN
        String actualStrategyToPlay = strategySelector.getStrategyToPlayFromRoundStrategy(roundPredefinedStrategy);

        // THEN
        assertEquals(expectedStrategyToPlay, actualStrategyToPlay);
    }

    private static Stream<Arguments> provideStringsForRoundPredefinedStrategy() {
        return Stream.of(
                Arguments.of("A Y", "Y"),
                Arguments.of("B X", "X"),
                Arguments.of("C Z", "Z")
        );
    }
}