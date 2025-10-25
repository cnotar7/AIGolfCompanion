package com.cnotar7.projects.aigolfcompanion.dto.external;

import lombok.Data;

import java.util.List;

@Data
public class ExternalCourseSearchResponse {
    private List<ExternalCourse> courses;
}
