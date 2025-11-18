package com.cnotar7.projects.aigolfcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayedHoleDTO {
    private int holeNumber;
    private int par;
    private int strokes;
    private int putts;
    private boolean completed;
    private List<ShotDTO> shots = new ArrayList<>();
}
