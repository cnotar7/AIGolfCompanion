package com.cnotar7.projects.aigolfcompanion.util;

import com.cnotar7.projects.aigolfcompanion.model.Round;
import org.springframework.stereotype.Component;

@Component
public class SummaryPromptBuilder {

    public String buildPrompt(Round round) {
        StringBuilder sb = new StringBuilder();

        sb.append("You are an expert golf coach. I will provide you with a Course, including" +
                " a yardage, slope, course rating and a description all of the holes, as well as each shot on each hole." +
                " Summarize this round and provide the following:\n")
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
        round.getHoles().forEach((holeNumber, hole) -> {
            sb.append("Hole #").append(holeNumber)
                    .append(" (Par ").append(hole.getPar()).append(", ")
                    .append(hole.getYardage()).append(" yards, ")
                    .append("Handicap ").append(hole.getHandicap())
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
