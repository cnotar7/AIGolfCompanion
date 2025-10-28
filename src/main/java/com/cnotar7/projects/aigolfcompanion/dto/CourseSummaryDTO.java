package com.cnotar7.projects.aigolfcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseSummaryDTO {
    private Long id;
    private String name;
    private String city;
    private String state;
    private String country;
}
