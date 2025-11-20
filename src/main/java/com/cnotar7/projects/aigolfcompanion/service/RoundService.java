package com.cnotar7.projects.aigolfcompanion.service;

import com.cnotar7.projects.aigolfcompanion.converter.GolfRoundObjectConverter;
import com.cnotar7.projects.aigolfcompanion.dto.*;
import com.cnotar7.projects.aigolfcompanion.enums.Gender;
import com.cnotar7.projects.aigolfcompanion.model.*;
import com.cnotar7.projects.aigolfcompanion.repository.*;
import com.cnotar7.projects.aigolfcompanion.util.PlayerStatsCalculator;
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
    private ShotRepository shotRepository;
    private RoundSummaryService roundSummaryService;
    private PlayerStatsCalculator playerStatsCalculator;
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

        round.setHoles(playedHoles);

        Round savedRound = roundRepository.save(round);
        return converter.mapRoundEntityToDTO(savedRound);
    }

    public RoundResponseDTO getRoundById(Long roundId) {
        Round round = roundRepository.findById(roundId).orElseThrow(() ->
                new MissingResourceException("Round not found", Round.class.getName(), roundId.toString()));
        return converter.mapRoundEntityToDTO(round);
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


    public PlayedHoleDTO addShotToHole(Long roundId, Integer holeNumber, ShotDTO shotDTO) {

        Round round = roundRepository.findById(roundId).orElseThrow(() ->
                new MissingResourceException("Round not found", Round.class.getName(), roundId.toString()));
        PlayedHole playedHole = round.getHoles().get(holeNumber);

        if (playedHole == null) {
            throw new MissingResourceException("Played Hole not found", PlayedHole.class.getName(), holeNumber.toString());
        }

        Shot newShot = converter.mapShotDTOToEntity(shotDTO, playedHole);
        newShot.setPlayedHole(playedHole);
        playedHole.getShots().add(newShot);

        playedholeRepository.save(playedHole);
        return converter.mapPlayedHoleEntityToDTO(playedHole);
    }

    public PlayedHoleDTO updateShot(Long shotId,  ShotDTO shotDTO) {
        Shot shotToUpdate = shotRepository.findById(shotId)
                .orElseThrow(() -> new MissingResourceException("Shot not found", Shot.class.getName(), shotId.toString()));

        PlayedHole playedHole = shotToUpdate.getPlayedHole();

        if (playedHole == null) {
            throw new MissingResourceException("Played Hole does not exist for this shot", PlayedHole.class.getName(), shotId.toString());
        }

        shotToUpdate.setClub(shotDTO.getClub());
        shotToUpdate.setShotNumber(shotDTO.getShotNumber());
        shotToUpdate.setResult(shotDTO.getResult());

        shotRepository.save(shotToUpdate);

        return converter.mapPlayedHoleEntityToDTO(playedHole);
    }

    public PlayedHoleDTO deleteShot(Long shotId) {
        Shot shotToDelete = shotRepository.findById(shotId)
                .orElseThrow(() -> new MissingResourceException("Shot not found", Shot.class.getName(), shotId.toString()));

        PlayedHole playedHole = shotToDelete.getPlayedHole();

        if (playedHole == null) {
            throw new MissingResourceException("Played hole is not attached to this shot", Shot.class.getName(), shotId.toString());
        }

        // 2. Remove the shot from the hole’s list
        boolean removed = playedHole.getShots().removeIf(s -> s.getId().equals(shotId));

        if (!removed) {
            throw new IllegalStateException("Shot exists but was not in PlayedHole.shots list");
        }

        playedholeRepository.save(playedHole);
        shotRepository.delete(shotToDelete);

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

    public RoundResponseDTO completeRound(Long roundId) {
        /*
        Round round = roundRepository.findById(roundId).orElseThrow(() ->
                new MissingResourceException("Round not found", Round.class.getName(), roundId.toString()));


         */

        Course fakeCourse = createFakeCourse(
                1L,
                "Arcadia Bluffs Gc",
                "Arcadia Bluffs Gc",
                "Arcadia",
                "Michigan",
                "US"
        );
        User fakeUser = new User(2345L, "cnotar7", "dsfdfgsdfdsf");

        Round fakeRound = createFakeRound(fakeCourse, fakeCourse.getTees().get(3), fakeUser);

        PlayerStatsDTO playerStatsDTO = playerStatsCalculator.calculatePlayerStats(fakeRound.getUser().getId());
        String AiSummary = roundSummaryService.generateAiSummary(fakeRound, playerStatsDTO);

        /*
        round.setAiSummary(AiSummary);
        round.setCompleted(true);
        roundRepository.save(round);
        return converter.mapRoundEntityToDTO(round);

         */
        return new RoundResponseDTO();

    }


    public Course createFakeCourse(Long courseId, String name, String clubName, String city, String state, String country) {
        Course course = new Course();
        course.setId(courseId);
        course.setName(name);
        course.setClubName(clubName);
        course.setAddress("123 Main St");
        course.setCity(city);
        course.setState(state);
        course.setCountry(country);
        course.setLatitude(42.123 + Math.random());
        course.setLongitude(-83.123 + Math.random());

        List<Tee> tees = new ArrayList<>();

        // Female tees
        tees.add(createFakeTee("White", Gender.FEMALE, course));
        tees.add(createFakeTee("Red", Gender.FEMALE, course));

        // Male tees
        tees.add(createFakeTee("Championship", Gender.MALE, course));
        tees.add(createFakeTee("Gold", Gender.MALE, course));

        course.setTees(tees);
        return course;
    }

    private Tee createFakeTee(String teeName, Gender gender, Course course) {
        Tee tee = new Tee();
        tee.setTeeName(teeName);
        tee.setGender(gender);
        tee.setCourseRating(72.0 + Math.random() * 5);
        tee.setSlopeRating(125 + (int)(Math.random() * 20));
        tee.setTotalYards(5000 + (int)(Math.random() * 2000));
        tee.setParTotal(72);
        tee.setCourse(course);

        List<Hole> holes = new ArrayList<>();
        for (int i = 1; i <= 18; i++) {
            Hole hole = new Hole();
            hole.setPar(3 + (int)(Math.random() * 3)); // par 3-5
            hole.setYardage(100 + (int)(Math.random() * 400));
            hole.setHandicap(i);
            hole.setTee(tee);
            holes.add(hole);
        }

        tee.setHoles(holes);
        return tee;
    }

    public Round createFakeRound(Course course, Tee selectedTee, User user) {
        Round round = new Round();
        round.setStartTime(LocalDateTime.now());
        round.setCourse(course);
        round.setSelectedTee(selectedTee);
        round.setUser(user);
        round.setCompleted(false);
        round.setCurrentHoleNumber(1);
        round.setWeather("Sunny");

        // Create 18 PlayedHoles
        for (int i = 1; i <= 18; i++) {
            PlayedHole playedHole = new PlayedHole();
            playedHole.setHoleNumber(i);

            // Use hole info from the selected Tee
            Hole teeHole = selectedTee.getHoles().get(i - 1);
            playedHole.setPar(teeHole.getPar());
            playedHole.setYardage(teeHole.getYardage());
            playedHole.setHandicap(teeHole.getHandicap());
            playedHole.setStrokes(0);
            playedHole.setPutts(0);
            playedHole.setCompleted(false);
            playedHole.setRound(round);

            // Add some fake shots
            for (int shotNum = 1; shotNum <= 3; shotNum++) {
                Shot shot = new Shot();
                shot.setShotNumber(shotNum);
                shot.setClub(randomClub());
                shot.setResult(randomResult());
                shot.setPlayedHole(playedHole);

                playedHole.getShots().add(shot);
            }

            round.getHoles().put(i, playedHole);
        }

        return round;
    }

    private String randomClub() {
        String[] clubs = {"Driver", "3-Wood", "5-Iron", "7-Iron", "Pitching Wedge", "Putter"};
        int idx = (int) (Math.random() * clubs.length);
        return clubs[idx];
    }

    private static String randomResult() {
        String[] results = {"Good", "Fairway", "Rough", "Sand", "Water", "Missed Green", "On Green"};
        int idx = (int) (Math.random() * results.length);
        return results[idx];
    }

}
