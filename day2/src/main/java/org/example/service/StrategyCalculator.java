package org.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategyCalculator {

    // Shapes to play depending on opponent played shape (opponentPlayedShape, ourShapeToPlay)
    private final Map<String, String> winStrategies;
    private final Map<String, String> losingStrategies;

    public StrategyCalculator() {
        winStrategies = new HashMap<>();
        winStrategies.put("A", "B");
        winStrategies.put("B", "C");
        winStrategies.put("C", "A");

        losingStrategies = new HashMap<>();
        losingStrategies.put("A", "C");
        losingStrategies.put("B", "A");
        losingStrategies.put("C", "B");
    }

    public String getStrategyToPlayFromRoundStrategy(String roundPredefinedStrategy) {
        return roundPredefinedStrategy.substring(2);
    }

    public String getOpponentShape(String roundPredefinedStrategy) {
        return roundPredefinedStrategy.substring(0, 1);
    }

    public int calculateWinningPointsForRound(String ourPlayedStrategy) {
        // We will win
        if (ourPlayedStrategy.equals("Z")) {
            return 6;
        }
        // We will draw
        if (ourPlayedStrategy.equals("Y")) {
            return 3;
        }
        // We will lose
        return 0;
    }

    public String getShapeToPlayFromRoundStrategy(String roundStrategy, String opponentPlayedShape) {
        if (roundStrategy.equals("Z")) {
            return winStrategies.get(opponentPlayedShape);
        }

        if (roundStrategy.equals("X")) {
            return losingStrategies.get(opponentPlayedShape);
        }

        // If draw, we use the same shape as the opponent
        return opponentPlayedShape;
    }

    public int getPointsForShape(String playedStrategy) {
        if (playedStrategy.equals("A")) {
            return 1;
        }
        if (playedStrategy.equals("B")) {
            return 2;
        }
        return 3;
    }

    public int getRoundScore(String round) {
        String ourPlayedStrategy = getStrategyToPlayFromRoundStrategy(round);
        String opponentPlayedShape = getOpponentShape(round);
        String ourPlayedShapeFromStrategy = getShapeToPlayFromRoundStrategy(ourPlayedStrategy, opponentPlayedShape);

        int totalRoundScore = 0;

        totalRoundScore += calculateWinningPointsForRound(ourPlayedStrategy);

        totalRoundScore += getPointsForShape(ourPlayedShapeFromStrategy);

        return totalRoundScore;
    }

    public int getTournamentScore(List<String> roundsFromFile) {
        int tournamentScore = 0;

        for (String round : roundsFromFile) {
            tournamentScore += getRoundScore(round);
        }

        return tournamentScore;
    }
}
