package com.cnotar7.projects.aigolfcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoundResponseDTO {
    private Long roundId;
    private Long courseId;
    private String courseName;
    private String userName;
    private int currentHoleNumber;
    private LocalDateTime startTime;
    private boolean completed;
}
