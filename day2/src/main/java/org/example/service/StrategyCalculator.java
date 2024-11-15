package org.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategyCalculator {

    private final Map<String, String> winStrategies;
    private final Map<String, String> drawStrategies;

    public StrategyCalculator() {
        winStrategies = new HashMap<>();
        winStrategies.put("A", "Y");
        winStrategies.put("B", "Z");
        winStrategies.put("C", "X");

        drawStrategies = new HashMap<>();
        drawStrategies.put("A", "X");
        drawStrategies.put("B", "Y");
        drawStrategies.put("C", "Z");
    }

    public String getStrategyToPlayFromRoundStrategy(String roundPredefinedStrategy) {
        return roundPredefinedStrategy.substring(2);
    }

    public String getOpponentStrategy(String roundPredefinedStrategy) {
        return roundPredefinedStrategy.substring(0, 1);
    }

    public int calculateWinningPointsForRound(String ourPlayedStrategy, String opponentPlayedStrategy) {
        String correctWinningStrategy = winStrategies.get(opponentPlayedStrategy);
        if (ourPlayedStrategy.equals(correctWinningStrategy)) {
            return 6;
        }
        String drawStrategy = drawStrategies.get(opponentPlayedStrategy);
        if (drawStrategy.equals(ourPlayedStrategy)) {
            return 3;
        }
        return 0;
    }

    public int getPointsForShape(String playedStrategy) {
        if (playedStrategy.equals("X")) {
            return 1;
        }
        if (playedStrategy.equals("Y")) {
            return 2;
        }
        return 3;
    }

    public int getRoundScore(String round) {
        String ourPlayedStrategy = getStrategyToPlayFromRoundStrategy(round);
        String opponentPlayedStrategy = getOpponentStrategy(round);

        int totalRoundScore = 0;

        totalRoundScore += calculateWinningPointsForRound(ourPlayedStrategy, opponentPlayedStrategy);

        totalRoundScore += getPointsForShape(ourPlayedStrategy);

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
