package com.cnotar7.projects.aigolfcompanion.controller;


import com.cnotar7.projects.aigolfcompanion.dto.*;
import com.cnotar7.projects.aigolfcompanion.model.Hole;
import com.cnotar7.projects.aigolfcompanion.model.Round;
import com.cnotar7.projects.aigolfcompanion.model.RoundHole;
import com.cnotar7.projects.aigolfcompanion.model.Shot;
import com.cnotar7.projects.aigolfcompanion.repository.RoundRepository;
import com.cnotar7.projects.aigolfcompanion.service.RoundService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/golfcompanion/rounds")
public class RoundController {

    private RoundService roundService;

    @PostMapping("/start")
    public ResponseEntity<StartRoundResponseDTO> startNewRound(@RequestBody StartRoundDTO startRoundDTO) {

        return ResponseEntity.ok(new StartRoundResponseDTO());
    }

    @GetMapping("/{roundId}/current")
    public ResponseEntity<Hole> getCurrentHole(@PathVariable Long roundId) {
        return ResponseEntity.ok(new Hole());
    }

    @GetMapping("/{roundId}/hole/{holeNumber}")
    public ResponseEntity<Hole> getSpecificHole(@PathVariable Long roundId, @PathVariable int holeNumber) {
        return ResponseEntity.ok(new Hole());
    }

    @PostMapping("/{roundId}/hole/shots")
    public ResponseEntity<RoundHole> recordHoleShots(@PathVariable Long roundId, @RequestBody HoleShotsDTO holeShotsDTO) {
        RoundHole roundHole = roundService.addShotsToRound(roundId, holeShotsDTO);

        return ResponseEntity.ok(roundHole);
    }

    @PostMapping("{roundId}/next")
    public ResponseEntity<RoundHole> moveToNextHole(@PathVariable Long roundId) {
        return ResponseEntity.ok(new RoundHole());
    }

    @PostMapping("{roundId}/previous")
    public ResponseEntity<RoundHole> moveToPreviousHole(@PathVariable Long roundId) {
        return ResponseEntity.ok(new RoundHole());
    }

    @PostMapping("{roundId}/complete")
    public ResponseEntity<Round> completeRound(@PathVariable Long roundId) {
        return ResponseEntity.ok(new Round());
    }


}
