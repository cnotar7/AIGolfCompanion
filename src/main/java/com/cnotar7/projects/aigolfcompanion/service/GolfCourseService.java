package com.cnotar7.projects.aigolfcompanion.service;

import com.cnotar7.projects.aigolfcompanion.service.api.GolfCourseAPIClient;
import com.cnotar7.projects.aigolfcompanion.dto.CourseSummaryDTO;
import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GolfCourseService {

    private final GolfCourseAPIClient golfCourseAPIClient;

    public List<CourseSummaryDTO> searchGolfCourse(String query) {
        List<ExternalCourse> courses = golfCourseAPIClient.searchGolfCourse(query);

        return courses.stream()
                .map(course -> new CourseSummaryDTO(
                        course.getId(),
                        course.getCourse_name(),
                        course.getLocation().getCity(),
                        course.getLocation().getState(),
                        course.getLocation().getCountry()

                )).toList();
    }
}
