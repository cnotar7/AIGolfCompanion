package com.cnotar7.projects.aigolfcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerStatsDTO {
    private Integer totalRounds;
    private Double averageScore;
    private Double averagePutts;
    private Map<Integer, Double> averageScoreByHole;
    private Double handicapEstimate;
}
