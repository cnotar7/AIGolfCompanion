package com.cnotar7.projects.aigolfcompanion.service;

import com.cnotar7.projects.aigolfcompanion.converter.GolfRoundObjectConverter;
import com.cnotar7.projects.aigolfcompanion.dto.*;
import com.cnotar7.projects.aigolfcompanion.model.*;
import com.cnotar7.projects.aigolfcompanion.repository.CourseRepository;
import com.cnotar7.projects.aigolfcompanion.repository.PlayedHoleRepository;
import com.cnotar7.projects.aigolfcompanion.repository.RoundRepository;
import com.cnotar7.projects.aigolfcompanion.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class RoundService {

    private RoundRepository roundRepository;
    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private PlayedHoleRepository playedholeRepository;
    private GolfRoundObjectConverter converter;

    public RoundResponseDTO startNewRound(StartRoundDTO startRoundDTO) {
        Course course = courseRepository.findById(startRoundDTO.getCourseId()).orElseThrow(() ->
                new MissingResourceException("Course not found", Course.class.getName(), startRoundDTO.getCourseId().toString()));

        User user = userRepository.findByUsername(startRoundDTO.getUserName()).orElseThrow(() ->
                new MissingResourceException("User not found", User.class.getName(), startRoundDTO.getUserName())); // replace this with Auth

        Tee tee = course.getTees().stream()
                .filter(t -> t.getId().equals(startRoundDTO.getTeeId()))
                .findFirst()
                .orElseThrow(() ->
                        new MissingResourceException("Round not found", Round.class.getName(), startRoundDTO.getTeeId().toString()));

        LocalDateTime now = LocalDateTime.now();
        Round round = Round.builder()
                .startTime(now)
                .completed(false)
                .currentHoleNumber(1)
                .course(course)
                .selectedTee(tee)
                .user(user)
                .build();


        // build empty holes for the user to fill in later
        Map<Integer, PlayedHole> playedHoles = new HashMap<>();
        int holeCounter = 1;
        for (Hole hole : tee.getHoles()) {
            PlayedHole playedHole = PlayedHole.builder()
                    .holeNumber(holeCounter)
                    .par(hole.getPar())
                    .strokes(0)
                    .putts(0)
                    .completed(false)
                    .round(round)
                    .shots(Collections.emptyList())
                    .build();

            playedHoles.put(holeCounter++, playedHole);
        }



        Round savedRound = roundRepository.save(round);
        RoundResponseDTO roundResponseDTO = RoundResponseDTO.builder()
                .courseId(savedRound.getCourse().getId())
                .roundId(savedRound.getId())
                .teeId(savedRound.getSelectedTee().getId())
                .courseName(savedRound.getCourse().getName())
                .userName(user.getUsername())
                .currentHoleNumber(1)
                .startTime(now)
                .completed(false)
                .build();
        return roundResponseDTO;
    }


    public PlayedHoleDTO getHoleForRound(Long roundId, Integer holeNumber) {
        Round round = roundRepository.findById(roundId).orElseThrow(() ->
                new MissingResourceException("Round not found", Round.class.getName(), roundId.toString()));

        // if not given a hole number, retrieve the current one
        holeNumber = Objects.requireNonNullElseGet(holeNumber, round::getCurrentHoleNumber);
        if (holeNumber < 1 || holeNumber > 18) {
            throw new IllegalStateException("Hole number outside of range 1-18");
        }

        return converter.mapPlayedHoleEntityToDTO(round.getHoles().get(holeNumber));
    }


    public PlayedHoleDTO addShotToHole(Long roundId, int holeNumber, ShotDTO shotDTO) {

        Round round = roundRepository.findById(roundId).orElseThrow(() -> new RuntimeException("Round not found"));
        PlayedHole playedHole = round.getHoles().get(holeNumber);

        if (playedHole == null) {
            throw new IllegalStateException("Hole " + holeNumber + " does not exist for this round");
        }

        Shot newShot = converter.mapShotDTOToEntity(shotDTO, playedHole);
        newShot.setPlayedHole(playedHole);
        playedHole.getShots().add(newShot);

        playedholeRepository.save(playedHole);
        return converter.mapPlayedHoleEntityToDTO(playedHole);
    }

    public PlayedHoleDTO moveToNextHole(Long roundId) {
        Round round = roundRepository.findById(roundId).orElseThrow(() ->
                new MissingResourceException("Round not found", Round.class.getName(), roundId.toString()));

        // if not given a hole number, retrieve the current one
        int currentHoleNumber = round.getCurrentHoleNumber();

        if (currentHoleNumber < 1 || currentHoleNumber > 18) {
            throw new MissingResourceException("Current Hole does not exist", Round.class.getName(), roundId.toString());
        }

        currentHoleNumber++;

        // if we are on last hole, go back to hole 1
        if (currentHoleNumber > 18) {
            currentHoleNumber = 1;
        }

        round.setCurrentHoleNumber(currentHoleNumber);
        roundRepository.save(round);

        return converter.mapPlayedHoleEntityToDTO(round.getHoles().get(currentHoleNumber));

    }

    public PlayedHoleDTO moveToPreviousHole(Long roundId) {
        Round round = roundRepository.findById(roundId).orElseThrow(() ->
                new MissingResourceException("Round not found", Round.class.getName(), roundId.toString()));

        // if not given a hole number, retrieve the current one
        int currentHoleNumber = round.getCurrentHoleNumber();

        if (currentHoleNumber < 1 || currentHoleNumber > 18) {
            throw new MissingResourceException("Current Hole does not exist", Round.class.getName(), roundId.toString());
        }

        currentHoleNumber--;

        // if we are on the first hole, go to the last
        if (currentHoleNumber < 1) {
            currentHoleNumber = 18;
        }

        round.setCurrentHoleNumber(currentHoleNumber);
        roundRepository.save(round);

        return converter.mapPlayedHoleEntityToDTO(round.getHoles().get(currentHoleNumber));

    }

}
