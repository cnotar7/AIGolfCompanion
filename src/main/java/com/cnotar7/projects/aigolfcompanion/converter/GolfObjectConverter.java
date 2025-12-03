package com.cnotar7.projects.aigolfcompanion.converter;

import com.cnotar7.projects.aigolfcompanion.dto.*;
import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourse;
import com.cnotar7.projects.aigolfcompanion.enums.Gender;
import com.cnotar7.projects.aigolfcompanion.model.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GolfObjectConverter {

    public CourseDetailDTO mapCourseEntityToDTO(Course course) {
        CourseDetailDTO dto = CourseDetailDTO.builder()
                .id(course.getId())
                .courseName(course.getName())
                .clubName(course.getClubName())
                .city(course.getCity())
                .country(course.getCountry())
                .address(course.getAddress())
                .latitude(course.getLatitude())
                .longitude(course.getLongitude())
                .tees(course.getTees().stream()
                        .map(this::mapTeeEntityToDTO)
                        .toList())
                .build();

        return dto;
    }

    public TeeDTO mapTeeEntityToDTO(Tee tee) {
        return TeeDTO.builder()
                .teeName(tee.getTeeName())
                .courseRating(tee.getCourseRating())
                .slopeRating(tee.getSlopeRating())
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
        Set<String> teeNames = new HashSet<>();
        if (ext.getTees() != null) {
            for (ExternalCourse.Tee t : ext.getTees().getMale()) {
                teeEntities.add(mapExternalTeeToEntity(t, course, Gender.MALE));
                teeNames.add(t.getTee_name());
            }
            for (ExternalCourse.Tee t : ext.getTees().getFemale()) {
                if (!teeNames.contains(t.getTee_name())) {
                    teeEntities.add(mapExternalTeeToEntity(t, course, Gender.FEMALE));
                    teeNames.add(t.getTee_name());
                }

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
                .courseRating(t.getCourse_rating())
                .slopeRating(t.getSlope_rating())
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
                .clubName(ext.getClub_name())
                .courseName(ext.getCourse_name())
                .city(ext.getLocation().getCity())
                .state(ext.getLocation().getState())
                .country(ext.getLocation().getCountry())
                .build();
    }

    public RoundDTO mapRoundEntityToDTO(Round round) {
        return RoundDTO.builder()
                .courseId(round.getCourse().getId())
                .roundId(round.getId())
                .teeId(round.getSelectedTee().getId())
                .userName(round.getUser().getUsername())
                .currentHoleNumber(round.getCurrentHoleNumber())
                .startTime(round.getStartTime())
                .completed(round.isCompleted())
                .holes(round.getHoles().entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> mapPlayedHoleEntityToDTO(e.getValue())
                        )))
                .aiSummary(round.getAiSummary())
                .build();
    }
    public PlayedHoleDTO mapPlayedHoleEntityToDTO(PlayedHole rh) {
        return PlayedHoleDTO.builder()
                .holeNumber(rh.getHoleNumber())
                .par(rh.getPar())
                .yardage(rh.getYardage())
                .handicap(rh.getHandicap())
                .strokes(rh.getStrokes())
                .putts(rh.getPutts())
                .completed(rh.isCompleted())
                .shots(rh.getShots().stream()
                        .map(this::mapShotEntityToDTO)
                        .toList())
                .build();
    }

    public ShotDTO mapShotEntityToDTO(Shot shot) {
        return ShotDTO.builder()
                .shotId(shot.getId())
                .club(shot.getClub())
                .shotNumber(shot.getShotNumber())
                .result(shot.getResult())
                .build();
    }

    public Shot mapShotDTOToEntity(ShotDTO shotDTO, PlayedHole playedHole) {
        return Shot.builder()
                .club(shotDTO.getClub())
                .shotNumber(shotDTO.getShotNumber())
                .result(shotDTO.getResult())
                .playedHole(playedHole)
                .build();
    }

}
