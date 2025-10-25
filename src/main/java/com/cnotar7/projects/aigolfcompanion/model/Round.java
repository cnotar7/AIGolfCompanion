package com.cnotar7.projects.aigolfcompanion.model;

import io.micrometer.core.instrument.AbstractDistributionSummary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String weather;
    private boolean completed;

    @OneToOne
    private Course course;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<Shot> shots;

    @OneToOne(mappedBy = "round", cascade = CascadeType.ALL)
    private AISummary aiSummary;


}
