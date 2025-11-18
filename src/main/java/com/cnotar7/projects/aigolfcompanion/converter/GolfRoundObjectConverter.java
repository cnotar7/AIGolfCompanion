package com.cnotar7.projects.aigolfcompanion.converter;

import com.cnotar7.projects.aigolfcompanion.dto.PlayedHoleDTO;
import com.cnotar7.projects.aigolfcompanion.dto.RoundResponseDTO;
import com.cnotar7.projects.aigolfcompanion.dto.ShotDTO;
import com.cnotar7.projects.aigolfcompanion.model.PlayedHole;
import com.cnotar7.projects.aigolfcompanion.model.Round;
import com.cnotar7.projects.aigolfcompanion.model.Shot;
import com.cnotar7.projects.aigolfcompanion.model.User;
import org.springframework.stereotype.Component;

@Component
public class GolfRoundObjectConverter {
    public RoundResponseDTO mapRoundEntityToDTO(Round round, User user) {
        return RoundResponseDTO.builder()
                .courseId(round.getCourse().getId())
                .roundId(round.getId())
                .teeId(round.getSelectedTee().getId())
                .courseName(round.getCourse().getName())
                .userName(user.getUsername())
                .currentHoleNumber(round.getCurrentHoleNumber())
                .startTime(round.getStartTime())
                .completed(round.isCompleted())
                .build();
    }
    public PlayedHoleDTO mapPlayedHoleEntityToDTO(PlayedHole rh) {
        return PlayedHoleDTO.builder()
                .holeNumber(rh.getHoleNumber())
                .par(rh.getPar())
                .strokes(rh.getStrokes())
                .putts(rh.getPutts())
                .completed(rh.isCompleted())
                .shots(rh.getShots().stream()
                        .map(this::mapShotEntityToDTO)
                        .toList())
                .build();
    }

    public ShotDTO mapShotEntityToDTO(Shot shot) {
        return ShotDTO.builder()
                .club(shot.getClub())
                .distanceYards(shot.getDistanceYards())
                .result(shot.getResult())
                .build();
    }

    public Shot mapShotDTOToEntity(ShotDTO shotDTO, PlayedHole playedHole) {
        return Shot.builder()
                .club(shotDTO.getClub())
                .distanceYards(shotDTO.getDistanceYards())
                .result(shotDTO.getResult())
                .playedHole(playedHole)
                .build();
    }
}
