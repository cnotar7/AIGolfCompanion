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
public class RoundHole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int holeNumber;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    @OneToMany(mappedBy = "roundHole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shot> shots = new ArrayList<>();
}
