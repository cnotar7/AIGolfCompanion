package com.cnotar7.projects.aigolfcompanion.util;

import com.cnotar7.projects.aigolfcompanion.dto.PlayerStatsDTO;
import com.cnotar7.projects.aigolfcompanion.model.PlayedHole;
import com.cnotar7.projects.aigolfcompanion.model.Round;
import com.cnotar7.projects.aigolfcompanion.repository.RoundRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class PlayerStatsCalculator {

    private RoundRepository roundRepository;

    private static int MIN_ROUNDS_FOR_STATS = 5;
    private static int NUM_LOWEST_ROUNDS_USED_FOR_HANDICAP = 5;

    public PlayerStatsDTO calculatePlayerStats(Long userId) {
        List<Round> rounds = roundRepository.findAllByUserId(userId);

        if (rounds.size() < MIN_ROUNDS_FOR_STATS) {
            return null;
        }

        int totalRounds = rounds.size();
        int totalScore = 0;
        int totalPutts = 0;
        int par3Score = 0;
        int par4Score = 0;
        int par5Score = 0;
        int numPar3s = 0;
        int numPar4s = 0;
        int numPar5s = 0;
        List<Double> differentials = new ArrayList<>();


        for (Round round : rounds) {
            int roundScore = 0;
            for (Map.Entry<Integer, PlayedHole> entry : round.getHoles().entrySet()) {
                PlayedHole hole = entry.getValue();
                roundScore += hole.getStrokes();
                totalPutts += hole.getPutts();
                if (hole.getPar() == 3) {
                    numPar3s += 1;
                    par3Score += hole.getStrokes();
                } else if (hole.getPar() == 4) {
                    numPar4s += 1;
                    par4Score += hole.getStrokes();
                } else if (hole.getPar() == 5) {
                    numPar5s += 1;
                    par5Score += hole.getStrokes();
                }
            }
            differentials.add(calculateRoundDifferential(round, roundScore));
            totalRounds += roundScore;
        }

        double averageScore = totalScore / (double) totalRounds;
        double averagePutts = totalPutts / (double) totalRounds;
        double averagePar3Score = par3Score / (double) numPar3s;
        double averagePar4Score = par4Score / (double) numPar4s;
        double averagePar5Score = par5Score / (double) numPar5s;
        Map<Integer, Double> averageScorePerHole = new HashMap<>();
        averageScorePerHole.put(3, averagePar3Score);
        averageScorePerHole.put(4, averagePar4Score);
        averageScorePerHole.put(5, averagePar5Score);

        Collections.sort(differentials);
        int count = Math.min(differentials.size(), NUM_LOWEST_ROUNDS_USED_FOR_HANDICAP);
        OptionalDouble handicapEstimate = differentials.stream()
                .limit(count)
                .mapToDouble(Double::doubleValue)
                .average();

        return PlayerStatsDTO.builder()
                .averageScore(averageScore)
                .averagePutts(averagePutts)
                .averageScoreByHole(averageScorePerHole)
                .handicapEstimate(handicapEstimate.isPresent() ? handicapEstimate.getAsDouble() : null)
                .build();

    }

    public double calculateRoundDifferential(Round round, int score) {
        double courseRating = round.getSelectedTee().getCourseRating();
        double slopeRating = round.getSelectedTee().getSlopeRating();

        return (score - courseRating) / (slopeRating - score);
    }
}
