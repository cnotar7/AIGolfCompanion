package com.cnotar7.projects.aigolfcompanion.converter;

import com.cnotar7.projects.aigolfcompanion.dto.CourseDetailDTO;
import com.cnotar7.projects.aigolfcompanion.dto.CourseSummaryDTO;
import com.cnotar7.projects.aigolfcompanion.dto.HoleDTO;
import com.cnotar7.projects.aigolfcompanion.dto.TeeDTO;
import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourse;
import com.cnotar7.projects.aigolfcompanion.enums.Gender;
import com.cnotar7.projects.aigolfcompanion.model.Course;
import com.cnotar7.projects.aigolfcompanion.model.Hole;
import com.cnotar7.projects.aigolfcompanion.model.Tee;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GolfCourseObjectConverter {

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

    public Course mapExternalCourseToEntity(ExternalCourse ext) {
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
                teeEntities.add(mapExternalTeeToEntity(t, course, Gender.FEMALE));
            }
            for (ExternalCourse.Tee t : ext.getTees().getMale()) {
                teeEntities.add(mapExternalTeeToEntity(t, course, Gender.MALE));
            }
        }
        course.setTees(teeEntities);

        return course;
    }

    public Tee mapExternalTeeToEntity(ExternalCourse.Tee t, Course course, Gender gender) {
        Tee tee = Tee.builder()
                .teeName(t.getTee_name())
                .parTotal(t.getPar_total())
                .totalYards(t.getTotal_yards())
                .gender(gender)
                .course(course)
                .build();

        List<Hole> holeEntities = t.getHoles().stream()
                .map(h -> mapExternalHoleToEntity(h, tee))
                .toList();

        tee.setHoles(holeEntities);
        return tee;
    }

    public Hole mapExternalHoleToEntity(ExternalCourse.Hole h, Tee tee) {
        return Hole.builder()
                .par(h.getPar())
                .yardage(h.getYardage())
                .handicap(h.getHandicap())
                .tee(tee)
                .build();
    }

    public CourseSummaryDTO mapExternalCoursetoDTO(ExternalCourse ext) {
        return CourseSummaryDTO.builder()
                .id(ext.getId())
                .name(ext.getCourse_name())
                .city(ext.getLocation().getCity())
                .state(ext.getLocation().getState())
                .country(ext.getLocation().getCountry())
                .build();
    }
}
