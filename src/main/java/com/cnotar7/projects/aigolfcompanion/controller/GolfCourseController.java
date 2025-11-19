package com.cnotar7.projects.aigolfcompanion.controller;

import com.cnotar7.projects.aigolfcompanion.dto.CourseDetailDTO;
import com.cnotar7.projects.aigolfcompanion.dto.CourseSummaryDTO;
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

    @GetMapping("/search")
    public ResponseEntity<List<CourseSummaryDTO>> searchGolfCourses(@RequestParam String query) {
        List<CourseSummaryDTO> courseSummaries = golfCourseService.searchGolfCourse(query);
        if (courseSummaries.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(courseSummaries);
        }
    }

    @GetMapping("/select/{id}")
    public ResponseEntity<CourseDetailDTO> selectGolfCourseById(@PathVariable Long id) {
        CourseDetailDTO courseDetailDTO = golfCourseService.getGolfCourseById(id);
        if (courseDetailDTO == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(courseDetailDTO);
        }
    }

}
