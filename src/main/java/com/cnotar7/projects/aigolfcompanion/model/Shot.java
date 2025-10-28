package com.cnotar7.projects.aigolfcompanion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String club;
    private int distanceYards;
    private String result;

    @ManyToOne
    @JoinColumn(name = "round_hole_id")
    private RoundHole roundHole;
}
