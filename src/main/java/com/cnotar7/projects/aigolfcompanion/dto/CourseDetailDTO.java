package com.cnotar7.projects.aigolfcompanion.dto;

import java.util.List;
import lombok.Data;

@Data
public class CourseDetailDTO {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String country;
    private double latitude;
    private double longitude;
    private List<TeeDTO> tees;
}

@Data
class TeeDTO {
    private String teeName;
    private double courseRating;
    private double slopeRating;
    private int totalYards;
    private int parTotal;
    private List<HoleDTO> holes;
}

@Data
class HoleDTO {
    private int par;
    private int yardage;
    private int handicap;
}

