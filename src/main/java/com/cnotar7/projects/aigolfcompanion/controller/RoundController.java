package com.cnotar7.projects.aigolfcompanion.controller;


import com.cnotar7.projects.aigolfcompanion.dto.*;
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
    public ResponseEntity<RoundDTO> startNewRound(@RequestBody StartRoundDTO startRoundDTO) {
        System.out.println("startRoundDTO =  " + startRoundDTO);
        RoundDTO roundDTO = roundService.startNewRound(startRoundDTO);
        if (roundDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(roundDTO);
    }

    @GetMapping("/{roundId}")
    public ResponseEntity<RoundDTO> getRoundById(@PathVariable Long roundId) {
        RoundDTO roundDTO = roundService.getRoundById(roundId);
        if (roundDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(roundDTO);
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
    public ResponseEntity<RoundDTO> addShotToHole(
            @PathVariable Long roundId, @PathVariable @Min(1) @Max(18) Integer holeNumber, @RequestBody ShotDTO shotDTO) {
        RoundDTO roundDTO = roundService.addShotToHole(roundId, holeNumber, shotDTO);

        return ResponseEntity.ok(roundDTO);
    }

    @PutMapping("/shots/{shotId}")
    public ResponseEntity<PlayedHoleDTO> updateShot(@PathVariable Long shotId, @RequestBody ShotDTO shotDTO) {
        PlayedHoleDTO playedHoleDTO = roundService.updateShot(shotId, shotDTO);
        return ResponseEntity.ok(playedHoleDTO);
    }

    @DeleteMapping("/shots/{shotId}")
    public ResponseEntity<PlayedHoleDTO> deleteShot(@PathVariable Long shotId) {
        PlayedHoleDTO playedHoleDTO = roundService.deleteShot(shotId);

        return ResponseEntity.ok(playedHoleDTO);
    }

    @PostMapping("/{roundId}/holes/next")
    public ResponseEntity<RoundDTO> moveToNextHole(@PathVariable Long roundId) {
        RoundDTO roundDTO = roundService.moveToNextHole(roundId);
        return ResponseEntity.ok(roundDTO);
    }

    @PostMapping("/{roundId}/holes/previous")
    public ResponseEntity<RoundDTO> moveToPreviousHole(@PathVariable Long roundId) {
        RoundDTO roundDTO = roundService.moveToPreviousHole(roundId);
        return ResponseEntity.ok(roundDTO);
    }

    @PostMapping("/{roundId}/complete")
    public ResponseEntity<RoundDTO> completeRound(@PathVariable Long roundId) {
        RoundDTO roundDTO = roundService.completeRound(roundId);
        return ResponseEntity.ok(roundDTO);
    }


}
