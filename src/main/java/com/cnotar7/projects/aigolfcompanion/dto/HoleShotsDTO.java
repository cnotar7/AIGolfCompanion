package com.cnotar7.projects.aigolfcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoleShotsDTO {
    private int holeNumber;
    private List<ShotDTO> shots;
}
