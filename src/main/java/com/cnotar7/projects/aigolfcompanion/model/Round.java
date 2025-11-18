package com.cnotar7.projects.aigolfcompanion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private String weather;
    private boolean completed;

    @Min(1)
    @Max(18)
    private int currentHoleNumber = 1;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Tee selectedTee;

    @ManyToOne
    private User user;


    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "holeNumber")
    private Map<Integer, PlayedHole> holes = new HashMap<>();

    @Lob // large text
    private String aiSummary; // JSON or plain text summary

}
