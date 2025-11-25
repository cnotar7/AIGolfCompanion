package com.cnotar7.projects.aigolfcompanion.controller;

import com.cnotar7.projects.aigolfcompanion.dto.CourseDetailDTO;
import com.cnotar7.projects.aigolfcompanion.dto.CourseSummaryDTO;
import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourse;
import com.cnotar7.projects.aigolfcompanion.service.GolfCourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/golfcompanion/courses")
public class GolfCourseController {

    private GolfCourseService golfCourseService;

    @GetMapping()
    public ResponseEntity<List<CourseSummaryDTO>> searchGolfCourses(@RequestParam String query) {
        System.out.println("GolfCourseController.searchGolfCourses: " + query);
        List<CourseSummaryDTO> courseSummaries = golfCourseService.searchGolfCourse(query);
        for (CourseSummaryDTO course : courseSummaries) {
            System.out.println("Summary Course Name = " + course.getCourseName() + ", ID = " + course.getId());
        }
        return ResponseEntity.ok(courseSummaries);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailDTO> selectGolfCourseById(@PathVariable Long id) {
        CourseDetailDTO courseDetailDTO = golfCourseService.getGolfCourseById(id);
        System.out.println("GolfCourseController.selectGolfCourseById: " + courseDetailDTO);
        if (courseDetailDTO == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(courseDetailDTO);
        }
    }

}
