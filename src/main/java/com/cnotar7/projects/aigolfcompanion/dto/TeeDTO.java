package com.cnotar7.projects.aigolfcompanion.dto;

import com.cnotar7.projects.aigolfcompanion.enums.Gender;
import lombok.Data;

import java.util.List;

@Data
public class TeeDTO {
    private String teeName;
    private double courseRating;
    private double slopeRating;
    private int totalYards;
    private int parTotal;
    private Gender gender;
    private List<HoleDTO> holes;
}