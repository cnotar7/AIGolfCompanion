package com.cnotar7.projects.aigolfcompanion.service;

import com.cnotar7.projects.aigolfcompanion.converter.GolfObjectConverter;
import com.cnotar7.projects.aigolfcompanion.dto.*;
import com.cnotar7.projects.aigolfcompanion.model.*;
import com.cnotar7.projects.aigolfcompanion.repository.*;
import com.cnotar7.projects.aigolfcompanion.util.PlayerStatsCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class RoundServiceTest {

    @Mock
    private RoundRepository roundRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlayedHoleRepository playedholeRepository;

    @Mock
    private ShotRepository shotRepository;

    @Mock
    private RoundSummaryService roundSummaryService;

    @Mock
    private PlayerStatsCalculator playerStatsCalculator;

    @Mock
    private GolfObjectConverter converter;

    @InjectMocks
    private RoundService roundService;

    Course course;
    User user;
    Round round;
    Map<Integer, PlayedHole> playedHoles;
    RoundDTO roundDTO;
    StartRoundDTO startRoundDTO;
    PlayedHoleDTO playedHoleDTO;
    ShotDTO shotDTO;
    Shot shot;

    @BeforeEach
    public void setUp() {
        startRoundDTO = StartRoundDTO.builder()
                .teeName("fake tee")
                .courseId(1L)
                .userName("admin")
                .build();

        Hole hole1 = Hole.builder()
                .par(3)
                .build();
        Hole hole2 = Hole.builder()
                .par(4)
                .build();
        Hole hole3 = Hole.builder()
                .par(5)
                .build();

        Tee tee = Tee.builder()
                .id(3L)
                .holes(List.of(hole1, hole2, hole3))
                .build();

        course = Course.builder()
                .id(1L)
                .name("Arcadia Bluffs Gc")
                .tees(List.of(tee))
                .build();

        user = User.builder()
                .id(2L)
                .password("123456")
                .username("admin")
                .build();



        round = Round.builder()
                .id(4L)
                .completed(false)
                .currentHoleNumber(1)
                .course(course)
                .selectedTee(tee)
                .user(user)
                .build();

        shot = Shot.builder()
                .id(1L)
                .club("7 iron")
                .shotNumber(2)
                .result("test result")
                .build();

        playedHoles = new HashMap<>();
        for (int i = 1; i <= 18; ++i) {
            playedHoles.put(i, createPlayedHole(round, i, shot));
        }
        round.setHoles(playedHoles);

        roundDTO = RoundDTO.builder()
                .roundId(4L)
                .courseId(course.getId())
                .completed(false)
                .userName(user.getUsername())
                .build();

        shot.setPlayedHole(round.getHoles().get(1));


        shotDTO = ShotDTO.builder()
                .club("7 iron")
                .shotNumber(2)
                .result("test result")
                .build();

        playedHoleDTO = PlayedHoleDTO.builder()
                .completed(true)
                .holeNumber(1)
                .par(4)
                .putts(2)
                .shots(List.of(shotDTO))
                .build();



    }

    @Test
    public void startNewRoundSuccess() {
        when(courseRepository.findById(startRoundDTO.getCourseId())).thenReturn(Optional.of(course));
        when(userRepository.findByUsername(startRoundDTO.getUserName())).thenReturn(Optional.of(user));
        when(roundRepository.save(any(Round.class))).thenReturn(round);
        when(converter.mapRoundEntityToDTO(round)).thenReturn(roundDTO);

        RoundDTO result = roundService.startNewRound(startRoundDTO);

        assertThat(result.getRoundId()).isEqualTo(roundDTO.getRoundId());
        assertThat(result.getCourseId()).isEqualTo(roundDTO.getCourseId());
    }

    @Test
    public void startNewRoundMissingCourse() {
        when(courseRepository.findById(startRoundDTO.getCourseId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roundService.startNewRound(startRoundDTO))
                .isInstanceOf(MissingResourceException.class);
    }

    @Test
    public void startNewRoundMissingUser() {
        when(courseRepository.findById(startRoundDTO.getCourseId())).thenReturn(Optional.of(course));
        when(userRepository.findByUsername(startRoundDTO.getUserName())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roundService.startNewRound(startRoundDTO))
                .isInstanceOf(MissingResourceException.class);
    }

    @Test
    public void startNewRoundMissingTee() {
        Course missingTeeCourse = Course.builder()
                .id(1L)
                .name("Arcadia Bluffs Gc")
                .tees(List.of())
                .build();
        when(courseRepository.findById(startRoundDTO.getCourseId())).thenReturn(Optional.of(missingTeeCourse));
        when(userRepository.findByUsername(startRoundDTO.getUserName())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> roundService.startNewRound(startRoundDTO))
                .isInstanceOf(MissingResourceException.class);

    }

    @Test
    public void getRoundByIdSuccess() {
        Long roundId = 1L;
        when(roundRepository.findById(roundId)).thenReturn(Optional.of(round));
        when(converter.mapRoundEntityToDTO(round)).thenReturn(roundDTO);

        RoundDTO result = roundService.getRoundById(roundId);

        assertThat(result.getRoundId()).isEqualTo(roundDTO.getRoundId());
        assertThat(result.getCourseId()).isEqualTo(roundDTO.getCourseId());

    }

    @Test
    public void getRoundByIdMissingRoundId() {
        Long roundId = 1L;
        when(roundRepository.findById(roundId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roundService.getRoundById(roundId))
                .isInstanceOf(MissingResourceException.class);
    }

    @Test
    public void getHoleForRoundSuccess() {
        Long roundId = 1L;
        Integer holeNumber = 1;
        when(roundRepository.findById(roundId)).thenReturn(Optional.of(round));
        when(converter.mapPlayedHoleEntityToDTO(round.getHoles().get(holeNumber))).thenReturn(playedHoleDTO);

        PlayedHoleDTO result = roundService.getHoleForRound(roundId, holeNumber);

        assertThat(result.getHoleNumber()).isEqualTo(holeNumber);
        assertThat(result.getPar()).isEqualTo(round.getHoles().get(holeNumber).getPar());
    }

    @Test
    public void getHoleForRoundNullHoleNumber() {
        Long roundId = 1L;
        when(roundRepository.findById(roundId)).thenReturn(Optional.of(round));
        when(converter.mapPlayedHoleEntityToDTO(round.getHoles().get(round.getCurrentHoleNumber()))).thenReturn(playedHoleDTO);

        PlayedHoleDTO result = roundService.getHoleForRound(roundId, null);

        assertThat(result.getHoleNumber()).isEqualTo(round.getCurrentHoleNumber());
        assertThat(result.getPar()).isEqualTo(round.getHoles().get(round.getCurrentHoleNumber()).getPar());
    }

    @Test
    public void getHoleForRoundMisingRound() {
        Long roundId = 1L;
        int holeNumber = 5;
        when(roundRepository.findById(roundId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roundService.getHoleForRound(roundId, holeNumber))
                .isInstanceOf(MissingResourceException.class);
    }

    @Test
    public void getHoleForRoundHoleNumberLessThan1() {
        Long roundId = 1L;
        int holeNumber = -1;
        when(roundRepository.findById(roundId)).thenReturn(Optional.of(round));

        assertThatThrownBy(() -> roundService.getHoleForRound(roundId, holeNumber))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void getHoleForRoundHoleNumberGreaterThan18() {
        Long roundId = 1L;
        int holeNumber = 34;
        when(roundRepository.findById(roundId)).thenReturn(Optional.of(round));

        assertThatThrownBy(() -> roundService.getHoleForRound(roundId, holeNumber))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void addShotToHole() {
        Long roundId = 1L;
        int holeNumber = 1;
        when(roundRepository.findById(roundId)).thenReturn(Optional.of(round));
        when(converter.mapShotDTOToEntity(any(ShotDTO.class), any(PlayedHole.class))).thenReturn(shot);
        when(converter.mapPlayedHoleEntityToDTO(any(PlayedHole.class))).thenReturn(playedHoleDTO);

        RoundDTO result = roundService.addShotToHole(roundId, holeNumber, shotDTO);

        assertThat(result.getHoles().get(holeNumber).getShots().size()).isEqualTo(1);
    }

    @Test
    public void addShotToHoleMissingRound() {
        Long roundId = 1L;
        int holeNumber = 1;
        when(roundRepository.findById(roundId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roundService.addShotToHole(roundId, holeNumber, shotDTO))
                .isInstanceOf(MissingResourceException.class);
    }

    @Test
    public void addShotToHoleMissingHole() {
        Long roundId = 1L;
        int holeNumber = 19;
        when(roundRepository.findById(roundId)).thenReturn(Optional.of(round));

        assertThatThrownBy(() -> roundService.addShotToHole(roundId, holeNumber, shotDTO))
                .isInstanceOf(MissingResourceException.class);
    }

    @Test
    public void updateShot() {
        Long shotId = 1L;
        when(shotRepository.findById(shotId)).thenReturn(Optional.of(shot));
        when(converter.mapPlayedHoleEntityToDTO(any(PlayedHole.class))).thenReturn(playedHoleDTO);

        PlayedHoleDTO result = roundService.updateShot(shotId, shotDTO);

        assertThat(result.getHoleNumber()).isEqualTo(1);
        assertThat(result.getShots().size()).isEqualTo(1);
    }

    @Test
    public void updateShotMissingShot() {
        Long shotId = 1L;
        when(shotRepository.findById(shotId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roundService.updateShot(shotId, shotDTO))
                .isInstanceOf(MissingResourceException.class);
    }

    @Test
    public void updateShotMissingHole() {
        Long shotId = 1L;
        Shot shotNoHole = Shot.builder().id(shotId).build();
        when(shotRepository.findById(shotId)).thenReturn(Optional.of(shotNoHole));

        assertThatThrownBy(() -> roundService.updateShot(shotId, shotDTO))
                .isInstanceOf(MissingResourceException.class);
    }

    @Test
    public void deleteShot() {
        Long shotId = 1L;
        PlayedHoleDTO deletedShotHole = PlayedHoleDTO.builder()
                .completed(true)
                .holeNumber(1)
                .par(4)
                .putts(2)
                .shots(List.of())
                .build();

        when(shotRepository.findById(shotId)).thenReturn(Optional.of(shot));
        when(converter.mapPlayedHoleEntityToDTO(any(PlayedHole.class))).thenReturn(deletedShotHole);

        PlayedHoleDTO result = roundService.deleteShot(shotId);

        assertThat(result.getHoleNumber()).isEqualTo(1);
        assertThat(result.getShots().size()).isEqualTo(0);
    }

    @Test
    public void deleteShotMissingShot() {
        Long shotId = 1L;
        when(shotRepository.findById(shotId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roundService.deleteShot(shotId))
                .isInstanceOf(MissingResourceException.class);
    }

    @Test
    public void deleteShotMissingHole() {
        Long shotId = 1L;
        Shot shotNoHole = Shot.builder().id(shotId).build();
        when(shotRepository.findById(shotId)).thenReturn(Optional.of(shotNoHole));

        assertThatThrownBy(() -> roundService.deleteShot(shotId))
                .isInstanceOf(MissingResourceException.class);
    }

    @Test
    public void deleteShotNotInPlayedHoles() {
        Long shotId = 453L;
        Shot shotNoHole = Shot.builder().id(shotId).build();
        when(shotRepository.findById(shotId)).thenReturn(Optional.of(shot));

        assertThatThrownBy(() -> roundService.deleteShot(shotId))
                .isInstanceOf(IllegalStateException.class);
    }


    private PlayedHole createPlayedHole(Round round, int holeNumber, Shot shot) {
        return PlayedHole.builder()
                .round(round)
                .shots(new ArrayList<>(List.of(shot)))
                .putts(2)
                .id(1L)
                .strokes(5)
                .holeNumber(holeNumber)
                .completed(true)
                .par(4)
                .build();
    }
}
