package com.cnotar7.projects.aigolfcompanion.service;

import com.cnotar7.projects.aigolfcompanion.converter.GolfObjectConverter;
import com.cnotar7.projects.aigolfcompanion.dto.CourseDetailDTO;
import com.cnotar7.projects.aigolfcompanion.dto.CourseSummaryDTO;
import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourse;
import com.cnotar7.projects.aigolfcompanion.model.Course;
import com.cnotar7.projects.aigolfcompanion.repository.CourseRepository;
import com.cnotar7.projects.aigolfcompanion.service.api.GolfCourseAPIClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GolfCourseServiceTest {

    @Mock
    private GolfCourseAPIClient golfCourseAPIClient;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private GolfObjectConverter golfObjectConverter;

    @InjectMocks
    private GolfCourseService golfCourseService;

    private ExternalCourse externalCourse;
    private Course course;
    private CourseSummaryDTO summaryDTO;
    private CourseDetailDTO detailDTO;

    @BeforeEach
    void setUp() {
        externalCourse = new ExternalCourse();
        externalCourse.setId(1L);
        externalCourse.setClub_name("Arcadia Bluffs Gc");

        course = Course.builder()
                .id(1L)
                .name("Arcadia Bluffs Gc")
                .build();

        summaryDTO = CourseSummaryDTO.builder()
                .id(1L)
                .courseName("Arcadia Bluffs Gc")
                .build();

        detailDTO = CourseDetailDTO.builder()
                .id(1L)
                .courseName("Arcadia Bluffs Gc")
                .build();

    }

    @Test
    void searchGolfCourse() {
        String query = "Arcadia";
        when(golfCourseAPIClient.searchGolfCourse(query)).thenReturn(List.of(externalCourse));
        when(golfObjectConverter.mapExternalCoursetoDTO(externalCourse)).thenReturn(summaryDTO);

        List<CourseSummaryDTO> result = golfCourseService.searchGolfCourse(query);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourseName()).isEqualTo("Arcadia Bluffs Gc");
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void searchGolfCourseWithNoResult() {
        when(golfCourseAPIClient.searchGolfCourse("")).thenReturn(List.of());

        List<CourseSummaryDTO> result = golfCourseService.searchGolfCourse("");
        assertThat(result).isEmpty();

    }

    @Test
    void getGolfCourseByIdRepoFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(golfObjectConverter.mapCourseEntityToDTO(course)).thenReturn(detailDTO);
        CourseDetailDTO result = golfCourseService.getGolfCourseById(1L);

        assertThat(result.getCourseName()).isEqualTo("Arcadia Bluffs Gc");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getGolfCourseByIdAPIFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        when(golfCourseAPIClient.getGolfCourseById(1L)).thenReturn(externalCourse);
        when(golfObjectConverter.mapExternalCourseToEntity(externalCourse)).thenReturn(course);

        when(courseRepository.save(course)).thenReturn(course);
        when(golfObjectConverter.mapCourseEntityToDTO(course)).thenReturn(detailDTO);


        CourseDetailDTO result = golfCourseService.getGolfCourseById(1L);

        assertThat(result.getCourseName()).isEqualTo("Arcadia Bluffs Gc");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getGolfCourseByIdNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        when(golfCourseAPIClient.getGolfCourseById(1L)).thenReturn(null);

        CourseDetailDTO result = golfCourseService.getGolfCourseById(1L);

        assertThat(result).isNull();
    }
}
