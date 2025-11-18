package com.cnotar7.projects.aigolfcompanion.controller;


import com.cnotar7.projects.aigolfcompanion.dto.*;
import com.cnotar7.projects.aigolfcompanion.model.Round;
import com.cnotar7.projects.aigolfcompanion.model.PlayedHole;
import com.cnotar7.projects.aigolfcompanion.service.RoundService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@AllArgsConstructor
@RequestMapping("/golfcompanion/rounds")
public class RoundController {

    private RoundService roundService;

    @PostMapping("/start")
    public ResponseEntity<RoundResponseDTO> startNewRound(@RequestBody StartRoundDTO startRoundDTO) {
        RoundResponseDTO roundResponseDTO = roundService.startNewRound(startRoundDTO);
        if (roundResponseDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(roundResponseDTO);
    }

    @GetMapping("/{roundId}/current")
    public ResponseEntity<PlayedHoleDTO> getCurrentHole(@PathVariable Long roundId) {
        PlayedHoleDTO playedHoleDTO = roundService.getHoleForRound(roundId, null);
        if (playedHoleDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(playedHoleDTO);
    }

    @GetMapping("/{roundId}/hole/{holeNumber}")
    public ResponseEntity<PlayedHoleDTO> getSpecificHole(@PathVariable Long roundId, @PathVariable int holeNumber) {
        PlayedHoleDTO playedHoleDTO = roundService.getHoleForRound(roundId, holeNumber);
        if (playedHoleDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(playedHoleDTO);
    }

    @PostMapping("/{roundId}/hole/{holeNumber}/shot")
    public ResponseEntity<PlayedHoleDTO> addShotToHole(
            @PathVariable Long roundId, @PathVariable @Min(1) @Max(18) Integer holeNumber, @RequestBody ShotDTO shotDTO) {
        PlayedHoleDTO playedHole = roundService.addShotToHole(roundId, holeNumber, shotDTO);

        return ResponseEntity.ok(playedHole);
    }

    @PostMapping("{roundId}/hole/next")
    public ResponseEntity<PlayedHoleDTO> moveToNextHole(@PathVariable Long roundId) {
        PlayedHoleDTO nextHole = roundService.moveToNextHole(roundId);
        return ResponseEntity.ok(nextHole);
    }

    @PostMapping("{roundId}/hole/previous")
    public ResponseEntity<PlayedHoleDTO> moveToPreviousHole(@PathVariable Long roundId) {
        PlayedHoleDTO previousHole = roundService.moveToPreviousHole(roundId);
        return ResponseEntity.ok(previousHole);
    }

    @PostMapping("{roundId}/complete")
    public ResponseEntity<Round> completeRound(@PathVariable Long roundId) {
        return ResponseEntity.ok(new Round());
    }


}
