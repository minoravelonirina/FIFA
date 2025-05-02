package org.example.fifa.rest.controller;

import org.example.fifa.model.ClubPlayer;
import org.example.fifa.model.Player;
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
    public ResponseEntity<List<ClubPlayer>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer ageMinimum,
            @RequestParam(required = false) Integer ageMaximum,
            @RequestParam(required = false) String clubName) {
        List<ClubPlayer> players = playerService.findPlayers(name, ageMinimum, ageMaximum, clubName);
        return ResponseEntity.ok(players);
    }

    @PutMapping
    public ResponseEntity<List<Player>> updatePlayers(@RequestBody List<Player> players) {
        List<Player> updatedPlayers = players.stream()
                .map(playerService::save)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(updatedPlayers);
    }

    @GetMapping("/{id}/statistics/{seasonYear}")
    public ResponseEntity<Object> getStatisticOfPlayer(
            @PathVariable String id,
            @PathVariable LocalDate seasonYear) {
        // TO DO
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        Player player = playerService.findById(id);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(player);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String id) {
        playerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
