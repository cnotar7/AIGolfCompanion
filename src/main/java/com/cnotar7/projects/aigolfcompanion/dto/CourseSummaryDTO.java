package com.cnotar7.projects.aigolfcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSummaryDTO {
    private Long id;
    private String name;
    private String city;
    private String state;
    private String country;
}
