package com.cnotar7.projects.aigolfcompanion.service;

import com.cnotar7.projects.aigolfcompanion.dto.HoleShotsDTO;
import com.cnotar7.projects.aigolfcompanion.dto.StartRoundDTO;
import com.cnotar7.projects.aigolfcompanion.model.Round;
import com.cnotar7.projects.aigolfcompanion.model.RoundHole;
import com.cnotar7.projects.aigolfcompanion.model.Shot;
import com.cnotar7.projects.aigolfcompanion.repository.CourseRepository;
import com.cnotar7.projects.aigolfcompanion.repository.RoundRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoundService {

    private RoundRepository roundRepository;
    private CourseRepository courseRepository;

    public RoundHole addShotsToRound(Long roundId, HoleShotsDTO holeShotsDTO) {
        Round round = roundRepository.findById(roundId).orElseThrow(() -> new RuntimeException("Round not found"));

        // Check if this hole already exists
        // If not, create new hole and add it to round
        RoundHole roundHole = round.getHoles().stream()
                .filter(hole -> hole.getHoleNumber() == holeShotsDTO.getHoleNumber())
                .findFirst()
                .orElseGet(() -> {
                    RoundHole newHole = RoundHole.builder()
                        .holeNumber(holeShotsDTO.getHoleNumber())
                        .round(round)
                        .build();
                    round.getHoles().add(newHole);
                    return newHole;
                });

        // Map shots and add them
        List<Shot> newShots = holeShotsDTO.getShots().stream()
                .map(dto -> Shot.builder()
                        .club(dto.getClub())
                        .distanceYards(dto.getDistanceYards())
                        .result(dto.getResult())
                        .roundHole(roundHole)
                        .build())
                .toList();

        roundHole.getShots().addAll(newShots);

        roundRepository.save(round);
        return roundHole;
    }

}
