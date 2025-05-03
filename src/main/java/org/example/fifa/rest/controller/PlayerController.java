package org.example.fifa.rest.controller;

import org.example.fifa.model.ClubPlayer;
import org.example.fifa.model.Player;
import org.example.fifa.model.PlayerStatistics;
import org.example.fifa.rest.dto.PlayerDto;
import org.example.fifa.rest.dto.PlayerStatisticDto;
import org.example.fifa.rest.dto.PlayerWithClubDto;
import org.example.fifa.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<String> getAllPlayers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer ageMinimum,
            @RequestParam(required = false) Integer ageMaximum,
            @RequestParam(required = false) String clubName) {
        return ResponseEntity.ok(playerService.findPlayers(name, ageMinimum, ageMaximum, clubName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        return playerService.findById(id);
    }


    @PutMapping
    public ResponseEntity<List<Player>> createOrUpdatePlayers(@RequestBody List<Player> players) {
        return playerService.saveAll(players);
    }


    @GetMapping("/{id}/statistics/{seasonYear}")
    public ResponseEntity<PlayerStatisticDto> getStatisticsOfPlayerById(
            @PathVariable String id,
            @PathVariable Integer seasonYear) {
        return playerService.findPlayerStatistics(id, seasonYear);
    }
}
