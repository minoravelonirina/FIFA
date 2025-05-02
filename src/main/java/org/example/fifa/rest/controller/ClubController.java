package org.example.fifa.rest.controller;

import org.example.fifa.model.Club;
import org.example.fifa.model.Player;
import org.example.fifa.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
@Component
public class ClubController {
    @Autowired private ClubService clubService;

    @GetMapping("/clubs")
    public ResponseEntity<Object> getClubs() throws SQLException {
        return clubService.getClubList();
    }

    @PutMapping("/clubs")
    public ResponseEntity<Object> createOrUpdateClubs(@RequestBody List<Club> clubs){
        return clubService.createOrUpdateClubs(clubs);
    }

    @GetMapping("/clubs/{id}/players")
    public ResponseEntity<Object> getPlayers(@PathVariable String id){
        return clubService.getActualPlayers(id);
    }

    @PutMapping("/clubs/{id}/players")
    public ResponseEntity<Object> updatePlayers(@PathVariable String id, @RequestBody List<Player> players) throws SQLException {
        return clubService.changePlayerOfClub(id, players);
    }

    @PostMapping("/clubs/{id}/players")
    public ResponseEntity<Object> addPlayers(@PathVariable String id, @RequestBody List<Player> players){
        return clubService.addPlayersInClub(id, players);
    }

    @GetMapping("/clubs/statistics/{seasonYear}")
    public ResponseEntity<Object> getClubsStatistic(@PathVariable LocalDate seasonYear, @RequestParam boolean hasToBeClassified) throws SQLException {
        return clubService.getClubsStatistics(seasonYear.getYear(), hasToBeClassified);
    }
}
