package com.cnotar7.projects.aigolfcompanion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private Integer holesCount;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Hole> holes;
}

