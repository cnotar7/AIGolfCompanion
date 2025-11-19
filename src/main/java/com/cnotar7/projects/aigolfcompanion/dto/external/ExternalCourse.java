package com.cnotar7.projects.aigolfcompanion.dto.external;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ExternalCourse {
    private Long id;
    private String club_name;
    private String course_name;
    private Location location;
    private Tees tees;

    @Data
    public static class Location {
        private String address;
        private String city;
        private String state;
        private String country;
        private double latitude;
        private double longitude;
    }

    @Data
    public static class Tees {
        private List<Tee> female = new ArrayList<>(); // optional empty if course doesn't have named female and male tees;
        private List<Tee> male = new ArrayList<>();
    }

    @Data
    public static class Tee {
        private String tee_name;
        private double course_rating;
        private double slope_rating;
        private double bogey_rating;
        private int total_yards;
        private int number_of_holes;
        private int par_total;
        private List<Hole> holes;
    }

    @Data
    public static class Hole {
        private int par;
        private int yardage;
        private int handicap;
    }
}