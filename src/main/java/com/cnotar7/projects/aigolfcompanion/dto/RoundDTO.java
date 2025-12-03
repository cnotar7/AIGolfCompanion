package com.cnotar7.projects.aigolfcompanion.dto;

import com.cnotar7.projects.aigolfcompanion.model.PlayedHole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoundDTO {
    private Long roundId;
    private LocalDateTime startTime;
    private int currentHoleNumber;
    private Long courseId;
    private Long teeId;
    private String userName;
    private Map<Integer, PlayedHoleDTO> holes = new HashMap<>();
    private String aiSummary;

    private boolean completed;
}
