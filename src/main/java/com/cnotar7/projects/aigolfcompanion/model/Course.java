package com.cnotar7.projects.aigolfcompanion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    private Long id;

    private String name;
    private String clubName;
    private String address;
    private String city;
    private String state;
    private String country;
    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tee> tees;
}

