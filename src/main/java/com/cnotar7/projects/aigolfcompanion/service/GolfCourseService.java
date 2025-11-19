package com.cnotar7.projects.aigolfcompanion.service;

import com.cnotar7.projects.aigolfcompanion.converter.GolfCourseObjectConverter;
import com.cnotar7.projects.aigolfcompanion.dto.CourseDetailDTO;
import com.cnotar7.projects.aigolfcompanion.model.Course;
import com.cnotar7.projects.aigolfcompanion.repository.CourseRepository;
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
    private final CourseRepository courseRepository;
    private final GolfCourseObjectConverter golfCourseObjectConverter;

    public List<CourseSummaryDTO> searchGolfCourse(String query) {
        List<ExternalCourse> courses = golfCourseAPIClient.searchGolfCourse(query);

        for (ExternalCourse course : courses) {
            System.out.println("Club Name = " + course.getClub_name() + ", ID = " + course.getId());
        }

        return courses.stream()
                .map(golfCourseObjectConverter::mapExternalCoursetoDTO)
                .toList();
    }

    public CourseDetailDTO getGolfCourseById(Long id) {
        Course course = courseRepository.findById(id).orElse(null);

        // if course is not already stored, call golf course api for it
        if (course == null) {
            System.out.println("Golf course with id = " + id + " not found");
            ExternalCourse externalCourse = golfCourseAPIClient.getGolfCourseById(id);
            if (externalCourse != null) {
                course = golfCourseObjectConverter.mapExternalCourseToEntity(externalCourse);
                courseRepository.save(course);
            } else {
                return null;
            }

        }

        return golfCourseObjectConverter.mapCourseEntityToDTO(course);
    }

}
