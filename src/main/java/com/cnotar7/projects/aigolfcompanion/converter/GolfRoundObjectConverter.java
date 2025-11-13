package com.cnotar7.projects.aigolfcompanion.converter;

import com.cnotar7.projects.aigolfcompanion.dto.RoundHoleDTO;
import com.cnotar7.projects.aigolfcompanion.dto.ShotDTO;
import com.cnotar7.projects.aigolfcompanion.model.RoundHole;
import com.cnotar7.projects.aigolfcompanion.model.Shot;
import org.springframework.stereotype.Component;

@Component
public class GolfRoundObjectConverter {
    public RoundHoleDTO mapRoundHoleEntityToRoundHoleDTO(RoundHole rh) {
        return RoundHoleDTO.builder()
                .holeNumber(rh.getHoleNumber())
                .par(rh.getPar())
                .strokes(rh.getStrokes())
                .putts(rh.getPutts())
                .completed(rh.isCompleted())
                .shots(rh.getShots().stream()
                        .map(this::mapShotEntityToShotDTO)
                        .toList())
                .build();
    }

    public ShotDTO mapShotEntityToShotDTO(Shot shot) {
        return ShotDTO.builder()
                .club(shot.getClub())
                .distanceYards(shot.getDistanceYards())
                .result(shot.getResult())
                .build();
    }
}
