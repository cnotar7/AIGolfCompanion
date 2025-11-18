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

    @PostMapping()
    public ResponseEntity<RoundResponseDTO> startNewRound(@RequestBody StartRoundDTO startRoundDTO) {
        RoundResponseDTO roundResponseDTO = roundService.startNewRound(startRoundDTO);
        if (roundResponseDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(roundResponseDTO);
    }

    @GetMapping("/{roundId}")
    public ResponseEntity<RoundResponseDTO> getRoundById(@PathVariable("roundId") Long roundId) {
        RoundResponseDTO roundResponseDTO = roundService.getRoundById(roundId);
        if (roundResponseDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(roundResponseDTO);
    }

    @GetMapping("/{roundId}/holes/current")
    public ResponseEntity<PlayedHoleDTO> getCurrentHole(@PathVariable Long roundId) {
        PlayedHoleDTO playedHoleDTO = roundService.getHoleForRound(roundId, null);
        if (playedHoleDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(playedHoleDTO);
    }

    @GetMapping("/{roundId}/holes/{holeNumber}")
    public ResponseEntity<PlayedHoleDTO> getSpecificHole(@PathVariable Long roundId, @PathVariable int holeNumber) {
        PlayedHoleDTO playedHoleDTO = roundService.getHoleForRound(roundId, holeNumber);
        if (playedHoleDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(playedHoleDTO);
    }

    @PostMapping("/{roundId}/holes/{holeNumber}/shots")
    public ResponseEntity<PlayedHoleDTO> addShotToHole(
            @PathVariable Long roundId, @PathVariable @Min(1) @Max(18) Integer holeNumber, @RequestBody ShotDTO shotDTO) {
        PlayedHoleDTO playedHole = roundService.addShotToHole(roundId, holeNumber, shotDTO);

        return ResponseEntity.ok(playedHole);
    }

    @PostMapping("/shots/{shotId}")
    public ResponseEntity<PlayedHoleDTO> updateShot(@PathVariable Long shotId, @RequestBody ShotDTO shotDTO) {
        PlayedHoleDTO playedHole = roundService.updateShot(shotId, shotDTO);
        return ResponseEntity.ok(playedHole);
    }

    @DeleteMapping("/shots/{shotId}")
    public ResponseEntity<PlayedHoleDTO> deleteShot(@PathVariable Long shotId) {
        PlayedHoleDTO playedHole = roundService.deleteShot(shotId);

        return ResponseEntity.ok(playedHole);
    }

    @PostMapping("/{roundId}/holes/next")
    public ResponseEntity<PlayedHoleDTO> moveToNextHole(@PathVariable Long roundId) {
        PlayedHoleDTO nextHole = roundService.moveToNextHole(roundId);
        return ResponseEntity.ok(nextHole);
    }

    @PostMapping("/{roundId}/holes/previous")
    public ResponseEntity<PlayedHoleDTO> moveToPreviousHole(@PathVariable Long roundId) {
        PlayedHoleDTO previousHole = roundService.moveToPreviousHole(roundId);
        return ResponseEntity.ok(previousHole);
    }

    @PostMapping("/{roundId}/complete")
    public ResponseEntity<Round> completeRound(@PathVariable Long roundId) {
        return ResponseEntity.ok(new Round());
    }


}
