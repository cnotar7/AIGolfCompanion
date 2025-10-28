package com.cnotar7.projects.aigolfcompanion.controller;


import com.cnotar7.projects.aigolfcompanion.dto.HoleShotsDTO;
import com.cnotar7.projects.aigolfcompanion.dto.ShotDTO;
import com.cnotar7.projects.aigolfcompanion.model.Round;
import com.cnotar7.projects.aigolfcompanion.model.RoundHole;
import com.cnotar7.projects.aigolfcompanion.model.Shot;
import com.cnotar7.projects.aigolfcompanion.repository.RoundRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/golfcompanion/rounds")
public class RoundController {

    private RoundRepository roundRepository;

    @PostMapping("/{roundId}/holes")
    public ResponseEntity<RoundHole> recordHoleShots(@PathVariable Long roundId, @RequestBody HoleShotsDTO holeShotsDTO) {

        return ResponseEntity.ok(new RoundHole());

    }
}
