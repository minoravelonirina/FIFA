package org.example.fifa.rest.controller;

import org.example.fifa.model.Player;
import org.example.fifa.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Component
public class PlayerController {
    @Autowired private PlayerService playerService;

    @GetMapping("/players")
    public ResponseEntity<Object> getAll(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) int ageMinimum,
                                         @RequestParam(required = false) int ageMaximum,
                                         @RequestParam(required = false) String clubName){
        return playerService.getAllPlayers();
    }

    @PutMapping("/players")
    public ResponseEntity<Object> updatePlayers(@RequestBody List<Player> players){
        return playerService.updatePlayerList(players);
    }

    @GetMapping("/players/{id}/statistics/{seasonYear}")
    public ResponseEntity<Object> getStatisticOfPlayer(@PathVariable String id,
                                                       @PathVariable LocalDate seasonYear){
        return playerService.getStatisticOfSpecificPlayer(id, seasonYear);
    }


}
