package com.cnotar7.projects.aigolfcompanion.model;

import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourse;
import com.cnotar7.projects.aigolfcompanion.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teeName;
    private double courseRating;
    private double slopeRating;
    private int totalYards;
    private int parTotal;

    @Enumerated(EnumType.STRING)
    private Gender gender; // FEMALE or MALE

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "tee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hole> holes = new ArrayList<>();
}
