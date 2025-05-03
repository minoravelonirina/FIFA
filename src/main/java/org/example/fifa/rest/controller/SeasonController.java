package org.example.fifa.rest.controller;

import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.example.fifa.rest.dto.CreateSeasonDto;
import org.example.fifa.rest.dto.UpdateSeasonStatusDto;
import org.example.fifa.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/seasons")
public class SeasonController {
    private final SeasonService seasonService;

    @Autowired
    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @GetMapping
    public ResponseEntity<List<Season>> getSeasons() {
        return ResponseEntity.ok(seasonService.findAll());
    }

    @PostMapping
    public ResponseEntity<List<Season>> createSeasons(@RequestBody List<CreateSeasonDto> createSeasonDtos) {
        return ResponseEntity.ok(seasonService.createSeasons(createSeasonDtos));
    }

    @PutMapping("/{seasonYear}/status")
    public ResponseEntity<Season> updateSeasonStatus(
            @PathVariable Integer seasonYear,
            @RequestBody UpdateSeasonStatusDto status) {
        return seasonService.updateStatus(seasonYear, status.getStatus());
    }
}