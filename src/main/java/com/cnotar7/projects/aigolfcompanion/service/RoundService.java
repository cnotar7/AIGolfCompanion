package com.cnotar7.projects.aigolfcompanion.service;

import com.cnotar7.projects.aigolfcompanion.converter.GolfRoundObjectConverter;
import com.cnotar7.projects.aigolfcompanion.dto.HoleShotsDTO;
import com.cnotar7.projects.aigolfcompanion.dto.RoundHoleDTO;
import com.cnotar7.projects.aigolfcompanion.dto.RoundResponseDTO;
import com.cnotar7.projects.aigolfcompanion.dto.StartRoundDTO;
import com.cnotar7.projects.aigolfcompanion.model.*;
import com.cnotar7.projects.aigolfcompanion.repository.CourseRepository;
import com.cnotar7.projects.aigolfcompanion.repository.RoundRepository;
import com.cnotar7.projects.aigolfcompanion.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.MissingResourceException;

@Service
@AllArgsConstructor
public class RoundService {

    private RoundRepository roundRepository;
    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private GolfRoundObjectConverter converter;

    public RoundResponseDTO startNewRound(StartRoundDTO startRoundDTO) {
        Course course = courseRepository.findById(startRoundDTO.getCourseId()).orElseThrow(() ->
                new MissingResourceException("Course not found", Course.class.getName(), startRoundDTO.getCourseId().toString()));

        User user = userRepository.findByUsername(startRoundDTO.getUserName()).orElseThrow(() ->
                new MissingResourceException("User not found", User.class.getName(), startRoundDTO.getUserName())); // replace this with Auth

        LocalDateTime now = LocalDateTime.now();
        Round round = Round.builder()
                .startTime(now)
                .completed(false)
                .currentHoleNumber(1)
                .course(course)
                .user(user)
                .build();

        Round savedRound = roundRepository.save(round);
        RoundResponseDTO roundResponseDTO = RoundResponseDTO.builder()
                .courseId(course.getId())
                .roundId(savedRound.getId())
                .courseName(course.getName())
                .userName(user.getUsername())
                .currentHoleNumber(1)
                .startTime(now)
                .completed(false)
                .build();
        return roundResponseDTO;
    }

    public RoundHoleDTO getCurrentHoleForRound(Long roundId) {
        Round round = roundRepository.findById(roundId).orElseThrow(() ->
                new MissingResourceException("Round not found", Round.class.getName(), roundId.toString()));

        int index = round.getCurrentHoleNumber() - 1;
        if (index < 0 || index >= round.getHoles().size()) {
            throw new MissingResourceException("Current Hole does not exist", Round.class.getName(), roundId.toString());
        }

        RoundHole roundHole = round.getHoles().get(round.getCurrentHoleNumber());

        return converter.mapRoundHoleEntityToRoundHoleDTO(roundHole);
    }

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
