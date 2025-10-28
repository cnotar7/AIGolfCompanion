package com.cnotar7.projects.aigolfcompanion.service;

import com.cnotar7.projects.aigolfcompanion.converter.GolfCourseObjectConverter;
import com.cnotar7.projects.aigolfcompanion.dto.CourseDetailDTO;
import com.cnotar7.projects.aigolfcompanion.dto.HoleDTO;
import com.cnotar7.projects.aigolfcompanion.dto.TeeDTO;
import com.cnotar7.projects.aigolfcompanion.enums.Gender;
import com.cnotar7.projects.aigolfcompanion.model.Course;
import com.cnotar7.projects.aigolfcompanion.model.Hole;
import com.cnotar7.projects.aigolfcompanion.model.Tee;
import com.cnotar7.projects.aigolfcompanion.repository.CourseRepository;
import com.cnotar7.projects.aigolfcompanion.service.api.GolfCourseAPIClient;
import com.cnotar7.projects.aigolfcompanion.dto.CourseSummaryDTO;
import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                .map(course -> CourseSummaryDTO.builder()
                        .id(course.getId())
                        .name(course.getCourse_name())
                        .city(course.getLocation().getCity())
                        .state(course.getLocation().getState())
                        .country(course.getLocation().getCountry())
                        .build())
                .toList();
    }

    public CourseDetailDTO getGolfCourseById(Long id) {
        Course course = courseRepository.findById(id).orElse(null);

        // if course is not already stored, call golf course api for it
        if (course == null) {
            System.out.println("Golf course with id = " + id + " not found");
            ExternalCourse externalCourse = golfCourseAPIClient.getGolfCourseById(id);
            System.out.println("sdsdfg");
            if (externalCourse != null) {
                course = golfCourseObjectConverter.mapExternalCourseToEntity(externalCourse);
                System.out.println("ghjghjd");
                courseRepository.save(course);
                System.out.println("htrrcsdv");
            } else {
                return null;
            }

        }

        return golfCourseObjectConverter.mapCourseEntityToDTO(course);
    }

    private Course mapExternalCourseToEntity(ExternalCourse ext) {
        System.out.println("External Course!!! " + ext);
        Course course = Course.builder()
                .id(ext.getId())
                .name(ext.getCourse_name())
                .clubName(ext.getClub_name())
                .city(ext.getLocation().getCity())
                .state(ext.getLocation().getState())
                .country(ext.getLocation().getCountry())
                .address(ext.getLocation().getAddress())
                .latitude(ext.getLocation().getLatitude())
                .longitude(ext.getLocation().getLongitude())
                .build();

        List<Tee> teeEntities = new ArrayList<>();
        if (ext.getTees() != null) {
            for (ExternalCourse.Tee t : ext.getTees().getFemale()) {
                teeEntities.add(mapTee(t, course, Gender.FEMALE));
            }
            for (ExternalCourse.Tee t : ext.getTees().getMale()) {
                teeEntities.add(mapTee(t, course, Gender.MALE));
            }
        }
        course.setTees(teeEntities);

        return course;
    }

    private Tee mapTee(ExternalCourse.Tee t, Course course, Gender gender) {
        Tee tee = Tee.builder()
                .teeName(t.getTee_name())
                .parTotal(t.getPar_total())
                .totalYards(t.getTotal_yards())
                .gender(gender)
                .course(course)
                .build();

        List<Hole> holeEntities = t.getHoles().stream()
                .map(h -> Hole.builder()
                        .par(h.getPar())
                        .yardage(h.getYardage())
                        .handicap(h.getHandicap())
                        .tee(tee)
                        .build())
                .toList();

        tee.setHoles(holeEntities);
        return tee;
    }

    public CourseDetailDTO mapCourseEntityToDTO(Course course) {
        CourseDetailDTO dto = CourseDetailDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .city(course.getCity())
                .country(course.getCountry())
                .address(course.getAddress())
                .latitude(course.getLatitude())
                .longitude(course.getLongitude())
                .build();

        List<TeeDTO> teeDTOs = course.getTees().stream()
                .map(this::mapTeeEntityToDTO)
                .toList();

        dto.setTees(teeDTOs);
        return dto;
    }

    public TeeDTO mapTeeEntityToDTO(Tee tee) {
        return TeeDTO.builder()
                .teeName(tee.getTeeName())
                .courseRating(tee.getCourseRating())
                .totalYards(tee.getTotalYards())
                .parTotal(tee.getParTotal())
                .gender(tee.getGender())
                .holes(tee.getHoles().stream()
                        .map(this::mapHoleEntityToDTO)
                        .toList())
                .build();
    }

    public HoleDTO mapHoleEntityToDTO(Hole hole) {
        return HoleDTO.builder()
                .handicap(hole.getHandicap())
                .par(hole.getPar())
                .yardage(hole.getYardage())
                .build();
    }


}
