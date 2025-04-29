package org.example.fifa.rest.controller;

import org.apache.catalina.LifecycleState;
import org.example.fifa.model.Club;
import org.example.fifa.model.Player;
import org.example.fifa.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Component
public class ClubController {
    @Autowired private ClubService clubService;

    @GetMapping("/clubs")
    public ResponseEntity<Object> getAll(){
        return clubService.getClubList();
    }

    @PutMapping("/clubs")
    public ResponseEntity<Object> createClubs(@RequestBody List<Club> clubs){
        return clubService.createOrUpdateClub(clubs);
    }

    @GetMapping("/clubs/{id}/players")
    public ResponseEntity<Object> getPlayers(@PathVariable String id){
        return clubService.getActualPlayers(id);
    }

    @PutMapping("/clubs/{id}/players")
    public ResponseEntity<Object> updatePlayers(@PathVariable String id, @RequestBody List<Player> players){
        return clubService.addPlayersInClub(id, players);
    }

    @PostMapping("/clubs/{id}/players")
    public ResponseEntity<Object> addPlayers(@PathVariable String id, List<Player> players){
        return clubService.addPlayersInClub(id, players);
    }

    @GetMapping("/clubs/statistics/{seasonYear}")
    public ResponseEntity<Object> getClubsStatistic(@PathVariable LocalDate seasonYear, @RequestParam boolean hasToBeClassified){
        return clubService.getClubsStatistics(seasonYear, hasToBeClassified);

    }
}
