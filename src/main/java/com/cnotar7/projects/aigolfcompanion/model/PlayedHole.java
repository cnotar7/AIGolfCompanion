package com.cnotar7.projects.aigolfcompanion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayedHole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int holeNumber;
    private int par;
    private int yardage;
    private int handicap;
    private int strokes;
    private int putts;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    @OneToMany(mappedBy = "playedHole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shot> shots = new ArrayList<>();
}
