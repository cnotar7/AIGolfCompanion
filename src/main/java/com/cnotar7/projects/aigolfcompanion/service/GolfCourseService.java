package com.cnotar7.projects.aigolfcompanion.service;

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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GolfCourseService {

    private final GolfCourseAPIClient golfCourseAPIClient;
    private final CourseRepository courseRepository;

    public List<CourseSummaryDTO> searchGolfCourse(String query) {
        List<ExternalCourse> courses = golfCourseAPIClient.searchGolfCourse(query);

        for (ExternalCourse course : courses) {
            System.out.println("Club Name = " + course.getClub_name() + ", ID = " + course.getId());
        }

        return courses.stream()
                .map(course -> new CourseSummaryDTO(
                        course.getId(),
                        course.getCourse_name(),
                        course.getLocation().getCity(),
                        course.getLocation().getState(),
                        course.getLocation().getCountry()

                )).toList();
    }

    public CourseDetailDTO getGolfCourseById(Long id) {
        Course course = courseRepository.findById(id).orElse(null);

        // if course is not already stored, call golf course api for it
        if (course == null) {
            System.out.println("Golf course with id = " + id + " not found");
            ExternalCourse externalCourse = golfCourseAPIClient.getGolfCourseById(id);
            System.out.println("sdsdfg");
            if (externalCourse != null) {
                course = mapExternalCourseToEntity(externalCourse);
                System.out.println("ghjghjd");
                courseRepository.save(course);
                System.out.println("htrrcsdv");
            } else {
                return null;
            }

        }

        return mapEntityToDTO(course);
    }

    private Course mapExternalCourseToEntity(ExternalCourse ext) {
        System.out.println("External Course!!! " + ext);
        Course course = new Course();
        course.setId(ext.getId());
        course.setName(ext.getCourse_name());
        course.setClubName(ext.getClub_name());
        course.setAddress(ext.getLocation().getAddress());
        course.setCity(ext.getLocation().getCity());
        course.setState(ext.getLocation().getState());
        course.setCountry(ext.getLocation().getCountry());
        course.setLatitude(ext.getLocation().getLatitude());
        course.setLongitude(ext.getLocation().getLongitude());

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
        Tee tee = new Tee();
        tee.setTeeName(t.getTee_name());
        tee.setParTotal(t.getPar_total());
        tee.setTotalYards(t.getTotal_yards());
        tee.setGender(gender);
        tee.setCourse(course);

        List<Hole> holeEntities = t.getHoles().stream().map(h -> {
            Hole hole = new Hole();
            hole.setPar(h.getPar());
            hole.setYardage(h.getYardage());
            hole.setHandicap(h.getHandicap());
            hole.setTee(tee);
            return hole;
        }).collect(Collectors.toList());

        tee.setHoles(holeEntities);
        return tee;
    }

    public CourseDetailDTO mapEntityToDTO(Course course) {
        CourseDetailDTO dto = new CourseDetailDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setAddress(course.getAddress());
        dto.setCity(course.getCity());
        dto.setState(course.getState());
        dto.setCountry(course.getCountry());

        List<TeeDTO> teeDTOs = course.getTees().stream().map(tee -> {
            TeeDTO tDto = new TeeDTO();
            tDto.setTeeName(tee.getTeeName());
            tDto.setParTotal(tee.getParTotal());
            tDto.setTotalYards(tee.getTotalYards());
            tDto.setGender(tee.getGender());
            tDto.setHoles(tee.getHoles().stream().map(h -> {
                HoleDTO hDto = new HoleDTO();
                hDto.setPar(h.getPar());
                hDto.setYardage(h.getYardage());
                hDto.setHandicap(h.getHandicap());
                return hDto;
            }).collect(Collectors.toList()));
            return tDto;
        }).collect(Collectors.toList());

        dto.setTees(teeDTOs);
        return dto;
    }


}
