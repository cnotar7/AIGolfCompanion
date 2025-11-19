package com.cnotar7.projects.aigolfcompanion.repository;

import com.cnotar7.projects.aigolfcompanion.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoundRepository extends JpaRepository<Round, Long> {
    List<Round> findAllByUserId(Long userId);
}
