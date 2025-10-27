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





