package com.cnotar7.projects.aigolfcompanion.util;

import com.cnotar7.projects.aigolfcompanion.dto.PlayerStatsDTO;
import com.cnotar7.projects.aigolfcompanion.model.Round;
import org.springframework.stereotype.Component;

@Component
public class SummaryPromptBuilder {

    public String buildPrompt(Round round, PlayerStatsDTO playerStatsDTO ) {
        StringBuilder sb = new StringBuilder();

        sb.append("You are an expert golf coach. I will provide you with a completed golf round, including" +
                " the course name, yardage, slope, course rating");
        if (playerStatsDTO != null) {
            sb.append(", and the player's stats from previous rounds");
        }

        sb.append(". I will also provide a description of each shot on each hole." +
                " Summarize player's round and provide the following:\n")
                .append("- Overall Strengths\n")
                .append("- Overall Weaknesses\n")
                .append("- Shot patterns with Driver\n")
                .append("- Shot patterns with Irons\n")
                .append("- Shot patterns with Wedges\n")
                .append("- Best ways to improve\n\n");

        sb.append("Course: ").append(round.getCourse().getName()).append("\n");
        sb.append("Yardage: ").append(round.getSelectedTee().getTotalYards()).append("\n");
        sb.append("Course Rating: ").append(round.getSelectedTee().getCourseRating()).append("\n");
        sb.append("Slope Rating: ").append(round.getSelectedTee().getSlopeRating()).append("\n");

        if (playerStatsDTO != null) {
            sb.append("Player Stats:\n");
            sb.append("- Average Score: ").append(playerStatsDTO.getAverageScore()).append("\n");
            sb.append("- Average Putts: ").append(playerStatsDTO.getAveragePutts()).append("\n");
            sb.append("- Average Par 3 Score: ").append(playerStatsDTO.getAverageScoreByHole().get(3)).append("\n");
            sb.append("- Average Par 4 Score: ").append(playerStatsDTO.getAverageScoreByHole().get(4)).append("\n");
            sb.append("- Average Par 5 Score: ").append(playerStatsDTO.getAverageScoreByHole().get(5)).append("\n");
            if (playerStatsDTO.getAveragePutts() != null) {
                sb.append("- Handicap Estimate: ").append(playerStatsDTO.getHandicapEstimate()).append("\n");
            }
        }

        round.getHoles().forEach((holeNumber, hole) -> {
            sb.append("Hole #").append(holeNumber)
                    .append(" (Par ").append(hole.getPar()).append(", ")
                    .append(hole.getYardage()).append(" yards, ")
                    .append("Handicap ").append(hole.getHandicap()).append("\n")
                    .append("Shots: ").append("\n");

            hole.getShots().forEach(shot -> {
                sb.append("- ").append(shot.getShotNumber())
                        .append(": ").append(shot.getClub())
                        .append(", result: ").append(shot.getResult()).append("\n");

            });
            sb.append("\n");
        });
        return sb.toString();
    }
}
